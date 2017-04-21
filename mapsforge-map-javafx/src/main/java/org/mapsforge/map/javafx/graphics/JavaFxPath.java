/**
 * Copyright (c) 2008-2016 DIEHL Metering - All Rights Reserved. NOTICE: All information contained herein is, and remains the property of DIEHL Metering
 * and its suppliers, if any. The intellectual and technical concepts contained herein are proprietary to DIEHL Metering and its suppliers and may
 * be covered by U.E. and Foreign Patents, patents in process, and are protected by trade secret or copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless prior written permission is obtained from DIEHL Metering.
 */

package org.mapsforge.map.javafx.graphics;

import org.mapsforge.core.graphics.FillRule;
import org.mapsforge.core.graphics.Path;

/**
 * @author pickelm
 *
 */
public class JavaFxPath implements Path {

    StringBuilder content = new StringBuilder();

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Path#clear()
     */
    @Override
    public void clear() {
        this.content = new StringBuilder();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Path#close()
     */
    @Override
    public void close() {
        this.content.append(" z");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Path#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return this.content.length() == 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Path#lineTo(float, float)
     */
    @Override
    public void lineTo(final float x, final float y) {
        this.content.append(" L ").append(x).append(' ').append(y);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Path#moveTo(float, float)
     */
    @Override
    public void moveTo(final float x, final float y) {
        this.content.append(" M ").append(x).append(' ').append(y);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Path#setFillRule(org.mapsforge.core.graphics.FillRule)
     */
    @Override
    public void setFillRule(final FillRule fillRule) {
        // this.path2D.setWindingRule(JavaFxPath.getWindingRule(fillRule));
    }

    public String getSvgPathString() {
        return this.content.toString();
    }

}
