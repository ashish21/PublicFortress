package com.fort.project.neighbourhoodwatch.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fort.project.neighbourhoodwatch.client.NotLoggedInException;
import com.fort.project.neighbourhoodwatch.client.UserService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class UserServiceImpl extends RemoteServiceServlet implements UserService {
	
	private static final long serialVersionUID = 1L;
	static String key = "MarkerLocationList";
	static Key markerLocationList = KeyFactory.createKey("Markers", key);
	static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	ArrayList<Date> dates = new ArrayList<Date>();

	@Override
	public String[] getUser(String user) throws NotLoggedInException {

		if(user.equals(null) || user.equals("")) user = "AYUSH";
 
		user.trim();
		System.out.println("Trying to Load");
		Query query = new Query("Parent", markerLocationList).addSort("date", Query.SortDirection.DESCENDING);
		List<Entity> details = datastore.prepare(query).asList(FetchOptions.Builder.withChunkSize(10));
		System.out.println(details.size());
		if(details.size()==0) {
			
			String [] NULL;
			NULL = new String[1];
			NULL[0]="NULL";			
			return NULL;
		}
		ArrayList<String> symbols = new ArrayList<String>();		
		
		for(int i=0; i<details.size(); i++) {

			String userCurrent = (String)details.get(i).getProperty("user");
			if(userCurrent.equals(user)) {				
				
				Date temp = (Date)details.get(i).getProperty("date");	
				dates.add(temp);
				String toParse = (String)details.get(i).getProperty("data");
				if(toParse.equals(null) || toParse.equals("")) continue;
				toParse.trim();
				String arr[] = toParse.split("`");
				if(arr.length != 6) continue;
				symbols.add(arr[0]+" "+arr[1]+" "+arr[2]);				
			}
			
		}
		if(symbols.size()==0) {
			
			String [] NULL;
			NULL = new String[1];
			NULL[0]="NULL";			
			return NULL;
		}
		System.out.println("Loaded");
		String [] output;
		output = new String[symbols.size()];
		for(int i=0; i<symbols.size(); i++) {
			
			output[i] = symbols.get(i);
		}
		return output;
	}
	
	@Override
	public Date [] getDate(){
		
		Date [] output;
		output = new Date [dates.size()];
		for(int i=0; i<dates.size(); i++) {
			
			output[i] = dates.get(i);
		}

		return output;
	}
}
