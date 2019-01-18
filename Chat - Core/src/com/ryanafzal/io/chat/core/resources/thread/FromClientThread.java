package com.ryanafzal.io.chat.core.resources.thread;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import com.ryanafzal.io.chat.core.server.Connection;
import com.ryanafzal.io.chat.core.server.Server;

import javafx.concurrent.Task;

import com.ryanafzal.io.chat.core.resources.command.RunnableCommand;
import com.ryanafzal.io.chat.core.resources.sendable.Packet;
import com.ryanafzal.io.chat.core.resources.sendable.PacketCommand;
import com.ryanafzal.io.chat.core.resources.sendable.PacketContents;
import com.ryanafzal.io.chat.core.resources.sendable.PacketData;

public class FromClientThread extends Task<Void> {

	private Socket socket;
	private Server server;
	private Connection connection;

	public FromClientThread(Socket socket, Server server, Connection connection) {
		this.socket = socket;
		this.server = server;
		this.connection = connection;
	}

	@Override
	public Void call() {
		try (ObjectInputStream fromServerThread = new ObjectInputStream(this.socket.getInputStream())) {

			while (!this.socket.isClosed() && this.server.isRunning() && !this.isCancelled()) {
				try {
					Packet input = (Packet) fromServerThread.readObject();
					
					if (input.getPacketData().ADDRESSTYPE == PacketData.AddressType.SERVER) {
						PacketContents contents = input.getPacketContents();
						if (contents instanceof PacketCommand) {
							PacketCommand cmd = (PacketCommand) contents;
							
							if (cmd.COMMAND.getType() == RunnableCommand.Type.SERVER) {
								cmd.COMMAND.run(this.server);
							} else if (cmd.COMMAND.getType() == RunnableCommand.Type.CONNECTION) {
								cmd.COMMAND.run(this.connection);
							}
						}
					} else {
						this.server.enqueuePacket(input);
					}

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					break;
				} catch (EOFException e) {
					e.printStackTrace();
					break;
				} catch (SocketException e) {
					e.printStackTrace();
					break;
				}
			}
			fromServerThread.close();
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
		return null;
	}

}
