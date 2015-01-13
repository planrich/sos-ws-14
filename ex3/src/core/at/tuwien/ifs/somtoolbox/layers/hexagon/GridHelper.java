package at.tuwien.ifs.somtoolbox.layers.hexagon;

import at.tuwien.ifs.somtoolbox.layers.LayerAccessException;
import at.tuwien.ifs.somtoolbox.layers.Unit;
import org.apache.commons.math.geometry.Vector3D;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rich on 1/12/15.
 */
public interface  GridHelper {
    double getMapDistance(int x1, int y1, int z1, int x2, int y2, int z2);

    ArrayList<Unit> getNeighbouringUnits(int x, int y, int z, double radius) throws LayerAccessException;

    Unit getUnit(int x, int y, int z) throws LayerAccessException;

    Vector3D[] shapeLinePoints(double x, double y, double width, double height);

    /**
     * @param ix index in the grid (x)
     * @param iy index in the grid (y)
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    Shape shape(int ix, int iy, int x, int y, int width, int height);

    double getWidthPx(int unit_width, int xCount);

    double getHeightPx(int unit_width, int yCount);

    double getRadius(double width, double height);

    Rectangle2D getBorder(double x, double y, double width, double height);
}
