//IAR stands for Internal Address Register. There might be several IAR in a computer.
public class IAR extends Register {
	private String name;
	//Size of MAR is 16bits.
	private static final int size = 16;
	//Initially set the content to 16 zeroes.
	private String content = "0000000000000000";
	
	public IAR(String name) {
		this.name = name;
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
			this.content = name + " Error";
		}		
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 16;
	}

}
