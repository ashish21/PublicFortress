package com.fort.project.neighbourhoodwatch.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class Works extends Composite {

	static private Works _instance = null;
	@UiField(provided = true)
	final GwtMapsResources res;	
	@UiField
	HorizontalPanel badge,twitter;
	@UiField
	HTMLPanel boxtxt;
	@UiField
	Button works, about, contact, homepage, privacy, about2, contact2, blog, android, support;
	@UiField
	PushButton logo;
	private static AbotUiBinder uiBinder = GWT.create(AbotUiBinder.class);

	interface AbotUiBinder extends UiBinder<Widget, Works> {
	}

	public Works() {
		this.res = GWT.create(GwtMapsResources.class);
		res.style().ensureInjected();	
		initWidget(uiBinder.createAndBindUi(this));
		drawBadge();
		drawTwitter();
		about.addClickHandler(new ClickHandler(){
	        @Override
	        public void onClick(ClickEvent event) {
	        	History.newItem("About");
	           }

	    });
	 about2.addClickHandler(new ClickHandler(){
	        @Override
	        public void onClick(ClickEvent event) {
	        	History.newItem("About");
	           }

	    }); 
		contact.addClickHandler(new ClickHandler(){
		        @Override
		        public void onClick(ClickEvent event) {
		        	History.newItem("Contact");
		           }

		    });
		 contact2.addClickHandler(new ClickHandler(){
		        @Override
		        public void onClick(ClickEvent event) {
		        	History.newItem("Contact");
		           }

		    });
		 privacy.addClickHandler(new ClickHandler(){
		        @Override
		        public void onClick(ClickEvent event) {
		        	History.newItem("Privacy");
		           }

		    });
		 blog.addClickHandler(new ClickHandler(){
		        @Override
		        public void onClick(ClickEvent event) {
		        	Window.open("http://publicfortress.blogspot.in/","_blank","");
		           }

		    });
		 logo.addClickHandler(new ClickHandler(){
		        @Override
		        public void onClick(ClickEvent event) {
		        	History.newItem("Home");
		           }

		    });
		 works.addClickHandler(new ClickHandler(){
		        @Override
		        public void onClick(ClickEvent event) {
		        	History.newItem("Works");
		           }

		    });
		 homepage.addClickHandler(new ClickHandler(){
		        @Override
		        public void onClick(ClickEvent event) {
		        	History.newItem("Home");
		           }

		    });
		 support.addClickHandler(new ClickHandler(){
		        @Override
		        public void onClick(ClickEvent event) {
		        	History.newItem("Contact");
		           }

		    });
		HTML label = new HTML(res.text2().getText());
		boxtxt.add(label);
		
		
		
	}
		
	public static Works getInstance(){
        if(null == _instance) {
        	_instance = new Works();
        }
        return _instance;
	}

	private void drawBadge() {
	    
		String s = "<g:page width=\"200\" href=\"https://plus.google.com/102192205824377892093\" rel=\"publisher\"></g:page>";
	    HTML h = new HTML(s);
	    badge.add(h);
	    
	    // You can insert a script tag this way or via your .gwt.xml
	    Document doc = Document.get();
	    ScriptElement script = doc.createScriptElement();
	    script.setSrc("https://apis.google.com/js/platform.js");
	    script.setType("text/javascript");
	    script.setLang("javascript");
	    doc.getBody().appendChild(script);
	  }
private void drawTwitter() {
	    
		
		String s = "<a class=\"twitter-timeline\" width=\"520\" height=\"320\" href=\"https://twitter.com/NWatch_gcdc\" data-widget-id=\"418124860338864128\">Tweets by @NWatch_gcdc</a>";

	   
		HTML h = new HTML(s);
		    twitter.add(h);
		    
		    // You can insert a script tag this way or via your .gwt.xml
		    Document doc = Document.get();
		    ScriptElement script = doc.createScriptElement();
		    script.setSrc("https://platform.twitter.com/widgets.js");
		    script.setType("text/javascript");
		    script.setLang("javascript");
		    doc.getBody().appendChild(script);
		  }

}
