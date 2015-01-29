package at.tuwien.ifs.somtoolbox.layers.grid;

import at.tuwien.ifs.somtoolbox.layers.LayerAccessException;
import at.tuwien.ifs.somtoolbox.layers.Unit;
import org.apache.commons.math.geometry.Vector3D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * @author Richard Plangger
 * @email e1025637@student.tuwien.ac.at
 * @date 12. Jan 15
 */
public interface GridGeometry {
    /**
     * Calculate the euclidean distance in 3D space.
     * Parameters are two units and their 3D space coordinates are taken for the calculation.
     */
    double getMapDistance(Unit a, Unit b);

    /**
     * Calculate the euclidean distance in 3D space.
     */
    double getMapDistance(int x1, int y1, int z1, int x2, int y2, int z2);

    /**
     * Get the neighbouring units for this shape.
     * @throws LayerAccessException
     */
    ArrayList<Unit> getNeighbouringUnits(int x, int y, int z, double radius) throws LayerAccessException;

    /**
     * Utility method to retreive the unit.
     * @throws LayerAccessException
     */
    Unit getUnit(int x, int y, int z) throws LayerAccessException;

    /**
     * The parameters specify the bounds the shape must lie in. If the
     * bounds do not give enough space the behaviour is undefined of the
     * resulting polygon lines.
     * @return a list of 3d vectors that specify the points to draw this shape
     */
    Vector3D[] shapeLinePoints(double x, double y, double width, double height);

    /**
     * Returns a renderable shape centering at position x/y.
     * @param ix index in the grid (x)
     * @param iy index in the grid (y)
     */
    Shape shape(int ix, int iy, double x, double y, double unitWidth, double unitHeight);

    /**
     * Get the shape using the unit dimensions.
     * @return a renderable shape
     */
    Shape shape(int indexX, int indexY, double unitWidth, double unitHeight);

    /**
     * Get the width in pixels of the whole map.
     */
    double getMapWidthInPx(int unitWidth, int xCount);

    /**
     * Get the height in pixels of the whole map.
     */
    double getMapHeightInPx(int unitHeight, int yCount);

    /**
     * Get the smallest circle that fully contains this shape.
     */
    double getRadius(double width, double height);

    /**
     * Get the rectangle that fully contains this shape. That is
     * the smallest rectangle to contian it fully.
     */
    Rectangle2D getBorder(double x, double y, double width, double height);

    /**
     * Get the position for this shape. Returns the point of
     * the top/left corner of the smalles rectangle that fits the whole shape.
     */
    Point getShapeBorderPointTopLeft(int xPos, int yPos, double unitWidth, double unitHeight);

    /**
     * Get the center of the unit in scaled coord system.
     */
    Point getShapeCenterPoint(int xPos, int yPos, double unitWidth, double unitHeight);

    /**
     * For a rectangle and hexagon returns the width.
     */
    double adjustUnitWidth(double width, double height);

    /**
     * A hexagon needs slightly more height thus here it
     * is correctly adjusted.
     */
    double adjustUnitHeight(double width, double height);

    /**
     * Returns a factor between [0,1] that should be multiplied by the
     * Y calculation before rendering.
     * In case of a hexagonal SOM every row is shifted only by 3/4.
     * Rectangular grids always return 1.
     */
    double getHeightAspect();

    /**
     * Calculate the distance but do not use Math.sqrt to get the euclidean distance.
     */
    double getMapDistanceSq(int x1, int y1, int z1, int x2, int y2, int z2);

    /**
     * Returns true for a rectangular grid.
     */
    boolean isRectangularGrid();

    /**
     * TODO DOCU or remove
     */
    int getXOffset(double unitWidth, double factorX);

    /**
     * TODO DOCU or remove
     */
    int getYOffset(double unitHeight, double factorY);

    /**
     * TODO DOCU
     */
    Point getMarkerPos(double unitWidth, double unitHeight, int markerWidth, int markerHeight, Point2D.Double loc);

    /**
     * TODO DOCU
     */
    Point getLinePos(double unitWidth, double unitHeight, Point2D.Double aDouble);

    /**
     * Draw a line from the first unit to the second in the center.
     */
    Line2D.Double centeredLine2dUnitAtoUnitB(Unit winner, Unit winner1, double unitWidth, double unitHeight);

    /**
     * Calculates the row shift (for rect this is always 0)
     * Depending on factorX. If it is greater than 1, shift is applied.
     * Note that the normal hexagonal (y % 2 == 1) shift is already applied in the shape calculation.
     * This is an 'advanced' method to fit subdivision such as it occurs in UMatrix.
     * @param y
     * @param unitWidth the width that will be used to draw
     * @param factorX the factor the map is subdivided by. 1 is no subdivision (thus no movement), 2 is a subdivition in
     *                such a way that 2 shapes fit into the parent shape.
     */
    double rowShift(int y, double unitWidth, int factorX);
}
