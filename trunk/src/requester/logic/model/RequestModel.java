package requester.logic.model;

import requester.controller.MainController;
import requester.logic.XmlFormatter;

/**
 * 
 * @author Andrey Dyachkov
 * Created on 15.07.2013
 */
public class RequestModel extends Model {

    private static RequestModel model;
    private String url;
    private String request;

    private RequestModel() {

    }

    public synchronized static RequestModel getInstance() {

        if (model == null) {
            model = new RequestModel();
        }

        return model;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {

        String oldUrl = this.url;
        this.url = url;
        firePropertyChange(MainController.REQUEST_URL_PROPERTY, oldUrl, this.url);
    }

    public String getRequest() {

        return request;
    }

    public void setRequest(String request) {

        String oldRequest = this.request;
        this.request = XmlFormatter.formatXml(request);
        firePropertyChange(MainController.REQUEST_TEXT_PROPERTY, oldRequest, this.request);
    }

}
