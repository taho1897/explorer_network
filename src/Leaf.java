
public class Leaf implements Component {
	String tag;
	String value;
	
	public Leaf (String tag, String value) {
		this.tag = tag;
		this.value = value;
	}
	@Override
	public void add(Component component) throws UnsupportedOperationException {
		
		throw new UnsupportedOperationException("Leaf can't have a child");
	}

	@Override
	public void remove(Component component) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Leaf can't have a child");
		
		
	}

	@Override
	public Component getChild(int i) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Leaf can't have a child");
	}

	@Override
	public String getTag() {
		
		return this.tag;
	}

	@Override
	public String getValue() {
		
		return this.value;
	}

	@Override
	public void print() {
		
		System.out.println(tag + value );
	}

	@Override
	public void print(int i) {
		for(int j = 0 ; j < i ; j++){
			System.out.print("  ");
		
		}
		System.out.println(tag + value );
	}
	

}
