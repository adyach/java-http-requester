package requester.logic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import requester.logic.model.RequestModel;

/**
 * 
 * @author Andrey Dyachkov
 * Created on 16.07.2013
 */
public class RequesterLogic {

    private static final ExecutorService executors = Executors.newFixedThreadPool(1);
    private static final FileHelper fileHelper = new FileHelper();
    private static final RequestModel requestModel = RequestModel.getInstance();

    public static void executeRequest() {

        final PostRequester postRequester = new PostRequester();
        executors.submit(postRequester);
    }

    public static void executeFileSave() {

        fileHelper.saveFileRIF();
    }

    public static void executeFileOpen() {

        fileHelper.readFile();
    }

    public static void removeCertifivate() {

        requestModel.setCertificate(null);
    }

    public static void removeCertifivatePassword() {

        requestModel.setPassword(null);
    }
}
