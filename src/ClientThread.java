import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

//test

public class ClientThread extends Thread {

	private Socket socket;
	private InputStream in;
	private String ip;
	private int port;

	public static void main(String[] args) {
		ClientThread clientThread = new ClientThread("127.0.0.1", 6001);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		clientThread.start();
		while (true) {
			try {
				clientThread.sendMessage(in.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ClientThread(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	@Override
	public void run() {
		int size;
		byte[] w = new byte[10240];
		String txtReceive = "";
		try {
			socket = new Socket(ip, port);
			in = socket.getInputStream();
			while (socket != null && socket.isConnected()) {
				size = in.read(w);
				if (size <= 0)
					continue;
				txtReceive = new String(w, 0, size, "UTF-8");
				receiveMessage(txtReceive);
			}
		} catch (Exception e) {
		}
	}

	public void receiveMessage(String message) {
		System.out.println(message);
	}

	public void sendMessage(String message) {
		try {
			if (socket != null && socket.isConnected()) {
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				out.println(message);
				out.flush();
			}
		} catch (Exception e) {
		}
	}

}
