package gui.controllers;

import core.CPU;
import gui.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SeeMemoryPaneController implements Controller {
    @FXML private TextField Address;
    @FXML private TextField Content;

    @Override
    public void initialise(){

    }

    public void searchMemory() {
        String searchField = Address.getText();
        Content.setText(CPU.getInstance().getMemory().getContent(searchField));
    }
}