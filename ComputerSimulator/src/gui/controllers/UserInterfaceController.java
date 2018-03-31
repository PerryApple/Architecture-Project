package gui.controllers;

import core.Cache;
import core.CacheLine;
import gui.Controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class UserInterfaceController implements Controller {

    @Override
    public void initialise() {

    }

    public void Switch() {

    }

    public void input() {

    }

    public void loadP2() {

    }

    public void loadP3() {

    }

    public void P3Program() {

    }

    public void Run() {

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
}