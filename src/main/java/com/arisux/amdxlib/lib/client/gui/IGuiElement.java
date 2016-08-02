package com.arisux.amdxlib.lib.client.gui;

import javax.vecmath.Vector2d;

public interface IGuiElement
{
    public boolean isActive();

    public void mousePressed(Vector2d mousePosition);

    public void mouseReleased(Vector2d mousePosition);

    public void mouseDragged(Vector2d mousePosition);

    public IAction getAction();

    public IGuiElement setAction(IAction action);
}