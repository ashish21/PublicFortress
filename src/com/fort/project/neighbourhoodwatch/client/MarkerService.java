package com.fort.project.neighbourhoodwatch.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("mark")
public interface MarkerService extends RemoteService {
	
	public void addMark(String symbol, String user) throws NotLoggedInException;
	public String[] getMarks() throws NotLoggedInException;
}