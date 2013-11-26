package com.fort.project.neighbourhoodwatch.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {
	
	public void getUser(String user, AsyncCallback<String []> async);
	public void getDate(AsyncCallback<Date []> async);
}