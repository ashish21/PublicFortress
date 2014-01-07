package com.fort.project.neighbourhoodwatch.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface CounterServiceAsync {
	
	  public void setCounter(String data, AsyncCallback<Void> async);  //username + markerID + magic
	  public void getCounter(String data, AsyncCallback<Integer> async);  //username + markerID
}