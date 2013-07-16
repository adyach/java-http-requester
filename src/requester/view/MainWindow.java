package requester.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.nio.charset.Charset;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import requester.controller.MainController;

public class MainWindow extends View implements Runnable {

    private static final int KILLOBYTE = 1024;
    private static final String RESPONSE_CODE = "Response code: ";
    private static final String RESPONSE_SIZE = "Response size: ";
    private static final String RESPONSE_TIME = "Response time: ";
    private static final String FORMAT_BUTTON_NAME = "Format";
    private static final String OPEN_BUTTON_NAME = "Open";
    private static final String SAVE_BUTTON_NAME = "Save";
    private static final String ABOUT_BUTTON_NAME = "About";
    private static final String SEND_BUTTON_NAME = "Send";

    private final JTextArea requestField = new JTextArea(10, 50);
    private final JTextField url = new JTextField(30);
    private static final JTextArea responseField = new JTextArea(10, 50);
    private static final JProgressBar progressBar = new JProgressBar();
    private static final JTextArea codeArea = new JTextArea(RESPONSE_CODE);
    private static final JTextArea sizeArea = new JTextArea(RESPONSE_SIZE);
    private static final JTextArea timeArea = new JTextArea(RESPONSE_TIME);

    private final MainController controller;

    public MainWindow(MainController controller) {

        this.controller = controller;
        this.controller.addView(this);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals(MainController.RESPONSE_TIME_PROPERTY)) {
            final String time = evt.getNewValue().toString();
            timeArea.append(time + " Ms");
        } else if (evt.getPropertyName().equals(MainController.RESPONSE_CODE_PROPERTY)) {
            final String code = evt.getNewValue().toString();
            codeArea.append(code);
        } else if (evt.getPropertyName().equals(MainController.RESPONSE_SIZE_PROPERTY)) {
            final String size = evt.getNewValue().toString();
            sizeArea.append(size + " Bytes");
        } else if (evt.getPropertyName().equals(MainController.RESPONSE_TEXT_PROPERTY)) {
            final String text = evt.getNewValue().toString();
            responseField.setText(text);
        } else if (evt.getPropertyName().equals(MainController.REQUEST_URL_PROPERTY)) {
            final String urlText = evt.getNewValue().toString();
            url.setText(urlText);
        } else if (evt.getPropertyName().equals(MainController.REQUEST_TEXT_PROPERTY)) {
            final String text = evt.getNewValue().toString();
            requestField.setText(text);
        }
    }

    private void createAndShowGui() {

        JFrame frame = createFrame();
        frame.setVisible(true);
    }

    private JFrame createFrame() {

        JFrame frame = new JFrame("Requester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(createPane());
        frame.setJMenuBar(createMenu());
        frame.setSize(600, 500);
        frame.setResizable(false);
        return frame;
    }

    private JPanel createPane() {

        final JPanel pane = new JPanel();
        final JButton button = createButton();
        button.setPreferredSize(new Dimension(500, 30));

        requestField.setText("Request");
        responseField.setText("Response");

        final JScrollPane scrollRequest = new JScrollPane(requestField);
        final JScrollPane scrollResponse = new JScrollPane(responseField);

        codeArea.setEditable(false);
        codeArea.setColumns(15);
        sizeArea.setEditable(false);
        sizeArea.setColumns(15);
        timeArea.setEditable(false);
        timeArea.setColumns(15);

        pane.add(url);
        pane.add(progressBar);
        pane.add(scrollRequest);
        pane.add(scrollResponse);
        pane.add(button);
        pane.add(codeArea);
        pane.add(sizeArea);
        pane.add(timeArea);

        return pane;
    }

    private JMenuBar createMenu() {

        final JMenuBar menu = new JMenuBar();
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new XmlFilter());
        fileChooser.setFileFilter(new RifFilter());

        final JMenuItem open = new JMenuItem(OPEN_BUTTON_NAME);
        menu.add(open);
        open.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                fileChooser.showOpenDialog(null);

                final File selectedFile = fileChooser.getSelectedFile();
                controller.changeFile(selectedFile);
                controller.openFile();
            }
        });

        final JMenuItem save = new JMenuItem(SAVE_BUTTON_NAME);
        menu.add(save);
        save.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                fileChooser.showSaveDialog(null);

                final File selectedFile = fileChooser.getSelectedFile();
                controller.changeFile(selectedFile);
                controller.executeSave();
            }
        });

        final JMenuItem format = new JMenuItem(FORMAT_BUTTON_NAME);
        menu.add(format);
        format.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                controller.changeRequest(requestField.getText());
                controller.changeResponse(responseField.getText());
            }
        });

        final JMenuItem about = new JMenuItem(ABOUT_BUTTON_NAME);
        menu.add(about);
        about.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                final String newLine = System.getProperty("line.separator");
                final StringBuilder sBuilder = new StringBuilder()
                                .append("Requester v1.0.")
                                .append(newLine)
                                .append(" Andrey Dyachkov")
                                .append(newLine)
                                .append(" andrey.dyachkov@gmail.com");
                JOptionPane.showMessageDialog(null, sBuilder, "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return menu;
    }

    private JButton createButton() {

        JButton send = new JButton(SEND_BUTTON_NAME);
        send.addActionListener(new ActionListener() {

            private int MIN_URL_LENGTH = 10;

            @Override
            public void actionPerformed(ActionEvent e) {

                final String urlStr = url.getText();
                String requestStr;
                requestStr = new String(requestField.getText().getBytes(Charset.forName("UTF-8")));

                codeArea.setText(RESPONSE_CODE);
                sizeArea.setText(RESPONSE_SIZE);
                timeArea.setText(RESPONSE_TIME);

                if (urlStr.isEmpty() || urlStr.length() < MIN_URL_LENGTH) {
                    JOptionPane.showMessageDialog(null, "URL can't be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                controller.changeUrl(urlStr);
                controller.changeRequest(requestStr);
                controller.executeRequest();
            }
        });
        return send;
    }

    @Override
    public void run() {

        createAndShowGui();
    }

}
