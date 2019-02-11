package org.oz.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.oz.fx.control.MonitorView;
import org.oz.fx.control.VideoView;

public class App4 extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("App4");

        primaryStage.setAlwaysOnTop(true);

        primaryStage.setResizable(false);

        primaryStage.setMaximized(false);

        String url = "rtmp://58.200.131.2:1935/livetv/hunantv";

//        MonitorView root = new MonitorView(4, url);

        VideoView videoView = new VideoView(url, 4);

        FlowPane root = new FlowPane(videoView);

        primaryStage.setScene(new Scene(root));

        primaryStage.show();

    }
}
