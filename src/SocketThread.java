import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketThread extends Thread {

	private static List<SocketThread> threads = new ArrayList<SocketThread>();
	private Socket socket;
	Explorer explorer;
	Composite root;
	int selectMenu;
	public SocketThread(Socket socket) {
		super();
		this.socket = socket;
		explorer = new Explorer(this.socket);
		File file = new File("e:/");
		root = new Composite(file.getAbsolutePath());
		try{
			root = explorer.makeTree(root);
		} catch(UnsupportedOperationException e){
			e.printStackTrace();
		}
		threads.add(this);
	}

	public void run() {
		try{
			explorer.fileList(root, this);
			while(true){
				try{
					selectMenu(selectMenu,root,this);
				}catch(IOException e){
					socket.close();
					threads.remove(this);
					return;
				}
			}
		} catch(Exception e){
			System.err.println(e);
		}
	}

	private void selectMenu(int selectMenu, Composite root, SocketThread socketThread) throws UnsupportedOperationException, IOException {
		// TODO Auto-generated method stub
		InputStream in = null;
		socketThread.sendMessage("이동하고자 하는 디렉토리 또는 파일을 선택하세요");
		in = socket.getInputStream();
		String message;
		int size;
		byte[] w = new byte[10240];
		
		size = in.read(w);
		if(size <= 0)throw new IOException();
		message = new String(w, 0, size, "UTF-8");
		message = message.trim();
		selectMenu = Integer.parseInt(message);
		explorer.selectMenu(selectMenu, root, socketThread);
	}
	
	public void fileList(Composite root, SocketThread socketThread) throws NumberFormatException, UnsupportedOperationException, IOException{
		explorer.fileList(root, socketThread);
	}

	public void sendMessageAll(String message) {
		SocketThread thread;
		for (int i = 0; i < threads.size(); i++) {
			thread = (SocketThread) threads.get(i);
			if (thread.isAlive())
				thread.sendMessage(message);
		}

	}

	public void sendMessage(String message) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			out.println(message);
			out.flush();
		} catch (IOException e) {
		}
	}
}
