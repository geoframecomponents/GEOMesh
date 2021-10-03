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

package it.geoframe.blogspot.geomesh.readmesh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import oms3.annotations.*;

@Description("This class read a .mesh file containing the triangularization of the computational domain.\r\n" + 
		" * The file .msh is the output of Gmsh.")
@Documentation("To read a text file:  https://www.journaldev.com/867/java-read-text-file")
@Author(name = "Niccolo' Tubini, Enrico Borinato and Riccardo Rigon", contact = "tubini.niccolo@gmail.com")
@Keywords("Hydrology, 2D, Infiltration")
//@Label()
//@Name()
//@Status()
@License("General Public License Version 3 (GPLv3)")
public class ReadGmshStructured3D {

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
	public List<Integer[]> facesVertices = new ArrayList<Integer[]>();

	@Out
	public List<Integer[]> elementsVertices = new ArrayList<Integer[]>();

	@Out
	public List<Integer> elementsLabel = new ArrayList<Integer>();

	@Out
	public List<Integer[]> borderEdgesVertices = new ArrayList<Integer[]>();

	@Out
	public List<Integer> borderEdgesLabel = new ArrayList<Integer>();

	private int nVertices =-999;
	private int nFaces = -999;
	private int nElements = -999;
	private int nBorderEdges = -999;
	private int step = 0;
	/**
	 * @param args
	 * @throws IOException 
	 */
	@Execute
	public void process() throws IOException {

		if(step==0) {
			
			verticesCoordinates.add(0, new Double[] {-9999.0,-9999.0,-9999.0});
			facesVertices.add(0, new Integer[] {-9999,-9999,-9999});
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
					//process the line
					System.out.println(line);
				}

			}

			int iLine = 0;
			while((line = br.readLine()) != null){

				String[] lineContent = line.split(splitter);

				if(iLine==4) {

					nVertices = Integer.valueOf(lineContent[1]);

				} else if(iLine>4 && iLine<=nVertices+4) {

					verticesCoordinates.add( iLine-4, new Double[] { Double.valueOf(lineContent[1]),Double.valueOf(lineContent[2]),Double.valueOf(lineContent[3]) } ); 

				} else if(iLine==4+nVertices+2) {

					nBorderEdges = Integer.valueOf(lineContent[1]);

				} else if(iLine>4+nVertices+2 && iLine<=4+nVertices+2+nBorderEdges) {

					borderEdgesVertices.add(iLine-(4+nVertices+2), new Integer[] {-9999, -9999} );
					borderEdgesLabel.add(iLine-(4+nVertices+2), -9999);

					if (Integer.valueOf(lineContent[3]) == 1) {
						// internal boundary
					} else {

						borderEdgesVertices.set(iLine-(4+nVertices+2), new Integer[] { Integer.valueOf(lineContent[1]),Integer.valueOf(lineContent[2]) } );
						borderEdgesLabel.set(iLine-(4+nVertices+2), Integer.valueOf(lineContent[3]));

					}
				} else if(iLine==4+nVertices+2+nBorderEdges+2) {

					nFaces = Integer.valueOf(lineContent[1]);

				} else if(iLine>4+nVertices+2+nBorderEdges+2 && iLine<=4+nVertices+2+nBorderEdges+2+nFaces) {

					facesVertices.add(iLine-(4+nVertices+2+nBorderEdges+2), new Integer[] { Integer.valueOf(lineContent[1]),Integer.valueOf(lineContent[2]), 
							Integer.valueOf(lineContent[3]), Integer.valueOf(lineContent[4])} ); 
					//				elementsLabel.add(iLine-(4+nVertices+2+nBorderEdges+2), Integer.valueOf(lineContent[4]));

				} else if(iLine==4+nVertices+2+nBorderEdges+2+nFaces+2) {

					nElements = Integer.valueOf(lineContent[1]);

				} else if(iLine>4+nVertices+2+nBorderEdges+2+nFaces+2 && iLine<=4+nVertices+2+nBorderEdges+2+nFaces+2+nElements) {

					elementsVertices.add(iLine-(4+nVertices+2+nBorderEdges+2+nFaces+2), new Integer[] {Integer.valueOf(lineContent[1]),Integer.valueOf(lineContent[2]), 
							Integer.valueOf(lineContent[3]), Integer.valueOf(lineContent[4]), Integer.valueOf(lineContent[5]), Integer.valueOf(lineContent[6]), 
							Integer.valueOf(lineContent[7]), Integer.valueOf(lineContent[8]) });
					elementsLabel.add(iLine-(4+nVertices+2+nBorderEdges+2+nFaces+2), Integer.valueOf(lineContent[9]));

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
				System.out.println("\t nFaces : " +nFaces);
				System.out.println("\t nElements : " +nElements);
				System.out.println("\t nBorderEdges : " +nBorderEdges);

				System.out.println("\n   Vertices set :");
				for(int vertex=1; vertex<verticesCoordinates.size(); vertex++) {
					System.out.println("      " + vertex + " : "+ verticesCoordinates.get(vertex)[0] + "," +verticesCoordinates.get(vertex)[1] + "," +verticesCoordinates.get(vertex)[2]);
				}

				System.out.println("\n   Faces' vertices :");
				for(int face=1; face<facesVertices.size(); face++) {
					System.out.println("      " + face + " : "+ facesVertices.get(face)[0] + "," +facesVertices.get(face)[1]
							+ "," +facesVertices.get(face)[2] + "," +facesVertices.get(face)[3]);
				}

				System.out.println("\n   Elements' vertices :");
				for(int element=1; element<elementsVertices.size(); element++) {
					System.out.println("      " + element + " : "+ elementsVertices.get(element)[0] + "," +elementsVertices.get(element)[1]
							+ "," +elementsVertices.get(element)[2] + "," +elementsVertices.get(element)[3] + "," +elementsVertices.get(element)[4] 
									+ "," +elementsVertices.get(element)[5] + "," +elementsVertices.get(element)[6] + "," +elementsVertices.get(element)[7]
											+ " ; " + elementsLabel.get(element));
				}

				System.out.println("\n   Border edges :");
				for(int edge=1; edge<borderEdgesVertices.size(); edge++) {
					System.out.println("      " + edge + " : "+ borderEdgesVertices.get(edge)[0] + "," +borderEdgesVertices.get(edge)[1]
							+ " ; " + borderEdgesLabel.get(edge));
				}

			}

			System.out.println("\nExit ReadGmshUnstructured\n\n\n");

		}

		step++;

	}// close @Execute


	public static void main(String[] args) throws IOException {

		ReadGmshStructured3D reader = new ReadGmshStructured3D();
		reader.fileName = "resources/input/gmsh_unstructured.mesh";
		reader.splitter = "\\s+";
		reader.checkData = true;

		reader.process();

	}

}
