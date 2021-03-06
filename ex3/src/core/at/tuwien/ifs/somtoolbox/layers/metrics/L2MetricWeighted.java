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
package at.tuwien.ifs.somtoolbox.layers.metrics;

/**
 * @author Rudolf Mayer
 * @version $Id: L2MetricWeighted.java 3587 2010-05-21 10:35:33Z mayer $
 */
public class L2MetricWeighted extends AbstractWeightedMetric {

    @Override
    public double distance(double[] vector1, double[] vector2, double[] featureWeights) throws MetricException {
        checkDimensions(vector1, vector2);
        double dist = 0;
        for (int i = 0; i < vector1.length; i++) {
            dist += featureWeights[i] * (vector1[i] - vector2[i]) * (vector1[i] - vector2[i]);
        }
        return Math.sqrt(dist);
    }

}
