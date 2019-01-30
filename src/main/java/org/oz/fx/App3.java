package org.oz.fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class App3 extends Application {


    public final static class AppController {


    }


    @Override
    public void init() throws Exception {
        super.init();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("App3");

        primaryStage.setAlwaysOnTop(true);

        primaryStage.setResizable(false);

        primaryStage.setMaximized(false);

        AnchorPane root = FXMLLoader.load(getClass().getResource("/layout/app3.fxml"));

        primaryStage.setScene(new Scene(root));

        primaryStage.show();

    }


    @Override
    public void stop() throws Exception {
        super.stop();

        Platform.exit();
    }


}
