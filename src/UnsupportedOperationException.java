public class UnsupportedOperationException extends Exception {// 지원하지 않는 동작 관련 Exception class
	public UnsupportedOperationException(String err) {// Exception 내용을 String으로 받아옴
		super(err);// Exception class에 에러 내용을 던짐
	}
}