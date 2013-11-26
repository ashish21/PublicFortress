package com.fort.project.neighbourhoodwatch.server;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.fort.project.neighbourhoodwatch.client.LoginInfo;
import com.fort.project.neighbourhoodwatch.client.LoginService;

public class LoginServiceImpl extends RemoteServiceServlet implements
    LoginService {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static LoginInfo loginInfo = new LoginInfo();

public LoginInfo login(String requestUri) {
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();   

    if (user != null) {

        System.out.println("create logout");
      loginInfo.setLoggedIn(true);
      loginInfo.setEmailAddress(user.getEmail());
      loginInfo.setNickname(user.getNickname());
      loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
      loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
    } else {

        System.out.println("create login");
      loginInfo.setLoggedIn(false);
      loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
      loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
    }
    return loginInfo;
  }
}