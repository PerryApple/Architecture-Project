package core;
//MAR stands for Memory Address Register, inherit methods from 
public class MAR implements Register {

	private static final String name = "MAR";
	//Size of MAR is 16bits.
	private static final int size = 12;
	//Initially set the content to 16 zeroes.
	private String content = "000000000000";
	//Our computer has only one MAR register
	private static final MAR instance = new MAR();
	
	private MAR() {
		
	}
	
	//Singleton Pattern get instance method
	public static Register getInstance() {
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
	public int getSize() {
		// TODO Auto-generated method stub
		return 16;
	}


	@Override
	public void setContent(String content) {
		// TODO Auto-generated method stub
		//check the length of content, if length is not the same as "size", return an error;
		if(content.length() == this.size) {
			this.content = content;
		}else if(content.length() == 16) {
			this.content = content.substring(4);
		}else{
			//fault handle will be implement in later version.
			this.content = "MAR Error";
		}
	}

}
