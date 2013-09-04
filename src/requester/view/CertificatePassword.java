package requester.view;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import requester.controller.MainController;

/**
 * 
 * @author Andrey Dyachkov
 * Created on 16.07.2013
 */
public class CertificatePassword extends JFrame implements View {

    private static final long serialVersionUID = 3492422123079354721L;
    private final MainController controller;

    public CertificatePassword(MainController controller, Point location) {

        this.controller = controller;
        setTitle("Certificate password");
        setSize(200, 70);
        setContentPane(createPane());
        setResizable(false);
        setVisible(true);
        setLocation(location);
    }

    private JPanel createPane() {

        final JPanel pane = new JPanel();
        final JTextField pwdField = new JTextField(10);
        final JButton confirmButton = new JButton("Save");
        confirmButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                controller.addCertificatePassword(pwdField.getText());
                dispose();
            }
        });

        pane.add(pwdField);
        pane.add(confirmButton);

        return pane;
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {

    }

}
