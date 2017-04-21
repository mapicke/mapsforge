/**
 * Copyright (c) 2008-2016 DIEHL Metering - All Rights Reserved. NOTICE: All information contained herein is, and remains the property of DIEHL Metering
 * and its suppliers, if any. The intellectual and technical concepts contained herein are proprietary to DIEHL Metering and its suppliers and may
 * be covered by U.E. and Foreign Patents, patents in process, and are protected by trade secret or copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless prior written permission is obtained from DIEHL Metering.
 */

package org.mapsforge.map.javafx.graphics;

import java.io.InputStream;

/**
 * @author pickelm
 *
 */
public class JavaFxSvgBitmap extends JavaFxResourceBitmap {

    /**
     * @param inputStream
     * @param scaleFactor
     * @param width
     * @param height
     * @param percent
     * @param hash
     */
    public JavaFxSvgBitmap(final InputStream inputStream, final float scaleFactor, final int width, final int height,
            final int percent, final int hash) {
        super(inputStream, width, height, hash);
    }

}
