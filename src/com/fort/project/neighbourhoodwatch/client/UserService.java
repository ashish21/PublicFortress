package com.fort.project.neighbourhoodwatch.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("user")
public interface UserService extends RemoteService {
	
	public String [] getUser(String user) throws NotLoggedInException;
	public Date [] getDate();
}