package com.ryanafzal.io.chat.core.resources.thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import com.ryanafzal.io.chat.core.resources.misc.Speed;
import com.ryanafzal.io.chat.core.server.Server;

import javafx.concurrent.Task;

public abstract class FileThread extends Task<Void> {
	
	private File file;
	
	private FileReader filereader;
	private FileWriter filewriter;
	
	private BufferedReader reader;
	private BufferedWriter writer;
	
	private LinkedList<String> out;
	private LinkedList<String> in;
	
	@Speed("n")
	public FileThread(Server server, String filename) throws IOException, FileNotFoundException {
		this.file = new File(filename);
		this.file.createNewFile();
		
		this.filereader = new FileReader(this.file);
		this.filewriter = new FileWriter(this.file);
		
		this.reader = new BufferedReader(this.filereader);
		this.writer = new BufferedWriter(this.filewriter);
		
		this.out = new LinkedList<String>();
		this.in = new LinkedList<String>();
		
		String line = this.reader.readLine();
		while (line != null) {
			this.out.addFirst(line);
			line = this.reader.readLine();
		}
	}

	@Override
	protected Void call() throws Exception {
		while (!this.isCancelled()) {
			if (!this.in.isEmpty()) {
				this.writeLine();
			}
		}
		
		//Cleanup resources
		this.reader.close();
		this.writer.close();
		this.filereader.close();
		this.filewriter.close();
		
		return null;
	}
	
	public boolean hasUnread() {
		return this.out.isEmpty();
	}
	
	public String pop() {
		return this.out.removeLast();
	}
	
	public void push(String string) {
		this.in.addFirst(string);
	}
	
	private void writeLine() throws IOException {
		this.writer.write(this.in.removeLast());
	}

}
