package com.fort.project.neighbourhoodwatch.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.History;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PublicFortress implements EntryPoint {

	@Override
	public void onModuleLoad() {
		
		System.out.println("onModuleLoad");	  
		String initToken = History.getToken();
	  	
	    if (initToken.length() == 0) {
	      History.newItem("Home");
	    }
		    
	  	History.addValueChangeHandler(new MyHistoryListener());
	  	History.fireCurrentHistoryState();
	}


}
