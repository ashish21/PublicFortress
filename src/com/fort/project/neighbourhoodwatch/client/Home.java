	package com.fort.project.neighbourhoodwatch.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import com.fort.project.neighbourhoodwatch.shared.Constants;
import com.fort.project.neighbourhoodwatch.shared.Constants.bundle;
import com.google.gwt.ajaxloader.client.ArrayHelper;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.Position.Coordinates;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.events.click.ClickMapEvent;
import com.google.gwt.maps.client.events.click.ClickMapHandler;
import com.google.gwt.maps.client.events.mouseout.MouseOutMapEvent;
import com.google.gwt.maps.client.events.mouseout.MouseOutMapHandler;
import com.google.gwt.maps.client.events.mouseover.MouseOverMapEvent;
import com.google.gwt.maps.client.events.mouseover.MouseOverMapHandler;
import com.google.gwt.maps.client.events.place.PlaceChangeMapEvent;
import com.google.gwt.maps.client.events.place.PlaceChangeMapHandler;
import com.google.gwt.maps.client.geometrylib.SphericalUtils;
import com.google.gwt.maps.client.overlays.Animation;
import com.google.gwt.maps.client.overlays.InfoWindow;
import com.google.gwt.maps.client.overlays.InfoWindowOptions;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.maps.client.placeslib.Autocomplete;
import com.google.gwt.maps.client.placeslib.AutocompleteOptions;
import com.google.gwt.maps.client.placeslib.PlaceResult;
import com.google.gwt.maps.client.services.Geocoder;
import com.google.gwt.maps.client.services.GeocoderRequest;
import com.google.gwt.maps.client.services.GeocoderRequestHandler;
import com.google.gwt.maps.client.services.GeocoderResult;
import com.google.gwt.maps.client.services.GeocoderStatus;
import com.google.gwt.maps.client.visualizationlib.HeatMapLayer;
import com.google.gwt.maps.client.visualizationlib.HeatMapLayerOptions;
import com.google.gwt.maps.utility.markerclustererplus.client.ClusterIconStyle;
import com.google.gwt.maps.utility.markerclustererplus.client.MarkerClusterer;
import com.google.gwt.maps.utility.markerclustererplus.client.MarkerClustererOptions;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Home extends Composite {
	
	static private Home _instance = null;
	public static Home getInstance(){
        if(null == _instance) {
        	_instance = new Home();
        }
        return _instance;
	}
	
	
	interface LoginWidgetURLBinder extends UiBinder <ScrollPanel, Home> {	}	
	private static LoginWidgetURLBinder uiBinder = GWT.create(LoginWidgetURLBinder.class);
	String historyToken="null";
	public static MapWidget map;    
    	
	private MarkerClusterer cluster;
	private HeatMapLayer heatMapLayer;
	
	private HashMap<String, ArrayList<Marker>> markerMap = new HashMap<String, ArrayList<Marker>>();
	private HashMap<String, JsArray<LatLng>> dataMap = new HashMap<String, JsArray<LatLng>>();
	
	private final ArrayList<holder> sortedOrder = new ArrayList<holder>();
	
	private int [] toShow = new int[Constants.legend.length];
	private String address = "?", addressOfUser="?";
	public static LoginInfo loginInfo = null;
	private LatLng initialLocation;	
    String url= "", link="";    
    private int t;
	
    
	private MarkerServiceAsync markerService = GWT.create(MarkerService.class);
	private MyTemplate template = GWT.create(MyTemplate.class);
	
	@UiField
	ScrollPanel scrollPast, scrollRecent, scrollNear;	
	@UiField(provided = true)
	final GwtMapsResources res;	
	@UiField
	TextBox input;	
	@UiField
	SimpleLayoutPanel map_canvas;
	@UiField
	HorizontalPanel badge;
	@UiField
	HorizontalPanel twitter;
	@UiField
	HorizontalPanel mapoverlay;
	@UiField
	MenuBar legend;
	@UiField
	Button works, about, contact, signin, privacy, about2, contact2, blog, android, support, toggle, past, nearby, latest, customize;
	@UiField
	DeckPanel legendDetails, togglePanel;
	@UiField
	VerticalPanel checkBoxes;
	@UiField
	PushButton logo;
	@UiField
	PushButton home;
	@UiHandler("home")
	protected void onClickHandler(final ClickEvent event) {
		map.setCenter(initialLocation);
		
	}
	
	@UiHandler("signin")
	void singin(ClickEvent e) {

		//Window.open(link, "replace", "");
		Window.Location.assign(link);
	}
	
	@UiHandler("past")
	void past(ClickEvent e) {

		togglePanel.showWidget(1);
	}
	@UiHandler("nearby")
	void nearby(ClickEvent e) {

		togglePanel.showWidget(3);
	}
	@UiHandler("latest")
	void latest(ClickEvent e) {

		togglePanel.showWidget(2);
	}
	@UiHandler("customize")
	void customize(ClickEvent e) {

		togglePanel.showWidget(0);
	}
	
	AsyncCallback<Date[]> getDate = new AsyncCallback<Date []>() {
		
		@Override
		public void onSuccess(Date [] result) {
			
			if(result.length == 0) {				

				return;
			}

			System.out.println("Dates received");
			
			Constants.temp = result;	

			if(!Constants.userInfo[0].equals("NULL")) {				

				MenuBar menu = new MenuBar(true);
				menu.setStyleName(res.style().menu());
				System.out.println("TRYING TO ADD TO MENU");
				MenuItem item = new MenuItem("Your Past Reports", new Command() {
				      @Override
				      public void execute() {
				      }
				});				
				item.setStyleName(res.style().blackText());
			    menu.addItem(item);
				for(int i=0; i<Constants.userInfo.length; i++) {
					
					if(i>10) break;
					String element = Constants.userInfo[i];
					Date date = Constants.temp[i];
					String [] temp;
					temp = element.split(" ");
					if(temp.length!=3) continue;
					String output=Constants.retranslate(temp[2])+", on "+date.toString();
					final double lat = Double.parseDouble(temp[0]);
					final double lng = Double.parseDouble(temp[1]);
					
					System.out.println(lat+" "+lng+" "+output);
					item = new MenuItem(template.createItem(getPath(temp[2]+"_low.png"),
							   SafeHtmlUtils.fromString(output)), new Command() {
					      @Override
					      public void execute() {
					    	  LatLng latlong=LatLng.newInstance(lat, lng);					    	  
					    	  map.setZoom(18);
					    	  map.setCenter(latlong);
					    	  
					      }
					});
					item.setStyleName(res.style().menuItem());
				    menu.addItem(item);
				}				
				
				if(menu != null) {	
					
					scrollPast.add(menu.asWidget());
					scrollPast.setHeight("250px");
					past.setVisible(true);
				}				
			}		
		}

		@Override
		public void onFailure(Throwable caught) {
			
			System.out.println(caught.getMessage());
			Window.alert("User details fetching failed");
		}
	};
	
	AsyncCallback<String[]> getMarks = new AsyncCallback<String []>() {
		
		@Override
		public void onSuccess(String [] result) {
			
			if(result[0].equals("NULL")) return;
			Constants.getSymbols(result);
			System.out.println("trying to mark with size" + Constants.symbols.size());
			if(Constants.symbols.isEmpty()) return;
			for(int i=0; i<Constants.symbols.size(); i++) {
				
				addMarker(Constants.symbols.get(i).location, 
						  Constants.symbols.get(i).type, 
						  Constants.symbols.get(i).strength,
						  Constants.symbols.get(i).date,
						  Constants.symbols.get(i).info,
						  Constants.symbols.get(i).tups,
						  Constants.symbols.get(i).tdwns,
						  Constants.symbols.get(i).flags,
						  Constants.symbols.get(i).address,
						  Constants.symbols.get(i).id);
			}
			
        	clear();
        	cluster.clearMarkers();
			for(ArrayList<Marker> a : markerMap.values()) {
				
				cluster.addMarkers(a);
			}			
	        JsArray<LatLng> dataPoints = JavaScriptObject.createArray().cast();
		    for(JsArray<LatLng> a : dataMap.values()) {
		    	
		    	for(int i=0; i<a.length(); i++) {
		    		
		    		dataPoints.push(a.get(i));
		    	}
		    }
		    show();
		    heatMapLayer.setData(dataPoints);
		}

		@Override
		public void onFailure(Throwable caught) {
			
			System.out.println(caught.getMessage());
		}
	};
	
	AsyncCallback<String[]> getUserDetails = new AsyncCallback<String []>() {
		
		@Override
		public void onSuccess(String [] result) {

			System.out.println("USer details received");
			Constants.userInfo = result;	
		}

		@Override
		public void onFailure(Throwable caught) {
			
			System.out.println("ErROR BITCH");
			System.out.println(caught.getMessage());
		}
	};
	
	AsyncCallback<Void> addMarks = new AsyncCallback<Void>() {
		
		@Override
		public void onSuccess(Void result) {
			
			System.out.println("Added marks");
		}

		@Override
		public void onFailure(Throwable caught) {
			
			System.out.println(caught.getMessage());
		}
	};

	AsyncCallback<LoginInfo> login = new AsyncCallback<LoginInfo>() {
		
		public void onFailure(Throwable error) {
			
			System.out.println("ERROR ="+error.getMessage());			
		}

		public void onSuccess(LoginInfo result) {
			
			System.out.println("RECEIVED USER= "+result.getNickname());
			loginInfo = result;
			if (loginInfo.isLoggedIn()) {
				
				Constants.setLoggenIn(true);
				System.out.println("Calling get userdetails");
			
			} else {
				Constants.setLoggenIn(false);
			}
			anchorHandler();				
		}
	};
	
//	@UiField
//	Button home;
//	@UiHandler("home")
//	protected void onClickHandler(final ClickEvent event) {
//		map.setCenter(initialLocation);
//		
//	}

	public void userManager() {  
		

		System.out.println("Login initiated");
		// (1) Check login status using login service.
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(com.google.gwt.core.client.GWT.getHostPageBaseURL(), login);
	}	
	
	public Home() {
		
		this.res = GWT.create(GwtMapsResources.class);
		res.style().ensureInjected();	
		
		initWidget(uiBinder.createAndBindUi(this));		
		// Building the map
		
		MapOptions myOptions = MapOptions.newInstance();
	    myOptions.setZoom(17);
	    myOptions.setStreetViewControl(true);	   
	    myOptions.setMapTypeId(MapTypeId.ROADMAP);
	    myOptions.setMinZoom(5);		
	    map = new MapWidget(myOptions);
	    
	    initialise();
	    
	    ClusterIconStyle cis= ClusterIconStyle.newInstance();
	    cis.setUrl("cluster.png");
	    cis.setHeight(40);
	    cis.setWidth(40);
	    cis.setTextSize(15);
	    cis.setTextColor("white");
	    
	    final MarkerClustererOptions mo= MarkerClustererOptions.newInstance();	    
	    mo.setStyle(cis);
	    cluster=MarkerClusterer.newInstance(map, mo);
	    
	    map_canvas.add(map);
	    
	    final HeatMapLayerOptions options = HeatMapLayerOptions.newInstance();
	    options.setOpacity(1);
	    options.setRadius(3);
	    options.setGradient(getSampleGradient());
	    heatMapLayer = HeatMapLayer.newInstance(options);
	    
	    map.addClickHandler(new ClickMapHandler() {
	        public void onEvent(final ClickMapEvent event) {
	        		       	        		        	
	        	Date date = new Date();
	        	long millis = 0;
	        		       
	        	if(!Constants.userInfo[0].equals("NULL"))
	        		millis = date.getTime()-Constants.temp[0].getTime();
	        	
        		System.out.println(millis);
        		
	        	if(Constants.isLoggenIn() == false) {	        		
	        		
	        		Window.alert("Please signin to report");	        		
	        	} 
//	        	else if(Constants.thisSessionReport == true) {
//	        		
//	        		Window.alert("You have already reported this session");
//	        	} else if(millis/86400000 < 1 && millis > 0) {
//	        		
//	        		System.out.println(millis);
//	        		long hours = (86400000-millis)/3600000;
//		        	String timeLeft = hours + " hours";
//		        	Window.alert(timeLeft + " before you can report again");
//	        	} else if(map.getZoom() < 14.0) {
//	        		
//	        		map.setZoom(14.0);
//	        	} 
	        	else {	

	        		Constants.mapClick = event.getMouseEvent().getLatLng();
	        		MainDialogBox dialogBox = new MainDialogBox();
	        		 dialogBox.setGlassEnabled(true);
	        		 dialogBox.setWidth("400px");

	        		 dialogBox.addCloseHandler(new CloseHandler<PopupPanel>() {
	        			
	        			@Override
	        			public void onClose(CloseEvent<PopupPanel> event1) {
	        				
	        				if(event1.isAutoClosed())
	        					reporting(Constants.uri, Constants.mapClick);
	        			}
	        		 }); 
	        		dialogBox.setStyleName(res.style().box2());
	                dialogBox.center();
	        	}
	        }
	    });	
	    
	    //Map built

		final LatLng myLatLng = LatLng.newInstance(28.60753,77.03505);
	    if (Geolocation.isSupported()) {                // GEOLOCATION STARTS HERE !
		       
		      Geolocation.getIfSupported().getCurrentPosition(
		          new Callback<Position, PositionError>() {
		
		            @Override
		            public void onSuccess(Position result) {
		            	
		            	System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		            	
			            Coordinates coords = result.getCoordinates();
			            initialLocation = LatLng.newInstance(coords.getLatitude(),
			            coords.getLongitude());
			            MarkerOptions newMarkerOpts2 = MarkerOptions.newInstance();
			      		newMarkerOpts2.setPosition(initialLocation);
			      	    newMarkerOpts2.setMap(map); 
					    newMarkerOpts2.setTitle("You are Here !");
				      	newMarkerOpts2.setDraggable(false);
				      	newMarkerOpts2.setAnimation(Animation.BOUNCE);
				      	newMarkerOpts2.setIcon("home-2.png");
				      	Marker.newInstance(newMarkerOpts2);

		            	map.setZoom(14);
				        map.setCenter(initialLocation);						        

				        System.out.println(initialLocation);
				        performCrimeVicinityTest(initialLocation);				        
			        }
		
		            public void onFailure(PositionError reason) {
		            	
		            	map.setZoom(14);
						initialLocation = myLatLng;
				        map.setCenter(initialLocation);		
		            }
		 
			          });
			} else {
				
				initialLocation = myLatLng;
				myOptions.setCenter(myLatLng);
			}
	    t=0;	    
	    
	    toggle.addClickHandler(new ClickHandler() {
	        @Override
	        public void onClick(ClickEvent event) {
	        	heatMapLayer.setMap(heatMapLayer.getMap() != null ? null : map);
	        	if(t==0)
	        	{
	        	clear();
	        	cluster.clearMarkers();
	        	t=1;
	        	toggle.setText("HeatMap View");
	        	}
	        	else
	        	{
	        	show();
	        	for(int i=0; i<toShow.length; i++) {

	    	    	if(toShow[i] == 1)
	    	    		cluster.addMarkers(markerMap.get(getPath(Constants.legend[i])));
	    	    }	
	    	    t=0;
	    	    toggle.setText("Clustered View");
	        	}
	           }

	    });
	    home.setTitle("Click to go to zap to your location");
	    toggle.setTitle("Click to toggle between Cluster and Heatmap view");
	    
		 
	    	  }
	
	protected void performCrimeVicinityTest(final LatLng initialLocation2) {
		
		final GeocoderRequest request = GeocoderRequest.newInstance();
	    request.setLocation(initialLocation2);
	    Geocoder geocoder = Geocoder.newInstance();				    
	    geocoder.geocode(request, new GeocoderRequestHandler() {

			@Override
			public void onCallback(JsArray<GeocoderResult> results,
					GeocoderStatus status) {
				
				if(status.toString().equals("OK")) {
					
					addressOfUser = results.get(0).getFormatted_Address().trim();
					if(addressOfUser.equals("?")) return;    
				    String country="", state="", city="";
				    String [] splitter;
				    splitter = addressOfUser.split(",");
				    if(splitter.length>0)
				    country = extracter(splitter[splitter.length-1]).trim();
				    if(splitter.length>1)
				    state = extracter(splitter[splitter.length-2]).trim();
				    if(splitter.length>2)
				    city = extracter(splitter[splitter.length-3]).trim();	    
				    
				    System.out.println(Constants.symbols.size());
				    
				    for(int i=0; i<Constants.symbols.size(); i++) {
				    	
				    	System.out.println("TRYING TO ACCESS BULLSHIT");
				    					    	
				    	String toCheck = Constants.symbols.get(i).address.trim(), City="", State="", Country="";
				    	
				    	System.out.println(i + " " + Constants.symbols.size());
				    	
				    	if(toCheck.equals("?")) {
				    		
				    		System.out.println("AAAAA" + toCheck);
				    		continue;
				    	}
				    	String [] splitter1;
				    	splitter1 = toCheck.split(",");

				    	if(splitter1.length>0)
			    	    Country = extracter(splitter1[splitter1.length-1]).trim();
			    	    if(splitter1.length>1)
			    	    State = extracter(splitter1[splitter1.length-2]).trim();
			    	    if(splitter1.length>2)
			    	    City = extracter(splitter1[splitter1.length-3]).trim();			    
			    	    
			    	    if(Country.equals(country) && City.equals(city) && State.equals(state)) {			    	    

			    	    	
			    	    	holder temp = new holder(Constants.symbols.get(i).type,
			    	    							 Constants.symbols.get(i).info,
			    	    							 Constants.symbols.get(i).date,
			    	    							 SphericalUtils.computeDistanceBetween(initialLocation2, Constants.symbols.get(i).location),
			    	    							 Constants.symbols.get(i).location);
			    	    	

			    	    	sortedOrder.add(temp);
			    	    	System.out.println(Constants.symbols.get(i).address);
			    	    	System.out.println(addressOfUser);
			    	    }
				    }	
				    
				    System.out.println(sortedOrder.size());
				    
				    //MAKING THE MENUS
				    
				    if(sortedOrder.size()>0) {				

						MenuBar menu = new MenuBar(true);
						menu.setStyleName(res.style().menu());
				    	
						MenuItem item = new MenuItem("Recent Reports", new Command() {
						      @Override
						      public void execute() {						    	  
						      }
						});
						item.setStyleName(res.style().blackText());
					    menu.addItem(item);
						System.out.println("TRYING TO ADD TO MENU");						
						for(int i=0; i<sortedOrder.size(); i++) {
							
							if(i>10) break;
							
							final int counter = i;
							
							String output=Constants.retranslate(sortedOrder.get(i).type)+", on "+sortedOrder.get(i).date;
							
							item = new MenuItem(template.createItem(getPath(sortedOrder.get(i).type)+"_low.png",
									   SafeHtmlUtils.fromString(output)), new Command() {
							      @Override
							      public void execute() {
							    	  
							    	  map.setZoom(18);
							    	  map.setCenter(sortedOrder.get(counter).loc);
							    	  
							      }
							});
							item.setStyleName(res.style().menuItem());
						    menu.addItem(item);
						}				
						
						if(menu != null) {	
							
							scrollRecent.add(menu.asWidget());
							scrollRecent.setHeight("250px");
							scrollRecent.onResize();
							latest.setVisible(true);
							scrollRecent.setVisible(true);
						}				
					}		
				    
				    //MENUS FINISHED
				    
				    Collections.sort(sortedOrder);
				    
				    //MAKING THE MENUS
				    
				    if(sortedOrder.size()>0) {				

						MenuBar menu = new MenuBar(true);
						menu.setStyleName(res.style().menu());
						MenuItem item = new MenuItem("Nearby Reports", new Command() {
						      @Override
						      public void execute() {						    	  
						      }
						});
						item.setStyleName(res.style().blackText());
					    menu.addItem(item);
						System.out.println("TRYING TO ADD TO MENU");						
						for(int i=0; i<sortedOrder.size(); i++) {
							
							if(i>10) break;
							
							final int counter = i;
							
							String output=Constants.retranslate(sortedOrder.get(i).type)+", on "+sortedOrder.get(i).date;
							
							item = new MenuItem(template.createItem(getPath(sortedOrder.get(i).type)+"_low.png",
									   SafeHtmlUtils.fromString(output)), new Command() {
							      @Override
							      public void execute() {
							    	  
							    	  map.setZoom(18);
							    	  map.setCenter(sortedOrder.get(counter).loc);
							    	  
							      }
							});
							item.setStyleName(res.style().menuItem());
						    menu.addItem(item);
						}				
						
						if(menu != null) {	
							
							scrollNear.add(menu.asWidget());
							scrollNear.setHeight("250px");
							scrollNear.onResize();
							nearby.setVisible(true);
							scrollNear.setVisible(true);

						}				
					}		
				    
				    //MENUS FINISHED
				    
				    //MAKING THE MENUS
				    
				    if(Constants.symbols.size()>0 && loginInfo.isLoggedIn()) {				

						MenuBar menu = new MenuBar(true);
						menu.setStyleName(res.style().menu());
						MenuItem item = new MenuItem("Your Past Reports", new Command() {
						      @Override
						      public void execute() {						    	  
						      }
						});
						item.setStyleName(res.style().blackText());
					    menu.addItem(item);
						System.out.println("TRYING TO ADD TO MENU");
					    int Counter=0;
						for(int i=0; i<Constants.symbols.size(); i++) {
							
							if(Counter>10) break;
							
							if(!(Constants.symbols.get(i).user.equals(loginInfo.getNickname()))) continue;
							
							Counter++;
							final int counter = i;
							
							String output=Constants.retranslate(Constants.symbols.get(i).type)+", on "+Constants.symbols.get(i).date;
							
							item = new MenuItem(template.createItem(getPath(Constants.symbols.get(i).type)+"_low.png",
									   SafeHtmlUtils.fromString(output)), new Command() {
							      @Override
							      public void execute() {
							    	  
							    	  map.setZoom(18);
							    	  map.setCenter(Constants.symbols.get(counter).location);
							    	  
							      }
							});
							item.setStyleName(res.style().menuItem());
						    menu.addItem(item);
						}				
						
						if(menu != null) {	
							
							scrollPast.add(menu.asWidget());
							scrollPast.setHeight("250px");
							scrollPast.onResize();
							past.setVisible(true);
							scrollPast.setVisible(true);
						}				
					}		
				    
				    //MENUS FINISHED
				    
				    for(int i=0; i<sortedOrder.size(); i++) {
				    	
				    	System.out.println(sortedOrder.get(i).distance);
				    }
				    
				    
				} 
			}
	    });	
	}
	
	private String extracter(String a) {
		
		a=a.trim();	
		String [] splitter;		
		splitter = a.split(" ");		
		String output="";
		for(String test : splitter) {
			
			test=test.trim();
			if(!Character.isDigit(test.charAt(0))) output+=test+" ";
		}	
		return output.trim();
	}

	private void clear()
	{
		for(ArrayList<Marker> a : markerMap.values()) {
			
			for(Marker b  : a) {
				b.setVisible(false);
			}
		}
	}
	private void show()
	{
		for(ArrayList<Marker> a : markerMap.values()) {
			
			for(Marker b  : a) {
				b.setVisible(true);
			}
		}
	}
	private JsArrayString getSampleGradient() {
		    String[] sampleColors = new String[] { "rgba(0, 255, 255, 0)", "rgba(0, 255, 255, 1)", "rgba(0, 191, 255, 1)",
		        "rgba(0, 127, 255, 1)", "rgba(0, 63, 255, 1)", "rgba(0, 0, 255, 1)", "rgba(0, 0, 223, 1)",
		        "rgba(0, 0, 191, 1)", "rgba(0, 0, 159, 1)", "rgba(0, 0, 127, 1)", "rgba(63, 0, 91, 1)", "rgba(127, 0, 63, 1)",
		        "rgba(191, 0, 31, 1)", "rgba(255, 0, 0, 1)" };
		    return ArrayHelper.toJsArrayString(sampleColors);
		  }
	
	  private void addMarker(final LatLng location, final String uri, final double strength, final String date, final String info, final int tups, final int tdwns, final int flags,final String address, final long id) {
		  
		  	MarkerOptions newMarkerOpts = MarkerOptions.newInstance();
		    newMarkerOpts.setPosition(location);
		    newMarkerOpts.setMap(map);
		    newMarkerOpts.setTitle("Click for more info");
		    newMarkerOpts.setDraggable(false);	    
		    String temp = "_low.png";
		    if(strength >= 90) temp = "_extreme.png";
		    else if(strength >= 60) temp = "_high.png";
		    else if(strength >= 30) temp = "_medium.png";
		    else temp = "_low.png";
		    newMarkerOpts.setIcon(url + uri + temp);		    
		    System.out.println("Trying to create marker #SERVER");
		    final Marker marker = Marker.newInstance(newMarkerOpts);
		    System.out.println("TESTING FOR ERROR= "+uri);
		    
		    final InfoWindowOptions iwo=InfoWindowOptions.newInstance();
		    final InfoWindow iw=InfoWindow.newInstance(iwo);
		    final HorizontalPanel vi=new HorizontalPanel();
		    final HTMLPanel hi=new HTMLPanel(Constants.retranslate(uri)+"</br></br>"+date);
		    vi.setWidth("100px");
		    vi.add(hi);	
		    
		    markerMap.get(uri).add(marker);		    
		    dataMap.get(uri).push(location);
		    
		    marker.addMouseOverHandler(new MouseOverMapHandler() {

				@Override
				public void onEvent(MouseOverMapEvent event) {
					
	            	iw.setContent(vi);	            	
	            	iw.open(map, marker);
				}	    	
		    });
		    
		    marker.addMouseOutMoveHandler(new MouseOutMapHandler() {

				@Override
				public void onEvent(MouseOutMapEvent event) {
           	
	            	iw.close();
				}
		    });
        	marker.addClickHandler(new ClickMapHandler() {

		    	@Override
	            public void onEvent(ClickMapEvent event) {
	            	
	            	marker.setAnimation(Animation.STOPANIMATION);
	            	System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAA!!");
	            	openMarkerDetails(location, uri, strength, date, info, tups, tdwns, flags, address, id); 
		    	}
	        });
	  }
	  
	  private void addMarker(LatLng location, double strength) {
		  
		  MarkerOptions newMarkerOpts = MarkerOptions.newInstance();
		  newMarkerOpts.setPosition(location);
		  newMarkerOpts.setMap(map);
		  newMarkerOpts.setTitle("Click for more info");
		  newMarkerOpts.setDraggable(false);
		  newMarkerOpts.setAnimation(Animation.BOUNCE);
		  String temp = "_low.png";
		  if(strength >= 90) temp = "_extreme.png";
		  else if(strength >= 60) temp = "_high.png";
		  else if(strength >= 30) temp = "_medium.png";
		  else temp = "_low.png";
		  newMarkerOpts.setIcon(url + Constants.uri + temp);
		  System.out.println("Trying to create marker #USER" + Constants.uri);
		  final Marker marker = Marker.newInstance(newMarkerOpts);
		  marker.addClickHandler(new ClickMapHandler() {

		    	@Override
	            public void onEvent(ClickMapEvent event) {
	            	
	            	marker.setAnimation(Animation.STOPANIMATION);
	            	//marker.clearInstanceListeners();
	            }
	        });
	  }
  
	  public void anchorHandler() {
		  
		  System.out.println("anchorHandler Called");
		  
			if(Constants.isLoggenIn() == false) {
				
				System.out.println("LOGIN");
				signin.setText("SignIn Please");
				link=loginInfo.getLoginUrl();
			} else {
				
				System.out.println("LOGOUT");
				signin.setText("SignOut");
				link=loginInfo.getLogoutUrl();
			}			
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
	  
	  @SuppressWarnings("unchecked")
	  private void initialise() {	

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
			 support.addClickHandler(new ClickHandler(){
			        @Override
			        public void onClick(ClickEvent event) {
			        	History.newItem("Contact");
			           }
			 });
			
			 
			        	
			        	
			 
		  
		 Constants.userInfo = new String[1];
		 Constants.userInfo[0] = "NULL";
		 
		 userManager();		 

		 for(String a : Constants.legend) {
			 
			 dataMap.put(getPath(a), (JsArray<LatLng>) JavaScriptObject.createArray().cast());
			 System.out.println("EXPECTED = " + getPath(a));
			 markerMap.put(getPath(a), new ArrayList<Marker>());
		 }
			
		 markerService.getMarks(getMarks);
		 Label text = new Label("Customize Map");
		 text.setStyleName(res.style().blackText());
		 checkBoxes.add(text);
		 
		 for(int i=0; i<Constants.legend.length; i++) {
			 
			 toShow[i] = 1;
	    	  final int temp = i;
	    	  
	    	  final CheckBox checkbox = new CheckBox(Constants.legend[i]);
	    	  checkbox.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					
					if(checkbox.getValue())
					toShow[temp] = 1;
					else toShow[temp] = 0;	
					
					clear();
			    	cluster.clearMarkers();
			        JsArray<LatLng> dataPoints = JavaScriptObject.createArray().cast();
			    	
				    for(int i=0; i<toShow.length; i++) {

				    	if(toShow[i] == 1) {
				    		
				    		System.out.println(getPath(Constants.legend[i]));
				    		cluster.addMarkers(markerMap.get(getPath(Constants.legend[i])));		
				    		JsArray<LatLng> temp = JavaScriptObject.createArray().cast();
				    		temp = dataMap.get(getPath(Constants.legend[i]));
				    		for(int j=0; j<temp.length(); j++) {
				    			
				    			System.out.println(temp.get(j));
				    			dataPoints.push(temp.get(j));
				    		}
				    	}
				    }	
				    show();
				    heatMapLayer.setData(dataPoints);
				}
	    	  });	
	    	  checkbox.setWidth("100%");
	    	  checkbox.setValue(true);
	    	  checkBoxes.add(checkbox);
	    	  
	    	  MenuItem item = new MenuItem(template.createItem(getPath(Constants.legend[i])+"_low.png",
	    			    									   SafeHtmlUtils.fromString(Constants.legend[i])), 
	    			    				   new Command() {	
	    		  
	    		  @Override
			      public void execute() {		    	  
			    	  
	    			  legendDetails.showWidget(temp);
			      }
	    	  });
	    	  item.setStylePrimaryName(res.style().menuItemLegend());
			  legend.addItem(item);
			  if(i!=Constants.legend.length-1)
				  legend.addSeparator();
		 }		 
		 
		 legendDetails.setAnimationEnabled(true);
		 legendDetails.showWidget(0);
		 togglePanel.setAnimationEnabled(true);
		 togglePanel.showWidget(0);
		 final AutocompleteOptions options = AutocompleteOptions.newInstance();
	     final Autocomplete autocomplete = Autocomplete.newInstance(input.getElement(), options);    
	     final InfoWindowOptions infoopts= InfoWindowOptions.newInstance();
	     final InfoWindow infowindow= InfoWindow.newInstance(infoopts);
	     autocomplete.addPlaceChangeHandler(new PlaceChangeMapHandler(){

			@Override
			public void onEvent(PlaceChangeMapEvent event) {
	    		PlaceResult place=autocomplete.getPlace();
	    		String address=place.getAddress_Components().get(0).getShort_Name();
	    		infowindow.setContent(place.getName()+", "+address);    	
	    		LatLng latLng = LatLng.newInstance(place.getGeometry().getLocation().getLatitude(), place.getGeometry().getLocation().getLongitude());
	    			
	    		map.setCenter(latLng);
	    	    map.setZoom(17); 				
			}
	     });
		 
		
		 
		
	  }
	  
	  private void reporting(String uri, final LatLng loc) {
		  
		if(uri.equals(null) || uri.equals("")) return;
  		Constants.thisSessionReport = true;
  	    System.out.println(uri);
  	    Constants.uri = uri;
  	    System.out.println("DETAILS= "+loginInfo.getNickname()+" "+
  	                       (new Date())+" "+
  	                       loc+" "+
  	                       Constants.uri);
  	    
  	    final GeocoderRequest request = GeocoderRequest.newInstance();
	    request.setLocation(loc);
	    Geocoder geocoder = Geocoder.newInstance();
	    geocoder.geocode(request, new GeocoderRequestHandler() {

			@Override
			public void onCallback(JsArray<GeocoderResult> results,
					GeocoderStatus status) {
				
				if(status.toString().equals("OK")) {
					
					address = results.get(0).getFormatted_Address();
					markerService.addMark(loc.getLatitude()+"`"+
				  			  loc.getLongitude()+"`"+
					          Constants.uri+"`"+   //type
							  "100.0"+"`"+    //strength
					          Constants.date+"`"+
							  Constants.info+"`"+   //user provided information
					          "0"+"`"   //thumbs up
							  +"0"+"`"  //thumbs down
					          +"0"+"`" //flags
					          +address, //address
					          loginInfo.getNickname(), addMarks);	
					addMarker(loc, 100.0);
					for(int k=0; k<results.length(); k++) {
						
						System.out.println(results.get(k).getFormatted_Address());
					}
				} 
				System.out.println(status.toString());
			}
	    });
	  }
	  
	  public static String getPath(String source) {
		  
			 String imageSource = new String(source);

	    	  String [] bullShit;
	    	  bullShit = imageSource.split(" ");
	    	  String out="";
	    	  
	    	  for(String a : bullShit) {
	    		  
	    		  	out+=a.toLowerCase();
	    	  }
	    	  return out;		  
	  }
	  
	  public void openMarkerDetails(LatLng location, String uri, double strength, String date, String info, int tups, int tdwns, int flags, String address2, long id) {
		  
		  Constants.dataPass = new bundle(location, uri, strength, date, info, tups, tdwns, flags, address2, id, "blank");
		  System.out.println(Constants.dataPass.id);
		  Report report = new Report();
		  report.setGlassEnabled(true);
		 
		  report.setStyleName(res.style().box2());
		  // report.show();
		  report.center();	
		  
	  }
	  
	  public interface MyTemplate extends SafeHtmlTemplates {
		  @Template("<table><tr><td><img src='{0}' height='30px' vertical-align='middle'/></td><td><span>{1}</span></td></tr></table>")
		  SafeHtml createItem(String imageSource, SafeHtml message);
	  }
	  
	  public final static class holder implements Comparable<holder>{
		  
		  String type, info, date;
		  double distance;
		  LatLng loc;
		  
		  public holder(String type, String info, String date, double distance, LatLng loc) {
			  
			  this.type = type;
			  this.info = info;
			  this.date = date;
			  this.distance = distance;
			  this.loc = loc;
		  }

		@Override
		public int compareTo(holder o) {
			
			return Double.compare(this.distance, o.distance);
		}
	  }
}
