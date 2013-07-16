package requester.logic.model;

import java.io.File;

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
    private File certificate;
    private transient String password;

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

    public File getCertificate() {

        return certificate;
    }

    public void setCertificate(File certificate) {

        File oldCertificate = this.certificate;
        this.certificate = certificate;
        firePropertyChange(MainController.CERT_PROPERTY, oldCertificate, this.certificate);
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        String oldPassword = this.password;
        this.password = password;
        firePropertyChange(MainController.CERT_PWD_PROPERTY, oldPassword, this.password);

    }
}
