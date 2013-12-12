	package com.fort.project.neighbourhoodwatch.client;

import java.util.Date;
import com.fort.project.neighbourhoodwatch.shared.Constants;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.Position.Coordinates;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.events.place.PlaceChangeMapEvent;
import com.google.gwt.maps.client.events.place.PlaceChangeMapHandler;
import com.google.gwt.maps.client.placeslib.Autocomplete;
import com.google.gwt.maps.client.placeslib.AutocompleteOptions;
import com.google.gwt.maps.client.placeslib.PlaceResult;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import java.util.ArrayList;
import com.google.gwt.ajaxloader.client.ArrayHelper;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.events.click.ClickMapEvent;
import com.google.gwt.maps.client.events.click.ClickMapHandler;
import com.google.gwt.maps.client.overlays.Animation;
import com.google.gwt.maps.client.overlays.InfoWindow;
import com.google.gwt.maps.client.overlays.InfoWindowOptions;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.maps.client.visualizationlib.HeatMapLayer;
import com.google.gwt.maps.client.visualizationlib.HeatMapLayerOptions;
import com.google.gwt.maps.utility.markerclustererplus.client.MarkerClusterer;
import com.google.gwt.maps.utility.markerclustererplus.client.MarkerClustererOptions;




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
	
	private MapWidget map;
	private JsArray<LatLng> dataPoints= JavaScriptObject.createArray().cast();
	private MarkerClusterer mc;
	private ArrayList<Marker> al = new ArrayList<Marker>();
	public LoginInfo loginInfo = null;
	private LatLng initialLocation;	
    String url= "", link="";    
    int t;
	private MainDialogBox dialogBox = new MainDialogBox();
    
	private MarkerServiceAsync markerService = GWT.create(MarkerService.class);
	private UserServiceAsync userService = GWT.create(UserService.class);
	private MyTemplate template = GWT.create(MyTemplate.class);
	
	@UiField
	ScrollPanel scroll;		
	@UiField(provided = true)
	final GwtMapsResources res;	
	@UiField
	TextBox input;	
	@UiField
	SimpleLayoutPanel map_canvas;	
	@UiField
	MenuBar legend;
	@UiField
	Button works, about, contact, signin, privacy, about2, contact2, blog, android, support;
	@UiField
	DeckPanel legendDetails;
	@UiField
	Button toggle;
	
	
	
	@UiHandler("signin")
	void singin(ClickEvent e) {

		Window.open(link, "replace", "");
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
					
					scroll.add(menu.asWidget());
					scroll.setHeight("250px");
					scroll.onResize();
					scroll.setVisible(true);
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
						  Constants.symbols.get(i).info);
			}
			mc.addMarkers(al);
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
			userService.getDate(getDate);
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
				userService.getUser(loginInfo.getNickname(), getUserDetails);
			
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
		
		System.out.println("GwtMaps");
		
		initWidget(uiBinder.createAndBindUi(this));		
		// Building the map
		
		MapOptions myOptions = MapOptions.newInstance();
	    myOptions.setZoom(17);
	    myOptions.setStreetViewControl(true);	   
	    myOptions.setMapTypeId(MapTypeId.ROADMAP);
	    myOptions.setMinZoom(5);		
	    map = new MapWidget(myOptions);
	    
	    final MarkerClustererOptions mo= MarkerClustererOptions.newInstance();
	    mc=MarkerClusterer.newInstance(map,mo);
	    
	    map_canvas.add(map);
	    
	    final HeatMapLayerOptions options = HeatMapLayerOptions.newInstance();
	    options.setOpacity(1);
	    options.setRadius(5);
	    options.setGradient(getSampleGradient());
	    

	    final HeatMapLayer heatMapLayer = HeatMapLayer.newInstance(options);
	    
	    
	    
	    heatMapLayer.setData(dataPoints);
	    
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
	                dialogBox.center();
	        	}
	        }
	    });	
	    
	    //Map built

	    initialise();

		final LatLng myLatLng = LatLng.newInstance(28.60753,77.03505);
	    if (Geolocation.isSupported()) {                // GEOLOCATION STARTS HERE !
		       
		      Geolocation.getIfSupported().getCurrentPosition(
		          new Callback<Position, PositionError>() {
		
		            @Override
		            public void onSuccess(Position result) {
		            	
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
				        map.setCenter(initialLocation);		              
			        }
		
		            public void onFailure(PositionError reason) {
		            	
		            	map.setZoom(14);
		            	map.setCenter(myLatLng);
		            }
		 
			          });
			} else {
				
				initialLocation = myLatLng;
				myOptions.setCenter(myLatLng);
			}
	    t=0;
	    toggle.addClickHandler(new ClickHandler(){
	        @Override
	        public void onClick(ClickEvent event) {
	        	heatMapLayer.setMap(heatMapLayer.getMap() != null ? null : map);
	        	if(t==0)
	        	{
	        	clear();
	        	mc.removeMarkers(al);
	        	t=1;
	        	toggle.setText("HeatMap View");
	        	}
	        	else
	        	{
	        	show();
	        	mc.addMarkers(al);
	    	    t=0;
	    	    toggle.setText("Clustered View");
	        	}
	           }

	    });
	  }
	
	private void clear()
	{
		for(int i=0;i<al.size();i++)
    	{
    		al.get(i).setVisible(false);
    	}
	}
	private void show()
	{
		for(int i=0;i<al.size();i++)
    	{
    		al.get(i).setVisible(true);
    	}
	}
	private JsArrayString getSampleGradient() {
		    String[] sampleColors = new String[] { "rgba(0, 255, 255, 0)", "rgba(0, 255, 255, 1)", "rgba(0, 191, 255, 1)",
		        "rgba(0, 127, 255, 1)", "rgba(0, 63, 255, 1)", "rgba(0, 0, 255, 1)", "rgba(0, 0, 223, 1)",
		        "rgba(0, 0, 191, 1)", "rgba(0, 0, 159, 1)", "rgba(0, 0, 127, 1)", "rgba(63, 0, 91, 1)", "rgba(127, 0, 63, 1)",
		        "rgba(191, 0, 31, 1)", "rgba(255, 0, 0, 1)" };
		    return ArrayHelper.toJsArrayString(sampleColors);
		  }
	
	  private void addMarker(LatLng location, final String uri, double strength, final String date, final String info) {
		  
		  	MarkerOptions newMarkerOpts = MarkerOptions.newInstance();
		    newMarkerOpts.setPosition(location);
		    newMarkerOpts.setMap(map);
		    newMarkerOpts.setTitle(Constants.retranslate(uri));
		    newMarkerOpts.setDraggable(false);	    
		    String temp = "_low.png";
		    if(strength >= 90) temp = "_extreme.png";
		    else if(strength >= 60) temp = "_high.png";
		    else if(strength >= 30) temp = "_medium.png";
		    else temp = "_low.png";
		    newMarkerOpts.setIcon(url + uri + temp);		    
		    System.out.println("Trying to create marker #SERVER");
		    final Marker marker = Marker.newInstance(newMarkerOpts);
		    al.add(marker);
		    
		    dataPoints.push(location);
		    marker.addClickHandler(new ClickMapHandler() {

	            @Override
	            public void onEvent(ClickMapEvent event) {
	            	
	            	MarkerDialogBox dialogBox = new MarkerDialogBox(date, info, uri);
	                dialogBox.setGlassEnabled(true);
	                dialogBox.setText(Constants.retranslate(uri));	
	                dialogBox.center();
	            }
	        });
		    
	  }
	  
	  private void addMarker(LatLng location, double strength) {
		  
		  MarkerOptions newMarkerOpts = MarkerOptions.newInstance();
		  newMarkerOpts.setPosition(location);
		  newMarkerOpts.setMap(map);
		  newMarkerOpts.setTitle(Constants.retranslate(Constants.uri));
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
	            	
	            	marker.setAnimation(null);
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
	  
	  private void initialise() {	

		 Constants.userInfo = new String[1];
		 Constants.userInfo[0] = "NULL";
		 
		 userManager();
			
		 markerService.getMarks(getMarks);
		 
		 final SlideAnimation animation = new SlideAnimation(legendDetails);
		 
		 for(int i=0; i<Constants.legend.length; i++) {
			 

	    	  final int temp = i;
   	  
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
		 
		 dialogBox.setGlassEnabled(true);
		 dialogBox.setText("Enter Details"); 
		 dialogBox.addCloseHandler(new CloseHandler<PopupPanel>() {
			
			@Override
			public void onClose(CloseEvent<PopupPanel> event1) {
				
				if(event1.isAutoClosed())
					reporting(Constants.uri, Constants.mapClick);
			}
		 }); 
	  }
	  
	  private void reporting(String uri, LatLng loc) {
		  
		if(uri.equals(null) || uri.equals("")) return;
  		Constants.thisSessionReport = true;
  	    System.out.println(uri);
  	    Constants.uri = uri;
  	    System.out.println("DETAILS= "+loginInfo.getNickname()+" "+
  	                       (new Date())+" "+
  	                       loc+" "+
  	                       Constants.uri);
  	    
		markerService.addMark(loc.getLatitude()+"`"+
				  			  loc.getLongitude()+"`"+
					          Constants.uri+"`"+
							  "100.0"+"`"+Constants.date+"`"+Constants.info, loginInfo.getNickname(), addMarks);	
		addMarker(loc, 100.0);
	  }
	  
	  private String getPath(String source) {
		  
			 String imageSource = new String(source);

	    	  String [] bullShit;
	    	  bullShit = imageSource.split(" ");
	    	  String out="";
	    	  
	    	  for(String a : bullShit) {
	    		  
	    		  	out+=a.toLowerCase();
	    	  }
	    	  return out;		  
	  }
	  
	  public interface MyTemplate extends SafeHtmlTemplates {
		  @Template("<table><tr><td><img src='{0}' height='30px' vertical-align='middle'/></td><td><span>{1}</span></td></tr></table>")
		  SafeHtml createItem(String imageSource, SafeHtml message);
	  }
}
