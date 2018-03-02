package core;
//This is a 16 bit quotient register. this is only one instance in this computer
public class QR implements Register {

	private final static String name = "QR";
	//Initially set the content to 16 zeroes.
	private String content = "0000000000000000";
	private final static int size = 16;
	//Our computer has only one PR register
	//So I use singleton pattern to design this class
	private final static QR instance = new QR();
	//Singleton pattern
	private QR() {}
	public static QR getInstance() {
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
		//No action in this method. Set content to this register is prohibited.
	}
	
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return size;
	}
	
	
	//This function add a 0 at the end of the content when left shift finish.
	public void leftShiftWithZero() {
		this.content = content.substring(1) + "0";
	}
	//This function add a 1 at the end of the content when left shift finish.
	public void leftShiftWithOne() {
		this.content = content.substring(1) + "1";
	}
}
