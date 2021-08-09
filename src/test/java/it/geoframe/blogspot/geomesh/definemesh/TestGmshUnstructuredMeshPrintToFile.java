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

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import it.geoframe.blogspot.geomesh.definemesh.Mesh2DMain;
import it.geoframe.blogspot.geomesh.printmesh.PrintTopologyAndGeometryUnstructuredGrid;
import it.geoframe.blogspot.geomesh.readmesh.ReadFreeFem2D;


public class TestGmshUnstructuredMeshPrintToFile {

	@Test
	public void Test() throws Exception {

		String fileName = "resources/input/freefem_unstructured.msh";

		String splitter = " ";

		ReadFreeFem2D reader = new ReadFreeFem2D();
		reader.fileName = fileName;
		reader.splitter = splitter;
		reader.checkData = true;
		reader.process();
		
		Mesh2DMain mesh2DMain = new Mesh2DMain();
		mesh2DMain.verticesCoordinates = reader.verticesCoordinates;
		mesh2DMain.elementsVertices = reader.elementsVertices;
		mesh2DMain.elementsLabel = reader.elementsLabel;
		mesh2DMain.borderEdgesVertices = reader.borderEdgesVertices;
		mesh2DMain.borderEdgesLabel = reader.borderEdgesLabel;
		mesh2DMain.checkData = true;
		mesh2DMain.meshType = "triangular";
		mesh2DMain.geometryType = "EuclideanCartesian";
		mesh2DMain.process();
		
		PrintTopologyAndGeometryUnstructuredGrid print = new PrintTopologyAndGeometryUnstructuredGrid();
		print.fileName = "resources/output/freefem_unstructured_topology_geometry_reference.csv";
		print.verticesCoordinates = reader.verticesCoordinates;
		print.delta_j = mesh2DMain.delta_j;
		print.edgeBoundaryBCType = mesh2DMain.edgeBoundaryBCType;
		print.edgeBoundaryBCValue = mesh2DMain.edgeBoundaryBCValue;
		print.edgeLeftNeighbour = mesh2DMain.edgeLeftNeighbour;
		print.edgeNormalVector = mesh2DMain.edgeNormalVector;
		print.edgeRightNeighbour = mesh2DMain.edgeRightNeighbour;
		print.edgesCentroidsCoordinates = mesh2DMain.edgesCentroidsCoordinates;
		print.edgesLength = mesh2DMain.edgesLength;
		print.edgesSet = mesh2DMain.edgesSet;
		print.elementEdgesSet = mesh2DMain.elementEdgesSet;
		print.elementEquationStateID = mesh2DMain.elementEquationStateID;
		print.elementParameterID = mesh2DMain.elementParameterID;
		print.elementsArea = mesh2DMain.elementsArea;
		print.elementsCentroidsCoordinates = mesh2DMain.elementsCentroidsCoordinates;
		print.process();
		
		
		File file1 = new File("resources/output/freefem_unstructured_topology_geometry_reference.csv");
		File file2 = new File("resources/output/freefem_unstructured_topology_geometry.csv");
		assertTrue("The files differ!", FileUtils.contentEquals(file1, file2));
		
	}
}
