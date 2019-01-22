package com.ryanafzal.io.chat.core.resources.file;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ryanafzal.io.chat.core.resources.user.User;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public class UserFileAccessor extends XMLFileAccessor<User> {

	protected UserFileAccessor(String filepath) throws ParserConfigurationException, SAXException, IOException {
		super(filepath, "user");
	}

	@Override
	protected User forEach(Node node) {
		NodeList children = node.getChildNodes();
		
		String name = "Malformed XML";
		int password = 0;
		long id;
		
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			
			
		}
		
		return null;//TODO
	}
	
}
