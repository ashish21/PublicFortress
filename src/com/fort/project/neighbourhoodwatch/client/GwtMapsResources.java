package com.fort.project.neighbourhoodwatch.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface GwtMapsResources extends ClientBundle {

   public interface MyCss extends CssResource {
	  
	   
	String togglebutton();
	   String blackText();

     
      String redText();

      String button();
      String button2();
      String box();
      
      String background();
      
      String fortress();
      
      String silver();
      
      String carbon();
      
      String trans_white();
      
      String menu();
      
      String menuItem();
      
      String heading();
   }

   @Source("GwtMaps.css")
   MyCss style();
   
   @Source("aboutustext.txt")
   TextResource text();
   
   @Source("privacytext.txt")
   TextResource text1();
   
   @Source("g+button.png")
   ImageResource logo();
   @Source("g+button2.png")
   ImageResource logo2();
   @Source("home2.png")
   ImageResource homebtn();
}