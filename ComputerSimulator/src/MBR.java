//MBR stands for Memory Buffer Register, our computer only has one MBR.
public class MBR extends Register {
	private static final String name = "MBR";
	//Size of MBR is 16 bits.
	private static final int size = 16;
	//Initially set MBR content to 16 zeroes.
	private String content = "0000000000000000";
	//Declare a MBR instance as static final
	private static final MBR instance = new MBR();
	
	//set the Constructor as private method
	private MBR() {
		
	}
	
	//method to get the instance in singleton pattern
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
	public void setContent(String content) {
		// TODO Auto-generated method stub
		//check the length of content, if length is not the same as "size", return an error;
		if(content.length() == this.size) {
			this.content = content;
		}else {
			//fault handle will be implement in later version.
			this.content = "MBR Error";
		}
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 16;
	}

}
