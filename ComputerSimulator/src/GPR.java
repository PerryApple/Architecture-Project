//GPR stands for General Program Register. our computer has 4 GPRs.
public class GPR extends Register {
	private String name;
	//The size of GPR is constant, so declare the attribute "size" as 16.
	private static final int size = 16;
	//Initially set content in GPR as 16 zeroes.
	private String content = "0000000000000000";
	
	//Constructor
	//When instantiate, the name of the instance should be the same as the parameter "name".
	public GPR(String name) {
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
		}else {
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
