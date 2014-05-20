package requester.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import requester.controller.HttpHeadersController;
import requester.logic.model.HttpHeadersModel;

public class HttpHeadersWindow extends JFrame implements View {

	private static final long serialVersionUID = 4431731029947618379L;
	private JPanel contentPane;
	private GridLayout grid;
	private HttpHeadersController hc;

	public HttpHeadersWindow(HttpHeadersController hc) {
		setBounds(100, 100, 502, 108);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		grid = new GridLayout(0, 3, 0, 0);
		contentPane.add(new JLabel("Header"));
		contentPane.add(new JLabel("Value"));
		contentPane.add(new JLabel(""));
		setVisible(true);
		contentPane.setLayout(grid);
		setSize(new Dimension(544, 111));
		hc.addView(this);
		this.hc = hc;

		Map<String, String> headers = HttpHeadersModel.getInstance().getHttpHeaders();
		for (String name : headers.keySet()) {
			createRow(name, headers.get(name));
		}
		createRow("", "");
	}
	
	private void createRow (String name, String value) {
		final JTextField nameField = new JTextField(name);
		contentPane.add(nameField);
		nameField.setColumns(15);
		final JTextField valueFiled = new JTextField(value);
		contentPane.add(valueFiled);
		valueFiled.setColumns(40);
		final JButton addBtn = new JButton("Add");
		addBtn.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				hc.addHeader(new String[] { nameField.getText(), valueFiled.getText() });
				addBtn.setText("Rem");
				addBtn.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseClicked(MouseEvent arg0) {
						hc.removeHeader(nameField.getText());
						contentPane.remove(nameField);
						contentPane.remove(valueFiled);
						contentPane.remove(addBtn);
						HttpHeadersWindow.this.setSize(getWidth(), getHeight() - 30);
					}
				});
				createRow("", "");
			}
		});
		contentPane.add(addBtn);
		setSize(getWidth(), getHeight() + 30);
	}

	@Override
	public void modelPropertyChange(PropertyChangeEvent evt) {

	}
}
