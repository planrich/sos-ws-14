package at.tuwien.ifs.somtoolbox.layers.hexagon;

import at.tuwien.ifs.commons.util.MathUtils;
import at.tuwien.ifs.somtoolbox.layers.LayerAccessException;
import at.tuwien.ifs.somtoolbox.layers.Unit;
import org.apache.commons.math.geometry.Vector3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rich on 1/12/15.
 */
public class RectangularHelper implements GridHelper {

    private final Unit[][][] units;
    private final int xSize;
    private final int ySize;
    private final int zSize;

    public RectangularHelper(int xSize, int ySize, int zSize, Unit[][][] units) {
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
        int lowerLimitZ = Math.max(z - rad, lowerLimitZ = 0);

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

}
