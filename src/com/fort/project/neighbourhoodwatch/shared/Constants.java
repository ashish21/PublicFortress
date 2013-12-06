package com.fort.project.neighbourhoodwatch.shared;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.maps.client.base.LatLng;

public class Constants {

	private static boolean isLoggenIn;
	public static ArrayList<bundle> symbols = new ArrayList<bundle>();
	public static String uri, date="undisclosed", info="undisclosed";
	public static Date [] temp;
	public static String [] userInfo;
	public static boolean thisSessionReport = false;
	public static LatLng mapClick = LatLng.newInstance(0.0, 0.0);
	
	static public void getSymbols(String[] Symbols) {
			
		for(int i=0; i<Symbols.length; i++) {
			
			String temp = Symbols[i];
			System.out.println("Received = " + temp);
			if(temp == null) continue;
			temp.trim();
			String arr[] = temp.split("`");
			System.out.println(arr.length);
			if(arr.length != 6) continue;
			bundle tempVar = new bundle(LatLng.newInstance(Double.parseDouble(arr[0]), 
													  Double.parseDouble(arr[1])), 
													  arr[2], 
													  Double.parseDouble(arr[3]),
													  arr[4],
													  arr[5]);
			symbols.add(tempVar);
			System.out.println("After Parsing = " + tempVar.location + tempVar.type);
		}
	}	

	public static boolean isLoggenIn() {
		return isLoggenIn;
	}

	public static void setLoggenIn(boolean isLoggenIn) {
		Constants.isLoggenIn = isLoggenIn;
	}
	
	public static class userDetails {
		
		public Date lastReport;
		public LatLng [] locations;
		public String [] reports;
		public Date [] dates;		
	}
	
	public static class bundle {
		
		public LatLng location;
		public String type, date, info;
		public double strength;
		public bundle(LatLng latLng, String type, double strength, String date, String info) {
	
			this.location = latLng;
			this.type = type;
			this.strength = strength;
			this.date = date;
			this.info = info;
		}
	}
	
	public static String retranslate(String name) {
		
		if(name.equals("harassment")) return "Harasment" ;
		if(name.equals("theft")) return "Theft" ;
		if(name.equals("gangrelatedactivity")) return "Gang Related Activity" ;
		if(name.equals("landside")) return "Landslide" ;
		if(name.equals("tornado")) return "Tornado" ;
		if(name.equals("biohazard")) return "Bio Hazard" ;
		if(name.equals("physicalassault")) return "Physical Assault" ;
		if(name.equals("unsafeatnight")) return "Unsafe At Night" ;
		if(name.equals("arson")) return "Arson" ;
		if(name.equals("bribe")) return "Bribe" ;
		return null;
	}
}
