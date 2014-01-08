package com.fort.project.neighbourhoodwatch.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface GwtMapsResources extends ClientBundle {

   public interface MyCss extends CssResource {
	  
      String blackText();
      
      String buttonbox();
      
      String slogan();
      
      String boxtxt();
      
      String footerback();
      
      String box1();
      
      String box2();
      
      String bigbtn();
      
      String footer();
      
      String bigbtn2();
      
      String homeback();
      
      String button2();
      
      String button3();
      
      String button4();
      
      String button5();
      
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
           
      String menuItem2();
      
      String menuItem3();
  
      String carbon();
      
      String trans_white();
      
      String menuLegend(); String menu();
      
      String menuItem(); String menuItemLegend(); String menuItemDetails();
      
      String heading();
            
      String menuItemTop();
   }

   @Source("Home.css")
   MyCss style();
   
   @Source("aboutustxt.txt")
   TextResource text();
   
   @Source("privacytxt.txt")
   TextResource text1();
   
   @Source("workstxt.txt")
   TextResource text2();
   
   @Source("Home-icon.png")
   ImageResource homebtn();
   @Source("Home-icon2.png")
   ImageResource homebtn2();
   @Source("pf_logo.png")
   ImageResource logoimg();
   
   @Source("up0.png")
   ImageResource up0();
   @Source("up1.png")
   ImageResource up1();
   @Source("down0.png")
   ImageResource down0();
   @Source("down1.png")
   ImageResource down1();
   @Source("flagon.png")
   ImageResource flagon();
   @Source("flagoff.png")
   ImageResource flagoff();
}