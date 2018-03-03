package core;
//This is a register which stores the product in instruction "MLT"
public class PR implements Register {

	private final static String name = "PR";
	//Initially set the content to 32 zeroes.
	private String content = "00000000000000000000000000000000";
	private final static int size = 32;
	//Our computer has only one PR register
	//So I use singleton pattern to design this class
	private final static PR instance = new PR();
	//Singleton pattern
	private PR() {}
	
	public static PR getInstance() {
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
			return this.content;
	}
	
	//get high or low order bits. H: high order bits; L: low order bits.
	public String getHighOrderBits() {
		return CPU.alignment(content.substring(0, 16));
	}

	public String getLowOrderBits() {
		return CPU.alignment(content.substring(16));
	}

	//This is a 32bits register, when initialize
	//the first 16 bits store the products and the second 16 bits stores the multiplier (Ry).
	@Override
	public void setContent(String content) {
		// TODO Auto-generated method stub
		//Check the length of input data.
		if(content.length() == 32) {
			this.content = content;
		}else {
			this.content = "PR Error";
		}
	}
	
	public void saveProduct(String product) {
		this.content = product + this.content.substring(16);
	}

	//This register provide shift right function
	public void rightShift() {
		this.content = "0" + content.substring(0,31);
	}
	
	
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return size;
	}
	
}
