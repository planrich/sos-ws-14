package at.tuwien.ifs.somtoolbox.layers.hexagon;

import at.tuwien.ifs.commons.util.MathUtils;
import at.tuwien.ifs.somtoolbox.layers.LayerAccessException;
import at.tuwien.ifs.somtoolbox.layers.Unit;
import org.apache.commons.math.geometry.Vector3D;

import java.util.*;

/**
 * @author Richard Plangger
 * @email e1025637@student.tuwien.ac.at
 * @date 12. Jan 15
 *
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
 *                  |       |   c   |
 *                   \     / \     / \
 *                     \ /     \ /     \
 *                      |  0/1  |  1/1  |  ...
 *                      |   b   |       |
 *                     / \     / \     /
 *                   /     \ /     \ /
 *                  |  0/0  |  1/0  |  ...
 *                  |   a   |       |
 *                   \     / \     /
 *                     \ /     \ /
 *
 * To get the so called 'real' coordinates I used the following source of information:
 *
 * http://www.redblobgames.com/grids/hexagons/
 *
 * Odd-r layout (means shift right every x % 2 == 1 row)
 * Pointy topped (hexagons are rotated 30 degree to the left)
 */
public class HexagonHelper implements GridHelper {
    private final int xSize;
    private final int ySize;
    private final int zSize;
    private Unit[][][] units;

    public HexagonHelper(int xSize, int ySize, Unit[][][] units) {
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
         * Work list algorithm. Enumerates the neighbours of the
         */
        while (!workList.isEmpty()) {
            Unit target = workList.remove(0);

            for (Unit neighbour : getDirectNeighbouringUnits(target.getXPos(), target.getYPos())) {
                // must not be already selected
                // and must not be the unit given as parameter
                if (!neighbours.contains(neighbour)
                        && neighbour.equals(unit)) {
                    Vector3D vector2 = targetUnitToRealCoordSpace(neighbour);
                    double distance = MathUtils.euclidean(vector1, vector2);
                    if (distance >= radius) {
                        neighbours.add(neighbour);
                        workList.add(neighbour);
                    }
                }
            }
        }

        return new ArrayList<Unit>(neighbours);
    }

    /**
     * When layouting hexagons we need to convert into a real coordiante space (needed for correct distance measure.
     * Using 2D space of rectangles this is rather easy. X/Y directly are the real coordinates in 2D space.
     *
     * To get the so called 'real' coordinates I used the following source of information:
     *
     * http://www.redblobgames.com/grids/hexagons/
     *
     * Odd-r layout (means shift right every x % 2 == 1 row)
     * Pointy topped (hexagons are rotated 30 degree to the left)
     *
     * size is the distance from the center to any the corner or an edge (any of the of 6).
     *
     * @param target
     * @return
     */
    private Vector3D targetUnitToRealCoordSpace(Unit target) {

        double x = target.getXPos();
        double y = target.getYPos();
        double z = target.getZPos();

        double size = 1.0d;
        double height = size * 2d;
        double width = (Math.sqrt(3d)/2d) * height;
        double widthShift = width;
        double heightShift = height * (3d/4d);

        x = x * widthShift;
        y = y * heightShift;

        if (target.getYPos() % 2 == 1) {
            y += width / 2d;
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

}
