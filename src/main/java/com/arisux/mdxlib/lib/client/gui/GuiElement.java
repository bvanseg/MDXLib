package com.arisux.mdxlib.lib.client.gui;

import javax.vecmath.Vector2d;

import com.arisux.mdxlib.lib.client.GuiElementTrackingModule;

import net.minecraft.client.gui.GuiButton;

public abstract class GuiElement extends GuiButton
{
    public GuiElement(int id, int x, int y, int width, int height, String displayName)
    {
        super(id, x, y, width, height, displayName);
    }

    public abstract boolean isActive();

    public abstract void mousePressed(Vector2d mousePosition);

    public abstract void mouseReleased(Vector2d mousePosition);

    public abstract void mouseDragged(Vector2d mousePosition);

    public abstract IAction getAction();

    public abstract GuiElement setAction(IAction action);
    
    public void add()
    {
        GuiElementTrackingModule.instance.add(this);
    }
    
    public void remove()
    {
        GuiElementTrackingModule.instance.remove(this);
    }
    
    public boolean isMouseInElement(Vector2d mousePosition)
    {
        int mouseX = (int) mousePosition.x;
        int mouseY = (int) mousePosition.y;
        
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }
}