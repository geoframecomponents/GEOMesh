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
import java.util.ArrayList;

/**
 * 
 * @author Niccolo` Tubini
 *
 */
public class EuclideanCartesianGeometry extends Geometry {

	private int count;
	private double tmpArea;
	private double tmpXC;
	private double tmpYC;
	private double tmpLength;
	private Double[] tmpNormalVector = new Double[2];
	private List<Double[]> elementsCentroidsCoordinates;
	private List<Double[]> edgesCentroidsCoordinates;
	private List<Double> elementsArea;
	private List<Double> edgeLength;
	private List<Double[]> edgeNormalVector;
	private List<Double> delta_j;


	/*
	 * compute the coordinates of the centroid of each element
	 */
	@Override
	public List<Double[]> computeCentroid(List<Integer[]> elementsVertices,
			List<Double[]> verticesCoordinates) {
		
		elementsCentroidsCoordinates = new ArrayList<Double[]>();
		elementsCentroidsCoordinates.add(0, new Double[] {-9999.0,-9999.0});
		
		for(int element=1; element<elementsVertices.size(); element++) {
			
			count = 0;
			tmpXC = 0.0;
			tmpYC = 0.0;
			
			for(Integer vertex : elementsVertices.get(element)) {
				
				tmpXC += verticesCoordinates.get(vertex)[0];
				tmpYC += verticesCoordinates.get(vertex)[1];
				count ++;
						
			}		
			
			tmpXC = tmpXC/count;
			tmpYC = tmpYC/count;
			elementsCentroidsCoordinates.add( element, new Double[] {tmpXC,tmpYC} );
		}
		
		return elementsCentroidsCoordinates;

	}



	@Override
	public List<Double> computeEdgeLength(List<Integer[]> edgesVertices,
			List<Double[]> verticesCoordinates) {
		
		edgeLength = new ArrayList<Double>();
		edgeLength.add(0, -9999.0);
		
		for(int edge=1; edge<edgesVertices.size(); edge++) {
			
			tmpLength = edgeLength( verticesCoordinates.get(edgesVertices.get(edge)[0]), verticesCoordinates.get(edgesVertices.get(edge)[1]) );
			if(tmpLength<=0) {
				System.out.println("\tError edge length < 0. Edge extremes: " + edgesVertices.get(edge)[0] +","+edgesVertices.get(edge)[1]);
			}
			edgeLength.add(edge, tmpLength);
			
		}
		
		return edgeLength;

	}
	
	
	
	@Override
	public List<Double[]> computeEdgeCentroid(List<Integer[]> edgesVertices,
			List<Double[]> verticesCoordinates) {
		
		edgesCentroidsCoordinates = new ArrayList<Double[]>();
		edgesCentroidsCoordinates.add(0, new Double[] {-9999.0,-9999.0});
		
		for(int edge=1; edge<edgesVertices.size(); edge++) {
			
			tmpXC = ( verticesCoordinates.get(edgesVertices.get(edge)[0])[0] + verticesCoordinates.get(edgesVertices.get(edge)[1])[0] )/2.0;
			tmpYC = ( verticesCoordinates.get(edgesVertices.get(edge)[0])[1] + verticesCoordinates.get(edgesVertices.get(edge)[1])[1] )/2.0;

			edgesCentroidsCoordinates.add(edge, new Double[] {tmpXC,tmpYC});
			
		}
		
		return edgesCentroidsCoordinates;

	}
	
	
	/*
	 * compute the area of each element
	 * O'Rourke, J., Computational Geometry in C, page 26
	 */
	@Override
	public List<Double> computeArea(List<Integer[]> elementsVertices, List<Double[]> verticesCoordinates) {

		elementsArea = new ArrayList<Double>();
		elementsArea.add(0, -9999.0);
		
		for(int element=1; element<elementsVertices.size(); element++) {
			
			tmpArea = 0.0;
			for(int iVertex=1; iVertex<elementsVertices.get(element).length-1; iVertex++) {
				
				tmpArea += triangleArea( verticesCoordinates.get(elementsVertices.get(element)[0]), verticesCoordinates.get(elementsVertices.get(element)[iVertex]),
						verticesCoordinates.get(elementsVertices.get(element)[iVertex+1]) );
				if(tmpArea<=0) {
					System.out.println("\tError element area < 0. Element nodes: " + elementsVertices.get(element)[0] + "," + elementsVertices.get(element)[iVertex] + "," + elementsVertices.get(element)[iVertex+1]);
				}
				
			}
			
			elementsArea.add(element, tmpArea);
			
		}
		
		return elementsArea;
	}

	

	@Override
	public List<Double[]> computeEdgeNormalVector(List<Double[]> verticesCoordinates, List<Integer[]> gamma_j,
														List<Integer> l, List<Integer> r) {
		
		edgeNormalVector = new ArrayList<Double[]>();
		edgeNormalVector.add(0, new Double[] {-9999.0,-9999.0});
		
		for(int edge=1; edge<gamma_j.size(); edge++) {

			tmpLength = edgeLength( verticesCoordinates.get(gamma_j.get(edge)[0]), verticesCoordinates.get(gamma_j.get(edge)[1]) );
			tmpNormalVector[0] = - ( verticesCoordinates.get(gamma_j.get(edge)[0])[1]-verticesCoordinates.get(gamma_j.get(edge)[1])[1] )/tmpLength;
			tmpNormalVector[1] = ( verticesCoordinates.get(gamma_j.get(edge)[0])[0]-verticesCoordinates.get(gamma_j.get(edge)[1])[0] )/tmpLength;
			edgeNormalVector.add( edge, tmpNormalVector.clone() );

		}
		
		return edgeNormalVector;
	}
	
	
	
	@Override
	public List<Double> computeCentroidsNormalDistance(List<Double[]> elementsCentroidsCoordinates, List<Integer> l, List<Integer> r,
			List<Double[]> edgeNormalVector, List<Double[]> verticesCoordinates, List<Integer[]> gamma_j, List<Double> edgeLegth) {
		
		delta_j = new ArrayList<Double>();
		delta_j.add(0, -9999.0);
		
		for(int edge=1; edge<gamma_j.size(); edge++) {
			
			if(l.get(edge) != 0 & r.get(edge) != 0) {
				
				tmpLength = Math.abs( (elementsCentroidsCoordinates.get(l.get(edge))[0] - elementsCentroidsCoordinates.get(r.get(edge))[0] )*edgeNormalVector.get(edge)[0] +
						(elementsCentroidsCoordinates.get(l.get(edge))[1] - elementsCentroidsCoordinates.get(r.get(edge))[1] )*edgeNormalVector.get(edge)[1] );
				
			} else {
				
				tmpArea = triangleArea( verticesCoordinates.get(gamma_j.get(edge)[0]),  verticesCoordinates.get(gamma_j.get(edge)[1]),
										elementsCentroidsCoordinates.get(l.get(edge)) );
				tmpLength = 2*tmpArea/edgeLength.get(edge);
			}
			
			delta_j.add(edge, tmpLength );
		}
		return delta_j;
	}
	
	

	private Double edgeLength(Double[] vertex0, Double[] vertex1) {
		
		return Math.sqrt( Math.pow( vertex0[0] - vertex1[0],2 ) + Math.pow( vertex0[1] - vertex1[1],2 ) );
		
	}
	
	
	
	/*
	 * compute the area of a triangle
	 * O'Rourke, J., Computational Geometry in C, page 20
	 */
	private Double triangleArea(Double[] vertex0, Double[] vertex1, Double[] vertex2) {		
		return 0.5*(vertex0[0]* vertex1[1] - vertex0[1]*vertex1[0] +
				vertex0[1]* vertex2[0] - vertex0[0]*vertex2[1] +
				vertex1[0]* vertex2[1] - vertex1[1]*vertex2[0]);
	}



}
