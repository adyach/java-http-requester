package requester.logic.model;

import java.util.HashMap;
import java.util.Map;

import requester.controller.HttpHeadersController;

public class HttpHeadersModel extends Model {

	private static HttpHeadersModel model;
	private Map<String, String> headers = new HashMap<String, String>();

	private HttpHeadersModel() {

	}

	public synchronized static HttpHeadersModel getInstance() {

		if (model == null) {
			model = new HttpHeadersModel();
		}
		return model;
	}

	public void setHttpHeader(String[] header) {
		if (!header[0].isEmpty()) {
			headers.put(header[0], header[1]);
			firePropertyChange(HttpHeadersController.HEADER_PROPERTY, null, headers);	
		}
	}

	public Map<String, String> getHttpHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers2) {
		this.headers = headers2;
	}

}
