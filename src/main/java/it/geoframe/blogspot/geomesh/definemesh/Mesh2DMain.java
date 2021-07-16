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

package it.geoframe.blogspot.geomesh.definemesh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.geoframe.blogspot.geomesh.geometry.Geometry;
import it.geoframe.blogspot.geomesh.geometry.GeometrySimpleFactory;
import it.geoframe.blogspot.geomesh.topology.Topology2D;
import it.geoframe.blogspot.geomesh.topology.TopologySimpleFactory;
import oms3.annotations.*;

/**
 * 
 * @author Niccolo` Tubini
 *
 */
public class Mesh2DMain {

	@In
	public List<Double[]> verticesCoordinates;

	@In
	public List<Integer[]> elementsVertices;
	
	@In
	public List<Integer> elementsLabel;

	@In
	public List<Integer[]> borderEdgesVertices;

	@In
	public List<Integer> borderEdgesLabel;

	@In
	public boolean checkData = false;

	@In 
	public String geometryType;
	
	@In 
	public String meshType;

	@Out
	public List<Integer> edgeLeftNeighbour;

	@Out
	public List<Integer> edgeRightNeighbour;
	
//	@Out
//	public List<Integer> shallowWater_ll;
//
//	@Out
//	public List<Integer> shallowWater_rr;

	@Out
	public List<Integer[]> edgesSet;

	@Out
	public List<ArrayList<Integer>> elementEdgesSet;

	@Out
	public List<Double[]> elementsCentroidsCoordinates;

	@Out
	public List<Double[]> edgesCentroidsCoordinates;

	@Out
	public List<Double> elementsArea;
		
	@Out
	public List<Integer> elementEquationStateID;
	
	@Out
	public List<Integer> elementParameterID;

	@Out
	public List<Double> edgesLength;

	@Out
	public List<Double> delta_j;

	@Out
	public List<Double[]> edgeNormalVector;

	@Out
	public List<Integer> edgeBoundaryBCType;

	@Out
	public List<Integer> edgeBoundaryBCValue;

	int step = 0;

	@Execute
	public void process() throws IOException {

		if (step == 0) {
			long startTime = System.nanoTime();

			elementsCentroidsCoordinates = new ArrayList<Double[]>();
			elementsArea = new ArrayList<Double>();
			elementEquationStateID = new ArrayList<Integer>();
			elementParameterID = new ArrayList<Integer>();
			edgesLength = new ArrayList<Double>();
			edgesCentroidsCoordinates = new ArrayList<Double[]>();
			delta_j = new ArrayList<Double>();
			edgeNormalVector = new ArrayList<Double[]>();
			edgeBoundaryBCType = new ArrayList<Integer>();
			edgeBoundaryBCValue = new ArrayList<Integer>();


			TopologySimpleFactory topologyFactory = new TopologySimpleFactory();
			Topology2D topology = topologyFactory.createTopology(meshType);
			GeometrySimpleFactory geometryFactory = new GeometrySimpleFactory();
			Geometry geometry = geometryFactory.createGeometry(geometryType);

			topology.set(verticesCoordinates, elementsVertices, borderEdgesVertices, borderEdgesLabel, elementsLabel, checkData);
			topology.defineTopology();
			edgeLeftNeighbour = topology.getEdgeLeftNeighbour();
			edgeRightNeighbour = topology.getEdgeRightNeighbour();
			edgesSet = topology.getEdgesSet();
			elementEdgesSet = new ArrayList<ArrayList<Integer>>(topology.getElementEdgesSet());
//			shallowWater_ll = topology.getShallowWaterL();
//			shallowWater_rr = topology.getShallowWaterR();
			elementEquationStateID = topology.getElementEquationStateID();
			elementParameterID = topology.getElementParameterID();

			edgeBoundaryBCType = topology.getEdgeBoundaryBCType();
			edgeBoundaryBCValue = topology.getEdgeBoundaryBCValue();
			


			/*
			 * GEOMETRY: compute the coordinates of the centroid of each element
			 */
			elementsCentroidsCoordinates = geometry.computeCentroid(elementsVertices, verticesCoordinates);

			if(checkData == true) {
				System.out.println("\n\tElements' centroid:");
				for(int element=1; element<elementsCentroidsCoordinates.size(); element++) {
					System.out.println( "\t\t" + element + " : "+ elementsCentroidsCoordinates.get(element)[0] 
							+ "," +elementsCentroidsCoordinates.get(element)[1] );
				}
			}



			/*
			 * GEOMETRY: compute the length of each element
			 *  lambda_j
			 */
			edgesLength = geometry.computeEdgeLength(edgesSet, verticesCoordinates);
			if(checkData == true) {
				System.out.println("\n\tEdges' length:");
				for(int edge=1; edge<edgesLength.size(); edge++) {
					System.out.println( "\t\t" + edge + " : "+ edgesLength.get(edge) ); 
				}
			}


			/*
			 * GEOMETRY: compute the coordinates of the centroid of each edge
			 *  lambda_j
			 *  
			 */
			edgesCentroidsCoordinates = geometry.computeEdgeCentroid(edgesSet, verticesCoordinates);
			/*
			 * When we consider the shallow water equation the controids of the elements are the centroids 
			 * of the edges at the top
			 */
//			int tmp = elementsCentroidsCoordinates.size();
//			for(int i=tmp+1; i<=s_i.size();i++) {
//				elementsCentroidsCoordinates.put(i, new Double[]{edgesCentroidsCoordinates.get(s_i.get(i).get(0))[0], edgesCentroidsCoordinates.get(s_i.get(i).get(0))[1]} );
//			}
			
			if(checkData == true) {
				System.out.println("\n\tEdges' centroid:");
				for(int edge=1; edge<edgesCentroidsCoordinates.size(); edge++) {
					System.out.println( "\t\t" + edge + " : "+ edgesCentroidsCoordinates.get(edge)[0] 
							+ "," +edgesCentroidsCoordinates.get(edge)[1] );
				}
			}



			/*
			 * GEOMETRY: compute the area of each element
			 * O'Rourke, J., Computational Geometry in C, page 26
			 */
			elementsArea = geometry.computeArea(elementsVertices, verticesCoordinates);
			/*
			 * When we consider the shallow water equation the shallow water area is the length of the edge
			 */
//			for(int i=tmp+1; i<=s_i.size();i++) {
//				elementsArea.put(i, edgesLength.get(s_i.get(i).get(0)) );
//			}
			
			if(checkData == true) {
				System.out.println("\n\tElements' area:");
				for(int element=1; element<elementsArea.size(); element++) {
					System.out.println( "\t\t" + element + " : "+ elementsArea.get(element) ); 
				}
			}


			/*
			 * GEOMETRY: Compute the normal directions for each internal edge
			 */
			edgeNormalVector = geometry.computeEdgeNormalVector(verticesCoordinates, edgesSet, edgeLeftNeighbour, edgeRightNeighbour);
			if(checkData == true) {
				System.out.println("\n\tComponents (x,y) of the normal vector of each edge:");
				for(int edge=1; edge<edgeNormalVector.size(); edge++) {
					System.out.println( "\t\t" + edge + " : " + edgeNormalVector.get(edge)[0] + "," + edgeNormalVector.get(edge)[1] ); 
				}
			}


			/*
			 * GEOMETRY: compute the distance normal to the edge between the centers of two adjacent polygons
			 * 	
			 * delta_j
			 */
			delta_j = geometry.computeCentroidsNormalDistance(elementsCentroidsCoordinates, edgeLeftNeighbour, edgeRightNeighbour, edgeNormalVector, verticesCoordinates, edgesSet, edgesLength);
			if(checkData == true) {
				System.out.println("\n\tDistance between the centroids of two adjacent polygon:");
				for(int edge=1; edge<delta_j.size(); edge++) {
					System.out.println( "\t\t" + edge + " : " + delta_j.get(edge) ); 
				}
			}


			System.out.println("Computed topology and geometry. Exit DefineMesh.\n\n");

		}

		step++;
	}//close @Execute

}