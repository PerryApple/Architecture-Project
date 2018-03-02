package core;

//This is Remainder Register, be used in store remainder of DVD instruction.
//only one instance of this register exist in this computer
//This is a 32 bits register.
public class RR implements Register {
	
	private final static String name = "RR";
	//Initially set the content to 32 zeroes.
	private String content = "00000000000000000000000000000000";
	private final static int size = 32;
	//Our computer has only one PR register
	//So I use singleton pattern to design this class
	private final static RR instance = new RR();
	//Singleton pattern
	private RR() {}
	
	public static RR getInstance() {
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
			this.content = "0000000000000000" + content;
		}else if (content.length() == 32 ){
			this.content = content;
		}else {
			this.content = "RR Error!";
		}
		
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return size;
	}

}
