package requester.logic.model;

import requester.controller.MainController;
import requester.logic.XmlFormatter;

/**
 * 
 * @author Andrey Dyachkov
 * Created on 16.07.2013
 */
public class ResponseModel extends Model {

    private static ResponseModel model;
    private String responseText;
    private int responseCode;
    private int responseSize;
    private long responseTime;

    public synchronized static ResponseModel getInstance() {

        if (model == null) {
            model = new ResponseModel();
        }

        return model;
    }

    private ResponseModel() {

    }

    public void setResponseText(String responseText) {

        final String oldresponseText = this.responseText;
        this.responseText = XmlFormatter.formatXml(responseText);
        firePropertyChange(MainController.RESPONSE_TEXT_PROPERTY, oldresponseText, this.responseText);
    }

    public void setResponseCode(int responseCode) {

        final int oldResponseCode = this.responseCode;
        this.responseCode = responseCode;
        firePropertyChange(MainController.RESPONSE_CODE_PROPERTY, oldResponseCode, this.responseCode);
    }

    public void setResponseSize(int responseSize) {

        final int oldResponseSize = this.responseSize;
        this.responseSize = responseSize;
        firePropertyChange(MainController.RESPONSE_SIZE_PROPERTY, oldResponseSize, this.responseSize);
    }

    public void setResponseTime(long responseTime) {

        final long oldResponseTime = this.responseTime;
        this.responseTime = responseTime;
        firePropertyChange(MainController.RESPONSE_TIME_PROPERTY, oldResponseTime, this.responseTime);
    }

    public String getResponseText() {

        return responseText;
    }

    public int getResponseCode() {

        return responseCode;
    }

    public int getResponseSize() {

        return responseSize;
    }

    public long getResponseTime() {

        return responseTime;
    }

}
