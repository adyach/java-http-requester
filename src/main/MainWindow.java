package main;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import requester.PostRequester;

public class MainWindow implements Runnable {
	private JTextArea request = new JTextArea(10, 50);
	private JTextField url = new JTextField("http://", 20);
	private JTextArea response = new JTextArea(10, 50);

	public static void main(String[] args) throws Exception {
		SwingUtilities.invokeLater(new MainWindow());
	}

	private void createAndShowGui() {
		JFrame frame = createFrame();
		frame.setVisible(true);
	}

	private JFrame createFrame() {
		JFrame frame = new JFrame("Requester");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(createPane());
		frame.setSize(600, 500);
		frame.setResizable(false);
		return frame;
	}

	private JPanel createPane() {
		JPanel pane = new JPanel();
		JButton button = createButton();

		request.setText("Request");
		response.setText("Response");

		pane.add(url);
		pane.add(request);
		pane.add(response);
		pane.add(button);

		return pane;
	}

	private JButton createButton() {
		JButton button = new JButton("Send");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PostRequester postRequester = new PostRequester();
				try {
					postRequester.sendPost(url.getText(), request.getText());
					response.setText(postRequester.getResponse());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		return button;
	}

	@Override
	public void run() {
		createAndShowGui();
	}

}
