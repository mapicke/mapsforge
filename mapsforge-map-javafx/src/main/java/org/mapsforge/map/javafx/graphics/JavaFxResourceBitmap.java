/**
 * Copyright (c) 2008-2016 DIEHL Metering - All Rights Reserved. NOTICE: All information contained herein is, and remains the property of DIEHL Metering
 * and its suppliers, if any. The intellectual and technical concepts contained herein are proprietary to DIEHL Metering and its suppliers and may
 * be covered by U.E. and Foreign Patents, patents in process, and are protected by trade secret or copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless prior written permission is obtained from DIEHL Metering.
 */

package org.mapsforge.map.javafx.graphics;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import org.mapsforge.core.graphics.ResourceBitmap;

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.app.beans.SVGIcon;

/**
 * @author pickelm
 *
 */
public class JavaFxResourceBitmap extends JavaFxBitmap implements ResourceBitmap {

    /**
     * 
     */
    public JavaFxResourceBitmap(final InputStream is, final int width, final int height, final int hash) {
        super(JavaFxResourceBitmap.genImageForSvg(is, 5, 5, hash));
    }

    /**
     * Method to generate a Image (rastered) from an SVG.
     *
     * @param resourceTh
     *            URL of the SVG to load.
     * @param width
     *            Size of the Image to generate.
     * @param height
     *            Size of the Image to generate.
     * @return The generated Image.
     */
    public static Image genImageForSvg(final InputStream is, final int width, final int height, final int hash) {
        // load SVG

        URI uri;
        try {
            uri = SVGCache.getSVGUniverse().loadSVG(is, Integer.toString(hash));
        } catch (final IOException e) {
            return null;
        }

        final SVGIcon icon = new SVGIcon();
        icon.setSvgURI(uri);
        icon.setScaleToFit(true);
        icon.setAntiAlias(true);
        icon.setInterpolation(SVGIcon.INTERP_NEAREST_NEIGHBOR);
        final int iconWidth = icon.getIconWidth();
        final int iconHeight = icon.getIconHeight();
        final double ratioX = (double) width / iconWidth;
        final double ratioY = (double) height / iconHeight;
        final double ratio = Math.min(ratioX, ratioY);
        final int prefWidth = (int) Math.round(iconWidth * ratio);
        final int prefHeight = (int) Math.round(iconHeight * ratio);
        icon.setPreferredSize(new Dimension(prefWidth, prefHeight));

        // render the SVG
        final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        icon.paintIcon(null, img.getGraphics(), (width - prefWidth) / 2, (height - prefHeight) / 2);
        return SwingFXUtils.toFXImage(img, null);
    }

}
