package logic;

/**
 * 
 * @author Andrey Dyachkov
 * Created on 02.07.2013
 */
public class Response {

    private final String responseText;
    private final int responseCode;
    private final int responseSize;
    private final long responseTime;

    Response(String responseText, int responseCode, int responseSize, long responseTime) {

        this.responseText = responseText;
        this.responseCode = responseCode;
        this.responseSize = responseSize;
        this.responseTime = responseTime;
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
