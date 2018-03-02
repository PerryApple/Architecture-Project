package core;

public interface Register {
	public abstract String getName();
	public abstract String getContent();
	public abstract void setContent(String content);
	public abstract int getSize();
}
