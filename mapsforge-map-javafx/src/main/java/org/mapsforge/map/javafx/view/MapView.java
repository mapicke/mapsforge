/**
 * Copyright (c) 2008-2016 DIEHL Metering - All Rights Reserved. NOTICE: All information contained herein is, and remains the property of DIEHL Metering
 * and its suppliers, if any. The intellectual and technical concepts contained herein are proprietary to DIEHL Metering and its suppliers and may
 * be covered by U.E. and Foreign Patents, patents in process, and are protected by trade secret or copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless prior written permission is obtained from DIEHL Metering.
 */

package org.mapsforge.map.javafx.view;

import javafx.scene.canvas.Canvas;

import org.mapsforge.core.graphics.GraphicContext;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.controller.FrameBufferController;
import org.mapsforge.map.controller.LayerManagerController;
import org.mapsforge.map.controller.MapViewController;
import org.mapsforge.map.javafx.graphics.JavaFxGraphicFactory;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.LayerManager;
import org.mapsforge.map.layer.TileLayer;
import org.mapsforge.map.layer.labels.LabelStore;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.scalebar.DefaultMapScaleBar;
import org.mapsforge.map.scalebar.MapScaleBar;
import org.mapsforge.map.util.MapPositionUtil;
import org.mapsforge.map.util.MapViewProjection;
import org.mapsforge.map.view.FpsCounter;
import org.mapsforge.map.view.FrameBuffer;

/**
 * @author pickelm
 *
 */
public class MapView implements org.mapsforge.map.view.MapView {

    private static final GraphicFactory GRAPHIC_FACTORY = JavaFxGraphicFactory.INSTANCE;
    private final FpsCounter fpsCounter;
    private final FrameBuffer frameBuffer;
    private final FrameBufferController frameBufferController;
    private LayerManager layerManager;
    private MapScaleBar mapScaleBar;
    private final MapViewProjection mapViewProjection;
    private final Model model;

    private final Canvas canvas = new Canvas();

    public MapView() {
        super();

        this.canvas.setWidth(500d);
        this.canvas.setHeight(500d);
        // super.getChildren().add(this.canvas);

        this.model = new Model();

        this.fpsCounter = new FpsCounter(MapView.GRAPHIC_FACTORY, this.model.displayModel);
        this.frameBuffer = new FrameBuffer(this.model.frameBufferModel, this.model.displayModel,
                MapView.GRAPHIC_FACTORY);
        this.frameBufferController = FrameBufferController.create(this.frameBuffer, this.model);

        this.layerManager = new LayerManager(this, this.model.mapViewPosition, MapView.GRAPHIC_FACTORY);
        this.layerManager.start();
        LayerManagerController.create(this.layerManager, this.model);

        MapViewController.create(this, this.model);

        this.model.mapViewDimension.setDimension(new Dimension(790, 200));

        this.mapScaleBar = new DefaultMapScaleBar(this.model.mapViewPosition, this.model.mapViewDimension,
                MapView.GRAPHIC_FACTORY, this.model.displayModel);

        this.mapViewProjection = new MapViewProjection(this);

        this.paint();
    }

    private void paint() {

        final GraphicContext graphicContext = JavaFxGraphicFactory.createGraphicContext(this.canvas);
        // graphicContext.fillColor(null);
        this.frameBuffer.draw(graphicContext);
        if (this.mapScaleBar != null) {
            this.mapScaleBar.draw(graphicContext);
        }
        this.fpsCounter.draw(graphicContext);

        // graphicContext.drawLine(5, 5, 25, 25, null);
        // graphicContext.drawText("balbal", 10, 10, null);
        // M 179.73811 L 271.0 175.0 L 275.0 175.0 L 275.0 179.0 L 271.0 179.0
        // final Path createPath = JavaFxGraphicFactory.INSTANCE.createPath();
        // createPath.moveTo(100, 100);
        // createPath.lineTo(300, 100);
        // createPath.lineTo(200, 300);

        // createPath.moveTo(271.9996f, 179.73811f);
        // createPath.lineTo(271.0f, 175.0f);
        // createPath.lineTo(271.0f, 175.0f);
        // createPath.moveTo(271.0f, 179.0f);
        // createPath.lineTo(271.0f, 179.0f);
        // createPath.close();
        // graphicContext.drawPath(createPath, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.map.view.MapView#addLayer(org.mapsforge.map.layer.Layer)
     */
    @Override
    public void addLayer(final Layer layer) {
        this.layerManager.getLayers().add(layer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.map.view.MapView#destroy()
     */
    @Override
    public void destroy() {
        this.layerManager.interrupt();
        this.layerManager = null;
        this.frameBufferController.destroy();
        this.frameBuffer.destroy();
        if (this.mapScaleBar != null) {
            this.mapScaleBar.destroy();
        }
        this.getModel().mapViewPosition.destroy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.map.view.MapView#destroyAll()
     */
    @Override
    public void destroyAll() {
        for (final Layer layer : this.layerManager.getLayers()) {
            this.layerManager.getLayers().remove(layer);
            layer.onDestroy();
            if (layer instanceof TileLayer) {
                ((TileLayer<?>) layer).getTileCache().destroy();
            }
            if (layer instanceof TileRendererLayer) {
                final LabelStore labelStore = ((TileRendererLayer) layer).getLabelStore();
                if (labelStore != null) {
                    labelStore.clear();
                }
            }
        }
        this.destroy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.map.view.MapView#getBoundingBox()
     */
    @Override
    public BoundingBox getBoundingBox() {
        return MapPositionUtil.getBoundingBox(this.model.mapViewPosition.getMapPosition(), this.getDimension(),
                this.model.displayModel.getTileSize());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.map.view.MapView#getDimension()
     */
    @Override
    public Dimension getDimension() {
        return new Dimension(this.getWidth(), this.getHeight());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.map.view.MapView#getFpsCounter()
     */
    @Override
    public FpsCounter getFpsCounter() {
        return this.fpsCounter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.map.view.MapView#getFrameBuffer()
     */
    @Override
    public FrameBuffer getFrameBuffer() {
        return this.frameBuffer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.map.view.MapView#getHeight()
     */
    @Override
    public int getHeight() {
        // TODO Auto-generated method stub
        return 25;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.map.view.MapView#getLayerManager()
     */
    @Override
    public LayerManager getLayerManager() {
        return this.layerManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.map.view.MapView#getMapScaleBar()
     */
    @Override
    public MapScaleBar getMapScaleBar() {
        return this.mapScaleBar;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.map.view.MapView#getMapViewProjection()
     */
    @Override
    public MapViewProjection getMapViewProjection() {
        return this.mapViewProjection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.map.view.MapView#getModel()
     */
    @Override
    public Model getModel() {
        return this.model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.map.view.MapView#getWidth()
     */
    @Override
    public int getWidth() {
        // TODO Auto-generated method stub
        return 25;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.map.view.MapView#repaint()
     */
    @Override
    public void repaint() {
        System.err.println("MapView.repaint() --> Do nothing");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.map.view.MapView#setCenter(org.mapsforge.core.model.LatLong)
     */
    @Override
    public void setCenter(final LatLong center) {
        this.model.mapViewPosition.setCenter(center);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.map.view.MapView#setMapScaleBar(org.mapsforge.map.scalebar.MapScaleBar)
     */
    @Override
    public void setMapScaleBar(final MapScaleBar mapScaleBar) {
        if (this.mapScaleBar != null) {
            this.mapScaleBar.destroy();
        }
        this.mapScaleBar = mapScaleBar;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.map.view.MapView#setZoomLevel(byte)
     */
    @Override
    public void setZoomLevel(final byte zoomLevel) {
        this.model.mapViewPosition.setZoomLevel(zoomLevel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.map.view.MapView#setZoomLevelMax(byte)
     */
    @Override
    public void setZoomLevelMax(final byte zoomLevelMax) {
        this.model.mapViewPosition.setZoomLevelMax(zoomLevelMax);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.map.view.MapView#setZoomLevelMin(byte)
     */
    @Override
    public void setZoomLevelMin(final byte zoomLevelMin) {
        this.model.mapViewPosition.setZoomLevelMin(zoomLevelMin);
    }

    /**
     * Getter.
     * 
     * @return the canvas
     */
    public Canvas getCanvas() {
        return this.canvas;
    }

}
