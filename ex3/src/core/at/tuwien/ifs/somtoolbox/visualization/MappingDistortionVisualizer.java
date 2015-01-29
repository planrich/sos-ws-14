/*
 * Copyright 2004-2010 Information & Software Engineering Group (188/1)
 *                     Institute of Software Technology and Interactive Systems
 *                     Vienna University of Technology, Austria
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.ifs.tuwien.ac.at/dm/somtoolbox/license.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.tuwien.ifs.somtoolbox.visualization;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import at.tuwien.ifs.somtoolbox.SOMToolboxException;
import at.tuwien.ifs.somtoolbox.apps.viewer.MapPNode;
import at.tuwien.ifs.somtoolbox.data.InputData;
import at.tuwien.ifs.somtoolbox.data.SOMVisualisationData;
import at.tuwien.ifs.somtoolbox.layers.Unit;
import at.tuwien.ifs.somtoolbox.layers.grid.GridGeometry;
import at.tuwien.ifs.somtoolbox.models.GrowingSOM;
import at.tuwien.ifs.somtoolbox.util.ImageUtils;

/**
 * @author Michael Dittenbach
 * @version $Id: MappingDistortionVisualizer.java 3590 2010-05-21 10:43:45Z mayer $
 */
public class MappingDistortionVisualizer extends AbstractBackgroundImageVisualizer implements QualityMeasureVisualizer {

    public MappingDistortionVisualizer() {
        NUM_VISUALIZATIONS = 2;
        VISUALIZATION_NAMES = new String[] { "Distortion (sqrt(2))", "Distortion (2nd-best>3rd-best)" };
        VISUALIZATION_SHORT_NAMES = new String[] { "DistortionSqrt", "Distortion2nd3rd" };
        VISUALIZATION_DESCRIPTIONS = new String[] {
                "Distortion is shown by lines between winners that are farther away than sqrt(2).",
                "If third-best winner is farther away from the winner than the second-best, a line is drawn." };
        neededInputObjects = new String[] { SOMVisualisationData.INPUT_VECTOR };

    }

    @Override
    public BufferedImage createVisualization(int index, GrowingSOM gsom, int width, int height)
            throws SOMToolboxException {
        switch (index) {
            case 0: {
                return createDistortionImage(gsom, width, height);
            }
            case 1: {
                return createDistortionImage2(gsom, width, height);
            }
            default: {
                return null;
            }
        }
    }

    private BufferedImage createDistortionImage(GrowingSOM gsom, int width, int height) throws SOMToolboxException {
        InputData data = gsom.getSharedInputObjects().getInputData();
        if (data == null) {
            throw new SOMToolboxException("You need to specify the " + neededInputObjects[0]);
        }

        // InputData data = new SOMLibSparseInputData(fileNames[0], true, true,1,7); // TODO: exception handling in
        // future?
        // FIXME: sparsity!!!!!!!!!!!!!!!!!!!!!!!!!!!

        BufferedImage res = ImageUtils.createEmptyImage(width, height);
        Graphics2D g = (Graphics2D) res.getGraphics();

        GridGeometry helper = gsom.getLayer().getGridGeometry();

        double unitWidth = helper.adjustUnitWidth(MapPNode.DEFAULT_UNIT_WIDTH, MapPNode.DEFAULT_UNIT_HEIGHT) / getPreferredScaleFactor();
        double unitHeight = helper.adjustUnitHeight(MapPNode.DEFAULT_UNIT_WIDTH, MapPNode.DEFAULT_UNIT_HEIGHT) / getPreferredScaleFactor();

        g.setColor(Color.RED);
        g.setStroke(new BasicStroke((float) (unitWidth/10)));
        for (int d = 0; d < data.numVectors(); d++) {
            Unit[] winners = gsom.getLayer().getWinners(data.getInputDatum(d), 2);
            if (helper.getMapDistance(winners[0], winners[1]) > Math.sqrt(2)) {
                Line2D.Double line = gsom.getLayer().getGridGeometry().centeredLine2dUnitAtoUnitB(winners[0], winners[1], unitWidth, unitHeight);
                g.draw(line);
            }
        }

        return res;
    }

    private BufferedImage createDistortionImage2(GrowingSOM gsom, int width, int height) throws SOMToolboxException {
        InputData data = gsom.getSharedInputObjects().getInputData();
        if (data == null) {
            throw new SOMToolboxException("You need to specify the " + neededInputObjects[0]);
        }
        // FIXME: sparsity!!!!!!!!!!!!!!!!!!!!!!!!!!!

        BufferedImage res = ImageUtils.createEmptyImage(width, height);
        Graphics2D g = (Graphics2D) res.getGraphics();

        GridGeometry helper = gsom.getLayer().getGridGeometry();

        double unitWidth = helper.adjustUnitWidth(MapPNode.DEFAULT_UNIT_WIDTH, MapPNode.DEFAULT_UNIT_HEIGHT) / getPreferredScaleFactor();
        double unitHeight = helper.adjustUnitHeight(MapPNode.DEFAULT_UNIT_WIDTH, MapPNode.DEFAULT_UNIT_HEIGHT) / getPreferredScaleFactor();

        g.setColor(Color.RED);
        g.setStroke(new BasicStroke((float) (unitWidth/10)));
        for (int d = 0; d < data.numVectors(); d++) {
            Unit[] winners = gsom.getLayer().getWinners(data.getInputDatum(d), 3);
            if (helper.getMapDistance(winners[0], winners[1]) > helper.getMapDistance(winners[0], winners[2])) {
                Line2D.Double line = helper.centeredLine2dUnitAtoUnitB(winners[0], winners[1], unitWidth, unitHeight);
                g.draw(line);
            }
        }

        return res;
    }
}
