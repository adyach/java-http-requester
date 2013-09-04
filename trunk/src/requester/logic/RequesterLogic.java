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

    private static ExecutorService executors = Executors.newFixedThreadPool(1);
    private static final FileHelper fileHelper = new FileHelper();
    private static final RequestModel requestModel = RequestModel.getInstance();
    private static final long INFINITE_CYCLE = 0;
    private static boolean started = true;

    public synchronized static void executeRequest() {

        requestModel.setCurrentCycles(0L);

        if (executors.isTerminated()) {
            executors = Executors.newFixedThreadPool(1);
        }

        if (requestModel.getCycles() == INFINITE_CYCLE) {
            started = true;

            new Thread(new Runnable() {

                @Override
                public void run() {

                    while (started) {

                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        final PostRequester postRequester = new PostRequester();
                        executors.submit(postRequester);
                    }
                }
            }).start();
        } else {
            new Thread(new Runnable() {

                @Override
                public void run() {

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    for (long i = 0; i < requestModel.getCycles(); i++) {
                        final PostRequester postRequester = new PostRequester();
                        executors.submit(postRequester);
                    }
                }
            }).start();

        }
    }

    public synchronized static void stopRequest() {

        started = false;
        executors.shutdownNow();
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

    public static long getCyclesCount() {

        return requestModel.getCycles();
    }

    public static int getTimeout() {

        return requestModel.getTimeout();
    }
}
