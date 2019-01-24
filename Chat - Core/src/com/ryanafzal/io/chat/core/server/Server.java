package com.ryanafzal.io.chat.core.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import com.ryanafzal.io.chat.core.resources.file.Logger;
import com.ryanafzal.io.chat.core.resources.misc.Slow;
import com.ryanafzal.io.chat.core.resources.misc.Speed;
import com.ryanafzal.io.chat.core.resources.sendable.Packet;
import com.ryanafzal.io.chat.core.resources.thread.PacketDistributionThread;
import com.ryanafzal.io.chat.core.resources.thread.ServerThread;
import com.ryanafzal.io.chat.core.resources.user.User;
import com.ryanafzal.io.chat.core.resources.user.UserNotFoundException;
import com.ryanafzal.io.chat.core.resources.user.groups.BaseGroup;
import com.ryanafzal.io.chat.core.resources.user.groups.GlobalServerGroup;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public class Server {
	
	public int PORT = 4444;
	
	public static final long GLOBAL_GROUP_ID = 0;
	public static final String CONFIGPATH = "data\\config.txt";
	
	private final ServerGUI parent;
	
	protected String serverHost;
	private ServerSocket serverSocket;
	
	private ServerThread acceptClientsThread;
	private PacketDistributionThread packetDistributionThread;
	
	private LinkedList<Packet> packetQueue;
	
	private HashSet<Connection> unmappedConnections;
	private HashMap<Long, Connection> connections;//UserID -> Connection to User
	
	private HashMap<String, User> usernames;//Username -> User;
	private HashMap<Long, User> users;//UserID -> User
	private HashMap<Long, BaseGroup> groups;//GroupID -> Group
	
	private Logger logger;
	
	@Slow
	@Speed("")
	public Server(ServerGUI parent) {
		this.parent = parent;
		
		this.logger = new Logger("data/logs/Log.txt");
		
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
	
	public void changeGroupPermissions(long group, long user, Level level) {
		this.changeGroupPermissions(this.getGroupByID(group), this.getUserByID(user), level);
	}
	
	public void changeGroupPermissions(BaseGroup group, User user, Level level) {
		group.changePermission(user.getID(), level);
		user.setPermissionLevel(group.GROUPID, level);
	}
	
	@Speed("1")
	public BaseGroup getGroupByID(long address) {
		return this.groups.get(address);
	}
	
	@Speed("1")
	public User getUserByID(long ID) {
		return this.users.get(ID);
	}
	
	@Speed("1")
	public void addUser(User user) {
		this.usernames.put(user.getName(), user);
		this.users.put(user.getID(), user);
	}
	
	@Speed("1")
	public void removeUser(User user) {
		this.usernames.remove(user.getName());
		this.users.remove(user.getID());
	}
	
	@Speed("1")
	private void initData() {
		this.usernames = new HashMap<String, User>();
		this.users = new HashMap<Long, User>();
		this.groups = new HashMap<Long, BaseGroup>();
		
		
		GlobalServerGroup global = new GlobalServerGroup();
		//User prepopulation here
		
		User ryanafzal = new User("Ryan Afzal", "ryanafzal", 1337);
		this.addUser(ryanafzal);
		global.addUser(ryanafzal, Level.ADMIN);
		this.groups.put(Server.GLOBAL_GROUP_ID, global);
		
		//TODO
	}

	@Speed("1")
	private void startServer() {
		serverSocket = null;

		try {
			InetAddress addr = InetAddress.getByName(this.serverHost);
			serverSocket = new ServerSocket(PORT, 50, addr);
			this.parent.outputCommandMessage("SERVER STARTING ON PORT: " + this.serverSocket.getLocalSocketAddress());
			
			this.logger.log("SERVER STARTING ON PORT: " + this.serverSocket.getLocalSocketAddress());
			
			this.acceptClients();
		} catch (IOException e) {
			e.printStackTrace();
			this.parent.outputErrorMessage("COULD NOT LISTEN ON PORT: " + PORT);
		}
	}
	
	/**
	 * Starts two threads:
	 * 1. The {@code ServerThread}, which accepts clients and creates connections.
	 * 2. The {@code PacketDistributionThread}, which distributes packets to clients.
	 */
	@Speed("1")
	private void acceptClients() {
		this.acceptClientsThread = new ServerThread(this, this.serverSocket);
		Thread serverThread = new Thread(this.acceptClientsThread);
		serverThread.start();
		
		this.packetDistributionThread = new PacketDistributionThread(this);
		Thread pdThread = new Thread(this.packetDistributionThread);
		pdThread.start();
	}
	
	public void onClose() {
		try {
			this.acceptClientsThread.cancel();
			this.packetDistributionThread.cancel();
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
	
	@Speed("1")
	public void addConnection(Socket socket) {
		this.addConnection(this.createConnection(socket));
	}
	
	@Speed("1")
	public void addConnection(Connection connection) {
		this.unmappedConnections.add(connection);
	}

	@Slow
	@Speed("n")
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
	
	@Speed("1")
	public void destroyConnection(Connection connection) {
		this.connections.remove(connection);
	}
	
	@Speed("1")
	public void inductConnection(long ID, Connection connection) {
		this.unmappedConnections.remove(connection);
		this.connections.put(ID, connection);
	}
	
	@Speed("1")
	public void deductConnection(long ID) {
		this.unmappedConnections.add(this.connections.get(ID));
		this.connections.remove(ID);
	}
	
	@Speed("1")
	private Connection createConnection(Socket socket) {
		return new Connection(this, socket);
	}

	@Slow
	@Speed("n")
	public User login(String username, int password, boolean register) throws UserNotFoundException {
		User found = this.usernames.get(username);
		
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
	
	@Speed("1")
	public void enqueuePacket(Packet packet) {
		this.packetQueue.addFirst(packet);
	}
	
	@Speed("1")
	public Packet dequeuePacket() {
		return this.packetQueue.removeLast();
	}
	
	@Speed("1")
	public boolean arePacketsQueued() {
		return !this.packetQueue.isEmpty();
	}
	
	@Speed("1")
	public Connection getConnectionByUserID(long l) {
		return this.connections.get(l);
	}
	
	@Speed("1")
	public boolean isRunning() {
		return this.parent.isRunning();
	}
	
	@Speed("1")
	public ServerGUI getParent() {
		return this.parent;
	}
	
	@Speed("1")
	public User getUserByName(String name) {
		return this.usernames.get(name);
	}

}
