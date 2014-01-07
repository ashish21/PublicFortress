package com.fort.project.neighbourhoodwatch.shared;

import java.util.ArrayList;
import java.util.Date;
import com.google.gwt.maps.client.base.LatLng;

public class Constants {

	private static boolean isLoggenIn;
	public static final ArrayList<bundle> symbols = new ArrayList<bundle>();
	public static bundle dataPass;
	public static String uri, date="undisclosed", info="undisclosed";
	public static Date [] temp;
	public static String [] userInfo;
	public static boolean thisSessionReport = false;
	public static LatLng mapClick = LatLng.newInstance(0.0, 0.0);
	public static String[] legend={"Harassment", "Theft", "Gang Related Activity", "Unsafe at Night", "Physical Assault",
		"Bribe", "Arson", "Disaster", "Hazard"};
	
	static public void getSymbols(String[] Symbols) {
			
		for(int i=0; i<Symbols.length; i++) {
			
			String temp = Symbols[i];
			System.out.println("Received = " + temp);
			if(temp == null) continue;
			temp.trim();
			String arr[] = temp.split("`");
			System.out.println(arr.length);
			if(arr.length != 12) continue;
			bundle tempVar = new bundle(LatLng.newInstance(Double.parseDouble(arr[0]), 
													  Double.parseDouble(arr[1])),    //location
													  arr[2], //type
													  Double.parseDouble(arr[3]),  //strength
													  arr[4],  //date
													  arr[5],  //information
													  Integer.parseInt(arr[6]),  //thumbs ups
													  Integer.parseInt(arr[7]), //thumbs downs
													  Integer.parseInt(arr[8]), //flags
													  arr[9], //address
													  Long.parseLong(arr[10]), //id
													  arr[11]
													  );
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
		public String type, date, info, address, user;
		public double strength;
		public int tups, tdwns, flags;
		public long id;
		
		public bundle(LatLng latLng, String type, double strength, String date, String info, int tups, int tdwns, int flags, String address, long id, String user) {
	
			this.location = latLng;
			this.type = type;
			this.strength = strength;
			this.date = date;
			this.info = info;
			this.tups = tups;
			this.tdwns = tdwns;
			this.flags = flags;
			this.address = address;
			this.id = id;
			this.user = user;
		}
	}
	
	public static String retranslate(String name) {
		
		if(name.equals("harassment")) return "Harassment" ;
		if(name.equals("theft")) return "Theft" ;
		if(name.equals("gangrelatedactivity")) return "Gang Related Activity" ;
		if(name.equals("disaster")) return "Disaster" ;
		if(name.equals("hazard")) return "Hazard" ;
		if(name.equals("physicalassault")) return "Physical Assault" ;
		if(name.equals("unsafeatnight")) return "Unsafe At Night" ;
		if(name.equals("arson")) return "Arson" ;
		if(name.equals("bribe")) return "Bribe" ;
		return null;
	}
}
