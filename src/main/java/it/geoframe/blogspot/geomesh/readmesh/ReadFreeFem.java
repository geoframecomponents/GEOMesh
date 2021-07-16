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

package it.geoframe.blogspot.geomesh.readmesh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import oms3.annotations.*;

/**
 * This class read a .msh file containing the triangularization of the computational domain.
 * The file .msh is the output of FreeFem++: https://freefem.org/
 * 
 * The description of the .msh file can be found at: 
 * 	https://doc.freefem.org/documentation/MeshGeneration/#data-structures-and-readwrite-statements-for-a-mesh
 * 
 * To read a text file:
 *  https://www.journaldev.com/867/java-read-text-file
 * 
 * @author Niccolo` Tubini
 *
 */
public class ReadFreeFem {

	@In
	public String fileName = null; 

	@In
	public String splitter = " ";

	@In
	public boolean printFile = false;

	@In 
	public boolean checkData = false;


	@Out
	public List<Double[]> verticesCoordinates = new ArrayList<Double[]>();

	@Out
	public List<Integer[]> elementsVertices = new ArrayList<Integer[]>();

	@Out
	public List<Integer> elementsLabel = new ArrayList<Integer>();

	@Out
	public List<Integer[]> borderEdgesVertices = new ArrayList<Integer[]>();

	@Out
	public List<Integer> borderEdgesLabel = new ArrayList<Integer>();

	private int nVertices =-999;
	private int nElements = -999;
	private int nBorderEdges = -999;

	//	private int step = 0;


	/**
	 * @param args
	 * @throws IOException 
	 */
	@Initialize
	public void process() throws IOException {

		//		if (step == 0) {
		verticesCoordinates.add(0, new Double[] {-9999.0,-9999.0});
		elementsVertices.add(0, new Integer[] {-9999,-9999,-9999});
		elementsLabel.add(0, -9999);
		borderEdgesVertices.add(0, new Integer[] {-9999,-9999});
		borderEdgesLabel.add(0, -9999); 

		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);

		String line;
		System.out.println("Opened the file: " + fileName + "\n\n");

		if(printFile == true) {
			while((line = br.readLine()) != null){
				System.out.println(line);
			}
		}

		int iLine = 0;
		while((line = br.readLine()) != null){

			String[] lineContent = line.split(splitter);

			if(iLine==0) {

				nVertices = Integer.valueOf(lineContent[0]);
				nElements = Integer.valueOf(lineContent[1]);
				nBorderEdges = Integer.valueOf(lineContent[2]);

			} else if(iLine>0 && iLine<=nVertices) {

				verticesCoordinates.add( iLine, new Double[] { Double.valueOf(lineContent[0]),Double.valueOf(lineContent[1]) } ); 

			} else if(iLine>nVertices && iLine<=nVertices+nElements) {

				elementsVertices.add(iLine-nVertices, new Integer[] { Integer.valueOf(lineContent[0]),Integer.valueOf(lineContent[1]), 
						Integer.valueOf(lineContent[2]) } ); 
				elementsLabel.add(iLine-nVertices, Integer.valueOf(lineContent[3])); 

			} else {
				
				borderEdgesVertices.add(iLine-(nVertices+nElements), new Integer[] {-9999, -9999} );
				borderEdgesLabel.add(iLine-(nVertices+nElements), -9999);

				if (Integer.valueOf(lineContent[2]) == -1) {
					// this is an internal boundary
				} else { 

					borderEdgesVertices.set(iLine-(nVertices+nElements), new Integer[] { Integer.valueOf(lineContent[0]),Integer.valueOf(lineContent[1]) } );
					borderEdgesLabel.set(iLine-(nVertices+nElements), Integer.valueOf(lineContent[2])); 

				}
			}

			iLine ++;

		}

		br.close();
		System.out.println("Reading file completed.");


		/*
		 * Check informations are correctly stored
		 */
		if(checkData == true) {
			

			System.out.println("\n\t nVertices : " +nVertices);
			System.out.println("\t nElements : " +nElements);
			System.out.println("\t nBorderEdges : " +nBorderEdges);

			System.out.println("\n   Vertices set :");
			for(int vertex=1; vertex<verticesCoordinates.size(); vertex++) {
				System.out.println("      " + vertex + " : "+ verticesCoordinates.get(vertex)[0] + "," +verticesCoordinates.get(vertex)[1]);
			}

			System.out.println("\n   Elements' vertices :");
			for(int element=1; element<elementsVertices.size(); element++) {
				System.out.println("      " + element + " : "+ elementsVertices.get(element)[0] + "," +elementsVertices.get(element)[1]
						+ "," +elementsVertices.get(element)[2] + " ; " + elementsLabel.get(element));
			}

			System.out.println("\n   Border edges :");
			for(int edge=1; edge<borderEdgesVertices.size(); edge++) {
				System.out.println("      " + edge + " : "+ borderEdgesVertices.get(edge)[0] + "," +borderEdgesVertices.get(edge)[1]
						+ " ; " + borderEdgesLabel.get(edge));
			}

		}

		System.out.println("\nExit ReadFreeFemUnstructured\n\n\n");

		//	}

		//		step++;
	}

	public static void main(String[] args) throws IOException {

		ReadFreeFem reader = new ReadFreeFem();
		reader.fileName = "resources/input/freefem_unstructured.msh";
		reader.splitter = " ";
		reader.checkData = true;

		reader.process();

	}

}
