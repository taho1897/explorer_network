import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/*   탐색기 클래스   */
public class Explorer {
	private Socket socket;// private 소켓 선언
	public Explorer(Socket socket) {// 소켓을 사용한 생성자
		super();// 부모 객체 생성자 호출로 초기화
		this.socket = socket;// 소켓 저장
	}
	
	/*   트리구조 생성 메소드, 동작 Exception   */
	public Component makeTree(Component root) throws UnsupportedOperationException {
		File file = new File(root.getTag());// 인자로 받은 복합요소 객체의 태그로 파일객체 생성
		File[] fileList;// 파일 리스트 객체 생성
		fileList = file.listFiles();// 파일 리스트 객체에 파일객체 목록 저장 
		if(fileList != null) {// 리스트가 비어있지 않은지 확인
			int listSize = fileList.length;// 리스트의 크기 저장
			for(int i = 0 ; i < listSize ; i++) {// 리스트의 크기만큼 반복
				if(fileList[i].isDirectory()) {// 현재 접근중인 리스트의 파일 객체가 디렉토리인지 확인
					Component temp = new Composite(fileList[i].getAbsolutePath());// 디렉토리 복합요소객체 생성
					root.add(temp);// 루트 복합요소 객체에 디렉토리 복합요소객체 추가
					root = makeTree(temp);// 디렉토리 복합요소 객체 하위의 트리구조 생성 및 저장
				}
				else if(fileList[i].isFile()) {// 현재 접근중인 리스트의 파일 객체가 파일인지 확인
					Component leaf = new Leaf("F: ", fileList[i].getName());// Leaf 객체 생성
					root.add(leaf);// 루트 복합요소 객체에 Leaf 객체 추가
				}
			}
		}
		return root;// 루트 복합요소 객체 리턴
	}
	
	/*   복합요소 객체, 소켓스레드 객체를 사용하는 파일 목록 생성 메소드, 동작, 숫자포맷, IOException   */
	public static void makeFileList(Component component, SocketThread socketThread) throws UnsupportedOperationException, NumberFormatException, IOException {
		socketThread.sendMessage("현재 디렉토리 : " + component.getTag());// 소켓스레드를 이용, 현재 디렉토리 안내 메세지 전송
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));// 콘솔에서 입력하는 메뉴 선택 번호 저장
		File file = new File(component.getTag());// 루트 복합요소 객체의 태그를 이용한 파일 객체 생성
		File[] fileList = file.listFiles();// 파일리스트에 루트 복합요소 객체의 목록 저장
		int size = fileList.length;// 파일리스트의 크기 저장
		for(int i = 0 ; i < size ; i++) {// 파일리스트의 크기만큼 반복
			Component temp = component.getChild(i);// 현재 접근중인 루트 복합요소의 자식 요소 객체 저장
			if(temp.getTag().equals("F: ")) {// 자식요소의 태그가 "F: "와 같을 때
				socketThread.sendMessage(i + 1 +". " + temp.getTag() + temp.getValue());// 소켓스레드를 이용, 번호와 태그, 값을 전송
			}
			else {// 아니라면 (디렉토리라면)
				String tempRoute = temp.getTag();// 현재 접근중인 자식 요소의 태그 저장
				int criteria = tempRoute.lastIndexOf("\\");// 태그에서 경로기호 "\\"의 인덱스 확인
				socketThread.sendMessage(i + 1 + ". D:" + tempRoute.substring(criteria));// 소켓스레드를 이용, 번호, 디렉토리 태그 직접 입력, 경로기호 이후의 값만 잘라내서 값을 전송
			}
		}
	}
	

	/*   번호, 복합요소 객체, 소켓스레드를 이용한 메뉴 선택 메소드, 동작, IOException   */
	public void selectMenu(int num, Component root, SocketThread socketThread) throws UnsupportedOperationException, IOException {
		Component temp = root.getChild(num - 1);// 해당 번호에서 1 감소한 자식 요소 객체를 저장
		if(temp.getTag().equals("F: ")) {// 해당 자식요소의 태그가 "F: "와 같다면(파일이라면)
			String route = root.getTag() + "/" + temp.getValue();// 루트 복합요소객체의 태그(경로)와 자식요소의 값을 더해서 경로 생성
			File file = new File(route);// 새로운 경로를 이용하여 파일 객체 생성
			if(file.canRead()) {// 현재 선택된 파일이 읽을 수 있는 파일인지 확인
				socketThread.sendMessage("선택한 항목은 " + num + 1 + ". " + route);// 소켓스레드를 이용, 선택한 번호와 경로 안내 메세지 전송
				makeFileList(root, socketThread);// 루트 복합요소 객체와 소켓스레드를 이용하여 파일목록 생성
				delay();// 딜레이
			}
			else {// 읽기 불가능한 파일이라면
				socketThread.sendMessage("읽기 불가능한 파일입니다");// 소켓스레드를 이용, 읽기 불가 안내 메세지 전송
				makeFileList(root, socketThread);// 루트 복합요소 객체와 소켓스레드를 이용하여 파일목록 생성 
				delay();// 딜레이 
			}
		}
		else {// 해당 자식요소가 디렉토리라면
			socketThread.sendMessage(num + 1 + "번을 선택하셨습니다. 해당 디렉토리로 이동합니다");// 소켓스레드를 사용하여 해당 디렉토리 선택 안내메세지 전송 
			makeFileList(temp, socketThread);// 선택 디렉토리, 소켓스레드를 이용하여 파일목록 생성
			delay();// 딜레이
		}
	}

	/*   강제 딜레이 메소드   */
	public void delay() {
		for(int i = 0 ; i < 900000000 ; i++) {}// 900000000번 for문 반복으로 강제 딜레이 생성
	}
}