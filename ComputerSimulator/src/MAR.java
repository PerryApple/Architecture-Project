
//MAR stands for Memory Address Register, inherit methods from 
public class MAR extends Register {

	private static final String name = "MAR";
	//Size of MAR is 16bits.
	private static final int size = 16;
	//Initially set the content to 16 zeroes.
	private String content = "0000000000000000";
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
		content = CPU.alignment(content);
		return content;
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
		}else{
			//fault handle will be implement in later version.
			this.content = "MAR Error";
		}
	}

}
