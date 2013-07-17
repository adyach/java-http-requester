package requester.controller;

import java.io.File;

import requester.logic.RequesterLogic;

/**
 * 
 * @author Andrey Dyachkov
 * Created on 15.07.2013
 */
public class MainController extends Controller {

    public static final String REQUEST_URL_PROPERTY = "Url";
    public static final String REQUEST_TEXT_PROPERTY = "Request";
    public static final String RESPONSE_TIME_PROPERTY = "ResponseTime";
    public static final String RESPONSE_SIZE_PROPERTY = "ResponseSize";
    public static final String RESPONSE_CODE_PROPERTY = "ResponseCode";
    public static final String RESPONSE_TEXT_PROPERTY = "ResponseText";
    public static final String FILE_PROPERTY = "File";
    public static final String CERT_PROPERTY = "Certificate";
    public static final String CERT_PWD_PROPERTY = "Password";

    public void changeUrl(String url) {

        setModelProperty(REQUEST_URL_PROPERTY, url);
    }

    public void changeRequest(String request) {

        setModelProperty(REQUEST_TEXT_PROPERTY, request);
    }

    public void changeResponse(String response) {

        setModelProperty(RESPONSE_TEXT_PROPERTY, response);
    }

    public void changeFile(File file) {

        setModelProperty(FILE_PROPERTY, new File(file.getAbsolutePath()));
    }

    public void executeRequest() {

        RequesterLogic.executeRequest();
    }

    public void executeSave() {

        RequesterLogic.executeFileSave();
    }

    public void openFile() {

        RequesterLogic.executeFileOpen();
    }

    public void addCertificate(File cert) {

        File file = null;
        if (cert != null) {
            file = new File(cert.getAbsolutePath());
        }

        setModelProperty(CERT_PROPERTY, file);
    }

    public void addCertificatePassword(String pwd) {

        setModelProperty(CERT_PWD_PROPERTY, pwd);
    }

    public void removeCertificate() {

        RequesterLogic.removeCertifivate();
    }

    public void removeCertificatePassword() {

        RequesterLogic.removeCertifivatePassword();
    }

}
