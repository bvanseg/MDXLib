package com.arisux.mdxlib.lib.client.gui;

import javax.vecmath.Vector2d;

public interface IGuiElement
{
    public boolean isEnabled();
    
    public boolean isRendered();
    
    public long lastRenderTime();
    
    public void updateElement();

    public void mousePressed(Vector2d mousePosition);

    public void mouseReleased(Vector2d mousePosition);

    public void mouseDragged(Vector2d mousePosition);

    public IAction getAction();

    public IGuiElement setAction(IAction action);
    
    public void trackElement();
    
    public void stopTracking();
    
    public boolean canTrackInput();
    
    public boolean setTrackInput(boolean trackInput);
    
    public boolean isMouseInElement(Vector2d mousePosition);
}
