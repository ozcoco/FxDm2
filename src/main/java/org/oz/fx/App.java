package org.oz.fx;

import com.sun.jna.NativeLibrary;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import uk.co.caprica.vlcj.binding.LibVlc;
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


    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/app.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));

        primaryStage.show();

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
        } else if (RuntimeUtil.isMac()) {
            NativeLibrary.addSearchPath(
                    RuntimeUtil.getLibVlcLibraryName(), "/Applications/VLC.app/Contents/MacOS/lib");
        } else if (RuntimeUtil.isNix()) {
            NativeLibrary.addSearchPath(
                    RuntimeUtil.getLibVlcLibraryName(), "/home/linux/vlc/install/lib");
        }
        //打印版本，用来检验是否获得文件
        System.out.println(LibVlc.INSTANCE.libvlc_get_version());

    }

}
