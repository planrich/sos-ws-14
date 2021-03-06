package at.tuwien.ifs.somtoolbox.layers.grid;

import at.tuwien.ifs.commons.util.MathUtils;
import at.tuwien.ifs.somtoolbox.layers.LayerAccessException;
import at.tuwien.ifs.somtoolbox.layers.Unit;
import org.apache.commons.math.geometry.Vector3D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @author Richard Plangger
 * @email e1025637@student.tuwien.ac.at
 * @date 12. Jan 15
 * @author Rene Koller
 * @email e0925021@student.tuwien.ac.at
 * GridGeometry helper methods to calculate distances, borders, circles and
 * render rectangular shapes.
 * See the interface GridGeometry for a details description of the methods.
 */
public class RectangularGeometry implements GridGeometry {

    private final Unit[][][] units;
    private final int xSize;
    private final int ySize;
    private final int zSize;

    public RectangularGeometry(int xSize, int ySize, int zSize, Unit[][][] units) {
        this.units = units;
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;

    }

    @Override
    public double getMapDistance(int x1, int y1, int z1, int x2, int y2, int z2) {
        Vector3D v1 = new Vector3D(x1,y1,z1);
        Vector3D v2 = new Vector3D(x2,y2,z2);
        return MathUtils.euclidean(v1, v2);
    }

    @Override
    public ArrayList<Unit> getNeighbouringUnits(int x, int y, int z, double radius) throws LayerAccessException{
        ArrayList<Unit> neighbourUnits = new ArrayList<Unit>();

        int rad = (int) Math.ceil(radius);
        int upperLimitX = Math.min(x + rad, xSize - 1);
        int lowerLimitX = Math.max(x - rad, 0);
        int upperLimitY = Math.min(y + rad, ySize - 1);
        int lowerLimitY = Math.max(y - rad, 0);
        int upperLimitZ = Math.min(z + rad, zSize - 1);
        int lowerLimitZ = Math.max(z - rad, 0);

        Logger.getLogger("at.tuwien").info(String.format(">> NU: x[%d-%d] y[%d-%d]",lowerLimitX,upperLimitX,lowerLimitY,upperLimitY));

        for (int x2 = lowerLimitX; x2 <= upperLimitX; x2++) {
            for (int y2 = lowerLimitY; y2 <= upperLimitY; y2++) {
                for (int z2 = lowerLimitZ; z2 <= upperLimitZ; z2++) {
                    if (x2 != x || y2 != y || z2 != z) {
                        if (getMapDistance(x, y, z, x2, y2, z2) <= radius) {
                            neighbourUnits.add(getUnit(x2, y2, z2));
                        }
                    }
                }
            }
        }
        return neighbourUnits;
    }

    public Unit getUnit(int x, int y, int z) throws LayerAccessException{
        try {
            return units[x][y][z];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new LayerAccessException("Position " + x + "/" + y + "/" + z + " is invalid. Map size is " + xSize
                    + "x" + ySize + "x" + zSize);
        }
    }

    @Override
    public Vector3D[] shapeLinePoints(double x, double y, double width, double height) {
        Vector3D[] vectors = new Vector3D[4];
        vectors[0] = new Vector3D(x      , y       , 0);
        vectors[1] = new Vector3D(x+width, y       , 0);
        vectors[2] = new Vector3D(x+width, y+height, 0);
        vectors[3] = new Vector3D(x      , y+height, 0);

        return vectors;
    }

    @Override
    public Shape shape(int ix, int iy, double x, double y, double unitWidth, double unitHeight) {
        return new Rectangle((int)x,(int)y,(int) unitWidth,(int) unitHeight);
    }

    @Override
    public Shape shape(int indexX, int indexY, double unitWidth, double unitHeight) {
        return new Rectangle((int)(indexX * unitWidth), (int)(indexY * unitHeight), (int)unitWidth, (int)unitHeight);
    }

    @Override
    public double getMapWidthInPx(int unitWidth, int xCount) {
        return unitWidth * xCount;
    }

    @Override
    public double getMapHeightInPx(int unitHeight, int yCount) {
        return unitHeight * yCount;
    }

    @Override
    public boolean isRectangularGrid() {
        return true;
    }

    @Override
    public Point getMarkerPos(double unitWidth, double unitHeight, int markerWidth, int markerHeight, Point2D.Double loc) {
        return new Point((int) Math.round(loc.x * unitWidth + (unitWidth - markerWidth) / 2), (int) Math.round(loc.y
                * unitHeight + (unitHeight - markerHeight) / 2));
    }

    @Override
    public Point getLinePos(double unitWidth, double unitHeight, Point2D.Double loc) {
        return new Point((int) Math.round(loc.x * unitWidth + unitWidth / 2), (int) Math.round(loc.y * unitHeight
                + unitHeight / 2));
    }

    @Override
    public double getRadius(double width, double height) {
        double w = width/2;
        double h = height/2;
        return Math.sqrt(w*w + h*h);
    }

    @Override
    public Rectangle2D getBorder(double x, double y, double width, double height) {
        return new Rectangle2D.Double(x,y,width,height);
    }

    @Override
    public Point getShapeBorderPointTopLeft(int xPos, int yPos, double unitWidth, double unitHeight) {
        Point p = new Point();
        p.x = (int) (xPos * unitWidth);
        p.y = (int) (yPos * unitHeight);
        return p;
    }

    @Override
    public Point getShapeCenterPoint(int xPos, int yPos, double unitWidth, double unitHeight) {
        Point borderPos = getShapeBorderPointTopLeft(xPos, yPos, unitWidth, unitHeight);
        borderPos.x += unitHeight/2;
        borderPos.y += unitWidth/2;
        return borderPos;
    }

    @Override
    public double adjustUnitWidth(double width, double height) {
        return width;
    }

    @Override
    public double adjustUnitHeight(double width, double height) {
        return height;
    }

    @Override
    public double getMapDistanceSq(int x1, int y1, int z1, int x2, int y2, int z2) {
        Vector3D v1 = new Vector3D(x1,y1,z1);
        Vector3D v2 = new Vector3D(x2,y2,z2);
        return MathUtils.euclidean2(v1, v2);
    }

    @Override
    public double getMapDistance(Unit a, Unit b) {
        return getMapDistance(a.getXPos(), a.getYPos(), a.getZPos(), b.getXPos(), b.getYPos(), b.getZPos());
    }

    @Override
    public Line2D.Double centeredLine2dUnitAtoUnitB(Unit a, Unit b, double unitWidth, double unitHeight) {
        double halfWidth = unitWidth / 2;
        double halfHeight = unitHeight / 2;
        int x1 = a.getXPos();
        int y1 = a.getYPos();
        int x2 = b.getXPos();
        int y2 = b.getYPos();
        return new Line2D.Double(x1 * unitWidth + halfWidth, y1 * unitHeight + halfHeight,
                                 x2 * unitWidth + halfWidth, y2 * unitHeight + halfHeight);
    }

    @Override
    public double getHeightAspect() {
        return 1d;
    }

    @Override
    public double rowShift(int y, double unitWidth, int factorX) {
        return 0;
    }
}
