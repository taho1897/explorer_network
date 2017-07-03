import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
/*   스레드를 상속받는 소켓 스레드 class   */
public class SocketThread extends Thread {
	private static List<SocketThread> threads = new ArrayList<SocketThread>();// 소켓스레드 목록 저장 리스트 선언
	private Socket socket;// 소켓 선언
	Explorer explorer;// 탐색기 선언
	Composite root;// 복합요소 root 선언
	int selectMenu;// 메뉴 선택 변수 선언
	public SocketThread(Socket socket) {// 소켓을 인자로 하는 소켓 스레드 생성자
		super();// 스레드 클래스 생성자 호출(스레드 초기화)
		this.socket = socket;// 받은 소켓으로 초기화
		explorer = new Explorer(this.socket);// 소켓을 이용한 탐색기 객체 선언
		File file = new File("e:/");// 루트 디렉토리를 이용하여 파일객체 생성
		root = new Composite(file.getAbsolutePath());// 루트 디렉토리의 절대경로를 이용, 복합요소 root 저장
		try {// try
			root = explorer.makeTree(root);// 탐색기 객체에서 트리 생성및 root에 저장
		} catch(UnsupportedOperationException e) {// catch 동작 Exception
			e.printStackTrace();// 에러 내용  출력
		}
		threads.add(this);// 스레드에 추가
	}
	/*   스레드 실행 메소드   */
	public void run() {
		try{// 외곽 try
			explorer.fileList(root, this);// 복합요소 root 객체와 소켓을 사용하여 하위 요소 목록 생성
			while(true) {// 계속 반복
				try {// 내각 try
					selectMenu(selectMenu,root,this);// 메뉴선택 메소드 호출(선택 메뉴, root 객체, 소켓)
				} catch(IOException e) {// 내각 catch IOException
					socket.close();// 소켓 닫기
					threads.remove(this);// 스레드에서 해당 소켓 제거
					return;// 리턴
				}
			}
		} catch(Exception e) {// 외곽 catch Exception
			System.err.println(e);// err.println
		}
	}
	/*   선택한 메뉴값, 복합요소, 소켓스레드를 이용한 메뉴 선택 메소드, 동작, IO Exception 발생  */
	private void selectMenu(int selectMenu, Composite root, SocketThread socketThread) throws UnsupportedOperationException, IOException {
		InputStream in = null;// 인풋스트림 선언
		socketThread.sendMessage("이동하고자 하는 디렉토리 또는 파일을 선택하세요");// 소켓스레드로 디렉토리, 파일 선택 안내 메세지 출력
		in = socket.getInputStream();// 소켓으로 전달받은 인풋스트림 저장
		String message;// 메세지 저장 변수 선언
		int size;// 크기 선언
		byte[] w = new byte[10240];// 전송 위한 크기가 10240인 바이트배열 선언
		
		size = in.read(w);// 인풋스트림으로 받은 내용의 크기를 바이트배열을 이용하여 저장
		if(size <= 0)throw new IOException();// 내용 크기가 0이하일 때 IO exception 발생
		message = new String(w, 0, size, "UTF-8");// String 객체를 이용하여 메세지 저장(매개변수 4개짜리 String 확인 요)
		message = message.trim();//메세지 전후 공백 제거
		selectMenu = Integer.parseInt(message);// 메세지를 int형으로 변환하여 메뉴 선택 변수에 저장
		explorer.selectMenu(selectMenu, root, socketThread);// 탐색기의 메뉴 선택 메소드 호출
	}
	/*   복합요소 객체, 소켓스레드를 받는 하위 요소 목록 생성 메소드, 숫자포맷, 동작, IO Exception 발생   */
	public void fileList(Composite root, SocketThread socketThread) throws NumberFormatException, UnsupportedOperationException, IOException {
		explorer.fileList(root, socketThread);// 탐색기 객테의 하위 요소 목록 생성 메소드 호출
	}
	/*   살아있는 전체 소켓 메세지 전달 메소드   */
	public void sendMessageAll(String message) {
		SocketThread thread;// 소켓스레드 선언
		for (int i = 0; i < threads.size(); i++) {// 스레드의 크기만큼 반복
			thread = (SocketThread) threads.get(i);// 현재 접근중인 소켓스레드의 값 저장
			if (thread.isAlive())// 소켓이 살아있는지 확인
				thread.sendMessage(message);// 메세지 전송
		}
	}
	/*   특정 소켓에 메세지 전달 메소드   */
	public void sendMessage(String message) {
		try {// try
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			// 소켓의 OutputStream을 이용하여 PrintWriter 객체 선언
			out.println(message);// 메세지 출력
			out.flush();// 객체 초기화
		} catch (IOException e) {// catch IOException
		}
	}
}
