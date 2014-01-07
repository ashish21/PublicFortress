package com.fort.project.neighbourhoodwatch.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;


public class CounterCorrector extends HttpServlet {
	/**
	 * 
	 */
	static String key1 = "CounterInformation";
	static Key counterInformationList = KeyFactory.createKey("Counters", key1);
	static String key2 = "MarkerLocationList";
	static Key markerLocationList = KeyFactory.createKey("Markers", key2);
	
	static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();	
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger _logger = Logger.getLogger(CounterCorrector.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		try {
			_logger.info("Cron Job has been executed");
			Query query1 = new Query("Counter", counterInformationList);
			List<Entity> marks1 = datastore.prepare(query1).asList(FetchOptions.Builder.withChunkSize(10));
			String temp;
			
			HashMap<Long, Long> thumbsUps = new HashMap<Long, Long>();
			HashMap<Long, Long> thumbsDwns = new HashMap<Long, Long>();
			HashMap<Long, Long> flags = new HashMap<Long, Long>();
			
			for(int i=0; i<marks1.size(); i++) {
			 
				temp = (String)marks1.get(i).getProperty("data");
				
				if(temp.equals(null) || temp.equals("")) continue;
				
				String [] splitter;
				splitter = temp.split(" ");
				
				for(int k=0; k<splitter.length-1; k+=2) {		
					
					if(splitter[k+1].equals("1")) {

						thumbsUps.put(Long.parseLong(splitter[k]), 1L+thumbsUps.get(Long.parseLong(splitter[k])));
					} else if(splitter[k+1].equals("2")) {

						thumbsDwns.put(Long.parseLong(splitter[k]), 1L+thumbsDwns.get(Long.parseLong(splitter[k])));
					} else if(splitter[k+1].equals("3")) {
						
						thumbsUps.put(Long.parseLong(splitter[k]), 1L+thumbsUps.get(Long.parseLong(splitter[k])));
						flags.put(Long.parseLong(splitter[k]), 1L+flags.get(Long.parseLong(splitter[k])));
					} else if(splitter[k+2].equals("4")) {
						
						thumbsDwns.put(Long.parseLong(splitter[k]), 1L+thumbsDwns.get(Long.parseLong(splitter[k])));
						flags.put(Long.parseLong(splitter[k]), 1L+flags.get(Long.parseLong(splitter[k])));
					} else if(splitter[k+2].equals("5")) {
						
						flags.put(Long.parseLong(splitter[k]), 1L+flags.get(Long.parseLong(splitter[k])));
					}
				}					
			}		
			
			Query query2 = new Query("Parent", markerLocationList);
			List<Entity> marks2 = datastore.prepare(query2).asList(FetchOptions.Builder.withChunkSize(10));
			
			for(int i=0; i<marks2.size(); i++) {
				
				temp = (String)marks2.get(i).getProperty("data");				
				if(temp.equals(null) || temp.equals("")) continue;	
				String [] splitter;
				splitter = temp.split("`");
				if(splitter.length != 10) continue;
				splitter[6] = String.valueOf(thumbsUps.get(marks2.get(i).getKey().getId()));
				splitter[7] = String.valueOf(thumbsDwns.get(marks2.get(i).getKey().getId()));
				splitter[8] = String.valueOf(flags.get(marks2.get(i).getKey().getId()));
				
				String temp2="";
				
				for(int k=0; k<splitter.length; k++) {
					
					temp2+=splitter[k];
				}
				
				marks2.get(i).setProperty("data", temp2);				
				datastore.put(marks2.get(i));
			}
			
			
		}
		catch (Exception ex) {
		//Log any exceptions in your Cron Job
		}
		}
		
		@Override
		public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		doGet(req, resp);
	}
}
