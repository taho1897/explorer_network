import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {

	ServerSocket server;

	public static void main(String[] args) {
		ServerThread serverThread = new ServerThread();
		serverThread.start(6001);
	}

	public void start(int port) {

		try {
			server = new ServerSocket(port);
			System.err.println("" + port);
			this.start();
		} catch (IOException e) {
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket socket = server.accept();
				SocketThread thread = new SocketThread(socket);
				thread.start();
			} catch (IOException e) {
			}
		}
	}

}
