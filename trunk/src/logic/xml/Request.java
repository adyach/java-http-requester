package logic.xml;

public final class Request extends Element{

	private Element header;
	private Element body;
	
	public Request(Element element) {
		
		super(element);
		
		header = element.getChild("header");
		body = element.getChild("body");
	}
	
	public Request(String tagName) {
		
		super(tagName);
		
		header = this.getChild("header");
		body = this.getChild("body");
	}

	public Element getHeader() {
		return header;
	}

	public Element getBody() {
		return body;
	}
	
	public String getMethod() {
		return header.getChild("method").getValue().trim();
	}

}
