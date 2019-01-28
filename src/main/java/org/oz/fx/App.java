package org.oz.fx;

import com.sun.jna.NativeLibrary;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.io.IOException;
import java.util.Random;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @FXML
    Label btn_show;

    @FXML
    MediaView mediaView;


    ScheduledService<Void> scheduledService;

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/app.fxml"));

        primaryStage.setTitle("Hello World");

        Scene scene = new Scene(root);

        scene.getWindow();

        primaryStage.setScene(scene);

        primaryStage.show();


        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!");

                System.out.println(Thread.currentThread().getName());

                stop();
            }
        };

        animationTimer.start();


        scheduledService = new ScheduledService<Void>() {

            private boolean isRunning = false;

            @Override
            protected Task<Void> createTask() {

                if (isRunning) {

                    return null;

                } else

                    isRunning = true;

                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {

                        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

                        System.out.println(Thread.currentThread().getName());

                        return null;
                    }
                };
            }
        };

        scheduledService.setPeriod(Duration.seconds(0.00000001));

        scheduledService.setOnRunning(new EventHandler<WorkerStateEvent>() {

            public void handle(WorkerStateEvent event) {


                System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhh");

                System.out.println(Thread.currentThread().getName());

            }
        });

        scheduledService.start();

    }


    @FXML
    public void onTest(ActionEvent event) {

        boolean found = new NativeDiscovery().discover();
        System.out.println(found);
        System.out.println(LibVlc.INSTANCE.libvlc_get_version());

        //指定VLC路径，这里使用的路径是安装默认路径。
        if (RuntimeUtil.isWindows()) {
            NativeLibrary.addSearchPath(
                    RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC");

            System.out.println("Windows");

        } else if (RuntimeUtil.isMac()) {
            NativeLibrary.addSearchPath(
                    RuntimeUtil.getLibVlcLibraryName(), "/Applications/VLC.app/Contents/MacOS/lib");

            System.out.println("Mac");

        } else if (RuntimeUtil.isNix()) {

            NativeLibrary.addSearchPath(
                    RuntimeUtil.getLibVlcLibraryName(), "/lib64");

            System.out.println("Linux");

        }
        //打印版本，用来检验是否获得文件
        System.out.println(LibVlc.INSTANCE.libvlc_get_version());

        EmbeddedMediaPlayerComponent playerComponent = new EmbeddedMediaPlayerComponent();

        AnchorPane pane = new AnchorPane();

//        pane.getChildren().add();

        scheduledService.reset();


//        scheduledService.cancel();

    }

}
