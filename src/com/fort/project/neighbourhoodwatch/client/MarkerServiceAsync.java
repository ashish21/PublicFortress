package com.fort.project.neighbourhoodwatch.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface MarkerServiceAsync {
	
	  public void addMark(String symbol, String user, AsyncCallback<Void> async);
	  public void getMarks(AsyncCallback<String[]> async);
}