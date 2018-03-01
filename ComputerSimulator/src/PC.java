

//PC is a subclass of Register, our simulated computer has only one PC register.
public class PC extends Register {

	private static final String name  = "PC";
	//Size of Program Counter is 12bits.
	private static final int size = 12;
	//Initially set the content to 12 zeroes.
	private String content = "000000000000";
	//create a static final PC instance.
	private static final PC instance = new PC();
	
	private PC() {
		
	}
	// Singleton Pattern to make sure there is only one PC instance.
	public static Register getInstance() {
		return instance;
	}
	
	//Override all abstract method in superclass.
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
		return 12;
	}

	@Override
	public void setContent(String content) {
		// TODO Auto-generated method stub
		//check the length of content, if length is not the same as "size", return an error;
		if(content.length() == this.size) {
			this.content = content;
		}else if(content.length() == 16) {
			this.content = content.substring(4, 16);
			//fault handle will be implement in later version.
		}else {
			this.content = "Error";
		}
	}
}
