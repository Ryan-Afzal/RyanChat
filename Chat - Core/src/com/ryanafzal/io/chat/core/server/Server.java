package com.ryanafzal.io.chat.core.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import com.ryanafzal.io.chat.core.resources.application.ApplicationWindow;
import com.ryanafzal.io.chat.core.resources.misc.Slow;
import com.ryanafzal.io.chat.core.resources.sendable.Packet;
import com.ryanafzal.io.chat.core.resources.thread.PacketDistributionThread;
import com.ryanafzal.io.chat.core.resources.thread.ServerThread;
import com.ryanafzal.io.chat.core.resources.user.User;
import com.ryanafzal.io.chat.core.resources.user.UserNotFoundException;
import com.ryanafzal.io.chat.core.resources.user.groups.BaseGroup;
import com.ryanafzal.io.chat.core.resources.user.groups.GlobalServerGroup;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public class Server extends ApplicationWindow {
	
	public int PORT = 440;
	
	public static final long GLOBAL_GROUP_ID = 0;
	public static final String CONFIGPATH = "data\\config.txt";
	
	protected String serverHost;
	private ServerSocket serverSocket;
	
	private ServerThread acceptClientsThread;
	private PacketDistributionThread packetDistributionThread;
	
	private LinkedList<Packet> packetQueue;
	
	private HashSet<Connection> unmappedConnections;
	private HashMap<Long, Connection> connections;//UserID -> Connection to User
	
	private HashMap<Long, User> users;//UserID -> User
	private HashMap<Long, BaseGroup> groups;//GroupID -> Group

	@Slow
	public Server() {
		this.unmappedConnections = new HashSet<Connection>();
		this.connections = new HashMap<Long, Connection>();
		
		this.packetQueue = new LinkedList<Packet>();
		
		try	{
			this.serverHost = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			this.serverHost = "";
			e.printStackTrace();
		}
		
		this.initData();
		this.startServer();
	}
	
	public BaseGroup getGroupByID(long address) {
		return this.groups.get(address);
	}
	
	public User getUserByID(long ID) {
		return this.users.get(ID);
	}
	
	public void addUser(User user) {
		this.users.put(user.getID(), user);
	}
	
	public void removeUser(User user) {
		this.users.remove(user.getID());
	}
	
	private void initData() {
		this.users = new HashMap<Long, User>();
		this.groups = new HashMap<Long, BaseGroup>();
		this.groups.put(Server.GLOBAL_GROUP_ID, new GlobalServerGroup());
		
		//TODO ADD OVERRIDES HERE
		
	}

	@Slow
	private void startServer() {
		serverSocket = null;

		try {
			InetAddress addr = InetAddress.getByName(this.serverHost);
			serverSocket = new ServerSocket(PORT, 50, addr);
			this.outputCommandMessage("SERVER STARTING ON PORT: " + this.serverSocket.getLocalSocketAddress());
			
			this.acceptClients();
		} catch (IOException e) {
			e.printStackTrace();
			this.outputErrorMessage("COULD NOT LISTEN ON PORT: " + PORT);
		}
	}
	
	/**
	 * Starts two threads:
	 * 1. The {@code ServerThread}, which accepts clients and creates connections.
	 * 2. The {@code PacketDistributionThread}, which distributes packets to clients.
	 */
	private void acceptClients() {
		this.acceptClientsThread = new ServerThread(this, this.serverSocket);
		Thread serverThread = new Thread(this.acceptClientsThread);
		serverThread.start();
		
		this.packetDistributionThread = new PacketDistributionThread(this);
		Thread pdThread = new Thread(this.packetDistributionThread);
		pdThread.start();
	}

	@Override
	public String getTitle() {
		return "MorphineChat Server";
	}

	@Override
	public void process(String input) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClose() {
		try {
			this.acceptClientsThread.cancel();
			this.serverSocket.close();
			
			for (Connection c : this.unmappedConnections) {
				c.destroy();
			}
			
			for (Connection c : this.connections.values()) {
				c.destroy();
			}
			
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Level getPermissionRank() {
		return Level.SERVER;
	}
	
	public static void main(String[] args) {
		Server.launch(args);
	}
	
	public void addConnection(Socket socket) {
		this.addConnection(this.createConnection(socket));
	}
	
	public void addConnection(Connection connection) {
		this.unmappedConnections.add(connection);
	}

	@Slow
	public void destroyConnection(Socket socket) {
		Connection con = this.unmappedConnections
		.stream()
		.filter(c -> c.socket.equals(socket))
		.findFirst()
		.orElse(null);
		
		if (con == null) {
			this.destroyConnection(this.connections
					.values()
					.stream()
					.filter(c -> c.socket.equals(socket))
					.findFirst()
					.orElseThrow(IllegalArgumentException::new));
		} else {
			this.destroyConnection(con);
		}
	}
	
	public void destroyConnection(Connection connection) {
		this.connections.remove(connection);
	}
	
	public void inductConnection(long ID, Connection connection) {
		this.unmappedConnections.remove(connection);
		this.connections.put(ID, connection);
	}
	
	public void deductConnection(long ID) {
		this.unmappedConnections.add(this.connections.get(ID));
		this.connections.remove(ID);
	}
	
	private Connection createConnection(Socket socket) {
		return new Connection(this, socket);
	}

	@Slow
	public User login(String username, int password, boolean register) throws UserNotFoundException {
		User found = this.users
				.values()
				.stream()
				.filter(u -> u.getName().equals(username))
				.findFirst()
				.orElse(null);
		
		if (found == null) {
			if (register) {
				User output = new User(username, password, new Object().hashCode());
				this.addUser(output);
				this.groups.get(Server.GLOBAL_GROUP_ID).addUser(output);
				return output;
			} else {
				throw new UserNotFoundException("User " + username + " does not exist.");
			}
		} else {
			if (found.getPassword() == password) {
				return found;
			} else {
				throw new UserNotFoundException("Incorrect Password.");
			}
		}
	}
	
	public void enqueuePacket(Packet packet) {
		this.packetQueue.addFirst(packet);
	}
	
	public Packet dequeuePacket() {
		return this.packetQueue.removeLast();
	}
	
	public boolean arePacketsQueued() {
		return !this.packetQueue.isEmpty();
	}

	public Connection getConnectionByUserID(long l) {//O(1)
		return this.connections.get(l);
	}

}
