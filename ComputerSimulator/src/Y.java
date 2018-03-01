// Y is a temporary register to store operands for calculation in ALU.
// This class will be construct using singleton pattern
public class Y extends Register {

	private static final String name = "Y";
	//The size of this register is set as 16bits.
	private static final int size = 16;
	//Initiate the content of this register as 16 zeroes.
	private String content = "0000000000000000";
	//instantiate the class
	private static final Y instance = new Y();
	
	//constructor should be declared as private
	private Y() {
		
	}
	//Method to get instance
	public static Register getInstance() {
		return instance;
	}
	
	//Override the methods form super class
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
			this.content = "Error";
		}
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 16;
	}

}
