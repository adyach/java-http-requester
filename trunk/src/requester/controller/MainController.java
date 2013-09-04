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
    public static final String PROGRESS_PROPERTY = "Progress";
    public static final String TIMEOUT_PROPERTY = "Timeout";
    public static final String METHOD_PROPERTY = "Method";
    public static final String CYCLES_PROPERTY = "Cycles";
    public static final String CURRENT_CYCLES_PROPERTY = "CurrentCycles";

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

    public void stopRequest() {

        RequesterLogic.stopRequest();
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

    public void changeTimeout(int timeout) {

        setModelProperty(TIMEOUT_PROPERTY, timeout);
    }

    public void changeMethod(String method) {

        setModelProperty(METHOD_PROPERTY, method);
    }

    public void changeCycles(long cycles) {

        setModelProperty(CYCLES_PROPERTY, cycles);
    }

    public long getCurrentCyclesCount() {

        return RequesterLogic.getCyclesCount();
    }

    public int getCurrentTimeout() {

        return RequesterLogic.getTimeout();
    }

}
