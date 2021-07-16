/*
 * GNU GPL v3 License
 *
 * Copyright 2019 Niccolo` Tubini
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.geoframe.blogspot.geomesh.geometry;

import java.util.List;

public abstract class Geometry {

	public abstract List<Double[]> computeCentroid(List<Integer[]> elementsVertices, List<Double[]> verticesCoordinates);
		
	public abstract List<Double> computeEdgeLength(List<Integer[]> elementsVertices, List<Double[]> verticesCoordinates);
	
	public abstract List<Double[]> computeEdgeCentroid(List<Integer[]> elementsVertices, List<Double[]> verticesCoordinates);

	public abstract List<Double> computeArea(List<Integer[]> elementsVertices, List<Double[]> verticesCoordinates);
	
	public abstract List<Double[]> computeEdgeNormalVector(List<Double[]> verticesCoordinates, List<Integer[]> gamma_j,
			List<Integer> l, List<Integer> r);
	
	public abstract List<Double> computeCentroidsNormalDistance(List<Double[]> elementsCentroidsCoordinates, List<Integer> l, List<Integer> r,
			List<Double[]> edgeNormalVector, List<Double[]> verticesCoordinates, List<Integer[]> gamma_j, List<Double> edgeLegth);
	

}
