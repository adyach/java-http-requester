package requester.view;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * 
 * @author Andrey Dyachkov
 * Created on 16.07.2013
 */
public class P12Filter extends FileFilter {

    @Override
    public boolean accept(File f) {

        if (f.getName().endsWith(".p12")) {
            return true;
        }

        return false;
    }

    @Override
    public String getDescription() {

        return "P12";
    }

}
