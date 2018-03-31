package gui.controllers;

import gui.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPaneController implements Controller {


    @Override
    public void initialise(){}

    public void viewUserInterface() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader()
                .getResource("gui/views/UserInterfacePane.fxml"));
        Pane userInterface = loader.load();
        Controller controller = loader.getController();
        controller.initialise();
        Stage stage = new Stage();

        stage.setTitle("User Simpler Interface");
        stage.setScene(new Scene(userInterface));
        stage.setResizable(false);
        stage.show();
    }

    public void viewEngineerConsole() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader()
                .getResource("gui/views/EngineerConsolePane.fxml"));
        Pane engineerConsole = loader.load();
        Controller controller = loader.getController();
        controller.initialise();
        Stage stage = new Stage();

        stage.setTitle("Engineer Console");
        stage.setScene(new Scene(engineerConsole));
        stage.setResizable(false);
        stage.show();
    }
}
