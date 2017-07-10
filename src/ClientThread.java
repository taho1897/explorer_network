import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/*   스레드를 상속받는 클라이언트 스레드 클래스   */
public class ClientThread extends Thread {
	Socket socket;// 소켓 선언
	InputStream in;// 인풋스트림 선언
	String ip;// ip저장할 변수 선언
	int port;// 포트번호 저장할 변수 선언

	/*   메인 메소드   */
	public static void main(String[] args) {
		ClientThread clientThread = new ClientThread("127.0.0.1", 6001);// ip, 포트번호를 이용하여 클라이언트 스레드 객체 생성
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));// 콘솔에 입력받기 위해 BufferedReader 선언
		clientThread.start();// 클라이언트 스레드 시작
		while(true) {// 계속 반복
			try {// try
				clientThread.sendMessage(br.readLine());// 콘솔에서 입력한 메세지를 전송 
			} catch (IOException e) {// catch IOException
				e.printStackTrace();// Exception 내용 출력
			}
		}
	}
	
	public ClientThread(String ip, int port) {// 클라이언트 스레드 생성자
		this.ip = ip;// ip값 저장
		this.port = port;// 포트값 저장		
	}

	@Override
	public void run() {// run()메소드 오버라이딩
		int size;// 사이즈 저장 변수 선언
		byte[] byteArray = new byte[10240];// 크기가 10240인 바이트 객체 선언
		String message;// 송신한 메세지 저장할 변수 선언
		try {// try
			socket = new Socket(ip, port);// ip, 포트번호를 사용하여 소켓 저장
			in = socket.getInputStream();// 입력받을 bufferedReader에 소켓의 인풋스트림 저장
			while(socket != null && socket.isConnected()) {// 소켓 객체가 null이 아니고, 연결되어있는동안 반복
				size = in.read(byteArray);// 바이트 객체를 이용하여 입력받은 값을 저장
				if(size <= 0) {// 사이즈가 0이하라면
					continue;// 계속 진행
				}
				message = new String(byteArray, 0, size, "UTF-8");// String 객체를 이용, 전송받은 메세지 저장(매개변수 4개짜리 확인)
				printMessage(message);// 전송받은 메세지 출력 메소드 호출
			}
		} catch(Exception e) {// catch Exception
			
		}
	}
	 

	/*   송신 메세지 출력 메소드   */
	public void printMessage(String message) {
		System.out.println(message);// 메세지 콘솔에 출력
	}

	/*   메세지 전송 메소드   */
	public void sendMessage(String message) {
		try {// try
			if(socket != null && socket.isConnected()) {// 소켓 객체가 null이 아니고 연결되어있는지 확인
				PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));// 소켓의 OutputStream을 이용, PrintWriter 객체 생성
				pw.println(message);// PrintWriter객체를 통해 메세지 출력
				pw.flush();// PrintWriter객체 초기화
			}
		} catch(Exception e) {// catch Exception
			
		}
	}
}