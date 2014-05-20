package requester.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import requester.controller.Controller;
import requester.logic.model.HttpServerModel;

public class HttpServerWindow extends JFrame implements View {

	private static final long serialVersionUID = -5589299087684260122L;
	public static final String DATA_PROPERTY = "Data";
	private JPanel contentPane;
	private JTextArea response;
	private Controller controller = new Controller() {
	};
	
	public HttpServerWindow() {
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		setVisible(true);
		
		response = new JTextArea("Walkie-talkie is here \r\n");
		response.setAutoscrolls(true);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(response);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		controller.addView(this);
		controller.addModel(HttpServerModel.getInstance());
	}

	@Override
	public void modelPropertyChange(PropertyChangeEvent evt) {
		response.append((String) evt.getNewValue());
	}

}
