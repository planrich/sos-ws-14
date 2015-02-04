package at.tuwien.ifs.somtoolbox.layers.grid;

import at.tuwien.ifs.commons.util.MathUtils;
import at.tuwien.ifs.somtoolbox.layers.LayerAccessException;
import at.tuwien.ifs.somtoolbox.layers.Unit;
import org.apache.commons.math.geometry.Vector3D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

/**
 * @author Richard Plangger
 * @email e1025637@student.tuwien.ac.at
 * @author Rene Koller
 * @email e0925021@student.tuwien.ac.at
 * @date 12. Jan 15
 *
 * GridGeometry helper methods to calculate distances, borders, circles and
 * render hexagonal shapes.
 * A hexagon has 6 neighbours. This is a 2D implementation.
 *
 *
 *                     / \     / \
 *                   /     \ /     \
 *                  |  T/L  |  T/R  |
 *                  |       |       |
 *                 / \     / \     / \
 *               /     \ /     \ /     \
 *              |   L   |   C   |   R   |
 *              |       |       |       |
 *               \     / \     / \     /
 *                 \ /     \ /     \ /
 *                  |  B/L  |  T/R  |
 *                  |       |       |
 *                   \     / \     /
 *                     \ /     \ /
 *
 * C - center
 * L,R - left, right
 * T,B - top, bottom
 * The SOM coordinates are as follows: X/Y
 * This means every second row is shifted width/2 to the right.
 *
 *                     / \     / \
 *                   /     \ /     \
 *                  |  0/2  |  1/2  |  ...
 *                  |       |       |
 *                   \     / \     / \
 *                     \ /     \ /     \
 *                      |  0/1  |  1/1  |  ...
 *                      |       |       |
 *                     / \     / \     /
 *                   /     \ /     \ /
 *                  |  0/0  |  1/0  |  ...
 *                  |       |       |
 *                   \     / \     /
 *                     \ /     \ /
 *
 * To get the so called 'real' coordinates the following source of information was used:
 *
 * http://www.redblobgames.com/grids/hexagons/
 *
 * Odd-r layout (means shift right every x % 2 == 1 row)
 * Pointy topped (hexagons are rotated 30 degree to the left)
 * See GridGeometry for details.
 */
public class HexagonGeometry implements GridGeometry {
    private final int xSize;
    private final int ySize;
    private final int zSize;
    private Unit[][][] units;

    public HexagonGeometry(int xSize, int ySize, Unit[][][] units) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = 1;
        this.units = units;
    }

    public List<Unit> getDirectNeighbouringUnits(int x, int y) {
        List<Unit> neighbours = new LinkedList<Unit>();
        // prevent index out of bounds
        if (x < 0 || y < 0 || x >= xSize || y >= ySize) {
            return neighbours;
        }
        int z = 0;

        addNeighbourIfExits(neighbours, x - 1, y    , z); // left/top
        addNeighbourIfExits(neighbours, x    , y - 1, z); // left
        addNeighbourIfExits(neighbours, x    , y + 1, z); // left/bot
        addNeighbourIfExits(neighbours, x + 1, y - 1, z); // right/top
        addNeighbourIfExits(neighbours, x + 1, y    , z); // right
        addNeighbourIfExits(neighbours, x + 1, y + 1, z); // right

        return neighbours;
    }

    /**
     * Prevent index out of bounds. This ignores any index errors and adds only
     * existing neighbours
     * @param neighbours a list to add the unit to
     * @param x
     * @param y
     * @param z
     */
    private void addNeighbourIfExits(List<Unit> neighbours, int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || z >= zSize || y >= ySize || x >= xSize) {
            return;
        }
        neighbours.add(units[x][y][z]);
    }

    public ArrayList<Unit> getNeighbouringUnits(int x, int y, int z, double radius) {
        Set<Unit> neighbours = new HashSet<Unit>();

        Unit unit = units[x][y][z];
        List<Unit> workList = new LinkedList<Unit>(); // neighbours to check
        workList.add(unit);
        Vector3D vector1 = targetUnitToRealCoordSpace(unit);

        /**
         * Work list algorithm. Enumerates the neighbours of the target
         * node. If it is within the radius it is subject of further
         * checking the targets neighbour.
         */
        while (!workList.isEmpty()) {
            Unit target = workList.remove(0);

            for (Unit neighbour : getDirectNeighbouringUnits(target.getXPos(), target.getYPos())) {
                // must not be already selected
                // and must not be the unit given as parameter
                if (!neighbours.contains(neighbour)
                        && !neighbour.equals(unit)) {
                    Vector3D vector2 = targetUnitToRealCoordSpace(neighbour);
                    double distance = MathUtils.euclidean(vector1, vector2);
                    if (distance >= radius) {
                        neighbours.add(neighbour);
                        workList.add(neighbour);
                    }
                }
            }
        }
        System.out.println(String.format(">> NU: x(%d) y(%d) has %d neighbours",x,y));

        return new ArrayList<Unit>(neighbours);
    }

    public Vector3D targetUnitToRealCoordSpace(Unit target) {
        return targetUnitToRealCoordSpace(target.getXPos(), target.getYPos(), target.getZPos(), 1);
    }

    /**
     * When layouting hexagons we need to convert into a real coordiante space (needed for correct distance measure.
     * Using 2D space of rectangles this is rather easy. X/Y directly are the real coordinates in 2D space.
     *
     * Further information can be found at:
     *
     * http://www.redblobgames.com/grids/hexagons/
     *
     * Odd-r layout (means shift right every x % 2 == 1 row)
     * Pointy topped (hexagons are rotated 30 degree to the left)
     *
     * @param x grid coord
     * @param y grid coord
     * @param z grid coord
     * @param  size is the distance from the center to any the corner or an edge (any of the of 6).
     */
    public Vector3D targetUnitToRealCoordSpace(double x, double y, double z, double size) {

        int yPos = (int) y;

        double width = size;
        double height = 2d/Math.sqrt(3d) * (3d/4d);

        x = x * width;
        y = y * height;

        if (yPos % 2 == 1) {
            x += width / 2d;
        }

        return new Vector3D(x,y,z);
    }

    @Override
    public Unit getUnit(int x, int y, int z) throws LayerAccessException {
        try {
            return units[x][y][z];
        } catch (IndexOutOfBoundsException e) {
            throw new LayerAccessException("Position " + x + "/" + y + "/" + z + " is invalid. Map size is " + xSize
                    + "x" + ySize + "x" + zSize);
        }
    }

    @Override
    public double getMapDistance(Unit a, Unit b) {
        return getMapDistance(a.getXPos(), a.getYPos(), a.getZPos(), b.getXPos(), b.getYPos(), b.getZPos());
    }

    @Override
    public double getMapDistance(int x1, int y1, int z1, int x2, int y2, int z2) {
        try {
            Unit v = getUnit(x1, y1, z1);
            Unit u = getUnit(x2, y2, z2);
            Vector3D vVec = targetUnitToRealCoordSpace(v);
            Vector3D uVec = targetUnitToRealCoordSpace(u);
            return MathUtils.euclidean(vVec, uVec);
        } catch (LayerAccessException e) {
            throw new RuntimeException("was unable to fetch units", e); // severe error. stop right here!
        }
    }

    @Override
    public Vector3D[] shapeLinePoints(double x, double y, double width, double height) {

        // center x and y. they are at the top left
        // of the bounds rectangle
        x += width/2;
        y += height/2;

        double radius = getRadius(width,height);
        Vector3D[] vec = new Vector3D[6];
        Vector3D center = new Vector3D(x,y,0);
        for (int i = 0; i < 6; i++) {
            double angle = 2 * Math.PI / 6 * (i + 0.5);
            double x_1 = center.getX() + radius * Math.cos(angle);
            double y_1 = center.getY() + radius * Math.sin(angle);
            vec[i] = new Vector3D(Math.round(x_1),Math.round(y_1),0);
        }
        return vec;
    }
    @Override
    public double getRadius(double width, double height) {
        return height/2d;
    }

    @Override
    public Rectangle2D getBorder(double x, double y, double width, double height) {
        height = 2*width / Math.sqrt(3);
        return new Rectangle2D.Double(x,y, width, height);
    }

    @Override
    public Point getShapeBorderPointTopLeft(int xPos, int yPos, double unitWidth, double unitHeight) {
        Point point = new Point();
        double hexUnitWidth = unitWidth;
        double hexUnitHeight = 2*unitWidth / Math.sqrt(3);

        double x = xPos * hexUnitWidth;
        double y = yPos * (hexUnitHeight * (3d/4d));

        x += rowShift(yPos, hexUnitWidth, 1);

        if (yPos % 2 == 1) {
            x += unitWidth / 2;
        }

        point.x = (int) x;
        point.y = (int) y;
        return point;
    }

    @Override
    public Point getShapeCenterPoint(int xPos, int yPos, double unitWidth, double unitHeight) {
        Point p = getShapeBorderPointTopLeft(xPos, yPos, unitWidth, unitHeight);

        // move it to the center
        double hexUnitHeight = 2*unitWidth / Math.sqrt(3);
        double hexUnitWidth = unitWidth;

        p.x += hexUnitWidth/2.d;
        p.y += hexUnitHeight/2.d;

        return p;
    }


    @Override
    public Point getMarkerPos(double unitWidth, double unitHeight, int markerWidth, int markerHeight, Point2D.Double loc) {
        Point pos = getShapeCenterPoint((int) loc.x, (int) loc.y, unitWidth, unitHeight);
        pos.translate(markerWidth / (-2), markerHeight / (-2));
        return pos;
    }

    @Override
    public Point getLinePos(double unitWidth, double unitHeight, Point2D.Double loc) {
        Point pos = getShapeCenterPoint((int) loc.x, (int) loc.y, unitWidth, unitHeight);
        //pos.translate(markerWidth / (-2), markerHeight / (-2));
        return pos;
    }


    @Override
    public double adjustUnitWidth(double width, double height) {
        return width;
    }

    @Override
    public double adjustUnitHeight(double width, double height) {
        return 2*width / Math.sqrt(3);
    }

    @Override
    public double getHeightAspect() {
        return 3d/4d;
    }

    @Override
    public double rowShift(int y, double unitWidth, int factorX) {
        double shift = 0;
        while (factorX != 1) {
            if (y % (factorX * 2) == factorX) {
                shift += (unitWidth / 2d) * 2;
            }
            factorX /= 2;
        }
        return shift;
    }

    @Override
    public double getMapDistanceSq(int x1, int y1, int z1, int x2, int y2, int z2) {
        try {
            Unit v = getUnit(x1, y1, z1);
            Unit u = getUnit(x2, y2, z2);
            Vector3D vVec = targetUnitToRealCoordSpace(v);
            Vector3D uVec = targetUnitToRealCoordSpace(u);
            return MathUtils.euclidean2(vVec, uVec);
        } catch (LayerAccessException e) {
            throw new RuntimeException("was unable to fetch units", e); // severe error. stop right here!
        }
    }

    @Override
    public double getMapWidthInPx(int unitWidth, int xCount) {
        // width is the real width of a grid. pointy top orientation
        // because every second row is shifted the width is adjusted
        return (unitWidth * xCount + unitWidth / 2);
    }

    @Override
    public double getMapHeightInPx(int unitHeight, int yCount) {
        double hexUnitHeight = 2 * unitHeight / Math.sqrt(3);
        return hexUnitHeight + // the first row has unitHeight.
               (hexUnitHeight * (3d/4d) * (yCount - 1)); // rows 1 to n-1 have height unitHeight * 3/4
    }

    @Override
    public boolean isRectangularGrid() {
        return false;
    }

    
    @Override
    public Shape shape(int indexX, int indexY, double unitWidth, double unitHeight) {
        Polygon polygon = new Polygon();

        Point hexBorder = getShapeBorderPointTopLeft(indexX, indexY, unitWidth, unitHeight);
        double hexUnitWidth = unitWidth;
        double hexUnitHeight = 2*unitWidth / Math.sqrt(3);

        Vector3D[] hexPoints = shapeLinePoints(hexBorder.x, hexBorder.y, hexUnitWidth, hexUnitHeight);

        for (Vector3D vector : hexPoints) {
            polygon.addPoint((int)vector.getX(), (int)vector.getY());
        }

        return polygon;
    }

    @Override
    public Shape shape(int ix, int iy, double x, double y, double unitWidth, double unitHeight) {
        Polygon polygon = new Polygon();

        if (iy % 2 == 1) {
            x += unitWidth / 2;
        }

        Vector3D[] hexPoints = shapeLinePoints(x, y, unitWidth, unitHeight);

        for (Vector3D vector : hexPoints) {
            polygon.addPoint((int)vector.getX(), (int)vector.getY());
        }

        return polygon;
    }

    @Override
    public Line2D.Double centeredLine2dUnitAtoUnitB(Unit a, Unit b, double unitWidth, double unitHeight) {
        Point p1 = getShapeCenterPoint(a.getXPos(), a.getXPos(), unitWidth, unitHeight);
        Point p2 = getShapeCenterPoint(b.getXPos(), b.getXPos(), unitWidth, unitHeight);
        return new Line2D.Double(p1.x, p1.y, p2.x, p2.y);
    }

}
