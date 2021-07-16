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

package it.geoframe.blogspot.geomesh.topology;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Niccolo` Tubini
 *
 */
public abstract class Topology2D {

	protected List<Double[]> verticesCoordinates;

	protected List<Integer[]> elementsVertices;
	
	protected List<Integer> elementsLabel;

	protected List<Integer[]> borderEdgesVertices;
	
	protected List<Integer> borderEdgesLabel;

	protected boolean checkData = false;
	
	protected List<Integer> edgeLeftNeighbour;
	
	protected List<Integer> edgeRightNeighbour;
	
//	protected List<Integer> shallowWater_ll;
//	
//	protected List<Integer> shallowWater_rr;
	
	protected List<Integer> elementParameterID;
	
	protected List<Integer> elementEquationStateID;
	
	protected List<Integer> edgeBoundaryBCType;
	
	protected List<Integer> edgeBoundaryBCValue;
	
	protected List<Integer[]> edgesSet;
	
	protected List<ArrayList<Integer>> elementEdgesSet;
	
	protected List<ArrayList<Integer>> elementNeighbours;

	protected static int[] temp_edgeExtreme = new int[2];
	
	
	
	public abstract void defineTopology();
	
	
	public void set(List<Double[]> verticesCoordinates, List<Integer[]> elementsVertices,
			List<Integer[]> borderEdgesVertices, List<Integer> borderEdgesLabel, List<Integer> elementsLabel,  boolean checkData) {

		this.verticesCoordinates = verticesCoordinates;
		this.elementsLabel = elementsLabel;
		this.elementsVertices = elementsVertices;
		this.borderEdgesVertices = borderEdgesVertices;
		this.borderEdgesLabel = borderEdgesLabel;
		this.checkData = checkData;

	}
	
	
	public List<Integer> getEdgeLeftNeighbour(){
		return edgeLeftNeighbour;
	}
	
	
	
//	public List<Integer> getShallowWaterL(){
//		return shallowWater_ll;
//	}
	
	
	
	public List<Integer> getEdgeRightNeighbour(){
		return edgeRightNeighbour;
	}

	
	
//	public List<Integer> getShallowWaterR(){
//		return shallowWater_rr;
//	}
	
	
	
	public List<Integer[]> getEdgesSet(){
		return edgesSet;
	}

	
	
	public List<ArrayList<Integer>> getElementEdgesSet(){
		return elementEdgesSet;
	}
	
	
	public List<ArrayList<Integer>> getElementNeighbours(){
		return elementNeighbours;
	}
	
	
	
	public List<Integer> getElementEquationStateID(){
		return elementEquationStateID;
	}
	
	
	public List<Integer> getElementParameterID(){
		return elementParameterID;
	}
	
	
	
	public List<Integer> getEdgeBoundaryBCType(){
		return edgeBoundaryBCType;
	}
	
	
	
	
	public List<Integer> getEdgeBoundaryBCValue(){
		return edgeBoundaryBCValue;
	}
	
	
	
	protected int[] sort2( int vertex0, int vertex1 ) {
		
		if(vertex0>vertex1) {
			temp_edgeExtreme[0] = vertex1;
			temp_edgeExtreme[1] = vertex0;
		} else {
			temp_edgeExtreme[0] = vertex0;
			temp_edgeExtreme[1] = vertex1;
		}
		return temp_edgeExtreme;
		
	}



	protected boolean isEqual(int[] edgeExtreme0, int[] edgeExtreme1) {
		
		boolean isEqual = true;
		for(int i=0; i<2; i++) {
			if(edgeExtreme0[i] != edgeExtreme1[i]) {
				isEqual = false;
				return isEqual;
			}
		}
		return isEqual;
		
	}



	protected int elementInVector( int[] edgeExtreme, int[][] temp_Gamma_j, int N_ins, int N_edges) {
		
		int tmp_elementInVector;
		if(N_ins==0) {
			tmp_elementInVector = 0;
			//return 0;
		} else {
			tmp_elementInVector = 0;
			for(int i=0; i<=N_ins ; i++) {
				if(isEqual(edgeExtreme, temp_Gamma_j[i]))
					tmp_elementInVector = i;
				//return i;
			}
		} 

		return tmp_elementInVector;
		
	}
	
	
	
}
