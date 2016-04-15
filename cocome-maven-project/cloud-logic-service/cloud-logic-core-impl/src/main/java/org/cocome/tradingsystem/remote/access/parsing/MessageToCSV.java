package org.cocome.tradingsystem.remote.access.parsing;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



public class MessageToCSV {
	
	private String message;
	
	public MessageToCSV(String message) {
		this.message = message;
	}
	
	public String getCSV()  {
		if (message != null) {
			 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			 DocumentBuilder db;
			 InputSource is = new InputSource();
			 is.setCharacterStream(new StringReader(this.message));
			 try {	
				 	 db = dbf.newDocumentBuilder();
					 Document doc = db.parse(is);
				     doc.getDocumentElement().normalize();
				     NodeList nodes = doc.getElementsByTagName("BodyEntry");
				     
				     for (int i = 0; i < nodes.getLength(); i++) {
						 Node node = nodes.item(i);
				
						 if (node.getNodeType() == Node.ELEMENT_NODE ) {
						        Element element = (Element) node;
						      if (element.getAttribute("name").equalsIgnoreCase("result")) {  
						        return getValue("Content", element);
						      }
						 }
				     }
			 } catch (IOException | ParserConfigurationException | SAXException e) {
				 message = "Error with Parser!";
			 }
		}
	     return "Error";
	}
	private  String getValue(String tag, Element element) {
		 NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		 Node node = (Node) nodes.item(0);
		 return node.getNodeValue();
	 }
}
