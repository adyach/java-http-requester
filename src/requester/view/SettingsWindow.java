package requester.view;

import java.beans.PropertyChangeEvent;

import javax.swing.JFrame;

/**
 * 
 * @author Andrey Dyachkov
 * Created on 16.07.2013
 */
public class SettingsWindow extends JFrame implements View {

    private static final long serialVersionUID = 4035751709461717703L;

    public SettingsWindow() {

        setTitle("Settings");
        setSize(300, 300);
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {

    }

}
