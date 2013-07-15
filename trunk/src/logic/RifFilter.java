package logic;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * 
 * @author Andrey Dyachkov
 * Created on 02.07.2013
 */
public class RifFilter extends FileFilter {

    @Override
    public boolean accept(File f) {

        if (f.getName().endsWith(".rif")) {
            return true;
        }

        return false;
    }

    @Override
    public String getDescription() {

        return "Requester Internal Format (*.rif)";
    }

}
