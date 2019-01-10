package com.ryanafzal.io.chat.core.resources.thread;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import com.ryanafzal.io.chat.core.client.Client;
import com.ryanafzal.io.chat.core.resources.command.RunnableCommand;
import com.ryanafzal.io.chat.core.resources.sendable.Packet;
import com.ryanafzal.io.chat.core.resources.sendable.PacketCommand;
import com.ryanafzal.io.chat.core.resources.sendable.PacketContents;
import com.ryanafzal.io.chat.core.resources.sendable.PacketMessage;

public class FromServerThread implements Runnable {

	private Socket socket;
	private Client client;

	public FromServerThread(Socket socket, Client client) {
		this.socket = socket;
		this.client = client;
	}

	@Override
	public void run() {
		try (ObjectInputStream fromServerThread = new ObjectInputStream(this.socket.getInputStream())) {

			while (!this.socket.isClosed() && this.client.isRunning()) {
				try {
					Packet input = (Packet) fromServerThread.readObject();
					PacketContents contents = input.getPacketContents();
					if (contents instanceof PacketMessage) {
						this.client.outputPacketMessage((PacketMessage) contents, input.getPacketData());
					} else if (contents instanceof PacketCommand) {
						PacketCommand cmd = (PacketCommand) contents;
						
						if (cmd.COMMAND.getType() == RunnableCommand.Type.CLIENT) {
							cmd.COMMAND.run(this.client);
						}
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
	}


}
