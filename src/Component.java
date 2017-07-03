/*   요소 인터페이스   */
public interface Component {
	public void add(Component component) throws UnsupportedOperationException;//요소 추가 메소드 선언, 동작 관련 Exception
	public void remove(Component component) throws UnsupportedOperationException;//요소 제거 메소드 선언, 동작 관련 Exception
	public Component getChild(int i) throws UnsupportedOperationException;//i번째 자식요소 가져오기 메소드 선언, 동작 관련 Exception
	public String getTag();//태그 받기 메소드 선언
	public String getValue();//값 받기 메소드 선언
	public void print();//일반 출력 메소드 선언
	public void print(int i);//i번째 자식 출력 메소드 선언
}