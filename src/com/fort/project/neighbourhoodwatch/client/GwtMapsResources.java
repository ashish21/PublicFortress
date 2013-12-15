package com.fort.project.neighbourhoodwatch.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface GwtMapsResources extends ClientBundle {

   public interface MyCss extends CssResource {
	  
      String blackText();
      
      String bigbtn();
      
      String footer();
      
      String bigbtn2();
      
      String homeback();
      
      String button2();
      
      String buttonh();
      
      String white();
      
      String newfont();
   
      String lowerback();
      
      String redText();

      String button();

      String box();
      
      String background();
      
      String fortress();
      
      String silver();
      
      String carbon();
      
      String trans_white();
      
      String menuLegend(); String menu();
      
      String menuItem(); String menuItemLegend(); String menuItemDetails();
      
      String heading();
            
      String menuItemTop();
   }

   @Source("Home.css")
   MyCss style();
   
   @Source("aboutustext.txt")
   TextResource text();
   
   @Source("privacytext.txt")
   TextResource text1();
   
   @Source("Home-icon.png")
   ImageResource homebtn();
   @Source("Home-icon2.png")
   ImageResource homebtn2();
}