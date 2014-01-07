package com.fort.project.neighbourhoodwatch.client;

import com.fort.project.neighbourhoodwatch.shared.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.events.click.ClickMapEvent;
import com.google.gwt.maps.client.events.click.ClickMapHandler;
import com.google.gwt.maps.client.overlays.Animation;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;

import com.google.gwt.user.client.ui.Widget;

public class Report extends Composite {

	private CounterServiceAsync counterService = GWT.create(CounterService.class);
	
	private int magic=0;
	
	AsyncCallback<Integer> getCounter = new AsyncCallback<Integer>() {
		
		@Override
		public void onSuccess(Integer result) {
			
			System.out.println("SUCCESS"+result);
			
			if(result.intValue() == 0) {
				
				up.getUpFace().setImage(new Image(res.up0()));
				down.getUpFace().setImage(new Image(res.down0()));
				flag.getUpFace().setImage(new Image(res.flagoff()));
				magic = 0;
			}
			
			if(result.intValue() == 1) { 
				
				upcount.setText(String.valueOf(Integer.parseInt(upcount.getText())+1));
				up.getUpFace().setImage(new Image(res.up1()));
				down.getUpFace().setImage(new Image(res.down0()));
				flag.getUpFace().setImage(new Image(res.flagoff()));
				magic = 1;
			} else if(result.intValue() == 2) { 
				
				downcount.setText(String.valueOf(Integer.parseInt(downcount.getText())+1));
				up.getUpFace().setImage(new Image(res.up0()));
				down.getUpFace().setImage(new Image(res.down1()));
				flag.getUpFace().setImage(new Image(res.flagoff()));
				magic = 2;
			} else if(result.intValue() == 3) { 
				
				upcount.setText(String.valueOf(Integer.parseInt(upcount.getText())+1));
				flagCount.setText(String.valueOf(Integer.parseInt(flagCount.getText())+1));
				
				up.getUpFace().setImage(new Image(res.up1()));
				down.getUpFace().setImage(new Image(res.down0()));
				flag.getUpFace().setImage(new Image(res.flagon()));
				magic = 3;
			} else if(result.intValue() == 4) { 
				
				downcount.setText(String.valueOf(Integer.parseInt(downcount.getText())+1));
				flagCount.setText(String.valueOf(Integer.parseInt(flagCount.getText())+1));				
				up.getUpFace().setImage(new Image(res.up0()));
				down.getUpFace().setImage(new Image(res.down1()));
				flag.getUpFace().setImage(new Image(res.flagon()));
				magic = 4;
			} else if(result.intValue() == 5) { 
				
				flagCount.setText(String.valueOf(Integer.parseInt(flagCount.getText())+1));				
				up.getUpFace().setImage(new Image(res.up0()));
				down.getUpFace().setImage(new Image(res.down0()));
				flag.getUpFace().setImage(new Image(res.flagon()));
				magic = 5;
			}
		}

		@Override
		public void onFailure(Throwable caught) {
			
			System.out.println(caught.getMessage());
		}
	};
	
	AsyncCallback<Void> setCounter = new AsyncCallback<Void>() {
		
		@Override
		public void onSuccess(Void result) {
			
			up.setEnabled(true);
			down.setEnabled(true);
			flag.setEnabled(true);
		}

		@Override
		public void onFailure(Throwable caught) {
			
			up.setEnabled(true);
			down.setEnabled(true);
			flag.setEnabled(true);
			System.out.println(caught.getMessage());
		}
	};
	
	static private Report _instance = null;
	private MapWidget mapw;
	@UiField(provided = true)
	final GwtMapsResources res;	
	@UiField
	HorizontalPanel badge,twitter;
	@UiField
	HorizontalPanel fb;
	@UiField
	Button works, about, contact, homepage, privacy, about2, contact2, blog, android, support;
	@UiField
	PushButton logo,up,down,flag;
	@UiField
	Image image;
	@UiField
	Label description,address,date,upcount,downcount, flagCount, type;
	@UiField
	SimpleLayoutPanel mapcanvas;	
	
	private static AbotUiBinder uiBinder = GWT.create(AbotUiBinder.class);

	interface AbotUiBinder extends UiBinder<Widget, Report> {
	}

	public Report() {
		
		System.out.println("ID= "+Constants.dataPass.id);
		this.res = GWT.create(GwtMapsResources.class);
		res.style().ensureInjected();	
		initWidget(uiBinder.createAndBindUi(this));
		drawBadge();
		drawfb();
		drawTwitter();
		description.setText(Constants.dataPass.info);
		address.setText(Constants.dataPass.address);
		date.setText(Constants.dataPass.date);
		upcount.setText(String.valueOf(Constants.dataPass.tups));
		downcount.setText(String.valueOf(Constants.dataPass.tdwns));
		flagCount.setText(String.valueOf(Constants.dataPass.flags));
		type.setText(Constants.retranslate(Constants.dataPass.type));
		
		
		
		MapOptions mapo = MapOptions.newInstance();
	    mapo.setZoom(14);
	    mapo.setMapTypeId(MapTypeId.ROADMAP);
	    mapo.setMinZoom(5);
	    
	    mapo.setCenter(Constants.dataPass.location);
	    		
	    mapw = new MapWidget(mapo);
	    mapcanvas.add(mapw);
	    mapw.setSize("400px", "350px");
        
		
		MarkerOptions mrko = MarkerOptions.newInstance();
		mrko.setPosition(Constants.dataPass.location);
		mrko.setMap(mapw);
		mrko.setTitle(Constants.retranslate(Constants.dataPass.type));
		mrko.setDraggable(false);
		mrko.setAnimation(Animation.BOUNCE);
		
		String temp = "_low.png";
		if(Constants.dataPass.strength >= 90) temp = "_extreme.png";
		else if(Constants.dataPass.strength >= 60) temp = "_high.png";
		else if(Constants.dataPass.strength >= 30) temp = "_medium.png";
		else temp = "_low.png";
		mrko.setIcon(Home.getPath(Constants.dataPass.type)+temp);
		System.out.println("Trying to create marker #USER" + Constants.uri);
		final Marker mark = Marker.newInstance(mrko);
		mark.addClickHandler(new ClickMapHandler() {

	    	@Override
            public void onEvent(ClickMapEvent event) {
            	
            	mark.setAnimation(Animation.STOPANIMATION);
            	//marker.clearInstanceListeners();
            }
        });
		
		
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
		 if(Home.loginInfo.isLoggedIn()) {
			 
			 counterService.getCounter(Home.loginInfo.getNickname()+" "+Constants.dataPass.id, getCounter);
			 up.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					
					System.out.println(magic);
					
					if(magic == 0) {
						
						up.getUpFace().setImage(new Image(res.up1()));
						down.getUpFace().setImage(new Image(res.down0()));
						flag.getUpFace().setImage(new Image(res.flagoff()));
						magic = 1;
						upcount.setText(String.valueOf(Integer.parseInt(upcount.getText()) + 1));
					} else if(magic == 1) {
						
						up.getUpFace().setImage(new Image(res.up0()));
						down.getUpFace().setImage(new Image(res.down0()));
						flag.getUpFace().setImage(new Image(res.flagoff()));
						magic = 0;
						upcount.setText(String.valueOf(Integer.parseInt(upcount.getText()) - 1));
					} else if(magic == 2) {
						
						up.getUpFace().setImage(new Image(res.up1()));
						down.getUpFace().setImage(new Image(res.down0()));
						flag.getUpFace().setImage(new Image(res.flagoff()));
						magic = 1;
						downcount.setText(String.valueOf(Integer.parseInt(downcount.getText()) - 1));
						upcount.setText(String.valueOf(Integer.parseInt(upcount.getText()) + 1));
					} else if(magic == 3) {
						
						up.getUpFace().setImage(new Image(res.up0()));
						down.getUpFace().setImage(new Image(res.down0()));
						flag.getUpFace().setImage(new Image(res.flagon()));
						magic = 5;
						upcount.setText(String.valueOf(Integer.parseInt(upcount.getText()) - 1));
					} else if(magic == 4) {
						
						up.getUpFace().setImage(new Image(res.up1()));
						down.getUpFace().setImage(new Image(res.down0()));
						flag.getUpFace().setImage(new Image(res.flagon()));
						magic = 3;
						upcount.setText(String.valueOf(Integer.parseInt(upcount.getText()) + 1));
						downcount.setText(String.valueOf(Integer.parseInt(downcount.getText()) - 1));
					} else if(magic == 5) {
						
						up.getUpFace().setImage(new Image(res.up1()));
						down.getUpFace().setImage(new Image(res.down0()));
						flag.getUpFace().setImage(new Image(res.flagon()));
						magic = 3;
						upcount.setText(String.valueOf(Integer.parseInt(upcount.getText()) + 1));
					} 
					
					System.out.println(magic + " " + upcount.getText() + " " + downcount.getText() + " " + flagCount.getText());
					
					up.setEnabled(false);
					down.setEnabled(false);
					flag.setEnabled(false);
					counterService.setCounter(Home.loginInfo.getNickname()+" "+
										      String.valueOf(Constants.dataPass.id)+" "+
										      magic, setCounter);
				} 
			 });
			 down.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						
						System.out.println(magic);

						if(magic == 0) {
							
							up.getUpFace().setImage(new Image(res.up0()));
							down.getUpFace().setImage(new Image(res.down1()));
							flag.getUpFace().setImage(new Image(res.flagoff()));
							magic = 2;
							downcount.setText(String.valueOf(Integer.parseInt(downcount.getText()) + 1));
						} else if(magic == 1) {
							
							up.getUpFace().setImage(new Image(res.up0()));
							down.getUpFace().setImage(new Image(res.down1()));
							flag.getUpFace().setImage(new Image(res.flagoff()));
							magic = 2;
							upcount.setText(String.valueOf(Integer.parseInt(upcount.getText()) - 1));
							downcount.setText(String.valueOf(Integer.parseInt(downcount.getText()) + 1));
						} else if(magic == 2) {
							
							up.getUpFace().setImage(new Image(res.up0()));
							down.getUpFace().setImage(new Image(res.down0()));
							flag.getUpFace().setImage(new Image(res.flagoff()));
							magic = 0;
							downcount.setText(String.valueOf(Integer.parseInt(downcount.getText()) - 1));
						} else if(magic == 3) {
							
							up.getUpFace().setImage(new Image(res.up0()));
							down.getUpFace().setImage(new Image(res.down1()));
							flag.getUpFace().setImage(new Image(res.flagon()));
							magic = 4;
							upcount.setText(String.valueOf(Integer.parseInt(upcount.getText()) - 1));
							downcount.setText(String.valueOf(Integer.parseInt(downcount.getText()) + 1));
						} else if(magic == 4) {
							
							up.getUpFace().setImage(new Image(res.up0()));
							down.getUpFace().setImage(new Image(res.down0()));
							flag.getUpFace().setImage(new Image(res.flagon()));
							magic = 5;
							downcount.setText(String.valueOf(Integer.parseInt(downcount.getText()) - 1));
						} else if(magic == 5) {
							
							up.getUpFace().setImage(new Image(res.up0()));
							down.getUpFace().setImage(new Image(res.down1()));
							flag.getUpFace().setImage(new Image(res.flagon()));
							magic = 4;
							downcount.setText(String.valueOf(Integer.parseInt(downcount.getText()) + 1));
						} 

						System.out.println(magic + " " + upcount.getText() + " " + downcount.getText() + " " + flagCount.getText());
						up.setEnabled(false);
						down.setEnabled(false);
						flag.setEnabled(false);
						counterService.setCounter(Home.loginInfo.getNickname()+" "+
							      String.valueOf(Constants.dataPass.id)+" "+
							      magic, setCounter);
					} 
				 });
			 flag.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						
						System.out.println(magic);

						if(magic == 0) {
							
							up.getUpFace().setImage(new Image(res.up0()));
							down.getUpFace().setImage(new Image(res.down0()));
							flag.getUpFace().setImage(new Image(res.flagon()));
							magic = 5;
							flagCount.setText(String.valueOf(Integer.parseInt(flagCount.getText()) + 1));
						} else if(magic == 1) {
							
							up.getUpFace().setImage(new Image(res.up1()));
							down.getUpFace().setImage(new Image(res.down0()));
							flag.getUpFace().setImage(new Image(res.flagon()));
							magic = 3;
							flagCount.setText(String.valueOf(Integer.parseInt(flagCount.getText()) + 1));
						} else if(magic == 2) {
							
							up.getUpFace().setImage(new Image(res.up0()));
							down.getUpFace().setImage(new Image(res.down1()));
							flag.getUpFace().setImage(new Image(res.flagon()));
							magic = 4;
							flagCount.setText(String.valueOf(Integer.parseInt(flagCount.getText()) + 1));
						} else if(magic == 3) {
							
							up.getUpFace().setImage(new Image(res.up1()));
							down.getUpFace().setImage(new Image(res.down0()));
							flag.getUpFace().setImage(new Image(res.flagoff()));
							magic = 1;
							flagCount.setText(String.valueOf(Integer.parseInt(flagCount.getText()) - 1));
						} else if(magic == 4) {
							
							up.getUpFace().setImage(new Image(res.up0()));
							down.getUpFace().setImage(new Image(res.down1()));
							flag.getUpFace().setImage(new Image(res.flagoff()));
							magic = 2;
							flagCount.setText(String.valueOf(Integer.parseInt(flagCount.getText()) - 1));
						} else if(magic == 5) {
							
							up.getUpFace().setImage(new Image(res.up0()));
							down.getUpFace().setImage(new Image(res.down0()));
							flag.getUpFace().setImage(new Image(res.flagoff()));
							magic = 0;
							flagCount.setText(String.valueOf(Integer.parseInt(flagCount.getText()) - 1));
						} 

						System.out.println(magic + " " + upcount.getText() + " " + downcount.getText() + " " + flagCount.getText());
						up.setEnabled(false);
						down.setEnabled(false);
						flag.setEnabled(false);
						
						counterService.setCounter(Home.loginInfo.getNickname()+" "+
							      String.valueOf(Constants.dataPass.id)+" "+
							      magic, setCounter);
					} 
				 });
		 }
		 
		 Timer timer = new Timer() {

		        @Override
		        public void run() {
		            mapReload();
		            
		        }
		    };
		    timer.schedule(5);
	}
		
	public static Report getInstance(){
        if(null == _instance) {
        	_instance = new Report();
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
	private void drawfb() {
	    	
		String s = "<fb:comments href=\"http://gcdc2013-neighbourhoodwatch.appspot.com/#"+String.valueOf(Constants.dataPass.id)+"\" numposts=\"10\" colorscheme=\"light\"></fb:comments>";
	    HTML h = new HTML(s);
	    fb.add(h);
	    Document doc = Document.get();
	    ScriptElement script = doc.createScriptElement();
	    script.setSrc("//connect.facebook.net/en_US/all.js#xfbml=1");
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
private void mapReload(){
	mapw.triggerResize();
	mapw.setCenter(Constants.dataPass.location);
}
}
