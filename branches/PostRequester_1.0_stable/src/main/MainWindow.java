package main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
import javax.swing.SwingUtilities;

import requester.FileHelper;
import requester.FileHelper.RIF;
import requester.PostRequester;
import requester.Response;
import requester.XmlFormatter;

public class MainWindow implements Runnable {

    private static final int KILLOBYTE = 1024;
    private static final String HTTP = "https://";
    private static final String RESPONSE_CODE = "Response code: ";
    private static final String RESPONSE_SIZE = "Response size: ";
    private static final String RESPONSE_TIME = "Response time: ";
    private static final String FORMAT_BUTTON_NAME = "Format";
    private static final String OPEN_BUTTON_NAME = "Open";
    private static final String SAVE_BUTTON_NAME = "Save";
    private static final String ABOUT_BUTTON_NAME = "About";
    private static final String SEND_BUTTON_NAME = "Send";
    private static final String RIF_EXT = "rif";
    private static final String XML_EXT = "xml";

    private final JTextArea requestField = new JTextArea(10, 50);
    private final JTextField url = new JTextField(HTTP, 30);
    private static final JTextArea responseField = new JTextArea(10, 50);
    private static final JProgressBar progressBar = new JProgressBar();
    private static final JTextArea codeArea = new JTextArea(RESPONSE_CODE);
    private static final JTextArea sizeArea = new JTextArea(RESPONSE_SIZE);
    private static final JTextArea timeArea = new JTextArea(RESPONSE_TIME);

    private final ExecutorService executors = Executors.newFixedThreadPool(1);
    private static Future<Response> futureResponse;

    public static void main(String[] args) throws Exception {

        SwingUtilities.invokeLater(new MainWindow());

        while (true) {
            Thread.sleep(1000);

            try {
                if (futureResponse != null && futureResponse.isDone()) {

                    final Response response = futureResponse.get();
                    responseField.setText(XmlFormatter.formatXml(response.getResponseText()));
                    codeArea.append(String.valueOf(response.getResponseCode()));
                    sizeArea.append(String.valueOf(response.getResponseSize() / KILLOBYTE) + " Kb");
                    timeArea.append(String.valueOf(response.getResponseTime()) + " Ms");

                    progressBar.setIndeterminate(false);
                    futureResponse = null;
                } else if (futureResponse != null && futureResponse.isCancelled()) {
                    futureResponse = null;
                    progressBar.setIndeterminate(false);
                }
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (ExecutionException e1) {
                e1.printStackTrace();
            }
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
                String fileStr = null;
                if (selectedFile.getName().endsWith(XML_EXT)) {
                    fileStr = FileHelper.readFileXml(selectedFile);
                } else if (selectedFile.getName().endsWith(RIF_EXT)) {
                    final RIF rif = FileHelper.readFileRif(selectedFile);
                    fileStr = rif.getData();
                    url.setText(rif.getUrl());
                }

                requestField.setText(XmlFormatter.formatXml(fileStr));
            }
        });

        final JMenuItem save = new JMenuItem(SAVE_BUTTON_NAME);
        menu.add(save);
        save.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                fileChooser.showSaveDialog(null);

                final File selectedFile = fileChooser.getSelectedFile();
                FileHelper.saveFileRIF(selectedFile, requestField.getText(), url.getText());
            }
        });

        final JMenuItem format = new JMenuItem(FORMAT_BUTTON_NAME);
        menu.add(format);
        format.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                requestField.setText(XmlFormatter.formatXml(requestField.getText()));
                responseField.setText(XmlFormatter.formatXml(responseField.getText()));
            }
        });

        final JMenuItem about = new JMenuItem(ABOUT_BUTTON_NAME);
        menu.add(about);
        about.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                final String newLine = System.getProperty("line.separator");

                final JOptionPane dialog = new JOptionPane("About");
                dialog.showMessageDialog(null, "Requester v1.0." + newLine + " Andrey Dyachkov" + newLine + " andrey.dyachkov@gmail.com");
            }
        });

        return menu;
    }

    private JButton createButton() {

        JButton button = new JButton(SEND_BUTTON_NAME);
        button.addActionListener(new ActionListener() {

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
                    final JOptionPane dialog = new JOptionPane("Error");
                    dialog.showMessageDialog(null, "URL can't be empty");
                    return;
                }

                final PostRequester postRequester = new PostRequester(urlStr, requestStr);
                futureResponse = executors.submit(postRequester);
                progressBar.setIndeterminate(true);
            }
        });
        return button;
    }

    @Override
    public void run() {

        createAndShowGui();
    }

}
