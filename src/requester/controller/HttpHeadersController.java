package requester.controller;

import requester.logic.model.HttpHeadersModel;

public class HttpHeadersController extends Controller {

	public static final String HEADER_PROPERTY = "HttpHeader";

	public void addHeader(String[] header) {
		setModelProperty(HEADER_PROPERTY, header);
	}

	public void removeHeader(String key) {
		HttpHeadersModel.getInstance().getHttpHeaders().remove(key);
	}
}
