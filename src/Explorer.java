import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Explorer {
	private Socket socket;
	public Explorer(Socket socket) {
		super();
		this.socket = socket;
	}

	public Composite makeTree(Composite dirRoot) throws UnsupportedOperationException {
		File file = new File(dirRoot.getTag());
		File[] list;
		list = file.listFiles();
		if(list != null) {
			int size = list.length;
			for(int i = 0 ; i < size ; i++) {
				if(list[i].isDirectory()) {
					Composite directory = new Composite(list[i].getPath());
					dirRoot.add(directory);
					directory = makeTree(directory);
				}
				else if(list[i].isFile()) {
					Leaf leaf = new Leaf("F: ",list[i].getName());
					dirRoot.add(leaf);
				}
			}
		}
		return dirRoot;
	}
	public static void fileList(Composite root, SocketThread socketThread) throws UnsupportedOperationException, NumberFormatException, IOException {
		socketThread.sendMessage("  현재 디렉토리 : " + root.getTag());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		File file = new File(root.getTag());
		File[] ls = file.listFiles();
		int size = ls.length;
		for (int i = 0 ; i < size ; i++) {
			Component temp = root.getChild(i);
			if(temp.getTag().equalsIgnoreCase("F: ")) {
				socketThread.sendMessage(i+1 + ". " + temp.getTag() + temp.getValue());
			}
			else{
				String tag = temp.getTag();
				int criteria = tag.lastIndexOf("\\");
				socketThread.sendMessage(i+1 + ". D: " + tag.substring(criteria+1));
			}
		}
	}
	public void selectMenu(int menuNum, Composite root, SocketThread socketThread)throws UnsupportedOperationException, IOException {
		Component temp = root.getChild(menuNum);
		Component temp1 = root.getChild(menuNum-1);
		if (temp1.getTag().equalsIgnoreCase("F: ")) {
			String path = root.getTag() +  "/" + temp1.getValue();
			File tempfile = new File(path);
			if(tempfile.canRead()) {
				socketThread.sendMessage(menuNum + "를  선택했습니다. 절대 경로는" + tempfile + "입니다.");
				fileList(root, socketThread);
				delay();
			}
			else {
				socketThread.sendMessage("읽기 불가능한 파일입니다.");
				fileList(root,socketThread);
				delay();
			}
		}
		else {
			socketThread.sendMessage(temp1.getTag() + "를 선택했습니다. 해당 폴더로 이동합니다.");
			fileList((Composite)temp1,socketThread);
			delay();
		}
	}
	public static void delay() {
		for (long i = 0 ; i < 900000000 ; i++){
		}
	}
}
