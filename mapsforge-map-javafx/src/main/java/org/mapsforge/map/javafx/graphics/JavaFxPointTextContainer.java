/**
 * Copyright (c) 2008-2016 DIEHL Metering - All Rights Reserved. NOTICE: All information contained herein is, and remains the property of DIEHL Metering
 * and its suppliers, if any. The intellectual and technical concepts contained herein are proprietary to DIEHL Metering and its suppliers and may
 * be covered by U.E. and Foreign Patents, patents in process, and are protected by trade secret or copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless prior written permission is obtained from DIEHL Metering.
 */

package org.mapsforge.map.javafx.graphics;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Display;
import org.mapsforge.core.graphics.Filter;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Position;
import org.mapsforge.core.mapelements.PointTextContainer;
import org.mapsforge.core.mapelements.SymbolContainer;
import org.mapsforge.core.model.Point;

/**
 * @author pickelm
 *
 */
public class JavaFxPointTextContainer extends PointTextContainer {

    /**
     * @param point
     * @param display
     * @param priority
     * @param text
     * @param paintFront
     * @param paintBack
     * @param symbolContainer
     * @param position
     * @param maxTextWidth
     */
    protected JavaFxPointTextContainer(final Point point, final Display display, final int priority, final String text,
            final Paint paintFront, final Paint paintBack, final SymbolContainer symbolContainer,
            final Position position, final int maxTextWidth) {
        super(point, display, priority, text, paintFront, paintBack, symbolContainer, position, maxTextWidth);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.mapelements.MapElementContainer#draw(org.mapsforge.core.graphics.Canvas, org.mapsforge.core.model.Point,
     * org.mapsforge.core.graphics.Matrix, org.mapsforge.core.graphics.Filter)
     */
    @Override
    public void draw(final Canvas canvas, final Point origin, final Matrix matrix, final Filter filter) {
        // TODO Auto-generated method stub
        canvas.drawText("test", (int) origin.x, (int) origin.y, null);
    }

}
