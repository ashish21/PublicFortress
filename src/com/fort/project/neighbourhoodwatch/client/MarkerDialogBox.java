package com.fort.project.neighbourhoodwatch.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


public class MarkerDialogBox extends DialogBox {	

	  
	  @UiField SimplePanel mainPanel;
	  @UiField TextBox dateBox, infoBox;
	  @UiField Image image;
	  @UiField Button close;
	  
	  @UiHandler("close")
	  void onClose(ClickEvent e) {
		  
		  this.hide();
	  }

	  
	
	  interface Binder extends UiBinder<Widget, MarkerDialogBox> {}

	  public MarkerDialogBox(String date, String info, String uri) {		  
		  
		  super.setWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		  infoBox.setReadOnly(true);
		  dateBox.setReadOnly(true);
		  dateBox.setText(date);
		  infoBox.setText(info);
		  if(info.length() < 100)
			  infoBox.setVisibleLength(info.length()+1);
		  else
			  infoBox.setVisibleLength(100);
		  image.setUrl(uri+"_low.png");
	  }  
	
	  @Override
	  public void setWidget(Widget widget) {
	    mainPanel.setWidget(widget);
	  }

	}