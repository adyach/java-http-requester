package requester.logic.model;

import java.io.File;

import requester.controller.MainController;

/**
 * 
 * @author Andrey Dyachkov
 * Created on 16.07.2013
 */
public class FileModel extends Model {

    private static FileModel model;
    private File file;

    private FileModel() {

    }

    public synchronized static FileModel getInstance() {

        if (model == null) {
            model = new FileModel();
        }

        return model;
    }

    public File getFile() {

        return file;
    }

    public void setFile(File file) {

        File oldFile = this.file;
        this.file = file;
        firePropertyChange(MainController.FILE_PROPERTY, oldFile, file);
    }

}
