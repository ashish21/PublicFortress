package com.fort.project.neighbourhoodwatch.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ContactUs extends Composite {
	private VerticalPanel page2 = new VerticalPanel();
	private Frame F1= new Frame();
	static private ContactUs _instance = null;
	
	@UiField(provided = true)
	final GwtMapsResources res;
	
	public ContactUs(){
		this.res = GWT.create(GwtMapsResources.class);
		res.style().ensureInjected();
		initPage();
		initWidget(page2);
	}

	public static ContactUs getInstance(){
        if(null == _instance) {
        	_instance = new ContactUs();
        }
        return _instance;
	}

	private void initPage() {
		F1.setPixelSize(600,570);
		F1.setUrl("https://docs.google.com/forms/d/1TTvFFAXLZ7kqaM-9SojiQXwwJz5xR0SkEHAr3EDoQY8/viewform?embedded=true");
		
		//page2.setBorderWidth(2);
		page2.setPixelSize(150, 150);
		
		Button button = new Button("Back");
		button.setStyleName(res.style().button());
		button.addClickHandler(new ClickHandler() {
		  public void onClick(ClickEvent event) {
			  History.back();
		  }
		});
		
		page2.add(F1);
		//page2.add(content);
		page2.add(button);
		
		//<iframe src="https://docs.google.com/forms/d/1TTvFFAXLZ7kqaM-9SojiQXwwJz5xR0SkEHAr3EDoQY8/viewform?embedded=true" width="760" height="500" frameborder="0" marginheight="0" marginwidth="0">Loading...</iframe>
	}
}
