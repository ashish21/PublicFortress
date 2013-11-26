package com.fort.project.neighbourhoodwatch.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

public class AboutUs extends Composite {
	static private AboutUs _instance = null;
	private final FlowPanel absolutePanel = new FlowPanel();
	final GwtMapsResources res;
	public AboutUs(){
		
		this.res = GWT.create(GwtMapsResources.class);
		res.style().ensureInjected();	
		initWidget(absolutePanel);
		
		absolutePanel.setStylePrimaryName(res.style().silver());
		
		Label buffer = new Label(".");	
		buffer.setStyleName(res.style().redText());
		
		Label heading = new Label("About Us");
		heading.setStyleName(res.style().heading());
		absolutePanel.add(heading);
		
		absolutePanel.add(buffer);
		absolutePanel.add(buffer);
		absolutePanel.add(buffer);
		
		HTML label = new HTML(new SafeHtmlBuilder().appendEscapedLines(res.text().getText()).toSafeHtml());
		label.setStyleName(res.style().blackText());
		
		ScrollPanel panel = new ScrollPanel(label);
		panel.setHeight("520px");
		
		absolutePanel.add(panel);
		
		absolutePanel.add(buffer);
		absolutePanel.add(buffer);
		
		Button button = new Button("Back");
		button.setStyleName(res.style().button());
		button.addClickHandler(new ClickHandler() {
		  public void onClick(ClickEvent event) {
			  History.back();
		  }
		});
		
		absolutePanel.add(button);
	}

	public static AboutUs getInstance(){
        if(null == _instance) {
        	_instance = new AboutUs();
        }
        return _instance;
	}
}
