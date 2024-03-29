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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import requester.controller.HttpHeadersController;
import requester.controller.MainController;
import requester.logic.HttpServer;
import requester.logic.model.HttpHeadersModel;

public class MainWindow implements Runnable, View {

	private static final String RESPONSE_CODE = "Response code: ";
	private static final String RESPONSE_SIZE = "Response size: ";
	private static final String RESPONSE_TIME = "Response time: ";
	private static final String CYCLES_COUNT = "Cycle: ";
	private static final String CERT = "Certificate: None";

	private static final String FORMAT_BUTTON_NAME = "Format";
	private static final String OPEN_BUTTON_NAME = "Open";
	private static final String SAVE_BUTTON_NAME = "Save";
	private static final String STOP_BUTTON_NAME = "Stop";
	private static final String ABOUT_BUTTON_NAME = "About";
	private static final String SEND_BUTTON_NAME = "Send";
	private static final String SETTINGS_BUTTON_NAME = "Settings";
	private static final String ADD_CERT_BUTTON_NAME = "Add certificate";
	private static final String REMOVE_CERT_BUTTON_NAME = "Remove certificate";

	private final JTextArea requestField = new JTextArea(10, 50);
	private final JTextField url = new JTextField("http://", 30);
	private static final JTextArea responseField = new JTextArea(10, 50);
	private static final JProgressBar progressBar = new JProgressBar();
	private static final JTextArea codeArea = new JTextArea(RESPONSE_CODE);
	private static final JTextArea sizeArea = new JTextArea(RESPONSE_SIZE);
	private static final JTextArea timeArea = new JTextArea(RESPONSE_TIME);
	private static final JTextArea certArea = new JTextArea(CERT);
	private static final JTextArea cycleArea = new JTextArea(CYCLES_COUNT);

	private static final JFrame frame = new JFrame("Http Requester");

	private final MainController controller;

	public MainWindow(MainController controller) {

		this.controller = controller;
		this.controller.addView(this);
	}

	@Override
	public void modelPropertyChange(PropertyChangeEvent evt) {

		if (evt.getPropertyName().equals(MainController.RESPONSE_TIME_PROPERTY)) {
			final String time = evt.getNewValue().toString();
			if (time.isEmpty()) {
				return;
			}

			timeArea.setText(RESPONSE_TIME + time + " Ms");
		} else if (evt.getPropertyName().equals(MainController.RESPONSE_CODE_PROPERTY)) {
			final String code = evt.getNewValue().toString();
			if (code.isEmpty()) {
				return;
			}
			codeArea.setText(RESPONSE_CODE + code);
		} else if (evt.getPropertyName().equals(MainController.RESPONSE_SIZE_PROPERTY)) {
			final String size = evt.getNewValue().toString();
			if (size.isEmpty()) {
				return;
			}
			sizeArea.setText(RESPONSE_SIZE + size + " Bytes");
		} else if (evt.getPropertyName().equals(MainController.RESPONSE_TEXT_PROPERTY)) {
			final String text = evt.getNewValue().toString();
			responseField.setText(text);
		} else if (evt.getPropertyName().equals(MainController.REQUEST_URL_PROPERTY)) {
			final String urlText = evt.getNewValue().toString();
			url.setText(urlText);
		} else if (evt.getPropertyName().equals(MainController.REQUEST_TEXT_PROPERTY)) {
			final String text = evt.getNewValue().toString();
			requestField.setText(text);
		} else if (evt.getPropertyName().equals(MainController.CERT_PROPERTY)) {

			if (evt.getNewValue() == null) {
				certArea.setText(CERT);
				return;
			}

			final String text = evt.getNewValue().toString();
			certArea.setText(text);
		} else if (evt.getPropertyName().equals(MainController.PROGRESS_PROPERTY)) {
			progressBar.setValue(Integer.valueOf(evt.getNewValue().toString()));
		} else if (evt.getPropertyName().equals(MainController.CURRENT_CYCLES_PROPERTY)) {
			final String count = evt.getNewValue().toString();
			if (count.isEmpty()) {
				return;
			}
			cycleArea.setText(CYCLES_COUNT + count);
		}
	}

	private void createAndShowGui() {

		JFrame frame = createFrame();
		frame.setVisible(true);
	}

	private JFrame createFrame() {

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(createPane());
		frame.setJMenuBar(createMenu());
		frame.setSize(600, 550);
		frame.setResizable(false);
		return frame;
	}

	private JPanel createPane() {

		final JPanel pane = new JPanel();
		final JButton send = createSendButton();
		send.setPreferredSize(new Dimension(500, 30));
		final JButton stop = createStopButton();
		stop.setPreferredSize(new Dimension(500, 30));

		progressBar.setMaximum(100);

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
		certArea.setEditable(false);
		certArea.setColumns(15);
		cycleArea.setEditable(false);
		cycleArea.setColumns(15);

		pane.add(url);
		pane.add(progressBar);
		pane.add(scrollRequest);
		pane.add(scrollResponse);
		pane.add(send);
		pane.add(stop);
		pane.add(codeArea);
		pane.add(sizeArea);
		pane.add(timeArea);
		pane.add(certArea);
		pane.add(cycleArea);

		return pane;
	}

	private JMenuBar createMenu() {

		final JMenuBar menu = new JMenuBar();
		final JFileChooser certChooser = new JFileChooser();
		certChooser.setFileFilter(new P12Filter());

		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new XmlFilter());
		fileChooser.setFileFilter(new RifFilter());

		final JMenuItem open = new JMenuItem(OPEN_BUTTON_NAME);
		open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				fileChooser.showOpenDialog(null);

				final File selectedFile = fileChooser.getSelectedFile();
				if (selectedFile == null) {
					return;
				}

				controller.changeFile(selectedFile);
				controller.openFile();
			}
		});

		final JMenuItem save = new JMenuItem(SAVE_BUTTON_NAME);
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				fileChooser.showSaveDialog(null);

				final File selectedFile = fileChooser.getSelectedFile();
				if (selectedFile == null) {
					return;
				}

				controller.changeFile(selectedFile);
				controller.changeRequest(requestField.getText());
				controller.changeUrl(url.getText());
				controller.executeSave();
			}
		});

		final JMenuItem addCert = new JMenuItem(ADD_CERT_BUTTON_NAME);
		addCert.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				certChooser.showOpenDialog(null);
				final File selectedFile = certChooser.getSelectedFile();
				if (selectedFile == null) {
					return;
				}

				controller.addCertificate(selectedFile);
				new CertificatePassword(controller, frame.getLocation());
			}
		});

		final JMenuItem removeCert = new JMenuItem(REMOVE_CERT_BUTTON_NAME);
		removeCert.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				controller.removeCertificate();
				controller.removeCertificatePassword();
			}
		});

		final JMenu fileMenu = new JMenu("File");
		menu.add(fileMenu);

		final JMenu certMenu = new JMenu("Certificates");
		certMenu.add(addCert);
		certMenu.add(removeCert);
		menu.add(certMenu);

		final JMenuItem format = new JMenuItem(FORMAT_BUTTON_NAME);
		format.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				controller.changeRequest(requestField.getText());
				controller.changeResponse(responseField.getText());
			}
		});

		final JMenuItem settings = new JMenuItem(SETTINGS_BUTTON_NAME);
		settings.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				new SettingsWindow(controller, frame.getLocation());
			}
		});

		final JMenuItem about = new JMenuItem(ABOUT_BUTTON_NAME);
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				final String newLine = System.getProperty("line.separator");
				final StringBuilder sBuilder = new StringBuilder().append("Requester v2.2").append(newLine)
						.append(" Andrey Dyachkov").append(newLine).append(" andrey.dyachkov@gmail.com");
				JOptionPane.showMessageDialog(null, sBuilder, "About", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		fileMenu.add(open);
		fileMenu.add(save);
		fileMenu.add(settings);
		fileMenu.add(format);
		fileMenu.add(about);

		final JButton httpHeadersMenu = new JButton("HTTP Headers");
		menu.add(httpHeadersMenu);
		httpHeadersMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				HttpHeadersController hc = new HttpHeadersController();
				hc.addModel(HttpHeadersModel.getInstance());
				new HttpHeadersWindow(hc);
			}
		});

		final JButton httpServerMenu = new JButton("Start server");
		menu.add(httpServerMenu);
		httpServerMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					new HttpServerWindow();
					HttpServer.start(9000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		return menu;
	}

	private JButton createSendButton() {

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
				progressBar.setValue(0);

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

	private JButton createStopButton() {
		JButton stop = new JButton(STOP_BUTTON_NAME);
		stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.stopRequest();
			}
		});
		return stop;
	}

	@Override
	public void run() {

		createAndShowGui();
	}
}
