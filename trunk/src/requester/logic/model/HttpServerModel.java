package requester.logic.model;

import requester.view.HttpServerWindow;

public class HttpServerModel extends Model {

	private static final int AROUND_MAX_BUFFER = 950;
	private static final int MAX_BUFFER = 1000;
	private static HttpServerModel model;
	private StringBuilder data;
	
	public HttpServerModel() {
		data = new StringBuilder(MAX_BUFFER);
	}
	
	public static synchronized HttpServerModel getInstance() {
		if (model == null) {
			model = new HttpServerModel();
		}
		return model;
	}

	public void setData(String data) {
		
		if (this.data.length() + data.length() > AROUND_MAX_BUFFER) {
			this.data.replace(0, data.length(), ""); // clear some space to insert new lines
		}		
		data = data + "\n";
		this.data.append(data);
		firePropertyChange(HttpServerWindow.DATA_PROPERTY, null, data);
	}

	public StringBuilder getData() {
		return this.data;
	}
}
