/*
 * Copyright 2010, 2011, 2012, 2013 mapsforge.org
 * Copyright 2014-2016 devemux86
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.mapsforge.map.awt.graphics;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.IndexColorModel;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Filter;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Path;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.Dimension;

class AwtCanvas implements Canvas {
    private static final String UNKNOWN_STYLE = "unknown style: ";

    private BufferedImage bufferedImage;
    private Graphics2D graphics2D;
    private BufferedImageOp grayscaleOp, invertOp, invertOp4;

    AwtCanvas() {
        this.createFilters();
    }

    AwtCanvas(final Graphics2D graphics2D) {
        this.graphics2D = graphics2D;
        this.enableAntiAliasing();

        this.createFilters();
    }

    private BufferedImage applyFilter(final BufferedImage src, final Filter filter) {
        if (filter == Filter.NONE) {
            return src;
        }
        BufferedImage dest = null;
        switch (filter) {
            case GRAYSCALE:
                dest = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
                this.grayscaleOp.filter(src, dest);
                break;
            case GRAYSCALE_INVERT:
                dest = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
                this.grayscaleOp.filter(src, dest);
                dest = this.applyInvertFilter(dest);
                break;
            case INVERT:
                dest = this.applyInvertFilter(src);
                break;
        }
        return dest;
    }

    private BufferedImage applyInvertFilter(final BufferedImage src) {
        final BufferedImage newSrc;
        if (src.getColorModel() instanceof IndexColorModel) {
            newSrc = new BufferedImage(src.getWidth(), src.getHeight(), src.getColorModel().getNumComponents() == 3
                    ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB);
            final Graphics2D g2 = newSrc.createGraphics();
            g2.drawImage(src, 0, 0, null);
            g2.dispose();
        } else {
            newSrc = src;
        }
        final BufferedImage dest = new BufferedImage(newSrc.getWidth(), newSrc.getHeight(), newSrc.getType());
        switch (newSrc.getColorModel().getNumComponents()) {
            case 3:
                this.invertOp.filter(newSrc, dest);
                break;
            case 4:
                this.invertOp4.filter(newSrc, dest);
                break;
        }
        return dest;
    }

    private void createFilters() {
        this.grayscaleOp = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

        final short[] invert = new short[256];
        final short[] straight = new short[256];
        for (int i = 0; i < 256; i++) {
            invert[i] = (short) (255 - i);
            straight[i] = (short) i;
        }
        this.invertOp = new LookupOp(new ShortLookupTable(0, invert), null);
        this.invertOp4 = new LookupOp(new ShortLookupTable(0, new short[][] { invert, invert, invert, straight }),
                null);
    }

    @Override
    public void destroy() {
        // do nothing
    }

    @Override
    public void drawBitmap(final Bitmap bitmap, final int left, final int top) {
        this.graphics2D.drawImage(AwtGraphicFactory.getBufferedImage(bitmap), left, top, null);
    }

    @Override
    public void drawBitmap(final Bitmap bitmap, final int left, final int top, final Filter filter) {
        this.graphics2D.drawImage(this.applyFilter(AwtGraphicFactory.getBufferedImage(bitmap), filter), left, top,
                null);
    }

    @Override
    public void drawBitmap(final Bitmap bitmap, final Matrix matrix) {
        this.graphics2D.drawRenderedImage(AwtGraphicFactory.getBufferedImage(bitmap),
                AwtGraphicFactory.getAffineTransform(matrix));
    }

    @Override
    public void drawBitmap(final Bitmap bitmap, final Matrix matrix, final Filter filter) {
        this.graphics2D.drawRenderedImage(this.applyFilter(AwtGraphicFactory.getBufferedImage(bitmap), filter),
                AwtGraphicFactory.getAffineTransform(matrix));
    }

    @Override
    public void drawCircle(final int x, final int y, final int radius, final Paint paint) {
        if (paint.isTransparent()) {
            return;
        }

        final AwtPaint awtPaint = AwtGraphicFactory.getPaint(paint);
        this.setColorAndStroke(awtPaint);
        final int doubleRadius = radius * 2;

        final Style style = awtPaint.style;
        switch (style) {
            case FILL:
                this.graphics2D.fillOval(x - radius, y - radius, doubleRadius, doubleRadius);
                return;

            case STROKE:
                this.graphics2D.drawOval(x - radius, y - radius, doubleRadius, doubleRadius);
                return;
        }

        throw new IllegalArgumentException(AwtCanvas.UNKNOWN_STYLE + style);
    }

    @Override
    public void drawLine(final int x1, final int y1, final int x2, final int y2, final Paint paint) {
        if (paint.isTransparent()) {
            return;
        }

        this.setColorAndStroke(AwtGraphicFactory.getPaint(paint));
        this.graphics2D.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void drawPath(final Path path, final Paint paint) {
        if (paint.isTransparent()) {
            return;
        }

        final AwtPaint awtPaint = AwtGraphicFactory.getPaint(paint);
        final AwtPath awtPath = AwtGraphicFactory.getPath(path);

        this.setColorAndStroke(awtPaint);
        this.graphics2D.setPaint(awtPaint.texturePaint);

        final Style style = awtPaint.style;
        switch (style) {
            case FILL:
                this.graphics2D.fill(awtPath.path2D);
                return;

            case STROKE:
                this.graphics2D.draw(awtPath.path2D);
                return;
        }

        throw new IllegalArgumentException(AwtCanvas.UNKNOWN_STYLE + style);
    }

    @Override
    public void drawText(final String text, final int x, final int y, final Paint paint) {
        if (text == null || text.trim().isEmpty()) {
            return;
        }
        if (paint.isTransparent()) {
            return;
        }

        final AwtPaint awtPaint = AwtGraphicFactory.getPaint(paint);

        if (awtPaint.stroke == null) {
            this.graphics2D.setColor(awtPaint.color);
            this.graphics2D.setFont(awtPaint.font);
            this.graphics2D.drawString(text, x, y);
        } else {
            this.setColorAndStroke(awtPaint);
            final TextLayout textLayout = new TextLayout(text, awtPaint.font, this.graphics2D.getFontRenderContext());
            final AffineTransform affineTransform = new AffineTransform();
            affineTransform.translate(x, y);
            this.graphics2D.draw(textLayout.getOutline(affineTransform));
        }
    }

    @Override
    public void drawTextRotated(final String text, final int x1, final int y1, final int x2, final int y2,
            final Paint paint) {
        if (text == null || text.trim().isEmpty()) {
            return;
        }
        if (paint.isTransparent()) {
            return;
        }

        final AffineTransform affineTransform = this.graphics2D.getTransform();

        final double theta = Math.atan2(y2 - y1, x2 - x1);
        this.graphics2D.rotate(theta, x1, y1);

        final double lineLength = Math.hypot(x2 - x1, y2 - y1);
        final int textWidth = paint.getTextWidth(text);
        final int dx = (int) (lineLength - textWidth) / 2;
        final int xy = paint.getTextHeight(text) / 3;
        this.drawText(text, x1 + dx, y1 + xy, paint);

        this.graphics2D.setTransform(affineTransform);
    }

    @Override
    public void fillColor(final Color color) {
        this.fillColor(AwtGraphicFactory.getColor(color));
    }

    @Override
    public void fillColor(final int color) {
        this.fillColor(new java.awt.Color(color));
    }

    @Override
    public Dimension getDimension() {
        return new Dimension(this.getWidth(), this.getHeight());
    }

    Graphics2D getGraphicObject() {
        return this.graphics2D;
    }

    @Override
    public int getHeight() {
        return this.bufferedImage != null ? this.bufferedImage.getHeight() : 0;
    }

    @Override
    public int getWidth() {
        return this.bufferedImage != null ? this.bufferedImage.getWidth() : 0;
    }

    @Override
    public void resetClip() {
        this.graphics2D.setClip(null);
    }

    @Override
    public void setBitmap(final Bitmap bitmap) {
        if (bitmap == null) {
            this.bufferedImage = null;
            this.graphics2D = null;
        } else {
            this.bufferedImage = AwtGraphicFactory.getBufferedImage(bitmap);
            this.graphics2D = this.bufferedImage.createGraphics();
            this.enableAntiAliasing();
            this.graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            this.graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        }
    }

    @Override
    public void setClip(final int left, final int top, final int width, final int height) {
        this.graphics2D.setClip(left, top, width, height);
    }

    @Override
    public void setClipDifference(final int left, final int top, final int width, final int height) {
        final Area clip = new Area(new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight()));
        clip.subtract(new Area(new Rectangle2D.Double(left, top, width, height)));
        this.graphics2D.setClip(clip);
    }

    private void enableAntiAliasing() {
        this.graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    private void fillColor(final java.awt.Color color) {
        final Composite originalComposite = this.graphics2D.getComposite();
        this.graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        this.graphics2D.setColor(color);
        this.graphics2D.fillRect(0, 0, this.getWidth(), this.getHeight());
        this.graphics2D.setComposite(originalComposite);
    }

    public void setColorAndStroke(final AwtPaint awtPaint) {
        this.graphics2D.setColor(awtPaint.color);
        if (awtPaint.stroke != null) {
            this.graphics2D.setStroke(awtPaint.stroke);
        }
    }
}
