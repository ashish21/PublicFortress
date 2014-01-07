package com.fort.project.neighbourhoodwatch.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("count")
public interface CounterService extends RemoteService {
	
	public void setCounter(String data) throws NotLoggedInException; //username + markerID + magicno.
	public Integer getCounter(String data) throws NotLoggedInException;  //username + markerID
}