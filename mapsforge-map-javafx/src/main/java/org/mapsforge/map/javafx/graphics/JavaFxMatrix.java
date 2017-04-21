/**
 * Copyright (c) 2008-2016 DIEHL Metering - All Rights Reserved. NOTICE: All information contained herein is, and remains the property of DIEHL Metering
 * and its suppliers, if any. The intellectual and technical concepts contained herein are proprietary to DIEHL Metering and its suppliers and may
 * be covered by U.E. and Foreign Patents, patents in process, and are protected by trade secret or copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless prior written permission is obtained from DIEHL Metering.
 */

package org.mapsforge.map.javafx.graphics;

import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

import org.mapsforge.core.graphics.Matrix;

/**
 * @author pickelm
 *
 */
public class JavaFxMatrix implements Matrix {

    Affine affine = new Affine();

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Matrix#reset()
     */
    @Override
    public void reset() {
        this.affine.setToIdentity();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Matrix#rotate(float)
     */
    @Override
    public void rotate(final float theta) {
        this.affine.appendRotation(theta);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Matrix#rotate(float, float, float)
     */
    @Override
    public void rotate(final float theta, final float pivotX, final float pivotY) {
        this.affine.appendRotation(theta, pivotX, pivotY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Matrix#scale(float, float)
     */
    @Override
    public void scale(final float scaleX, final float scaleY) {
        Transform.scale(scaleX, scaleY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Matrix#scale(float, float, float, float)
     */
    @Override
    public void scale(final float scaleX, final float scaleY, final float pivotX, final float pivotY) {
        Transform.scale(scaleX, scaleY, pivotX, pivotY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.core.graphics.Matrix#translate(float, float)
     */
    @Override
    public void translate(final float translateX, final float translateY) {
        Transform.translate(translateX, translateY);
    }

}
