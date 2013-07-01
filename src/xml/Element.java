package xml;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.SAXException;

public class Element {

	private Map<String, List<Element>> childs;
	private Element parent;
	private String value = "";
	private String tagName = "";

	Element(String tagName) {
		this.tagName = tagName;
		childs = new HashMap<String, List<Element>>();
	}

	Element(final Element element) {
		this.tagName = element.tagName;
		this.value = element.value;
		childs = new HashMap<String, List<Element>>() {
			{
				put(tagName, element.getChilds());
			}
		};
	}

	Element(String tagName, String value) {

		this.tagName = tagName;
		this.value = value;
		childs = new HashMap<String, List<Element>>();
	}

	public Element addChild(String tagName, String value) {

		Element element = new Element(tagName, value);

		addChild(tagName, element);

		return element;
	}
	
	public Element addChild(String tagName) {

		Element element = new Element(tagName);

		addChild(tagName, element);

		return element;
	}
	
	void addChilds (Map<String, List<Element>> childs) {
		this.childs.putAll(childs);
	}

	public List<Element> getChilds() {

		List<Element> elements = null;

		for (String tagName : childs.keySet()) {
			if (elements == null) {
				elements = new LinkedList<Element>();
			}

			elements.addAll(childs.get(tagName));
		}

		if (elements == null) {
			return Collections.emptyList();
		}

		return elements;
	}

	public Element getChild(String tagName) {

		if (childs.get(tagName) == null) {
			return null;
		}

		return childs.get(tagName).get(0);
	}

	public String getValue() {
		return this.value;
	}

	public String getName() {
		return this.tagName;
	}

	public Element getParent() {
		return parent;
	}

	public void setParent(Element parent) {
		this.parent = parent;
	}

	void addChild(String tagName, Element element) {

		List<Element> values = childs.get(tagName);

		if (values == null) {
			List<Element> list = new LinkedList<Element>();
			list.add(element);
			childs.put(tagName, list);
		} else {
			values.add(element);
			childs.put(tagName, values);
		}
	}
	
	void addChild(Element element) {

		addChild(element.getName(), element);
	}

	void addValue(String value) {
		this.value = value;
	}

	void renderElement(TransformerHandler transformerHandler)
			throws SAXException {		
		transformerHandler.startElement("", "", this.tagName, null);
		transformerHandler.characters(value.toCharArray(), 0, value.length());

		for (String tagName : childs.keySet()) {
			for (Element e : childs.get(tagName)) {
				e.renderElement(transformerHandler);				
			}
		}

		transformerHandler.endElement("", "", this.tagName);
	}
}
