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

    /**
     * The parameters specify the bounds the shape must lie in. If the
     * bounds do not give enough space the behaviour is undefined of the
     * resulting polygon lines.
     * @return a list of 3d vectors that specify the points to draw this shape
     */
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
    Shape shape(int ix, int iy, double x, double y, double width, double height);

    double getWidthPx(int unitWidth, int xCount);

    double getHeightPx(int unitHeight, int yCount);

    /**
     * Get the smallest circle that fully contains this shape
     * @param width
     * @param height
     */
    double getRadius(double width, double height);

    /**
     * Get a rectangle that fully contains this shape
     * @param x
     * @param y
     * @param width
     * @param height
     */
    Rectangle2D getBorder(double x, double y, double width, double height);

    /**
     * Get the position for this shape. This mainly depends
     * on the implementation.
     * For a rectangle it is the top/left corner,
     * for a hexagon it is the center of the hexagon itself.
     * @param xPos
     * @param yPos
     * @param width
     * @param height
     */
    Point getPosition(int xPos, int yPos, double width, double height);

    /**
     * For a rectangle and hexagon returns the width.
     * @param width
     * @param height
     * @return
     */
    double adjustUnitWidth(double width, double height);

    /**
     * A hexagon needs sligthly more height thus here it
     * is correctly adjusted
     * @param width
     * @param height
     * @return
     */
    double adjustUnitHeight(double width, double height);
}
