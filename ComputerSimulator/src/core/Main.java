package core;

import java.io.IOException;

import gui.Controller;

//import gui.controllers.*;
//import gui.css.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Pane centerPane = loadCenterPane();
        BorderPane root = new BorderPane();
        root.setCenter(centerPane);

        primaryStage.setTitle("SIMULATED COMPUTER");
        primaryStage.setScene(new Scene(root, 750, 640));
//        primaryStage.setMaxWidth(750);
//        primaryStage.setMaxHeight(520);
        primaryStage.show();
    }

    private Pane loadCenterPane() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("gui/views/CenterPane.fxml"));
        Pane centerPane = loader.load();
        Controller controller = loader.getController();
        controller.initialise();
        return centerPane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
