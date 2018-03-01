//Z is register to store the result calculated in ALU. This is an accumulator.
//This class will be construct 
public class Z extends Register {
	private static final String name = "Z";
	//Size of this register is set as 16 bits
	private static final int size = 16;
	//Initiate the content of this register as 16 zeroes
	private String content = "0000000000000000";
	//Singleton Pattern, instantiate an instance of this class and set as private
	private static final Z instance = new Z();
	//Constructor should be declare as private
	private Z() {
		
	}
	//Method to get the instance
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
		}else{
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
