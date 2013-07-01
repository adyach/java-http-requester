package xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ParserHelper extends DefaultHandler {

	private Element rootElement;
	private Element currentElement;
	private StringBuilder tmpValue = new StringBuilder();

	public String renderXml(Element element) throws UnsupportedEncodingException {

		final SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			final TransformerHandler th = tf.newTransformerHandler();
			final OutputStreamWriter writer = new OutputStreamWriter(baos, "utf-8");

			th.setResult(new StreamResult(writer));
			th.startDocument();
			element.renderElement(th);
			th.endDocument();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
		}
		
		//return baos.toString();
		return new String(baos.toByteArray(), "utf-8");
	}

	String renderXml() {

		final SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			final TransformerHandler th = tf.newTransformerHandler();
			final OutputStreamWriter writer = new OutputStreamWriter(baos, "UTF-8");

			th.setResult(new StreamResult(writer));
			th.startDocument();
			rootElement.renderElement(th);
			th.endDocument();
		} catch (SAXException e) {

		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
		}

		return baos.toString();
	}

	public void parse(InputStream xml) {
		SAXParserFactory factory = SAXParserFactory.newInstance();

		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(xml, this);
		} catch (ParserConfigurationException e) {
			System.err.println("ParserConfig error");			
		} catch (SAXException e) {
			System.err.println("SAXException : xml not well formed");
		} catch (IOException e) {
			System.err.println("IO error");
		}
	}

	@Override
	public void startDocument() throws SAXException {

	}

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

		if (this.rootElement == null) {
			this.rootElement = new Element(qName);
			this.currentElement = this.rootElement;
		} else {
			final Element element = new Element(qName);
			element.setParent(currentElement);
			currentElement.addChild(qName, element);
			currentElement = element;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		currentElement.addValue(tmpValue.toString());
		currentElement = currentElement.getParent();
		tmpValue.setLength(0);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		tmpValue.append(ch, start, length);
	}

	@Override
	public void endDocument() throws SAXException {
	}

	public Element getElement() {

		return rootElement;
	}
}
