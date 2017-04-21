/**
 * Copyright (c) 2008-2016 DIEHL Metering - All Rights Reserved. NOTICE: All information contained herein is, and remains the property of DIEHL Metering
 * and its suppliers, if any. The intellectual and technical concepts contained herein are proprietary to DIEHL Metering and its suppliers and may
 * be covered by U.E. and Foreign Patents, patents in process, and are protected by trade secret or copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless prior written permission is obtained from DIEHL Metering.
 */

package org.mapsforge.map.javafx.graphics;

import java.io.IOException;
import java.io.InputStream;

import javafx.scene.transform.Affine;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Display;
import org.mapsforge.core.graphics.GraphicContext;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Path;
import org.mapsforge.core.graphics.Position;
import org.mapsforge.core.graphics.ResourceBitmap;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.mapelements.PointTextContainer;
import org.mapsforge.core.mapelements.SymbolContainer;
import org.mapsforge.core.model.Point;

/**
 * @author pickelm
 *
 */
public class JavaFxGraphicFactory implements GraphicFactory {

    public static final GraphicFactory INSTANCE = new JavaFxGraphicFactory();

    public static javafx.scene.canvas.Canvas getBitmap(final Bitmap bitmap) {
        return ((JavaFxBitmap) bitmap).getImage();
    }

    static Affine getAffineTransform(final Matrix matrix) {
        return ((JavaFxMatrix) matrix).affine;
    }

    public static GraphicContext createGraphicContext(final javafx.scene.canvas.Canvas canvas) {
        return new JavaFxCanvas(canvas);
    }

    public static JavaFxPath getPath(final Path path) {
        return (JavaFxPath) path;
    }

    /**
     * @param paint
     */
    public static JavaFxPaint getPaint(final Paint paint) {
        return (JavaFxPaint) paint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicFactory#createBitmap(int, int)
     */
    @Override
    public Bitmap createBitmap(final int width, final int height) {
        return new JavaFxBitmap(width, height);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicFactory#createBitmap(int, int, boolean)
     */
    @Override
    public Bitmap createBitmap(final int width, final int height, final boolean isTransparent) {
        return this.createBitmap(width, height); // TODO transparent not handled
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicFactory#createCanvas()
     */
    @Override
    public Canvas createCanvas() {
        return new JavaFxCanvas();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicFactory#createColor(org.mapsforge.core.graphics.Color)
     */
    @Override
    public int createColor(final Color color) {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicFactory#createColor(int, int, int, int)
     */
    @Override
    public int createColor(final int alpha, final int red, final int green, final int blue) {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicFactory#createMatrix()
     */
    @Override
    public Matrix createMatrix() {
        return new JavaFxMatrix();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicFactory#createPaint()
     */
    @Override
    public Paint createPaint() {
        return new JavaFxPaint();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicFactory#createPaint(org.mapsforge.core.graphics.Paint)
     */
    @Override
    public Paint createPaint(final Paint paint) {
        return new JavaFxPaint();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicFactory#createPath()
     */
    @Override
    public Path createPath() {
        return new JavaFxPath();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicFactory#createPointTextContainer(org.mapsforge.core.model.Point, org.mapsforge.core.graphics.Display, int,
     * java.lang.String, org.mapsforge.core.graphics.Paint, org.mapsforge.core.graphics.Paint, org.mapsforge.core.mapelements.SymbolContainer,
     * org.mapsforge.core.graphics.Position, int)
     */
    @Override
    public PointTextContainer createPointTextContainer(final Point xy, final Display display, final int priority,
            final String text, final Paint paintFront, final Paint paintBack, final SymbolContainer symbolContainer,
            final Position position, final int maxTextWidth) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicFactory#createResourceBitmap(java.io.InputStream, int)
     */
    @Override
    public ResourceBitmap createResourceBitmap(final InputStream inputStream, final int hash) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicFactory#createTileBitmap(java.io.InputStream, int, boolean)
     */
    @Override
    public TileBitmap createTileBitmap(final InputStream inputStream, final int tileSize, final boolean isTransparent)
            throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicFactory#createTileBitmap(int, boolean)
     */
    @Override
    public TileBitmap createTileBitmap(final int tileSize, final boolean isTransparent) {
        return new JavaFxTileBitmap(tileSize, tileSize);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicFactory#platformSpecificSources(java.lang.String, java.lang.String)
     */
    @Override
    public InputStream platformSpecificSources(final String relativePathPrefix, final String src) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.GraphicFactory#renderSvg(java.io.InputStream, float, int, int, int, int)
     */
    @Override
    public ResourceBitmap renderSvg(final InputStream inputStream, final float scaleFactor, final int width,
            final int height, final int percent, final int hash) throws IOException {
        // TODO Auto-generated method stub
        return new JavaFxSvgBitmap(inputStream, scaleFactor, width, height, percent, hash);
    }

}
