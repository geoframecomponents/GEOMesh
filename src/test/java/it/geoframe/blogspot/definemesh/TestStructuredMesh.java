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

package it.geoframe.blogspot.definemesh;


import org.junit.Test;

import it.geoframe.blogspot.geomesh.definemesh.Mesh2DMain;
import it.geoframe.blogspot.geomesh.readmesh.ReadCSVStructured;


public class TestStructuredMesh {

	@Test
	public void Test() throws Exception {

		String fileName = "resources/input/csv_structured.csv";

		String splitter = ",";

		ReadCSVStructured reader = new ReadCSVStructured();
		reader.fileName = fileName;
		reader.splitter = splitter;
		reader.checkData = false;
		reader.process();
		
		Mesh2DMain mesh2DMain = new Mesh2DMain();
		mesh2DMain.verticesCoordinates = reader.verticesCoordinates;
		mesh2DMain.elementsVertices = reader.elementsVertices;
		mesh2DMain.elementsLabel = reader.elementsLabel;
		mesh2DMain.borderEdgesVertices = reader.borderEdgesVertices;
		mesh2DMain.borderEdgesLabel = reader.borderEdgesLabel;
		mesh2DMain.checkData = false;
		mesh2DMain.meshType = "cartesian";
		mesh2DMain.geometryType = "EuclideanCartesian";
		mesh2DMain.process();

		
	}
}
