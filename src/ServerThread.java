import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/*   스레드를 상속받은 서버 스레드 클래스   */
public class ServerThread extends Thread {
	ServerSocket server;// 서버소켓 선언
	/*   메인 메소드   */
	public static void main(String[] args) {
		ServerThread serverThread = new ServerThread();// 서버스레드 객체 선언
		serverThread.start(6001);// 6001포트로 서버 스레드 시작
	}
	/*   포트 번호를 받아 서버스레드 시작 메소드   */
	public void start(int port) {
		try {// try
			server = new ServerSocket(port);// 포트번호를 받아 서버소켓에 저장
			System.err.println("" + port);// 에러 발생시 포트 번호 출력
			this.start();// 소켓 스레드 실행
		} catch (IOException e) {// catch IOException
		}
	}

	@Override
	public void run() {// run() 메소드 오버라이딩
		while (true) {// 계속 반복
			try {// try
				Socket socket = server.accept();// 소켓에 서버허용
				SocketThread thread = new SocketThread(socket);// 서버 소켓을 이용하여 소켓 스레드 생성
				thread.start();// 소켓스레드 시작
			} catch (IOException e) {// catch IOException
			}
		}
	}

}
