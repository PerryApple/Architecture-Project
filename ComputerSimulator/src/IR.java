//IR stands for Instruction Register. Our computer only has one IR.
public class IR extends Register {
	private static final String name = "IR";
	//the size of IR is set to be 16bits.
	private static final int size = 16;
	//Initially set the content of IR to 16 zeroes.
	private String content = "0000000000000000";
	
	//Use singleton pattern to make sure there is only one instance of IR exist.
	private static final IR instance = new IR();
	//Set constructor to private.
	private IR() {
		
	}
	//method to get instance in Singleton Pattern.
	public static Register getInstance() {
		return instance;
	}
	
	//Override method in super class.
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
			this.content = "IR Error";
		}
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 16;
	}

}
