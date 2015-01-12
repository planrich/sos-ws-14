package at.tuwien.ifs.somtoolbox.layers.hexagon;

import at.tuwien.ifs.somtoolbox.layers.LayerAccessException;
import at.tuwien.ifs.somtoolbox.layers.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rich on 1/12/15.
 */
public interface  GridHelper {
    double getMapDistance(int x1, int y1, int z1, int x2, int y2, int z2);

    ArrayList<Unit> getNeighbouringUnits(int x, int y, int z, double radius) throws LayerAccessException;

    Unit getUnit(int x, int y, int z) throws LayerAccessException;
}
