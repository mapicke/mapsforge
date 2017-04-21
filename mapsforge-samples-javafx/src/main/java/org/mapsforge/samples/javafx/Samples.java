/**
 * Copyright (c) 2008-2016 DIEHL Metering - All Rights Reserved. NOTICE: All information contained herein is, and remains the property of DIEHL Metering
 * and its suppliers, if any. The intellectual and technical concepts contained herein are proprietary to DIEHL Metering and its suppliers and may
 * be covered by U.E. and Foreign Patents, patents in process, and are protected by trade secret or copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless prior written permission is obtained from DIEHL Metering.
 */

package org.mapsforge.samples.javafx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.datastore.MultiMapDataStore;
import org.mapsforge.map.javafx.graphics.JavaFxGraphicFactory;
import org.mapsforge.map.javafx.view.MapView;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.FileSystemTileCache;
import org.mapsforge.map.layer.cache.InMemoryTileCache;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.cache.TwoLevelTileCache;
import org.mapsforge.map.layer.debug.TileCoordinatesLayer;
import org.mapsforge.map.layer.debug.TileGridLayer;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

/**
 * @author pickelm
 *
 */
public class Samples extends Application {

    private static final GraphicFactory GRAPHIC_FACTORY = JavaFxGraphicFactory.INSTANCE;
    private static final boolean SHOW_DEBUG_LAYERS = true;

    final List<File> mapFiles = new ArrayList<>();

    public static void main(final String[] args) {
        Application.launch(args);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello JavaFx mapsforge!");
        final StackPane root = new StackPane();

        this.mapFiles.addAll(this.getMapFiles(this.getParameters().getUnnamed()));
        final MapView mapView = this.createMapView();
        this.addLayers(mapView, this.mapFiles);
        final Canvas canvas = mapView.getCanvas();
        root.getChildren().add(canvas);

        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    private TileCache createTileCache(final int capacity) {
        final TileCache firstLevelTileCache = new InMemoryTileCache(capacity);
        final File cacheDirectory = new File(System.getProperty("java.io.tmpdir"), "mapsforge");
        final TileCache secondLevelTileCache = new FileSystemTileCache(1024, cacheDirectory, Samples.GRAPHIC_FACTORY);
        return new TwoLevelTileCache(firstLevelTileCache, secondLevelTileCache);
    }

    private TileRendererLayer createTileRendererLayer(final TileCache tileCache, final MapDataStore mapDataStore,
            final MapViewPosition mapViewPosition) {
        final TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore, mapViewPosition,
                Samples.GRAPHIC_FACTORY) {
            @Override
            public boolean onTap(final LatLong tapLatLong, final Point layerXY, final Point tapXY) {
                System.out.println("Tap on: " + tapLatLong);
                return true;
            }
        };
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);
        return tileRendererLayer;
    }

    private BoundingBox addLayers(final MapView mapView, final List<File> mapFiles) {
        final Layers layers = mapView.getLayerManager().getLayers();

        // Raster
        /*
         * mapView.getModel().displayModel.setFixedTileSize(256); TileSource tileSource = OpenStreetMapMapnik.INSTANCE; TileDownloadLayer tileDownloadLayer =
         * createTileDownloadLayer(createTileCache(256), mapView.getModel().mapViewPosition, tileSource); layers.add(tileDownloadLayer);
         * tileDownloadLayer.start(); BoundingBox boundingBox = new BoundingBox(LatLongUtils.LATITUDE_MIN, LatLongUtils.LONGITUDE_MIN,
         * LatLongUtils.LATITUDE_MAX, LatLongUtils.LONGITUDE_MAX); mapView.setZoomLevelMin(tileSource.getZoomLevelMin());
         * mapView.setZoomLevelMax(tileSource.getZoomLevelMax());
         */

        // Vector
        mapView.getModel().displayModel.setFixedTileSize(512);
        final MultiMapDataStore mapDataStore = new MultiMapDataStore(MultiMapDataStore.DataPolicy.RETURN_ALL);
        for (final File file : mapFiles) {
            mapDataStore.addMapDataStore(new MapFile(file), false, false);
        }
        final TileRendererLayer tileRendererLayer = this.createTileRendererLayer(this.createTileCache(64), mapDataStore,
                mapView.getModel().mapViewPosition);
        layers.add(tileRendererLayer);
        final BoundingBox boundingBox = mapDataStore.boundingBox();

        // Debug
        if (Samples.SHOW_DEBUG_LAYERS) {
            layers.add(new TileGridLayer(Samples.GRAPHIC_FACTORY, mapView.getModel().displayModel));
            layers.add(new TileCoordinatesLayer(Samples.GRAPHIC_FACTORY, mapView.getModel().displayModel));
        }

        return boundingBox;
    }

    private MapView createMapView() {
        final MapView mapView = new MapView();
        mapView.getMapScaleBar().setVisible(true);
        if (Samples.SHOW_DEBUG_LAYERS) {
            mapView.getFpsCounter().setVisible(true);
        }

        return mapView;
    }

    private List<File> getMapFiles(final List<String> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("missing argument: <mapFile>");
        }

        final List<File> result = new ArrayList<>();
        for (final String arg : list) {
            final File mapFile = new File(arg);
            if (!mapFile.exists()) {
                throw new IllegalArgumentException("file does not exist: " + mapFile);
            } else if (!mapFile.isFile()) {
                throw new IllegalArgumentException("not a file: " + mapFile);
            } else if (!mapFile.canRead()) {
                throw new IllegalArgumentException("cannot read file: " + mapFile);
            }
            result.add(mapFile);
        }
        return result;
    }

}
