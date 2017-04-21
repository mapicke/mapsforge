/**
 * Copyright (c) 2008-2016 DIEHL Metering - All Rights Reserved. NOTICE: All information contained herein is, and remains the property of DIEHL Metering
 * and its suppliers, if any. The intellectual and technical concepts contained herein are proprietary to DIEHL Metering and its suppliers and may
 * be covered by U.E. and Foreign Patents, patents in process, and are protected by trade secret or copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless prior written permission is obtained from DIEHL Metering.
 */

package org.mapsforge.map.javafx.graphics;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Filter;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Path;
import org.mapsforge.core.model.Dimension;

/**
 * @author pickelm
 *
 */
public class JavaFxCanvas implements Canvas {

    javafx.scene.canvas.Canvas canvas;
    javafx.scene.paint.Paint paintImage;
    GraphicsContext GRAPHICS_CONTEXT2D;

    public JavaFxCanvas() {
        this(0, 0);
    }

    public JavaFxCanvas(final double width, final double height) {
        this(new javafx.scene.canvas.Canvas(width, height));
    }

    public JavaFxCanvas(final javafx.scene.canvas.Canvas canvas) {
        this.canvas = canvas;
        this.GRAPHICS_CONTEXT2D = this.canvas.getGraphicsContext2D();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicContext#drawBitmap(org.mapsforge.core.graphics.Bitmap, int, int)
     */
    @Override
    public void drawBitmap(final Bitmap bitmap, final int left, final int top) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                JavaFxCanvas.this.GRAPHICS_CONTEXT2D.drawImage(((JavaFxBitmap) bitmap).render(), left, top);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicContext#drawBitmap(org.mapsforge.core.graphics.Bitmap, int, int, org.mapsforge.core.graphics.Filter)
     */
    @Override
    public void drawBitmap(final Bitmap bitmap, final int left, final int top, final Filter filter) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                JavaFxCanvas.this.GRAPHICS_CONTEXT2D.drawImage(((JavaFxBitmap) bitmap).render(), left, top);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicContext#drawBitmap(org.mapsforge.core.graphics.Bitmap, org.mapsforge.core.graphics.Matrix)
     */
    @Override
    public void drawBitmap(final Bitmap bitmap, final Matrix matrix) {
        JavaFxCanvas.this.GRAPHICS_CONTEXT2D.drawImage(((JavaFxBitmap) bitmap).render(), 25d, 25d); // TODO matrix not used!!!
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicContext#drawBitmap(org.mapsforge.core.graphics.Bitmap, org.mapsforge.core.graphics.Matrix,
     * org.mapsforge.core.graphics.Filter)
     */
    @Override
    public void drawBitmap(final Bitmap bitmap, final Matrix matrix, final Filter filter) {

        // Image image = JavaFxGraphicFactory.getBitmap(bitmap);
        // image.gett
        // GRAPHICS_CONTEXT2D.drawImage(image,
        // JavaFxGraphicFactory.getAffineTransform(matrix));
        System.out.println("--> not implemented not --> javafxcanvas.drawbitmap_");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicContext#drawCircle(int, int, int, org.mapsforge.core.graphics.Paint)
     */
    @Override
    public void drawCircle(final int x, final int y, final int radius, final Paint paint) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                JavaFxCanvas.this.GRAPHICS_CONTEXT2D.fillOval(x, y, radius, radius);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicContext#drawLine(int, int, int, int, org.mapsforge.core.graphics.Paint)
     */
    @Override
    public void drawLine(final int x1, final int y1, final int x2, final int y2, final Paint paint) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                JavaFxCanvas.this.GRAPHICS_CONTEXT2D.strokeLine(x1, y1, x2, y2);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicContext#drawPath(org.mapsforge.core.graphics.Path, org.mapsforge.core.graphics.Paint)
     */
    @Override
    public void drawPath(final Path path, final Paint paint) {

        final JavaFxPath fxPath = JavaFxGraphicFactory.getPath(path);
        JavaFxGraphicFactory.getPaint(paint);

        fxPath.close();

        this.GRAPHICS_CONTEXT2D.appendSVGPath(fxPath.getSvgPathString());

        this.GRAPHICS_CONTEXT2D.fill();
        this.GRAPHICS_CONTEXT2D.stroke();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicContext#drawText(java.lang.String, int, int, org.mapsforge.core.graphics.Paint)
     */
    @Override
    public void drawText(final String text, final int x, final int y, final Paint paint) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                JavaFxCanvas.this.GRAPHICS_CONTEXT2D.fillText(text, x, y);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicContext#drawTextRotated(java.lang.String, int, int, int, int, org.mapsforge.core.graphics.Paint)
     */
    @Override
    public void drawTextRotated(final String text, final int x1, final int y1, final int x2, final int y2,
            final Paint paint) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                JavaFxCanvas.this.drawText(text, x1, y1, paint);
                final double theta = Math.atan2(y2 - y1, x2 - x1);
                JavaFxCanvas.this.GRAPHICS_CONTEXT2D.rotate(theta);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicContext#fillColor(org.mapsforge.core.graphics.Color)
     */
    @Override
    public void fillColor(final Color color) {
        // TODO set color
        this.GRAPHICS_CONTEXT2D.setFill(javafx.scene.paint.Color.RED);
        this.GRAPHICS_CONTEXT2D.setStroke(javafx.scene.paint.Color.RED);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicContext#fillColor(int)
     */
    @Override
    public void fillColor(final int color) {
        // final int opacity = (color & 0xFF0000) >> 32;
        // final int r = (color & 0xFF0000) >> 16;
        // final int g = (color & 0xFF00) >> 8;
        // final int b = color & 0xFF;
        // this.fillColor(new javafx.scene.paint.Color(r, g, b, opacity));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicContext#resetClip()
     */
    @Override
    public void resetClip() {
        this.canvas.setClip(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicContext#setClip(int, int, int, int)
     */
    @Override
    public void setClip(final int left, final int top, final int width, final int height) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                JavaFxCanvas.this.canvas.setClip(new Rectangle(left, top, width, height));
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicContext#setClipDifference(int, int, int, int)
     */
    @Override
    public void setClipDifference(final int left, final int top, final int width, final int height) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Canvas#destroy()
     */
    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Canvas#getDimension()
     */
    @Override
    public Dimension getDimension() {
        return new Dimension(this.getWidth(), this.getHeight());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Canvas#getHeight()
     */
    @Override
    public int getHeight() {
        return (int) this.canvas.getHeight();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Canvas#getWidth()
     */
    @Override
    public int getWidth() {
        return (int) this.canvas.getWidth();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Canvas#setBitmap(org.mapsforge.core.graphics.Bitmap)
     */
    @Override
    public void setBitmap(final Bitmap bitmap) {

        this.canvas = JavaFxGraphicFactory.getBitmap(bitmap);
        this.GRAPHICS_CONTEXT2D = this.canvas.getGraphicsContext2D();
    }

}
