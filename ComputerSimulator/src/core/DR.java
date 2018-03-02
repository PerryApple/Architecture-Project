package core;
//This is Divider Register, be used in store divider of DVD instruction.
//only one instance of this register exist in this computer
//This is a 32 bits register.
public class DR implements Register {

	private final static String name = "DR";
	//Initially set the content to 32 zeroes.
	private String content = "00000000000000000000000000000000";
	private final static int size = 32;
	//Our computer has only one PR register
	//So I use singleton pattern to design this class
	private final static DR instance = new DR();
	//Singleton pattern
	private DR() {}
	
	public static DR getInstance() {
		return instance;
	}
	
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		return content;
	}

	@Override
	public void setContent(String content) {
		// TODO Auto-generated method stub
		if(content.length() == 16) {
			this.content = content + "0000000000000000";
		}else {
			this.content = "DR Error!";
		}
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return size;
	}
	
	//This register provide shift right function
	public void rightShift() {
		this.content = "0" + content.substring(0,31);
	}

}
