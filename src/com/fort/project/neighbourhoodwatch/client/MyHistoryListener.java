package com.fort.project.neighbourhoodwatch.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class MyHistoryListener implements ValueChangeHandler<String> {

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		System.out.println("Current State : " + event.getValue());
		
		if (event.getValue().equals("Home")){
    		RootLayoutPanel.get().clear();
    		RootLayoutPanel.get().add(Home.getInstance());
		} else if (event.getValue().equals("Contact")){
			RootLayoutPanel.get().clear();
			RootLayoutPanel.get().add(Contact.getInstance());
		} else if (event.getValue().equals("About")){
			RootLayoutPanel.get().clear();
			RootLayoutPanel.get().add(About.getInstance());
		} else if (event.getValue().equals("Privacy")){
			RootLayoutPanel.get().clear();
			RootLayoutPanel.get().add(Privacy.getInstance());
		} else if (event.getValue().equals("Works")){
			RootLayoutPanel.get().clear();
			RootLayoutPanel.get().add(Works.getInstance());
		} 
		
	}

}
