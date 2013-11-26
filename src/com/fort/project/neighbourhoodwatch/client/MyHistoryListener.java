package com.fort.project.neighbourhoodwatch.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class MyHistoryListener implements ValueChangeHandler<String> {

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		System.out.println("Current State : " + event.getValue());
		
		if (event.getValue().equals("GwtMaps")){
    		RootLayoutPanel.get().clear();
    		RootLayoutPanel.get().add(GwtMaps.getInstance());
		} else if (event.getValue().equals("contactus")){
			RootLayoutPanel.get().clear();
			RootLayoutPanel.get().add(Page2.getInstance());
		} else if (event.getValue().equals("aboutus")){
			RootLayoutPanel.get().clear();
			RootLayoutPanel.get().add(AboutUs.getInstance());
		} else if (event.getValue().equals("privacy")){
			RootLayoutPanel.get().clear();
			RootLayoutPanel.get().add(Privacy.getInstance());
		}	
	}

}
