package com.fort.project.neighbourhoodwatch.server;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;


public class GAEJCronServlet extends HttpServlet {
	/**
	 * 
	 */
	static String key = "MarkerLocationList";
	static Key markerLocationList = KeyFactory.createKey("Markers", key);
	static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();	
	private static final long serialVersionUID = 1L;
	int per=100;
	private static final Logger _logger = Logger.getLogger(GAEJCronServlet.class.getName());
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		try {
			_logger.info("Cron Job has been executed");
			Query query = new Query("Parent", markerLocationList).addSort("date", Query.SortDirection.DESCENDING);
			List<Entity> marks = datastore.prepare(query).asList(FetchOptions.Builder.withChunkSize(10));
			System.out.println("Size of received stuff = "+marks.size());
			String temp;
			for(int i=0; i<marks.size(); i++) {
			 
				temp = (String)marks.get(i).getProperty("data");				
				if(temp.equals(null) || temp.equals("")) continue;
				
				temp.trim();
				
				String arr[] = temp.split("`");
				if(arr.length != 6) continue;
				
				double strength = Double.parseDouble(arr[3]);
				if(strength > 10)
					strength--;
				String output;
				output = arr[0] + "`" + arr[1] + "`" + arr[2] + "`" + strength +"`"+ arr[4] + "`" + arr[5];
				marks.get(i).setProperty("data", output);
				datastore.put(marks.get(i));
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
