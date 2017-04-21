package org.mapsforge.map.javafx.graphics;

/**
 * Copyright (c) 2008-2016 DIEHL Metering - All Rights Reserved. NOTICE: All information contained herein is, and remains the property of DIEHL Metering
 * and its suppliers, if any. The intellectual and technical concepts contained herein are proprietary to DIEHL Metering and its suppliers and may
 * be covered by U.E. and Foreign Patents, patents in process, and are protected by trade secret or copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless prior written permission is obtained from DIEHL Metering.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import org.mapsforge.core.graphics.Bitmap;

/**
 * @author pickelm
 *
 */
public class JavaFxBitmap implements Bitmap {

    /** The javafx image. */
    private Canvas image;

    public JavaFxBitmap(final int width, final int height) {
        super();
        this.image = new Canvas(width, height);
    }

    public JavaFxBitmap(final String url) {
        this(new Image(url));
    }

    public JavaFxBitmap(final InputStream is) {
        this(new Image(is));
    }

    public JavaFxBitmap(final Image image) {
        super();
        if (image == null) {
            this.image = new Canvas(0, 0);
        } else {
            this.image = new Canvas(image.getWidth(), image.getHeight());
            this.image.getGraphicsContext2D().drawImage(image, 0, 0);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Bitmap#compress(java.io.OutputStream)
     */
    @Override
    public void compress(final OutputStream outputStream) throws IOException {
        if (!Platform.isFxApplicationThread()) {

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    final Image img = JavaFxBitmap.this.render();
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", outputStream);
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Bitmap#decrementRefCount()
     */
    @Override
    public void decrementRefCount() {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Bitmap#getHeight()
     */
    @Override
    public int getHeight() {
        if (this.image == null) {
            return 0;
        }
        return (int) this.image.getHeight();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Bitmap#getWidth()
     */
    @Override
    public int getWidth() {
        if (this.image == null) {
            return 0;
        }
        return (int) this.image.getWidth();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Bitmap#incrementRefCount()
     */
    @Override
    public void incrementRefCount() {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Bitmap#isDestroyed()
     */
    @Override
    public boolean isDestroyed() {
        return this.image == null;
    }

    public Image render() {
        final WritableImage img = new WritableImage((int) this.image.getWidth(), (int) this.image.getHeight());
        this.image.snapshot(null, img);
        return img;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Bitmap#scaleTo(int, int)
     */
    @Override
    public void scaleTo(final int width, final int height) {
        final Image img = this.render();
        this.image = new Canvas(width, height);
        this.image.getGraphicsContext2D().drawImage(img, 0, 0, width, height);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Bitmap#setBackgroundColor(int)
     */
    @Override
    public void setBackgroundColor(final int col) {
        final GraphicsContext graphicsContext2D = this.image.getGraphicsContext2D();
        graphicsContext2D.setFill(Color.rgb(col >> 16 & 0xFF, col >> 8 & 0xFF, col & 0xFF));
        graphicsContext2D.fill();
    }

    /**
     * Getter.
     * 
     * @return the image
     */
    public Canvas getImage() {
        return this.image;
    }

}
