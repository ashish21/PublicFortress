package com.fort.project.neighbourhoodwatch.server;

import java.util.List;

import com.fort.project.neighbourhoodwatch.client.CounterService;
import com.fort.project.neighbourhoodwatch.client.NotLoggedInException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class CounterServiceImpl extends RemoteServiceServlet implements CounterService {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static String key = "CounterInformation";
	static Key counterInformationList = KeyFactory.createKey("Counters", key);
	static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();	
	
	public void setCounter(final String data) {
		  
		String [] temp;
		temp = data.split(" ");

		Query query = new Query("Counter", counterInformationList);
		List<Entity> entities = datastore.prepare(query).asList(FetchOptions.Builder.withChunkSize(10));
		
		boolean firstTime = true;
		
		System.out.println(entities.size());
		
		for(int i=0; i<entities.size(); i++) {
			
			if(((String)entities.get(i).getProperty("user")).equals(temp[0])) {
				
				firstTime = false;
				String bigData = (String) entities.get(i).getProperty("data");
				String [] bigSplit;
				bigSplit = bigData.split(" ");
				boolean exists = false;
				for(int k=0; k<bigSplit.length-1; k+=2) {
					
					if(bigSplit[k].equals(temp[1])) {
						
						bigSplit[k+1] = temp[2];
						exists = true;
						break;
					}
				}
				if(exists == false) {
					
					bigData += (" "+temp[1]+" "+temp[2]);
				} else {
					
					String out="";
					for(String a : bigSplit) {
						
						out+=(a+" ");
					}
					bigData = out.trim();
				}
				System.out.println("User data= " + bigData);
				entities.get(i).setProperty("data", bigData);	
				datastore.put(entities.get(i));
				break;
			}
		}
		
		if(firstTime == true) {
			
			Entity entity = new Entity("Counter", counterInformationList);			
			entity.setProperty("user", temp[0]);
			entity.setProperty("data", temp[1]+" "+temp[2]);
			System.out.println("Setting data= "+temp[1]+temp[0]+temp[2]);
			datastore.put(entity);
		}
	}  
	
	public Integer getCounter(String data) throws NotLoggedInException {
		  		
		String [] temp;
		temp = data.split(" ");

		Query query = new Query("Counter", counterInformationList);
		List<Entity> entities = datastore.prepare(query).asList(FetchOptions.Builder.withChunkSize(10));
				
		System.out.println(entities.size());
		
		for(int i=0; i<entities.size(); i++) {
			
			System.out.println("GET" + (String)entities.get(i).getProperty("user") + " " + temp[0]);
			
			if(((String)entities.get(i).getProperty("user")).equals(temp[0])) {				
				
				String bigData = (String) entities.get(i).getProperty("data");
				String [] bigSplit;
				bigSplit = bigData.split(" ");
				for(int k=0; k<bigSplit.length-1; k+=2) {
					
					System.out.println("GET" + bigSplit[k] + " " + temp[1]);
					
					if(bigSplit[k].equals(temp[1])) {
						
						System.out.println("User found in counters");
						return Integer.parseInt(bigSplit[k+1]);						
					}
				}
				
				return 0;
			}
		}
		
		return 0;
	}
}
