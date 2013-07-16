package requester;

import javax.swing.SwingUtilities;

import requester.controller.MainController;
import requester.logic.model.FileModel;
import requester.logic.model.RequestModel;
import requester.logic.model.ResponseModel;
import requester.view.MainWindow;

/**
 * 
 * @author Andrey Dyachkov
 * Created on 15.07.2013
 */
public class Requester {

    public static void main(String[] args) throws Exception {

        final MainController mainController = new MainController();

        mainController.addModel(RequestModel.getInstance());
        mainController.addModel(ResponseModel.getInstance());
        mainController.addModel(FileModel.getInstance());

        SwingUtilities.invokeLater(new MainWindow(mainController));
    }
}
