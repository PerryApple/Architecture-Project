//Reserved Register MFR, Only one instance of this class exist in computer
public class MFR extends Register {

	private static final String name  = "MFR";
	//Size of Program Counter is 4bits.
	private static final int size = 4;
	//Initially set the content to 4 zeroes.
	private String content = "0000";
	//create a static final CC instance.
	private static final MFR instance = new MFR();
	
	private MFR() {
		
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
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return size;
	}
}
