package com.fort.project.neighbourhoodwatch.client;

import java.util.Date;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ShowRangeEvent;
import com.google.gwt.event.logical.shared.ShowRangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.fort.project.neighbourhoodwatch.shared.Constants;


public class MainDialogBox extends DialogBox {
	
	  String uri = "";
	  static Date d;
	  
	  @UiField SimplePanel mainPanel;
	  
	  @UiField Button okButton, closeButton;
	  
	  @UiField MenuBar menuBar;
	  
	  @UiField DatePicker date;
	  
	  @UiField TextBox input;
	  
	  @UiField MenuItem theft, harassment, unsafe, assault, landSlide, bio, gang, bribe, arson, sub;
	  
	  @UiHandler("okButton")	  
	  void onSubmit(ClickEvent e) {
		  
		  if(uri.equals(null) || uri.equals("") || date.equals(null)) return;
		  Constants.uri = uri;
		  String parser = date.getValue().toString();
		  String [] split = parser.split(" ");
		  
		  Constants.date = split[0]+", "+split[1]+" "+split[2]+" "+split[5];
		  Constants.date.trim();
		  if(Constants.date.equals(null ) || Constants.date.equals("")) Constants.date = "undisclosed" ;
		  
		  Constants.info=input.getText();
		  Constants.info.trim();
		  if(Constants.info.equals(null ) || Constants.info.equals("")) Constants.info = "undisclosed" ;
		  
		  System.out.println("BOX STUFF= "+Constants.date + "YO" + Constants.info);
		  this.hide(true);
	  }
	  
	  @UiHandler("closeButton")
	  void onClose(ClickEvent e) {
		  
		  this.hide(false);
	  }


	  interface Binder extends UiBinder<Widget, MainDialogBox> {}

	  public MainDialogBox() {
		  
		  super.setWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));	
		  input.setVisibleLength(100);		  
		  date.addValueChangeHandler(new ValueChangeHandler<Date>() {
		      @Override
		      public void onValueChange(final ValueChangeEvent<Date> dateValueChangeEvent) {
		          if (dateValueChangeEvent.getValue().after(today())) {
		              date.setValue(today(), false);
		          }
		      }
		  });
		  
		  date.addShowRangeHandler(new ShowRangeHandler<Date>() {
		      @Override
		      public void onShowRange(final ShowRangeEvent<Date> dateShowRangeEvent) {
		          final Date today = today();
		          d = zeroTime(dateShowRangeEvent.getStart());
		          while (d.after(today))
		          {
		              date.setTransientEnabledOnDates(false, d);
		              nextDay(d);
		          }
		      }
		  });
		  initialise();
	  }
	  
	  private static Date today() {
	      return zeroTime(new Date());
	  }

	  /** this is important to get rid of the time portion, including ms */
	  private static Date zeroTime(final Date date) {
	      return DateTimeFormat.getFormat("yyyyMMdd").parse(DateTimeFormat.getFormat("yyyyMMdd").format(date));
	  }

	  private static void nextDay(final Date date) {
	     com.google.gwt.user.datepicker.client.CalendarUtil.addDaysToDate(d, 1);
	  }
	  
	  public void initialise() {
		  
		  
		  menuBar.setTitle("What do you want to report ?");
		  date.setTitle("When did this mishap occur ?");
		  input.setTitle("Give us some more details");
		  
		  Command cmdBtnTheft = new Command() {
			  public void execute() {
		    		
		    		uri = theft.getText().replaceAll(" ", "").toLowerCase();
		    		sub.setText(theft.getText());
		    	}
		  };
		  theft.setScheduledCommand(cmdBtnTheft);
		  ////////////---------------////////////
		  Command cmdBtnHarassment = new Command() {
			  public void execute() {
		    		
		    		uri = harassment.getText().replaceAll(" ", "").toLowerCase();
		    		sub.setText(harassment.getText());
		    	}
		  };
		  harassment.setScheduledCommand(cmdBtnHarassment);
		  ////////////---------------////////////
		  Command cmdBtnUnsafe = new Command() {
			  public void execute() {
		    		
		    		uri = unsafe.getText().replaceAll(" ", "").toLowerCase();
		    		sub.setText(unsafe.getText());
		    	}
		  };
		  unsafe.setScheduledCommand(cmdBtnUnsafe);		  
		  ////////////---------------////////////
		  Command cmdBtnAssault = new Command() {
			  public void execute() {
		    		
		    		uri = assault.getText().replaceAll(" ", "").toLowerCase();
		    		sub.setText(assault.getText());
		    	}
		  };
		  assault.setScheduledCommand(cmdBtnAssault);
		  ////////////---------------////////////
		  Command cmdBtnLandslide = new Command() {
			  public void execute() {
		    		
		    		uri = landSlide.getText().replaceAll(" ", "").toLowerCase();
		    		sub.setText(landSlide.getText());
		    	}
		  };
		  landSlide.setScheduledCommand(cmdBtnLandslide);
		  ////////////---------------////////////
		  Command cmdBtnBio = new Command() {
			  public void execute() {
		    		
		    		uri = bio.getText().replaceAll(" ", "").toLowerCase();
		    		sub.setText(bio.getText());
		    	}
		  };
		  bio.setScheduledCommand(cmdBtnBio);
		  ////////////---------------////////////
		  Command cmdBtnGang = new Command() {
			  public void execute() {
		    		
		    		uri = gang.getText().replaceAll(" ", "").toLowerCase();
		    		sub.setText(gang.getText());
		    	}
		  };
		  gang.setScheduledCommand(cmdBtnGang);
		  ////////////---------------////////////
		  Command cmdBtnBribe = new Command() {
			  public void execute() {
		    		
		    		uri = bribe.getText().replaceAll(" ", "").toLowerCase();
		    		sub.setText(bribe.getText());
		    	}
		  };
		  bribe.setScheduledCommand(cmdBtnBribe);
		  ////////////---------------////////////
		  Command cmdBtnArson = new Command() {
			  public void execute() {
		    		
		    		uri = arson.getText().replaceAll(" ", "").toLowerCase();
		    		sub.setText(arson.getText());
		    	}
		  };
		  arson.setScheduledCommand(cmdBtnArson);
		  
	  }


	  @Override
	  public void setWidget(Widget widget) {
	    mainPanel.setWidget(widget);
	  }

	  @Override
	  public void center() {
	    super.center();
	    okButton.setFocus(true);
	  }	 
	}