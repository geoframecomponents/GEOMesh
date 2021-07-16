/*
 * GNU GPL v3 License
 *
 * Copyright 2021 Niccolo` Tubini
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

package it.geoframe.blogspot.geomesh.printmesh;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import oms3.annotations.*;

public class PrintTopologyAndGeometryUnstructuredGrid {

	@In
	public String fileName;
	
	@In
	public List<Double[]> verticesCoordinates;
	
	@In
	public List<Integer> edgeLeftNeighbour;

	@In
	public List<Integer> edgeRightNeighbour;
	
	@In
	public List<Integer[]> edgesSet;

	@In
	public List<ArrayList<Integer>> elementEdgesSet;

	@In
	public List<Double[]> elementsCentroidsCoordinates;

	@In
	public List<Double[]> edgesCentroidsCoordinates;

	@In
	public List<Double> elementsArea;
		
	@In
	public List<Integer> elementEquationStateID;
	
	@In
	public List<Integer> elementParameterID;

	@In
	public List<Double> edgesLength;

	@In
	public List<Double> delta_j;

	@In
	public List<Double[]> edgeNormalVector;

	@In
	public List<Integer> edgeBoundaryBCType;

	@In
	public List<Integer> edgeBoundaryBCValue;
	
	@Execute
	public void process() {
		
		File csvFile = new File(fileName);
		try (PrintWriter csvWriter = new PrintWriter(new FileWriter(csvFile));){
			csvWriter.println("#Vertex,x,y");
			for(int i=1; i<verticesCoordinates.size(); i++){
				csvWriter.println(i + "," + verticesCoordinates.get(i)[0] + "," + verticesCoordinates.get(i)[1]);
			}
			csvWriter.println("#Edge,extreme1,extreme2");
			for(int i=1; i<edgesSet.size(); i++){
				csvWriter.println(i + "," + edgesSet.get(i)[0] + "," + edgesSet.get(i)[1]);
			}
			csvWriter.println("#Edge,left,right");
			for(int i=1; i<edgesSet.size(); i++){
				csvWriter.println(i + "," + edgeLeftNeighbour.get(i) + "," + edgeRightNeighbour.get(i));
			}
			csvWriter.println("#Edge,BC_type,BC_value");
			for(int i=1; i<edgesSet.size(); i++){
				csvWriter.println(i + "," + edgeBoundaryBCType.get(i) + "," + edgeBoundaryBCValue.get(i));
			}
			csvWriter.println("#Edge,x_c,y_c");
			for(int i=1; i<edgesCentroidsCoordinates.size(); i++){
				csvWriter.println(i + "," + edgesCentroidsCoordinates.get(i)[0] + "," + edgesCentroidsCoordinates.get(i)[0]);
			}
			csvWriter.println("#Edge,length");
			for(int i=1; i<edgesLength.size(); i++){
				csvWriter.println(i + "," + edgesLength.get(i));
			}
			csvWriter.println("#Edge,normal_x,normal_y");
			for(int i=1; i<edgeNormalVector.size(); i++){
				csvWriter.println(i + "," + edgeNormalVector.get(i)[0] + "," + edgeNormalVector.get(i)[1]);
			}
			csvWriter.println("#Edge,distance");
			for(int i=1; i<delta_j.size(); i++){
				csvWriter.println(i + "," + delta_j.get(i));
			}
			csvWriter.println("#Element,edge_1,edge_2,edge_3");
			for(int i=1; i<elementEdgesSet.size(); i++){
				csvWriter.println(i + "," + elementEdgesSet.get(i).get(0) + "," + elementEdgesSet.get(i).get(1) + "," + elementEdgesSet.get(i).get(2));
			}
			csvWriter.println("#Element,x_c,y_c");
			for(int i=1; i<elementsCentroidsCoordinates.size(); i++){
				csvWriter.println(i + "," + elementsCentroidsCoordinates.get(i)[0] + "," + elementsCentroidsCoordinates.get(i)[0]);
			}
			csvWriter.println("#Element,area");
			for(int i=1; i<elementsArea.size(); i++){
				csvWriter.println(i + "," + elementsArea.get(i));
			}
			
		} catch (IOException e) {
		    //Handle exception
		    e.printStackTrace();
		}
	}
	
//	public static void main(String[] args) {
//		List<Double> elementsArea = new ArrayList<Double>();
//		
//		elementsArea.add(0, -9999.0);
//		elementsArea.add(1, 1.0);
//		elementsArea.add(2, 2.0);
//		elementsArea.add(3, 3.0);
//		elementsArea.add(4, 4.0);
//		elementsArea.add(5, 5.0);
//		
//		String fileName = "resources/output/prova.csv";
//		PrintTopologyAndGeometry printer = new PrintTopologyAndGeometry();
//		printer.fileName = fileName;
//		printer.elementsArea = elementsArea;
//		printer.process();
//		
//	}

}
