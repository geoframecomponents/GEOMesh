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
import java.util.Arrays;



/**
 * 
 * @author Niccolo` Tubini
 *
 */
public class Topology2DCartesianMesh extends Topology2D {


	public void defineTopology() {


		/*
		 * TOPOLOGY FOR TRIANGULAR MESH (arrays and matrices)
		 * Aim: define the edges and the left and right element of each edge
		 * 
		 * 	The grid is counterclockwise oriented
		 *  tmp_l array containing the left triangle of an edge
		 *  tmp_r array containing the right triangle of an edge
		 *  tmp_S matrix for each triangle the set of its edges
		 *  tmp_Gamma_j matrix containing for each edge its extremes. These are not oriented.
		 */
		int[] tmp_edgeLeftNeighbour = new int[4*elementsVertices.size()];
		int[] tmp_edgeRightNeighbour = new int[4*elementsVertices.size()];
		int[] loc_ind_S = new int[4*elementsVertices.size()];
		int[][] tmp_elementEdgesSet = new int[4*elementsVertices.size()][4];
		int[][] tmp_edgesSet = new int[4*elementsVertices.size()][2];

		int N_ins = 0;
		for(int element=1; element<elementsVertices.size(); element++) { 

			loc_ind_S[element] = 0;
			int j1 = elementInVector(sort2(elementsVertices.get(element)[0],elementsVertices.get(element)[1]),tmp_edgesSet,N_ins, 4*elementsVertices.size());
			int j2 = elementInVector(sort2(elementsVertices.get(element)[1],elementsVertices.get(element)[2]),tmp_edgesSet,N_ins, 4*elementsVertices.size());
			int j3 = elementInVector(sort2(elementsVertices.get(element)[2],elementsVertices.get(element)[3]),tmp_edgesSet,N_ins, 4*elementsVertices.size());
			int j4 = elementInVector(sort2(elementsVertices.get(element)[3],elementsVertices.get(element)[0]),tmp_edgesSet,N_ins, 4*elementsVertices.size()); 
			
			if(j1==0) {
				
				N_ins = N_ins+1;
				loc_ind_S[element] = loc_ind_S[element];
				tmp_edgesSet[N_ins][0] = sort2(elementsVertices.get(element)[0],elementsVertices.get(element)[1])[0];
				tmp_edgesSet[N_ins][1] = sort2(elementsVertices.get(element)[0],elementsVertices.get(element)[1])[1];
				tmp_elementEdgesSet[element][loc_ind_S[element]] = N_ins;
				tmp_edgeLeftNeighbour[N_ins] = element;		
				
			} else {
				
				loc_ind_S[element] = loc_ind_S[element];
				tmp_elementEdgesSet[element][loc_ind_S[element]] = j1;
				tmp_edgeRightNeighbour[j1] = element;

			}

			if(j2==0) {
				
				N_ins = N_ins + 1;
				loc_ind_S[element] = loc_ind_S[element] +1;
				tmp_edgesSet[N_ins][0] = sort2(elementsVertices.get(element)[1],elementsVertices.get(element)[2])[0];
				tmp_edgesSet[N_ins][1] = sort2(elementsVertices.get(element)[1],elementsVertices.get(element)[2])[1];
				tmp_elementEdgesSet[element][loc_ind_S[element]] = N_ins;
				tmp_edgeLeftNeighbour[N_ins] = element;		
				
			} else {
				
				loc_ind_S[element] = loc_ind_S[element] +1;
				tmp_elementEdgesSet[element][loc_ind_S[element]] = j2;
				tmp_edgeRightNeighbour[j2] = element;

			}

			if(j3==0) {
				
				N_ins = N_ins + 1;
				loc_ind_S[element] = loc_ind_S[element] +1;
				tmp_edgesSet[N_ins][0] = sort2(elementsVertices.get(element)[2],elementsVertices.get(element)[3])[0];
				tmp_edgesSet[N_ins][1] = sort2(elementsVertices.get(element)[2],elementsVertices.get(element)[3])[1];
				tmp_elementEdgesSet[element][loc_ind_S[element]] = N_ins;
				tmp_edgeLeftNeighbour[N_ins] = element;		
				
			} else {
				
				loc_ind_S[element] = loc_ind_S[element] +1;
				tmp_elementEdgesSet[element][loc_ind_S[element]] = j3;
				tmp_edgeRightNeighbour[j3] = element;

			}
			
			if(j4==0) {
				
				N_ins = N_ins + 1;
				loc_ind_S[element] = loc_ind_S[element] +1;
				tmp_edgesSet[N_ins][0] = sort2(elementsVertices.get(element)[3],elementsVertices.get(element)[0])[0];
				tmp_edgesSet[N_ins][1] = sort2(elementsVertices.get(element)[3],elementsVertices.get(element)[0])[1];
				tmp_elementEdgesSet[element][loc_ind_S[element]] = N_ins;
				tmp_edgeLeftNeighbour[N_ins] = element;	
				
			} else {
				
				loc_ind_S[element] = loc_ind_S[element] +1;
				tmp_elementEdgesSet[element][loc_ind_S[element]] = j4;
				tmp_edgeRightNeighbour[j4] = element;

			}

		}



		/*
		 * tmp_edgesSet has to be modified in order to define the correct orientation of each edge
		 */
		for(int element=1; element<elementsVertices.size(); element++) {

			for(int i=0; i<4; i++) {
				
				int edge = tmp_elementEdgesSet[element][i];
				
				if(tmp_edgeLeftNeighbour[edge] == element) {
					
					if(tmp_edgesSet[edge][0] == elementsVertices.get(element)[0] & tmp_edgesSet[edge][1] == elementsVertices.get(element)[1] ||
							tmp_edgesSet[edge][1] == elementsVertices.get(element)[0] & tmp_edgesSet[edge][0] == elementsVertices.get(element)[1]) {
						
						tmp_edgesSet[edge][0] = elementsVertices.get(element)[0];
						tmp_edgesSet[edge][1] = elementsVertices.get(element)[1];
						
					} else if (tmp_edgesSet[edge][0] == elementsVertices.get(element)[1] & tmp_edgesSet[edge][1] == elementsVertices.get(element)[2] ||
							tmp_edgesSet[edge][1] == elementsVertices.get(element)[1] & tmp_edgesSet[edge][0] == elementsVertices.get(element)[2]) {
						
						tmp_edgesSet[edge][0] = elementsVertices.get(element)[1];
						tmp_edgesSet[edge][1] = elementsVertices.get(element)[2];
						
					} else if(tmp_edgesSet[edge][0] == elementsVertices.get(element)[2] & tmp_edgesSet[edge][1] == elementsVertices.get(element)[3] ||
							tmp_edgesSet[edge][1] == elementsVertices.get(element)[2] & tmp_edgesSet[edge][0] == elementsVertices.get(element)[3]) {
						
						tmp_edgesSet[edge][0] = elementsVertices.get(element)[2];
						tmp_edgesSet[edge][1] = elementsVertices.get(element)[3];
						
					} else if(tmp_edgesSet[edge][0] == elementsVertices.get(element)[3] & tmp_edgesSet[edge][1] == elementsVertices.get(element)[0] ||
							tmp_edgesSet[edge][1] == elementsVertices.get(element)[3] & tmp_edgesSet[edge][0] == elementsVertices.get(element)[0]) {
						
						tmp_edgesSet[edge][0] = elementsVertices.get(element)[3];
						tmp_edgesSet[edge][1] = elementsVertices.get(element)[0];
						
					}
				}


				if(tmp_edgeRightNeighbour[edge] == 0) {
					
					if(tmp_edgesSet[edge][0] == elementsVertices.get(element)[0] & tmp_edgesSet[edge][1] == elementsVertices.get(element)[1] ||
							tmp_edgesSet[edge][1] == elementsVertices.get(element)[0] & tmp_edgesSet[edge][0] == elementsVertices.get(element)[1]) {
						
						tmp_edgesSet[edge][0] = elementsVertices.get(element)[0];
						tmp_edgesSet[edge][1] = elementsVertices.get(element)[1];
						
					} else if (tmp_edgesSet[edge][0] == elementsVertices.get(element)[1] & tmp_edgesSet[edge][1] == elementsVertices.get(element)[2] ||
							tmp_edgesSet[edge][1] == elementsVertices.get(element)[2] & tmp_edgesSet[edge][0] == elementsVertices.get(element)[2]) {
						
						tmp_edgesSet[edge][0] = elementsVertices.get(element)[1];
						tmp_edgesSet[edge][1] = elementsVertices.get(element)[2];
						
					} else if(tmp_edgesSet[edge][0] == elementsVertices.get(element)[2] & tmp_edgesSet[edge][1] == elementsVertices.get(element)[3] ||
							tmp_edgesSet[edge][1] == elementsVertices.get(element)[2] & tmp_edgesSet[edge][0] == elementsVertices.get(element)[3]) {
						
						tmp_edgesSet[edge][0] = elementsVertices.get(element)[2];
						tmp_edgesSet[edge][1] = elementsVertices.get(element)[3];
						
					} else if(tmp_edgesSet[edge][0] == elementsVertices.get(element)[3] & tmp_edgesSet[edge][1] == elementsVertices.get(element)[0] ||
							tmp_edgesSet[edge][1] == elementsVertices.get(element)[3] & tmp_edgesSet[edge][0] == elementsVertices.get(element)[0]) {
						
						tmp_edgesSet[edge][0] = elementsVertices.get(element)[3];
						tmp_edgesSet[edge][1] = elementsVertices.get(element)[0];
						
					}
				}
			}
		}





		/*
		 * Move topology in ArrayList
		 */
		edgeLeftNeighbour = new ArrayList<Integer>();
		edgeLeftNeighbour.add(0, -9999);
		edgeRightNeighbour = new ArrayList<Integer>();
		edgeRightNeighbour.add(0, -9999);
		edgesSet = new ArrayList<Integer[]>();
		edgesSet.add(0, new Integer[] {-9999,-9999});
		elementEdgesSet = new ArrayList<ArrayList<Integer>>();
		elementEdgesSet.add(0, new ArrayList<Integer>(Arrays.asList(-9999)));
		elementNeighbours = new ArrayList<ArrayList<Integer>>(); 
		elementNeighbours.add(0, new ArrayList<Integer>(Arrays.asList(-9999)));
		elementEquationStateID = new ArrayList<Integer>();
		elementEquationStateID.add(0, -9999);
		elementParameterID = new ArrayList<Integer>();
		elementParameterID.add(0, -9999);

		int[] tmp_p = new int[4];
		for(int i=1; i<=N_ins; i++) {
			edgeLeftNeighbour.add(i,  tmp_edgeLeftNeighbour[i]);
			edgeRightNeighbour.add(i, tmp_edgeRightNeighbour[i]);
			edgesSet.add(i, new Integer[] {tmp_edgesSet[i][0],tmp_edgesSet[i][1]});
		}
		for(int i=1; i<elementsVertices.size(); i++) {
			elementEquationStateID.add(i,0);
			elementParameterID.add(i,elementsLabel.get(i));
			elementEdgesSet.add( i, new ArrayList<Integer>(Arrays.asList(tmp_elementEdgesSet[i][0],tmp_elementEdgesSet[i][1],tmp_elementEdgesSet[i][2],tmp_elementEdgesSet[i][3])) );
			for(int j=0; j<tmp_elementEdgesSet[0].length; j++) {
				if(edgeRightNeighbour.get(tmp_elementEdgesSet[i][j]) != i) {
					tmp_p[j] = edgeRightNeighbour.get(tmp_elementEdgesSet[i][j]);
				} else {
					tmp_p[j] = edgeLeftNeighbour.get(tmp_elementEdgesSet[i][j]);
				}
			}
			elementNeighbours.add( i, new ArrayList<Integer>(Arrays.asList(tmp_p[0],tmp_p[1],tmp_p[2],tmp_p[3]) ) );
		}
		//System.out.println("\n\nMoved tmp in definitive maps. Elapsed time was " + (System.nanoTime()-startTime)/1000000 + " ms" );
		//System.out.println("\n\nMoved tmp in definitive maps. Elapsed time was " + (System.nanoTime()-startTime) + " ns" );
		int[] a = new int[2];
		int[] b = new int[2];
		
		edgeBoundaryBCType = new ArrayList<Integer>();
		edgeBoundaryBCType.add(0, -9999);
		edgeBoundaryBCValue = new ArrayList<Integer>();
		edgeBoundaryBCValue.add(0, -9999);

		for(int edge=1; edge<edgesSet.size(); edge++) {
			
			edgeBoundaryBCType.add(edge, -9999);
			edgeBoundaryBCValue.add(edge, -9999);
			a = sort2(edgesSet.get(edge)[0], edgesSet.get(edge)[1]).clone();

			for(int edge1=1; edge1<borderEdgesVertices.size(); edge1++) {

				b = sort2(borderEdgesVertices.get(edge1)[0],borderEdgesVertices.get(edge1)[1]).clone();

				if( isEqual( a,b  ) ) {
					/*
					 * Tens of borderEdgesLabel are used to define the type of the boundary condition (edgeBoundaryBCType),
					 * the borderEdgesLabel are used to identify the value of the boundary condition (edgeBoundaryValue).
					 */
					edgeBoundaryBCType.set(edge, borderEdgesLabel.get(edge1)/10);
					edgeBoundaryBCValue.set(edge, borderEdgesLabel.get(edge1));
					break;
					
				}
				
			}
		}	
		

		if(checkData == true) {
			
			System.out.println("\nTopology and geometry summary:");

			System.out.println("\n\tEdges extremes:");
			for(int i=1; i<edgesSet.size(); i++) {
				
				System.out.println( "\t\tedge " + i + " : " + edgesSet.get(i)[0] + "-" + edgesSet.get(i)[1] );
				
			}
			
			System.out.println("\n\tEdges of each element:");
			for(int element=1; element<elementEdgesSet.size(); element++) {
				
				System.out.println( "\t\telement " + element + " : " + elementEdgesSet.get(element).get(0) + "," + elementEdgesSet.get(element).get(1) + "," + elementEdgesSet.get(element).get(2)  + "," + elementEdgesSet.get(element).get(3) ); 
			
			}
			
			System.out.println("\n\tLeft and right element of each edge:");
			for(int i=1; i<edgesSet.size(); i++) {
				
				System.out.println( "\t\tedge " + i + " : left " + edgeLeftNeighbour.get(i) + " , right " + edgeRightNeighbour.get(i)); 
				
			}
			
			System.out.println("\n\tBoundary labels:");
			for(int i=1; i<edgeBoundaryBCType.size(); i++) {
				
				System.out.println( "\t\tedge " + i + " BC type " + edgeBoundaryBCType.get(i) + " BC value ID " + edgeBoundaryBCValue.get(i) ); 
				
			}
			
			System.out.println("\n\tElement neighbours:");
			for(int element=1; element<elementNeighbours.size(); element++) {
				System.out.print( "\t\telement " + element + " : " ); 
				for(Integer neighbour : elementNeighbours.get(element)) {
					System.out.print( neighbour + " "); 
				}
				System.out.println("\n");
			}

		}

	}//close defineTopology

}