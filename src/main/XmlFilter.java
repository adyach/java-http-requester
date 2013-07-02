package main;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * 
 * @author Andrey Dyachkov
 * Created on 02.07.2013
 */
public class XmlFilter extends FileFilter {

    @Override
    public boolean accept(File f) {

        if (f.getName().endsWith(".xml")) {
            return true;
        }

        return false;
    }

    @Override
    public String getDescription() {

        return "XML";
    }

}
