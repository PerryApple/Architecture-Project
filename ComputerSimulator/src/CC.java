//Reserved Register CC, Only one instance of this class exist in computer
public class CC implements Register {

	private static final String name  = "CC";
	//Size of Program Counter is 4bits.
	private static final int size = 4;
	//Initially set the content to 4 zeroes.
	private String content = "0000";
	//create a static final CC instance.
	private static final CC instance = new CC();
	
	private CC() {
		
	}
	// Singleton Pattern to make sure there is only one PC instance.
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
		return content;
	}

	@Override
	public void setContent(String content) {
		// TODO Auto-generated method stub
		//Reserved
		if(content.equalsIgnoreCase("OF")) {
			this.content = "1" + this.content.substring(1);
		}else if(content.equalsIgnoreCase("UF")) {
			this.content = this.content.substring(0, 1) + "1" + this.content.substring(2);
		}else if (content.equalsIgnoreCase("DZ")){
			this.content = this.content.substring(0,2) + "1" + this.content.substring(3);
		}else if (content.equalsIgnoreCase("EQ")) {
			this.content = this.content.substring(0,3) + "1";
		}else if(content.equalsIgnoreCase("NOF")) {
			this.content = "0" + this.content.substring(1);
		}else if(content.equalsIgnoreCase("NUF")) {
			this.content = this.content.substring(0, 1) + "0" + this.content.substring(2);
		}else if (content.equalsIgnoreCase("NDZ")){
			this.content = this.content.substring(0,2) + "0" + this.content.substring(3);
		}else if (content.equalsIgnoreCase("NEQ")) {
			this.content = this.content.substring(0,3) + "0";
		}else if (content.equalsIgnoreCase("0000")){
			this.content = "0000";
		}else {
			content = "CC Error!";
		}
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return size;
	}

}
