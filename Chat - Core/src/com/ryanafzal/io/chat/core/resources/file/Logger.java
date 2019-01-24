package com.ryanafzal.io.chat.core.resources.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	
	private FileWriter logger;
	
	private boolean ready;
	
	public Logger(String file) {
		this(new File(file));
	}
	
	public Logger(File file) {
		this.ready = false;
		try {
			this.logger = new FileWriter(file);
			
			this.ready = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean log(String line) {
		if (this.ready) {
			try {
				this.logger.write(line + "\n");
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

}
