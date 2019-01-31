package org.oz.fx;

import com.jfoenix.controls.JFXListView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.oz.fx.control.ItemReader;
import org.oz.fx.control.VideoView;


public class App3 extends Application {


    public final static class AppController {


        @FXML
        JFXListView list_RFID;


        @FXML
        JFXListView list_monitor;


        private void initView() {

            ObservableList readerItems = list_RFID.getItems();

            ObservableList monitorItems = list_monitor.getItems();

            for (int i = 0; i < 10; i++) {

                final ItemReader itemReader = new ItemReader();

                itemReader.setText("reader" + (i + 1));

                readerItems.add(itemReader);

            }

            String url1 = "rtsp://admin:lz123456@192.168.1.201/h265/ch1/main/av_stream";
            String url2 = "rtmp://58.200.131.2:1935/livetv/hunantv";


            monitorItems.add(new StackPane(new VideoView(url1, 6)));

            monitorItems.add(new StackPane(new VideoView(url2, 3)));

            monitorItems.add(new StackPane(new VideoView(url1, 6)));

            monitorItems.add(new StackPane(new VideoView(url2, 3)));


        }

        private void initData() {

            System.out.println("init data 。。。。");

            System.out.println(list_RFID.getId());

        }


        @FXML
        public void initialize() {

            System.out.println("initialize.........");

            initData();

            initView();

        }


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
