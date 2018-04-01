package core;

import java.io.IOException;

import gui.Controller;

//import gui.controllers.*;
//import gui.css.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Pane mainPane = loadMainPane();
        BorderPane root = new BorderPane();
        root.setCenter(mainPane);

        primaryStage.setTitle("SIMULATED COMPUTER");
        primaryStage.setScene(new Scene(root, 380, 500));
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    private Pane loadMainPane() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("gui/views/MainPane.fxml"));
        Pane mainPane = loader.load();
        Controller controller = loader.getController();
        controller.initialise();
        return mainPane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
