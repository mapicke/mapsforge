/**
 * Copyright (c) 2008-2016 DIEHL Metering - All Rights Reserved. NOTICE: All information contained herein is, and remains the property of DIEHL Metering
 * and its suppliers, if any. The intellectual and technical concepts contained herein are proprietary to DIEHL Metering and its suppliers and may
 * be covered by U.E. and Foreign Patents, patents in process, and are protected by trade secret or copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless prior written permission is obtained from DIEHL Metering.
 */

package org.mapsforge.map.javafx.graphics;

import org.mapsforge.core.graphics.TileBitmap;

/**
 * @author pickelm
 *
 */
public class JavaFxTileBitmap extends JavaFxBitmap implements TileBitmap {

    public JavaFxTileBitmap() {
        super(0, 0);
        // TODO Auto-generated constructor stub
    }

    public JavaFxTileBitmap(final int width, final int height) {
        super(width, height);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.TileBitmap#getTimestamp()
     */
    @Override
    public long getTimestamp() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.TileBitmap#isExpired()
     */
    @Override
    public boolean isExpired() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.TileBitmap#setExpiration(long)
     */
    @Override
    public void setExpiration(final long expiration) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.TileBitmap#setTimestamp(long)
     */
    @Override
    public void setTimestamp(final long timestamp) {
        // TODO Auto-generated method stub

    }

}
