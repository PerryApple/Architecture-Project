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
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class P3ParagraphPaneController implements Controller {
    @FXML private TextArea paragraph;
    @Override
    public void initialise(){}

    public void saveParagraph() throws IOException {
        System.out.println(paragraph.getText());
    }
}
