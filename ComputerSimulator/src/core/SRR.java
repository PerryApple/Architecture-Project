package core;

//This is Shift and Rotate Register, be used in shift and rotate data.
//only one instance of this register exist in this computer, all shift and rotate instruction will be done by this register.
//This is a 16 bits register.
public class SRR implements Register {

	private final static String name = "SRR";
	//Initially set the content to 16 zeroes.
	private String content = "0000000000000000";
	private final static int size = 16;
	//Our computer has only one SRR register
	//So I use singleton pattern to design this class
	private final static SRR instance = new SRR();
	//Singleton pattern
	private SRR() {}
	
	public static SRR getInstance() {
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
		return CPU.alignment(content);
	}

	@Override
	public void setContent(String content) {
		// TODO Auto-generated method stub
		if(content.length() == 16) {
			this.content = content;
		}else {
			this.content = "SRR Error!";
		}
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return size;
	}
	
	//Shift function
	//Arithmetic left shift
	public void arithmeticLeftShift() {
		this.content = String.valueOf(content.charAt(0)) + content.substring(2) + "0";
	}
	//Logical left shift
	public void logicalLeftShift() {
		this.content = content.substring(1) + "0";
	}
	//Arithmetic right shift
	public void arithmeticRightShift() {
		this.content = String.valueOf(content.charAt(0)) + String.valueOf(content.charAt(0)) + content.substring(1, 15);
	}
	//Logical right shift
	public void logicalRightShift() {
		this.content = "0" + content.substring(0,15);
	}
	
	//Rotate function
	//Left Rotate
	public void leftRotate() {
		this.content = content.substring(1) + String.valueOf(content.charAt(0));
	}
	//Right Rotate
	public void rightRotate() {
		this.content = String.valueOf(content.charAt(15)) + content.substring(0,15);
	}
}
