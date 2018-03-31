package gui.controllers;

import core.Cache;
import core.CacheLine;
import gui.Controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;


public class SeeCachePaneController implements Controller {
    @FXML private TableView<CacheLineOuput> CacheView;
    @FXML private TableColumn TAG;
    @FXML private TableColumn Valid;
    @FXML private TableColumn BLOCK1;
    @FXML private TableColumn BLOCK2;
    @FXML private TableColumn BLOCK3;
    @FXML private TableColumn BLOCK4;

    ObservableList<CacheLineOuput> data;

    @Override
    public void initialise() {
        updateCache();

    }

    public void updateCache(){
        TAG.setCellValueFactory(new PropertyValueFactory<CacheLineOuput, String>("TAG"));
        Valid.setCellValueFactory(new PropertyValueFactory<CacheLineOuput, String>("Valid"));
        BLOCK1.setCellValueFactory(new PropertyValueFactory<CacheLineOuput, String>("BLOCK1"));
        BLOCK2.setCellValueFactory(new PropertyValueFactory<CacheLineOuput, String>("BLOCK2"));
        BLOCK3.setCellValueFactory(new PropertyValueFactory<CacheLineOuput, String>("BLOCK3"));
        BLOCK4.setCellValueFactory(new PropertyValueFactory<CacheLineOuput, String>("BLOCK4"));

        data = FXCollections.observableArrayList();
        for(CacheLine tmp: Cache.getInstance().getCacheLines()) {
            CacheLineOuput cur = new CacheLineOuput(tmp.getTag(), tmp.getValid(), tmp.getBlock(0), tmp.getBlock(1), tmp.getBlock(2), tmp.getBlock(3));
            data.add(cur);
        }
        CacheView.setItems(data);
    }

    public static class CacheLineOuput {
        private final SimpleStringProperty TAG;
        private final SimpleStringProperty Valid;
        private final SimpleStringProperty BLOCK1;
        private final SimpleStringProperty BLOCK2;
        private final SimpleStringProperty BLOCK3;
        private final SimpleStringProperty BLOCK4;

        public CacheLineOuput(String TAG, String Valid, String BLOCK1, String BLOCK2, String BLOCK3, String BLOCK4) {
            this.TAG = new SimpleStringProperty(TAG);
            this.Valid = new SimpleStringProperty(Valid);
            this.BLOCK1 = new SimpleStringProperty(BLOCK1);
            this.BLOCK2 = new SimpleStringProperty(BLOCK2);
            this.BLOCK3 = new SimpleStringProperty(BLOCK3);
            this.BLOCK4 = new SimpleStringProperty(BLOCK4);
        }

        public String getTAG() {
            return TAG.get();
        }

        public String getValid() {
            return Valid.get();
        }

        public String getBLOCK1() {
            return BLOCK1.get();
        }

        public String getBLOCK2() {
            return BLOCK2.get();
        }

        public String getBLOCK3() {
            return BLOCK3.get();
        }

        public String getBLOCK4() {
            return BLOCK4.get();
        }
    }
}
