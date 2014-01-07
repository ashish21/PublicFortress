package com.fort.project.neighbourhoodwatch.server;

import java.io.IOException;
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
import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.services.Geocoder;
import com.google.gwt.maps.client.services.GeocoderRequest;
import com.google.gwt.maps.client.services.GeocoderRequestHandler;
import com.google.gwt.maps.client.services.GeocoderResult;
import com.google.gwt.maps.client.services.GeocoderStatus;


public class GeoCoder extends HttpServlet {
	/**
	 * 
	 */
	static String key = "MarkerLocationList", address="?";
	static Key markerLocationList = KeyFactory.createKey("Markers", key);
	static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();	
	private static final long serialVersionUID = 1L;
	int per=100;
	private static final Logger _logger = Logger.getLogger(GeoCoder.class.getName());
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		try {
			_logger.info("Cron Job has been executed");
			Query query = new Query("Parent", markerLocationList).addSort("date", Query.SortDirection.DESCENDING);
			List<Entity> marks = datastore.prepare(query).asList(FetchOptions.Builder.withChunkSize(10));
			String temp;
			for(int i=0; i<marks.size(); i++) {
			 
				temp = (String)marks.get(i).getProperty("data");				
				if(temp.equals(null) || temp.equals("")) continue;
				
				temp.trim();
				
				String arr[] = temp.split("`");
				if(arr.length != 10) continue;
				
				if(arr[9].equals("?")) {
					
					LatLng loc = LatLng.newInstance(Double.parseDouble(arr[0]), 
							  						Double.parseDouble(arr[1]));
			  	    final GeocoderRequest request = GeocoderRequest.newInstance();
				    request.setLocation(loc);
				    Geocoder geocoder = Geocoder.newInstance();				    
				    geocoder.geocode(request, new GeocoderRequestHandler() {

						@Override
						public void onCallback(JsArray<GeocoderResult> results,
								GeocoderStatus status) {
							
							if(status.toString().equals("OK")) {
								
								address = results.get(0).getFormatted_Address();
							} 
						}
				    });
					
					if(!address.equals("?")) {
						
						arr[9] = address;
					}
					
					String temp2="";
					
					for(int k=0; k<arr.length; k++) {
						
						temp2+=arr[k];
					}
					
					marks.get(i).setProperty("data", temp2);
					
					datastore.put(marks.get(i));
				}
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
