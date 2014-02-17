package com.bardo.proyectjane.framework.IA;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.bardo.proyectjane.framework.IA.Interfaces.Place;

public class Room implements Place {
	private int nPeople = 0;

	private ArrayList<String> peopleRecon = null;

	private double temp = 25;

	private double coordinates[] = new double[2];

	private Date lastDate = new Date();

	@Override
	public HashMap<String, String> getVariables() {
		HashMap<String, String> mapV = new HashMap<String, String>();

		mapV.put("Temperature", String.valueOf(temp));
		mapV.put(
				"Coordinates",
				String.valueOf(coordinates[0]) + ":"
						+ String.valueOf(coordinates[1]));
		mapV.put("nPeople", String.valueOf(nPeople));
		mapV.put("LastDate", lastDate.toString());
		return null;
	}

	@Override
	public void updateVariables(HashMap<String, String> mapV) {
		for (int i = 0; i < mapV.size(); i++) {
			if (mapV.containsKey("Temperature")) {
				temp = Double.valueOf(mapV.get("Temperature"));
			}
			if (mapV.containsKey("nPeople")) {

			}
			if (mapV.containsKey("LastDate")) {

			}
			if (mapV.containsKey("Coordinates")) {

			}
		}

	}

}
