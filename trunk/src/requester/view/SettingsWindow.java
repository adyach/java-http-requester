package requester.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import requester.controller.MainController;

/**
 * 
 * @author Andrey Dyachkov
 * Created on 16.07.2013
 */
public class SettingsWindow extends JFrame implements View {

    private static final long serialVersionUID = 4035751709461717703L;

    private final MainController controller;

    public SettingsWindow(MainController controller, Point location) {

        this.controller = controller;
        setTitle("Settings");
        setContentPane(createPane());
        setSize(180, 150);
        setResizable(false);
        setVisible(true);
        setLocation(location);
    }

    private JPanel createPane() {

        final JPanel pane = new JPanel();
        final JLabel timeoutLabel = new JLabel("Timeout");
        final JTextField timeout = new JTextField(3);
        final JLabel methodLabel = new JLabel("Method");
        final JComboBox methods = new JComboBox(new String[] {"POST", "GET"});
        final JLabel cyclesLabel = new JLabel("Cycles (0 - infinite)");
        final JTextField cycles = new JTextField(3);

        final JButton confirmButton = new JButton("Save");
        final JButton cancelButton = new JButton("Cancel");

        // set current state
        timeout.setText(String.valueOf(controller.getCurrentTimeout()));
        cycles.setText(String.valueOf(controller.getCurrentCyclesCount()));

        confirmButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                controller.changeTimeout(Integer.valueOf(timeout.getText()));
                controller.changeMethod(String.valueOf(methods.getSelectedItem()));
                controller.changeCycles(Long.valueOf(cycles.getText()));

                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();
            }
        });

        //        final GridLayout experimentLayout = new GridLayout(0, 2);
        GridBagConstraints c = new GridBagConstraints();
        pane.setLayout(new GridBagLayout());

        // timeout
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        pane.add(timeoutLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        pane.add(timeout, c);

        // methods
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        pane.add(methodLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        pane.add(methods, c);

        // cycles
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        pane.add(cyclesLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        pane.add(cycles, c);

        // confirmation or cancelation
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        pane.add(cancelButton, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        pane.add(confirmButton, c);

        return pane;
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {

    }

}
