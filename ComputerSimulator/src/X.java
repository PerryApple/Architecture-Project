//X stands to index register. our computer has 3 index registers.
public class X extends Register {
	//Our computer has 3 index register which will be named to X1, X2, X3 when instantiate.
	private String name;
	//The size of index registers is constant, so declare the attribute "size" as 16.
	private static final int size = 16;
	//Initially set content in X as 16 zeroes.
	private String content = "0000000000000000";
	
	//Constructor
	//When instantiate the class, name of the instance should be the same as the parameter "name".
	public X(String name) {
		this.name = name;
	}
	
	//Override method inherited from super class
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
