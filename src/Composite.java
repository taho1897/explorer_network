import java.util.ArrayList;
import java.util.List;
/*   Component 인터페이스로 Composite class 구현   */
public class Composite implements Component {
	String tag;// 태그 저장 변수
	List<Component> components = new ArrayList<Component>();// 요소 객체를 저장할 리스트 선언
	public Composite(String tag) {// Composite class 초기화
		this.tag = tag;// 인자로 받은 태그 저장
	}
	@Override
	public void add(Component component) throws UnsupportedOperationException {// 자식요소 추가 메소드 구현
		components.add(component);// 요소 객체 리스트에 추가
	}

	@Override
	public void remove(Component component)	throws UnsupportedOperationException {// 자식요소 제거 메소드 구현
		components.remove(component);// 요소 객체 리스트에서 제거
	}

	@Override
	public Component getChild(int i) throws UnsupportedOperationException {// i번째 자식요소 가져오기 메소드 구현
		if(components.size()>i)// 자식요소 리스트의 크기가 i보다 크다면
			return components.get(i);// i번째 자식요소를 목록에서 선택해 리턴
		return null;// 아니라면 null 리턴
	}

	@Override
	public String getTag() {// 태그 getter 구현
		return this.tag;// 태그 리턴
	}

	@Override
	public String getValue() {//값 getter 구현
		String values = "";// 경로를 저장할 변수 선언 및 초기화
		for(int j = 0 ; j < components.size(); j++) {// 요소 객체 저장 리스트 크기만큼 반복
			Component component = components.get(j);// 현재 접근중인 요소객체 저장
			values +=  component.getTag() + component.getValue();// 현재 접근중인 요소객체의 태그 및 값을 경로저장 변수에 누적 
		}
		return values;// 경로저장 변수 리턴
	}

	@Override
	public void print() {// 일반출력 메소드 구현
		System.out.println(tag + this.getValue());// 태그 + 값 출력
	}

	@Override
	public void print(int i) {// i번째 자식요소 출력 메소드 구현
		for (int j = 0 ; j < i ; j++) {// i만큼 반복
			System.out.print("  ");// 공백 출력
		}
		System.out.println("D: " + tag);// "D: " + 태그 출력
		for (int j = 0 ; j < components.size(); j++) {// 요소객체 저장 리스트 크기만큼 반복
			Component component = components.get(j);// 현재 접근중인 요소객체 저장
			System.out.print(j + 1 +". ");// 현재 번호 + ". " 출력
			component.print(i + 1);// 선택한 i번째 자식요소 출력 
		}
	}
}
