package com.ryanafzal.io.chat.core.resources.file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class XMLFileAccessor<T> {
	
	protected Document doc;
	protected Node root;
	protected NodeList nodes;
	
	protected XMLFileAccessor(String filepath, String nodePattern) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		this.doc = docBuilder.parse(filepath);
		
		this.root = this.doc.getFirstChild();
		this.nodes = this.doc.getElementsByTagName(nodePattern);
	}
	
	public List<T> readAllNodes() {
		List<T> output = new ArrayList<T>(this.nodes.getLength());
		
		for (int i = 0; i < nodes.getLength(); i++) {
			output.add(this.forEach(this.nodes.item(i)));
		}
		
		return output;
	}
	
	protected abstract T forEach(Node node);

}
