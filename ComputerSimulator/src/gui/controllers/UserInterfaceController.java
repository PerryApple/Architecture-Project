package gui.controllers;

import core.CPU;
import core.Cache;
import core.CacheLine;
import core.Controler;
import core.Decoder;
import core.Halt;
import core.HashCharEncode;
import core.IOmemory;
import gui.Controller;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class UserInterfaceController implements Controller {
	//Flags for condition deciding.
	private static boolean open = false;
	private boolean loadStatus = false;
	private boolean paraLoadStatus = false;
	private static int programNumber = 0;
	private String paragraph = "";
	private String userInput = "";
	private static int p2SentenceNumber = -1;
	private static int p2WordNumber = -1;
	private static String stepInformation;
	//MultiThread
	Halt halt = new Halt();
	Thread simulator = new Thread(halt);
	
	CPU cpu = CPU.getInstance();
	//Element in Interface
	@FXML private TextArea Printer;
	@FXML private TextField Input;
	
    @Override
    public void initialise() {

    }

    public void Switch() {
    		if (!open) {
            // clear all data before run
            CPU.getInstance().clearAll();
            CPU.getInstance().getMemory().clear();
            IOmemory.getInstance().clearIOmemory();
            Cache.getInstance().clearCache();
            Decoder.getInstance().clear();
            Printer.setText("Welcome to use Computer Simulator implemented by group 6.\r\n"
            		+ "Group member: Yunpeng Li, Linghai Xu, Zhuo Zheng, Yunlong Zhao");
            Input.setText("");
            loadStatus = false;
            paraLoadStatus = false;
            p2SentenceNumber = -1;
            p2WordNumber = -1;
            // show the information and reverse open status
            open = !open;
        }
        else {
        		open = !open;
            shutdown();
        }
    }
    //shut down
    private void shutdown() {
    		Printer.setText("");
    		Input.setText("");
    		Halt.flag=true;
    }

    public void input() {
    		if(programNumber == 1) {
    			userInput = Input.getText();
    	        String binaryInput = CPU.alignment(Integer.toBinaryString(Integer.valueOf(userInput))); 
    	        IOmemory.getInstance().setContent("00000", binaryInput);
    	        Printer.setText("Input: " + binaryInput);
    	        Input.setText("");
    		}else if(programNumber == 2) {
    			userInput = Input.getText();
    			String encodedUserWord = HashCharEncode.getCode(userInput);
    			IOmemory.getInstance().setContent("00000", CPU.alignment(encodedUserWord));
    			String currentPrinterText = Printer.getText();
    			Printer.setText(currentPrinterText + "User Inputed Word: " + userInput);
    		}
    }

    public void loadP1() {
    	if(open){
            if(loadStatus) {
            		Printer.setText("Program" + programNumber +"Already loaded!");
            }else {
                BufferedReader br=null;
                FileReader fr=null;
                try{
                		programNumber = 1;
                    String txt="Program1.txt";
                    fr=new FileReader(txt);
                    br=new BufferedReader(fr);
                    String line;
                    while((line=br.readLine())!=null){
                        if(line.substring(0,2).equals("//")) {
                            continue;
                        }
                        String[] contents = line.split(",");
                        CPU.getInstance().getMemory().setContent(contents[0], contents[1]);
                    }
                    
                    Printer.setText("Program1 has been successfully loaded");
                    loadStatus = true;
                    //put the beginning address of a program into PC.
                    CPU.getInstance().getPC().setContent("000001111110");
                    simulator.start();

                }catch (IOException e){
                    Printer.setText("Program1 Load Error");
                    System.out.println(e.toString());
                }
            }
        }
    }

    public void loadP2() {
	    	if(open){
            if(loadStatus) {
                Printer.setText("Program" + programNumber +"Already loaded!");
            }else {
                BufferedReader br=null;
                FileReader fr=null;
                try{
                		UserInterfaceController.programNumber = 2;
                    String txt="Program2.txt";
                    fr=new FileReader(txt);
                    br=new BufferedReader(fr);
                    String line;
                    
                    while((line=br.readLine())!=null){
                    	//If this line starts with "//", skip it
                        if(line.substring(0,2).equals("//")) {
                            continue;
                        	}
                        // else load data or instructions into memory
                        String[] contents = line.split(",");
                        CPU.getInstance().getMemory().setContent(contents[0], contents[1]);
                    }
                        
                    Printer.setText("Program2 has been successfully loaded.");
                    loadStatus = true;
                    //put the beginning address of a program into PC.
                    cpu.getPC().setContent("000001000000");
                    simulator.start();
                }catch (IOException e){
                    Printer.setText("Program2 Load error");
                    System.out.println(e.toString());
                }
            }
        }
    }

    public void p2Paragraph() {
    		if(open) {
    			if(paraLoadStatus) {
    				Printer.setText("Paragraph has already been loaded!");
    			}else{
    				 BufferedReader br=null;
    	             FileReader fr=null;
    	             try{
                     String txt="Program2 Paragraph.txt";
                     fr=new FileReader(txt);
                     br=new BufferedReader(fr);
                     String line;
                     while((line=br.readLine())!=null){
                     	//Store sentences in an array of String
                    	 	paragraph = paragraph + line + "\n";
                     }
                     //Split String paragraph
                     String[] sentences = paragraph.split("\\n");
                     //Encode the paragraph and store in memory begin at Address[512]
                     HashCharEncode.saveInMemory("001000000000", sentences);
                     Printer.setText(paragraph);
                     paraLoadStatus = true;

                 }catch (IOException e){
                     Printer.setText("Program2 Load error");
                     System.out.println(e.toString());
                 }
    			}
    		}
    }
    
    //print result of Program2 in printer
    private void showP2Result() {
    		if(open && programNumber == 2) {
    			if(p2SentenceNumber != -1 && p2WordNumber != -1) {
    				if(p2SentenceNumber == 0) {
    					Printer.setText(paragraph + "\n" + "The user input word DOES NOT exist in this paragraph!");
    				}else {
    					Printer.setText(paragraph + "\n" + "The user input word first appears in SENTENCE NO." + p2SentenceNumber + " WORD NO." + p2WordNumber + "!");
    				}	
    			}
    		}
    }
    
//    Get content from Device "printer"
    public static void getP2Result(String printerContent) {
    		if(open && programNumber == 2) {
			if(p2SentenceNumber == -1) {
				p2SentenceNumber = Integer.valueOf(printerContent, 2);
			}else if(p2WordNumber == -1) {
				p2WordNumber = Integer.valueOf(printerContent, 2);
			}
	    	}
    }
    
//	Get step information
    public static void setStepInformation(String information) {
    		stepInformation = information;
    }
//	Display step information
    public void showStepInformation() {
    		Printer.setText(stepInformation);
    }

    public void Run() {
    		if(!Controler.getInstance().end) {
			if(open && loadStatus) {
	    			Halt.flag = false;
	    			while(!Halt.flag){}
	    			if(programNumber == 2) {
	    				showP2Result();
	    			}
	    			showStepInformation();
			}
    		}else{
			Platform.exit();
			System.exit(0);
		}
    }

    public void seeMemory() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader()
                .getResource("gui/views/SeeMemoryPane.fxml"));
        Pane seeMemoryPane = loader.load();
        Controller controller = loader.getController();
        controller.initialise();
        Stage stage = new Stage();

        stage.setTitle("Cache");
        stage.setScene(new Scene(seeMemoryPane));
        stage.setResizable(false);
        stage.show();
    }

    public void seeCache() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader()
                .getResource("gui/views/SeeCachePane.fxml"));
        Pane seeCachePane = loader.load();
        Controller controller = loader.getController();
        controller.initialise();
        Stage stage = new Stage();

        stage.setTitle("Cache");
        stage.setScene(new Scene(seeCachePane));
        stage.setResizable(false);
        stage.show();
    }

    public void seeRegisters() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader()
                .getResource("gui/views/SeeRegistersPane.fxml"));
        Pane seeRegistersPane = loader.load();
        Controller controller = loader.getController();
        controller.initialise();
        Stage stage = new Stage();

        stage.setTitle("Registers");
        stage.setScene(new Scene(seeRegistersPane));
        stage.setResizable(false);
        stage.show();
    }
    
    public static boolean getOpen() {
        return open;
    }
}