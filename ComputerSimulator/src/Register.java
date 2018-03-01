//This is an abstract class, all specific registers will inherit this superclass.
public abstract class Register {
	private String name;
	private String content;
	private int size;
	
	public abstract String getName();
	public abstract String getContent();
	public abstract void setContent(String content);
	public abstract int getSize();
}
