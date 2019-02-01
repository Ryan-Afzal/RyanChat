package com.ryanafzal.io.chat.core.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;

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

import javafx.concurrent.Task;

public class Server extends Task<Void> {
	
	public int PORT = 4444;
	
	public static final long GLOBAL_GROUP_ID = 0;
	public static final String CONFIGPATH = "data\\config.txt";
	
	private final ServerGUI parent;
	
	private InetAddress serverHost;
	private ServerSocket serverSocket;
	
	private ServerThread acceptClientsThread;
	private PacketDistributionThread packetDistributionThread;
	
	private HashSet<Connection> unmappedConnections;
	private HashMap<Long, Connection> connections;//UserID -> Connection to User
	
	private HashMap<String, User> usernames;//Username -> User;
	private HashMap<Long, User> users;//UserID -> User
	private HashMap<Long, BaseGroup> groups;//GroupID -> Group
	
	public Server(ServerGUI parent) throws UnknownHostException {
		this.parent = parent;
		
		this.unmappedConnections = new HashSet<Connection>();
		this.connections = new HashMap<Long, Connection>();
		
		this.serverHost = InetAddress.getLocalHost();
		
		this.initData();
		this.startServer();
	}
	
	@Speed("1")
	private void initData() {
		this.usernames = new HashMap<String, User>();
		this.users = new HashMap<Long, User>();
		this.groups = new HashMap<Long, BaseGroup>();
		
		
		GlobalServerGroup global = new GlobalServerGroup();
		//User prepopulation here
		
		User ryanafzal = new User("Ryan Afzal", "ryanafzal", 1337);
		User brandonchen = new User("Brandon Chen", "brandonchen", 0226);
		User fishbear = new User("Fish Bear", "fishbear", 2448);
		User isabelleteh = new User("Isabelle Teh", "isabelleteh", 3559);
		
		this.addUser(ryanafzal);
		this.addUser(brandonchen);
		this.addUser(fishbear);
		this.addUser(isabelleteh);
		global.addUser(ryanafzal, Level.ADMIN);
		global.addUser(brandonchen, Level.OFFICER);
		global.addUser(fishbear, Level.MODERATOR);
		global.addUser(isabelleteh, Level.USER);
		
		this.groups.put(Server.GLOBAL_GROUP_ID, global);
		
		//TODO
	}
	
	@Speed("1")
	private synchronized void startServer() {
		serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(PORT, 50, this.serverHost);
			this.log("SERVER STARTING ON PORT: " + this.serverSocket.getLocalSocketAddress());
			
			this.acceptClients();
		} catch (IOException e) {
			e.printStackTrace();
			this.logError("COULD NOT LISTEN ON PORT: " + PORT);
		}
	}
	
	/**
	 * Starts distribution threads:
	 * 1. The ServerThread, which accepts clients and creates connections.
	 * 2. The PacketDistributionThread, which distributes packets to clients.
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
	
	public synchronized void changeGroupPermissions(long group, long user, Level level) {
		this.changeGroupPermissions(this.getGroupByID(group), this.getUserByID(user), level);
	}
	
	public synchronized void changeGroupPermissions(BaseGroup group, User user, Level level) {
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
	public synchronized void addUser(User user) {
		this.usernames.put(user.getName(), user);
		this.users.put(user.getID(), user);
	}
	
	@Speed("1")
	public synchronized void removeUser(User user) {
		this.usernames.remove(user.getName());
		this.users.remove(user.getID());
	}
	
	@Speed("1")
	public synchronized void addConnection(Socket socket) {
		this.addConnection(this.createConnection(socket));
	}
	
	@Speed("1")
	public synchronized void addConnection(Connection connection) {
		this.unmappedConnections.add(connection);
	}

	@Slow
	@Speed("n")
	public synchronized void destroyConnection(Socket socket) {
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
	public synchronized void destroyConnection(Connection connection) {
		this.connections.remove(connection);
	}
	
	@Speed("1")
	public synchronized void inductConnection(long ID, Connection connection) {
		this.unmappedConnections.remove(connection);
		this.connections.put(ID, connection);
	}
	
	@Speed("1")
	public synchronized void deductConnection(long ID) {
		this.unmappedConnections.add(this.connections.get(ID));
		this.connections.remove(ID);
	}
	
	@Speed("1")
	private Connection createConnection(Socket socket) {
		return new Connection(this, socket);
	}
	
	@Speed("1")
	public synchronized User login(String username, int password, boolean register) throws UserNotFoundException {
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
	public synchronized void enqueuePacket(Packet packet) {
		this.packetDistributionThread.queuePacket(packet);
	}
	
	@Speed("1")
	public synchronized Connection getConnectionByUserID(long l) {
		return this.connections.get(l);
	}
	
	@Speed("1")
	public boolean isServerRunning() {
		return this.parent.isRunning();
	}
	
	@Speed("1")
	public synchronized User getUserByName(String name) {
		return this.usernames.get(name);
	}
	
	public void log(String text) {
		this.parent.outputCommandMessage(text);
		this.logToFile(text);
	}
	
	public void logError(String text) {
		text = "[ERROR]" + text;
		this.parent.outputErrorMessage(text);
		this.logToFile(text);
	}
	
	private void logToFile(String text) {
		//TODO
	}
	
	@Override
	protected Void call() throws Exception {
		this.startServer();
		
		while (this.isServerRunning() && !this.isCancelled()) {
			//TODO
		}
		
		this.close();
		
		return null;
	}
	
	protected void close() {
		try {
			this.acceptClientsThread.cancel();
			this.packetDistributionThread.cancel();
			
			for (Connection c : this.unmappedConnections) {
				c.destroy();
			}
			
			for (Connection c : this.connections.values()) {
				c.destroy();
			}
			
			this.serverSocket.close();
			
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
