/*   Component인터페이스를 사용 최하위 원소 Leaf class 구현   */	
public class Leaf implements Component {
	String tag;// 태그 저장 변수
	String value;// 값 저장 변수

	public Leaf(String tag, String value) {// Leaf 생성자
		this.tag = tag;// 인자로 받은 태그 저장
		this.value = value;// 인자로 받은 값 저장
	}

	@Override
	public void add(Component component) throws UnsupportedOperationException {// 요소 추가 메소드 구현
		throw new UnsupportedOperationException("Leaf can't have a child");// 지원하지 않는 동작 Exception
	}

	@Override
	public void remove(Component component) throws UnsupportedOperationException {// 요소 제거 메소드 구현
		throw new UnsupportedOperationException("Leaf can't have a child");// 지원하지 않는 동작 Exception
	}

	@Override
	public Component getChild(int i) throws UnsupportedOperationException {// i번째 자식요소 가져오기 메소드 구현
		throw new UnsupportedOperationException("Leaf can't have a child");// 지원하지 않는 동작 Exception
	}

	@Override
	public String getTag() {// 태그 getter 구현
		return this.tag;// 태그 리턴
	}

	@Override
	public String getValue() {// 값 getter 구현
		return this.value;// 값 리턴
	}

	@Override
	public void print() {// 일반 출력 메소드 구현
		System.out.println(this.tag + this.value);// 태그 + 값 출력
	}

	@Override
	public void print(int i) {// i번째 자식 출력 메소드 구현
		for(int j = 0 ; j < i ; j++) {// i회 반복
			System.out.print("  ");// 공백 출력
			System.out.println(this.tag + this.value);// 태그 + 값 출력	
		}
	}
}