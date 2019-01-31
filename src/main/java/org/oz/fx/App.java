package org.oz.fx;

import com.sun.jna.Memory;
import com.sun.jna.NativeLibrary;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import javafx.stage.Stage;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.DefaultDirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.io.IOException;
import java.nio.ByteBuffer;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

//    private static final String VIDEO_FILE = "rtsp://admin:lz123456@192.168.1.201/h264/ch1/main/av_stream";
    private static final String VIDEO_FILE = "rtmp://58.200.131.2:1935/livetv/hunantv";

    /**
     * Pixel writer to update the canvas.
     */
    private PixelWriter pixelWriter;

    /**
     * Pixel format.
     */
    private WritablePixelFormat<ByteBuffer> pixelFormat;

    /**
     * Set this to <code>true</code> to resize the display to the dimensions of the
     * video, otherwise it will use {@link #WIDTH} and {@link #HEIGHT}.
     */
    private static final boolean useSourceSize = true;

    /**
     * Target width, unless {@link #useSourceSize} is set.
     */
    private static final int WIDTH = 720;

    /**
     * Target height, unless {@link #useSourceSize} is set.
     */
    private static final int HEIGHT = 576;

    private Stage stage;

    /**
     * Lightweight JavaFX canvas, the video is rendered here.
     */
    private Canvas canvas;

    ScheduledService<Void> scheduledService;

    @Override
    public void start(Stage primaryStage) throws IOException {

        this.stage = primaryStage;

        primaryStage.setTitle("VlcJ");

        Group group = new Group();

        group.setAutoSizeChildren(true);

        canvas = new Canvas();

        group.getChildren().add(canvas);

        Scene scene = new Scene(group);

        primaryStage.setScene(scene);

        primaryStage.show();

        pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();

        pixelFormat = PixelFormat.getByteBgraInstance();

        initPlayer();

    }


    DirectMediaPlayer mediaPlayer;

    DirectMediaPlayerComponent playerComponent;

    void initPlayer() {

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

        playerComponent = new DirectMediaPlayerComponent((sourceWidth, sourceHeight) -> {
            final int width;
            final int height;
            if (useSourceSize) {
                width = sourceWidth;
                height = sourceHeight;
            } else {
                width = WIDTH;
                height = HEIGHT;
            }

            Platform.runLater(() -> {
                canvas.setWidth(width);
                canvas.setHeight(height);
                stage.setWidth(width);
                stage.setHeight(height);
            });

            return new RV32BufferFormat(width, height);
        }
        );

        mediaPlayer = playerComponent.getMediaPlayer();

        playerComponent.getMediaPlayer().playMedia(VIDEO_FILE);

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!");

                System.out.println(Thread.currentThread().getName());

                renderFrame();

            }
        };

        animationTimer.start();

    }


    protected final void renderFrame() {
        Memory[] nativeBuffers = mediaPlayer.lock();
        if (nativeBuffers != null) {
            // FIXME there may be more efficient ways to do this...
            // Since this is now being called by a specific rendering time, independent of the native video callbacks being
            // invoked, some more defensive conditional checks are needed
            Memory nativeBuffer = nativeBuffers[0];
            if (nativeBuffer != null) {
                ByteBuffer byteBuffer = nativeBuffer.getByteBuffer(0, nativeBuffer.size());
                BufferFormat bufferFormat = ((DefaultDirectMediaPlayer) mediaPlayer).getBufferFormat();
                if (bufferFormat.getWidth() > 0 && bufferFormat.getHeight() > 0) {
                    pixelWriter.setPixels(0, 0, bufferFormat.getWidth(), bufferFormat.getHeight(), pixelFormat, byteBuffer, bufferFormat.getPitches()[0]);
                }
            }
        }
        mediaPlayer.unlock();
    }


    @FXML
    public void onTest(ActionEvent event) {


    }


}
