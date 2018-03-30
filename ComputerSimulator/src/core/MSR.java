package core;

public class MSR implements Register {
    private static final String name = "MSR";
    //Size of MLR is 16bits.
    private static final int size = 16;
    //Initially set the content to 16 zeroes.
    private String content = "0000000000000000";
    //Our computer has only one MLR register
    //So I use singleton pattern to design this class
    private static final MSR instance = new MSR();

    private MSR() {}

    public static Register getInstance() {
        return instance;
    }

    public String getName() {
        // TODO Auto-generated method stub
        return name;
    }

    @Override
    public String getContent() {
        // TODO Auto-generated method stub
        return CPU.alignment(content);
    }

    @Override
    public void setContent(String content) {
        // TODO Auto-generated method stub
        if(content.length() == 16) {
            this.content = content ;
        }else {
            this.content = "MLR Length Error!";
        }
    }

    @Override
    public int getSize() {
        // TODO Auto-generated method stub
        return size;
    }
}
