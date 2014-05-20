package requester.logic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import requester.logic.model.HttpServerModel;

/**
 * Simple http server.
 *
 * @author Andrey Dyachkov <br/>
 *         20 мая 2014 г.
 */
public class HttpServer {

	private static final HttpServerModel model = HttpServerModel.getInstance();

	public static void start(int port) throws Exception {
		model.setData("Trying to start server ...");
		final ServerSocket ss = new ServerSocket(port);
		model.setData("Server is started on port " + port);

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					Socket s;
					try {
						s = ss.accept();
						model.setData("--------------------------- Client accepted ---------------------------");
						new Thread(new SocketProcessor(s)).start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	private static class SocketProcessor implements Runnable {

		private Socket s;
		private InputStream is;
		private OutputStream os;

		private SocketProcessor(Socket s) throws Exception {
			this.s = s;
			this.is = s.getInputStream();
			this.os = s.getOutputStream();
		}

		public void run() {
			try {
				readInputHeaders();
				writeResponse("<html><body><h1>PostRequester</h1></body></html>");
			} catch (Throwable t) {
				t.printStackTrace();
			} finally {
				try {
					s.close();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			model.setData("--------------------------- Client processing finished ---------------------------");
		}

		private void writeResponse(String s) throws Throwable {
			String response = "HTTP/1.1 200 OK\r\n" + "Server: PostRequester \r\n" + "Content-Type: text/html\r\n"
					+ "Content-Length: " + s.length() + "\r\n" + "Connection: close\r\n\r\n";
			String result = response + s;
			model.setData("<<<<<<<<< \n\n" + result);
			os.write(result.getBytes());
			os.flush();
		}

		private void readInputHeaders() throws Throwable {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			model.setData(">>>>>>>>> \n");
			while (true) {
				String s = br.readLine();
				model.setData(s);
				if (s == null || s.trim().length() == 0) {
					break;
				}
			}
		}
	}
}
