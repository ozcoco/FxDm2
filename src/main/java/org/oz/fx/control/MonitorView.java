package org.oz.fx.control;

import com.sun.jna.Memory;
import com.sun.jna.NativeLibrary;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.DefaultDirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.nio.ByteBuffer;

public class MonitorView extends AnchorPane {

    /**
     * Pixel writer to update the canvas.
     */
    private PixelWriter pixelWriter;

    /**
     * Pixel format.
     */
    private WritablePixelFormat<ByteBuffer> pixelFormat;


    private DirectMediaPlayer mediaPlayer;

    private AnimationTimer animationTimer;

    private Canvas canvas;

    private StackPane vRoot;

    private String url;

    private int scale = 1;

    public MonitorView() {
        this(1, null);
    }

    public MonitorView(int scale, String url) {

        super();

        this.scale = scale;

        this.url = url;

        initialize();


    }

    public void initialize() {

        initData();

        initView();
    }

    private void initView() {

        vRoot = new StackPane();

        getChildren().add(vRoot);

        canvas = new Canvas();

        vRoot.getChildren().add(canvas);

        pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();

        pixelFormat = PixelFormat.getByteBgraInstance();

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

        mediaPlayer = new DirectMediaPlayerComponent((sourceWidth, sourceHeight) -> {

            final int width = sourceWidth / scale;

            final int height = sourceHeight / scale;

            Platform.runLater(() -> {

                canvas.setWidth(width);

                canvas.setHeight(height);

            });


            return new RV32BufferFormat(width, height);
        }

        ).getMediaPlayer();

        mediaPlayer.playMedia(url);


       /* new Thread(() -> {

            for (; ; ) {

                Platform.runLater(this::renderFrame);

                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }).start();*/

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                renderFrame();
            }
        };

        animationTimer.start();
    }


    private void initData() {

    }


    public void play(String url) {

        assert mediaPlayer != null;

        this.url = url;

        mediaPlayer.playMedia(url);

        start();

    }


    private void start() {

        assert animationTimer != null;

        animationTimer.start();
    }

    private void stop() {

        assert animationTimer != null;

        animationTimer.stop();
    }

    private void renderFrame() {

        final Memory[] nativeBuffers = mediaPlayer.lock();

        if (nativeBuffers != null) {

            final Memory nativeBuffer = nativeBuffers[0];

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


    public void close() {

        assert mediaPlayer != null;

        mediaPlayer.stop();

        mediaPlayer.release();

    }

}
