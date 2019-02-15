package org.oz.fx.control;

import com.sun.jna.Memory;
import com.sun.jna.NativeLibrary;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.DefaultDirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.nio.ByteBuffer;

public class VideoView extends Canvas {

    private String url;

    private int videoWidth, videoHeight;

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

    private int scale;


    private DirectMediaPlayer mediaPlayer;

    public VideoView(String url, int scale) {
        super();
        this.url = url;

        this.scale = scale;

        initialize();
    }

    private void initialize() {

        pixelWriter = getGraphicsContext2D().getPixelWriter();

        pixelFormat = PixelFormat.getByteBgraInstance();

        initPlayer();

    }


    private void initPlayer() {

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

        final DirectMediaPlayerComponent playerComponent = new DirectMediaPlayerComponent((sourceWidth, sourceHeight) -> {

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

                setWidth(width / scale);

                setHeight(height / scale);

            });

            return new RV32BufferFormat(width / scale, height / scale);
        }
        );

        mediaPlayer = playerComponent.getMediaPlayer();

        mediaPlayer.playMedia(url);

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                renderFrame();
            }
        };

        animationTimer.start();

    }


    private void renderFrame() {
        Memory[] nativeBuffers = mediaPlayer.lock();
        if (nativeBuffers != null) {
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


}
