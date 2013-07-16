package requester.logic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author Andrey Dyachkov
 * Created on 16.07.2013
 */
public class RequesterLogic {

    private static final ExecutorService executors = Executors.newFixedThreadPool(1);
    private static final FileHelper fileHelper = new FileHelper();

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
}
