package requester.logic.xml;


public class Response extends Element {

	public Response(final Request request) {
		super("response");

		this.addChild(request.getHeader());
		this.addChild(new Element("body"));
	}

	public Element getHeader() {
		return this.getChild("header");
	}

	public Element getBody() {
		return this.getChild("body");
	}

}
