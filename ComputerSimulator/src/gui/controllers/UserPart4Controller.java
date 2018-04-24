package gui.controllers;

import core.BranchTargetBuffer;
import core.CPU;
import core.Cache;
import core.CacheLine;
import core.Controler;
import core.Decoder;
import core.DirectionPredictor;
import core.Halt;
import core.HashCharEncode;
import core.IOmemory;
import core.ReOrderBuffer;
import core.RegisterFile;
import core.ReservationStation;
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
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserPart4Controller implements Controller {
    @FXML private TextField PC;

    // Table Branch Predictor
    @FXML private TableView<BranchPredictorOutPut> BranchPredictorView;
    @FXML private TableColumn Address;
    @FXML private TableColumn BP;
    ObservableList<BranchPredictorOutPut> data;

    // Table Reorder Buffer
    @FXML private TableView<ReorderBufferOutput> ReorderBufferView;
    @FXML private TableColumn Index;
    @FXML private TableColumn Address1;
    @FXML private TableColumn Instruction;
    ObservableList<ReorderBufferOutput> data1;

    // Table Reservation Station
    @FXML private TableView<ReservationStationOutput> ReservationStationView;
    @FXML private TableColumn Index1;
    @FXML private TableColumn OpCode;
    @FXML private TableColumn Qi;
    @FXML private TableColumn Vi;
    @FXML private TableColumn Qj;
    @FXML private TableColumn Vj;
    @FXML private TableColumn des;
    @FXML private TableColumn ex;
    ObservableList<ReservationStationOutput> data2;

    // Branch Target Buffer
    @FXML private TableView<BranchTargetBufferOutput> BranchTargetBufferView;
    @FXML private TableColumn Address2;
    @FXML private TableColumn TargetAddress;
    ObservableList<BranchTargetBufferOutput> data3;

    // T1
    @FXML private TableView<T1Output> T1View;
    @FXML private TableColumn Register;
    @FXML private TableColumn Content;
    ObservableList<T1Output> data4;

    // T2
    @FXML private TableView<T2Output> T2View;
    @FXML private TableColumn Register1;
    @FXML private TableColumn Content1;
    ObservableList<T2Output> data5;

    @Override
    public void initialise() {
        PC.setText("000000000000");
        updateBranchPredictor();
        updateReorderBuffer();
        updateReservationStation();
        updateBranchTargetBuffer();
        updateT1();
        updateT2();
    }

    public void Refresh() {
        // Void
    		PC.setText(CPU.getInstance().getPC().getContent().substring(4));
    		updateBranchPredictor();
        updateReorderBuffer();
        updateReservationStation();
        updateBranchTargetBuffer();
        updateT1();
        updateT2();
    }

    public void updateBranchPredictor(){
        Address.setCellValueFactory(new PropertyValueFactory<BranchPredictorOutPut, String>("Address"));
        BP.setCellValueFactory(new PropertyValueFactory<BranchPredictorOutPut, String>("BP"));
        data = FXCollections.observableArrayList();
        Iterator it = DirectionPredictor.getInstance().getContent().entrySet().iterator();
        
        for(int i=0; i<10; i++) {
        		BranchPredictorOutPut cur;
        		
        		if(it.hasNext()) {
        			Map.Entry element = (Map.Entry)it.next();
        			cur = new BranchPredictorOutPut( (String)element.getKey(), (String)element.getValue());
        		}else {
        			cur = new BranchPredictorOutPut("000000000000", "00");    
        		}
        		 data.add(cur);
        }
        BranchPredictorView.setItems(data);
    }

    public void updateReorderBuffer() {
        Index.setCellValueFactory(new PropertyValueFactory<ReorderBufferOutput, String>("Index"));
        Address1.setCellValueFactory(new PropertyValueFactory<ReorderBufferOutput, String>("Address1"));
        Instruction.setCellValueFactory(new PropertyValueFactory<ReorderBufferOutput, String>("Instruction"));
        data1 = FXCollections.observableArrayList();
        Iterator it = ReOrderBuffer.getContent().entrySet().iterator();
        
        for(int i=1; i<11; i++) {
        		ReorderBufferOutput cur;
        		if(it.hasNext()) {
        			Map.Entry element = (Map.Entry)it.next();
        			cur = new ReorderBufferOutput(String.valueOf(i), ReOrderBuffer.getAddress(i), ReOrderBuffer.getInstruction(i));
        		}else {
        			cur = new ReorderBufferOutput(String.valueOf(i), "000000000000", "0000000000000000");
        		}
            data1.add(cur);
        }
        ReorderBufferView.setItems(data1);
    }

    public void updateReservationStation() {
        Index1.setCellValueFactory(new PropertyValueFactory<ReservationStationOutput, String>("Index1"));
        OpCode.setCellValueFactory(new PropertyValueFactory<ReservationStationOutput, String>("OpCode"));
        Qi.setCellValueFactory(new PropertyValueFactory<ReservationStationOutput, String>("Qi"));
        Vi.setCellValueFactory(new PropertyValueFactory<ReservationStationOutput, String>("Vi"));
        Qj.setCellValueFactory(new PropertyValueFactory<ReservationStationOutput, String>("Qj"));
        Vj.setCellValueFactory(new PropertyValueFactory<ReservationStationOutput, String>("Vj"));
        des.setCellValueFactory(new PropertyValueFactory<ReservationStationOutput, String>("des"));
        ex.setCellValueFactory(new PropertyValueFactory<ReservationStationOutput, String>("ex"));
        data2 = FXCollections.observableArrayList();
        Iterator it = ReservationStation.getContent().entrySet().iterator();
        for(int i=1; i<11; i++) {
            ReservationStationOutput cur;
            if(it.hasNext()) {
            		Map.Entry element = (Map.Entry)it.next();
            		ReservationStation.Instruction instruction = ReservationStation.getInstruction(i);
            		cur = new ReservationStationOutput(String.valueOf(i), instruction.opCode, String.valueOf(instruction.Qi), instruction.Vi, String.valueOf(instruction.Qj), instruction.Vj, instruction.des, String.valueOf(instruction.ex));
            }else {
            		cur = new ReservationStationOutput(String.valueOf(i), "000", "00", "0000000000000000", "00", "0000000000000000", "0000000000000000", "false");
            }
            data2.add(cur);
        }
        ReservationStationView.setItems(data2);
    }

    public void updateBranchTargetBuffer() {
        Address2.setCellValueFactory(new PropertyValueFactory<ReservationStationOutput, String>("Address2"));
        TargetAddress.setCellValueFactory(new PropertyValueFactory<ReservationStationOutput, String>("TargetAddress"));
        data3 = FXCollections.observableArrayList();
        Iterator it = BranchTargetBuffer.getInstance().getContent().entrySet().iterator();
        for(int i=0; i<10; i++) {
            BranchTargetBufferOutput cur;
            if(it.hasNext()) {
            		Map.Entry element = (Map.Entry)it.next();
            		cur = new BranchTargetBufferOutput((String)element.getKey(), (String)element.getValue());
            }else {
            		cur = new BranchTargetBufferOutput("000000000000", "000000000000");
            }
            data3.add(cur);
        }
        BranchTargetBufferView.setItems(data3);
    }

    public void updateT1() {
        Register.setCellValueFactory(new PropertyValueFactory<T1Output, String>("Register"));
        Content.setCellValueFactory(new PropertyValueFactory<T1Output, String>("Content"));
        data4 = FXCollections.observableArrayList();
        
        T1Output cur = new T1Output("R0", String.valueOf(RegisterFile.getRegisterName("00")));
        data4.add(cur);
        cur = new T1Output("R1", String.valueOf(RegisterFile.getRegisterName("01")));
        data4.add(cur);
        cur = new T1Output("R2", String.valueOf(RegisterFile.getRegisterName("10")));
        data4.add(cur);
        cur = new T1Output("R3", String.valueOf(RegisterFile.getRegisterName("11")));
        data4.add(cur);
        cur = new T1Output("X1", String.valueOf(RegisterFile.getRegisterName("x01")));
        data4.add(cur);
        cur = new T1Output("X2", String.valueOf(RegisterFile.getRegisterName("x10")));
        data4.add(cur);
        cur = new T1Output("X3", String.valueOf(RegisterFile.getRegisterName("x11")));
        data4.add(cur);
        T1View.setItems(data4);
    }

    public void updateT2() {
        Register1.setCellValueFactory(new PropertyValueFactory<T2Output, String>("Register1"));
        Content1.setCellValueFactory(new PropertyValueFactory<T2Output, String>("Content1"));
        data5 = FXCollections.observableArrayList();
        for(int i=1; i<11; i++) {
            T2Output cur = new T2Output(String.valueOf(i), RegisterFile.getTempResult(i));
            data5.add(cur);
        }
        T2View.setItems(data5);
    }

    public static class BranchPredictorOutPut {
        private final SimpleStringProperty Address;
        private final SimpleStringProperty BP;

        public BranchPredictorOutPut(String Address, String BP) {
            this.Address = new SimpleStringProperty(Address);
            this.BP = new SimpleStringProperty(BP);
        }

        public String getAddress() {
            return Address.get();
        }

        public String getBP() {
            return BP.get();
        }
    }

    public static class ReorderBufferOutput {
        private final SimpleStringProperty Index;
        private final SimpleStringProperty Address1;
        private final SimpleStringProperty Instruction;

        public ReorderBufferOutput(String Index, String Address1, String Instruction) {
            this.Index = new SimpleStringProperty(Index);
            this.Address1 = new SimpleStringProperty(Address1);
            this.Instruction = new SimpleStringProperty(Instruction);
        }

        public String getIndex() {
            return Index.get();
        }

        public String getAddress1() {
            return Address1.get();
        }

        public String getInstruction() {
            return Instruction.get();
        }
    }

    public static class ReservationStationOutput {
        private final SimpleStringProperty Index1;
        private final SimpleStringProperty OpCode;
        private final SimpleStringProperty Qi;
        private final SimpleStringProperty Vi;
        private final SimpleStringProperty Qj;
        private final SimpleStringProperty Vj;
        private final SimpleStringProperty des;
        private final SimpleStringProperty ex;

        public ReservationStationOutput(String Index1, String OpCode, String Qi, String Vi, String Qj, String Vj, String des, String ex) {
            this.Index1 = new SimpleStringProperty(Index1);
            this.OpCode = new SimpleStringProperty(OpCode);
            this.Qi = new SimpleStringProperty(Qi);
            this.Vi = new SimpleStringProperty(Vi);
            this.Qj = new SimpleStringProperty(Qj);
            this.Vj = new SimpleStringProperty(Vj);
            this.des = new SimpleStringProperty(des);
            this.ex = new SimpleStringProperty(ex);
        }

        public String getIndex1() {
            return Index1.get();
        }

        public String getOpCode() {
            return OpCode.get();
        }

        public String getQi() {
            return Qi.get();
        }
        public String getVi() {
            return Vi.get();
        }
        public String getQj() {
            return Qj.get();
        }
        public String getVj() {
            return Vj.get();
        }
        public String getDes() {
            return des.get();
        }
        public String getEx() {
            return ex.get();
        }
    }

    public static class BranchTargetBufferOutput {
        private final SimpleStringProperty Address2;
        private final SimpleStringProperty TargetAddress;

        public BranchTargetBufferOutput(String Address2, String TargetAddress) {
            this.Address2 = new SimpleStringProperty(Address2);
            this.TargetAddress = new SimpleStringProperty(TargetAddress);
        }

        public String getAddress2() {
            return Address2.get();
        }

        public String getTargetAddress() {
            return TargetAddress.get();
        }
    }

    public static class T1Output {
        private final SimpleStringProperty Register;
        private final SimpleStringProperty Content;

        public T1Output(String Register, String Content) {
            this.Register = new SimpleStringProperty(Register);
            this.Content = new SimpleStringProperty(Content);
        }

        public String getRegister() {
            return Register.get();
        }

        public String getContent() {
            return Content.get();
        }
    }

    public static class T2Output {
        private final SimpleStringProperty Register1;
        private final SimpleStringProperty Content1;

        public T2Output(String Register1, String Content1) {
            this.Register1 = new SimpleStringProperty(Register1);
            this.Content1 = new SimpleStringProperty(Content1);
        }

        public String getRegister1() {
            return Register1.get();
        }

        public String getContent1() {
            return Content1.get();
        }
    }
}
