package com.fort.project.neighbourhoodwatch.client;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.DeckPanel;

public class SlideAnimation extends Animation
{
    private final DeckPanel widget;
    private boolean opening;
    private int toShow = 0;

    public SlideAnimation(DeckPanel widget)
    {
        this.widget = widget;
    }

    @Override
    protected void onComplete()
    {
        DOM.setStyleAttribute(this.widget.getElement(), "height", "auto");
        
        if(! opening) {
        	
            this.widget.setVisible(false);
            widget.showWidget(toShow);
            this.run(500);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        opening = ! this.widget.isVisible();

        if(opening)
        {
            DOM.setStyleAttribute(this.widget.getElement(), "height", "0px");
            this.widget.setVisible(true);
        }

    }

    @Override
    protected void onUpdate(double progress)
    {
        int scrollHeight = DOM.getElementPropertyInt(this.widget.getElement(), "scrollHeight");
        int height = (int) (progress * scrollHeight);
        if( !opening )
        {
            height = scrollHeight - height;
        }
        height = Math.max(height, 1);
        DOM.setStyleAttribute(this.widget.getElement(), "height", height + "px");
    }

	public void reanimate(int temp) {
		
		toShow=temp;		
	}
}