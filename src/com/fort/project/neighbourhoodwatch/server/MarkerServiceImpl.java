package com.fort.project.neighbourhoodwatch.server;

import java.util.Date;
import java.util.List;

import com.fort.project.neighbourhoodwatch.client.MarkerService;
import com.fort.project.neighbourhoodwatch.client.NotLoggedInException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MarkerServiceImpl extends RemoteServiceServlet implements MarkerService {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static String key = "MarkerLocationList";
	static Key markerLocationList = KeyFactory.createKey("Markers", key);
	static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();	
	
	public void addMark(final String symbol, final String user) {
		  
		System.out.println("Trying to add");
		if(user == null) {
		
			System.out.println("User = NULL");
			return;
		}
		Entity entity = new Entity("Parent", markerLocationList);
		entity.setProperty("user", user);
		entity.setProperty("date", new Date());
		entity.setProperty("data", symbol);
		datastore.put(entity);
		System.out.println("Added");
	}  
	
	public String [] getMarks() throws NotLoggedInException {
		  
		System.out.println("Trying to Load");
		Query query = new Query("Parent", markerLocationList).addSort("date", Query.SortDirection.DESCENDING);
		List<Entity> marks = datastore.prepare(query).asList(FetchOptions.Builder.withChunkSize(10));
		System.out.println("Size of received stuff = "+marks.size());
		if(marks.size()==0) {
			
			String [] NULL;
			NULL = new String[1];
			NULL[0]="NULL";
			return NULL;
		}
		String [] symbols;
		symbols = new String[marks.size()];
		for(int i=0; i<marks.size(); i++) {
		 
			symbols[i] = ((String)marks.get(i).getProperty("data"))+"`"+
						  String.valueOf(marks.get(i).getKey().getId())+"`"+
					      (String)marks.get(i).getProperty("user");
			System.out.println("TESting " + symbols[i]);
		}
		System.out.println("Loaded");
		return symbols;
	}	
}
