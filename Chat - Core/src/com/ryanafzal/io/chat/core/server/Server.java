package com.ryanafzal.io.chat.core.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import com.ryanafzal.io.chat.core.resources.application.ApplicationWindow;
import com.ryanafzal.io.chat.core.resources.sendable.Packet;
import com.ryanafzal.io.chat.core.resources.sendable.PacketData;
import com.ryanafzal.io.chat.core.resources.thread.ServerThread;
import com.ryanafzal.io.chat.core.resources.user.User;
import com.ryanafzal.io.chat.core.resources.user.UserNotFoundException;
import com.ryanafzal.io.chat.core.resources.user.groups.Group;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public class Server extends ApplicationWindow {
	
	public int PORT = 440;
	
	public static final long GLOBAL_GROUP_ID = 0;
	public static final String CONFIGPATH = "data\\config.txt";
	
	protected String serverHost;
	private ServerSocket serverSocket;
	
	private HashSet<Connection> unmappedConnections;
	private HashMap<Long, Connection> connections;
	
	private HashMap<Long, User> users;
	private HashMap<Long, Group> groups;
	
	public Server() {
		this.unmappedConnections = new HashSet<Connection>();
		this.connections = new HashMap<Long, Connection>();
		
		try	{
			this.serverHost = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			this.serverHost = "";
			e.printStackTrace();
		}
		
		this.initData();
		this.startServer();
	}
	
	public Group getGroupByID(int ID) {
		return this.groups.get(ID);
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
		this.groups = new HashMap<Long, Group>();
		
		this.groups.put(Server.GLOBAL_GROUP_ID, new Group(User.SERVER, "Global Chat", Server.GLOBAL_GROUP_ID));
		
		//TODO ADD OVERRIDES HERE
		
	}
	
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

	private void acceptClients() {
		ServerThread thread = new ServerThread(this, this.serverSocket);
		Thread serverThread = new Thread(thread);
		serverThread.start();
	}

	@Override
	public String getTitle() {
		return "RyanChat Server";
	}

	@Override
	public void process(String input) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClose() {
		try {
			this.serverSocket.close();
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
	
	/*
	 * //TODO Issue here:
	 * 
	 * Connection that is being passed is null.
	 * 
	 * Reason: The 'owner' is the Server, which is a legitimate user.
	 * However, its USERID does not correspond to a Connection
	 */
	public void distributePacket(Packet packet) {
		long address = packet.getPacketData().ADDRESS;
		if (packet.getPacketData().ADDRESSTYPE == PacketData.AddressType.GROUP) {
			Group g = this.groups.get(address);
			HashSet<Connection> connections = new HashSet<Connection>();
			
			for (Long l : g.getUsersAtRank(packet.getPacketData().LEVEL)) {
				connections.add(this.connections.get(l));
			}
			
			this.distributePacket(packet, connections);
		} else if (packet.getPacketData().ADDRESSTYPE == PacketData.AddressType.INDIVIDUAL) {
			ArrayList<Connection> connections = new ArrayList<Connection>();
			connections.add(this.connections.get(address));
			this.distributePacket(packet, connections);
		}
	}
	
	private void distributePacket(Packet packet, Iterable<Connection> recipients) {
		for (Connection c : recipients) {
			c.queuePacket(packet);
		}
	}
	
	public void addConnection(Socket socket) {
		this.addConnection(this.createConnection(socket));
	}
	
	public void addConnection(Connection connection) {
		this.unmappedConnections.add(connection);
	}
	
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

	public User login(String username, int password, boolean register) throws UserNotFoundException {
		User found = this.users
				.values()
				.stream()
				.filter(u -> u.getName().equals(username))
				.findFirst()
				.orElse(null);
		
		if (found == null) {
			if (register) {
				User output = new User(username, password, UUID.randomUUID().hashCode());
				this.addUser(output);
				this.groups.get(Server.GLOBAL_GROUP_ID).addUser(output);
				return output;
			} else {
				throw new UserNotFoundException("User with username " + username + " and password hashcode " + password + " does not exist.");
			}
		} else {
			if (found.getPassword() == password) {
				return found;
			} else {
				throw new UserNotFoundException("Incorrect Password.");
			}
		}
	}

}
