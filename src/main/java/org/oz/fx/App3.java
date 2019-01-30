package org.oz.fx;

import com.jfoenix.controls.JFXListView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class App3 extends Application {


    public final static class AppController implements Initializable {


        @FXML
        JFXListView list_RFID;


        @FXML
        JFXListView list_monitor;


        @Override
        public void initialize(URL location, ResourceBundle resources) {

            System.out.println("initialize.........");

            initData();

            initView();

        }


        private void initView() {

            ObservableList readerItems = list_RFID.getItems();

            ObservableList monitorItems = list_monitor.getItems();


            for (int i = 0; i < 30; i++) {

                readerItems.add(new Label("reader" + (i + 1)));

                monitorItems.add(new Label("monitor" + (i + 1)));

            }
        }

        private void initData() {

            System.out.println("init data 。。。。");
        }



        /*        @FXML
        private void initialize() {

            System.out.println("initialize.........");

            initData();

            initView();

        }*/


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
