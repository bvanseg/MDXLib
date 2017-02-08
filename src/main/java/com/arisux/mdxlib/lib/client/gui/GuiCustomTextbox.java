package com.arisux.mdxlib.lib.client.gui;

import javax.vecmath.Vector2d;

import com.arisux.mdxlib.lib.client.GUIElementTracker;
import com.arisux.mdxlib.lib.game.Game;

import net.minecraft.client.gui.GuiTextField;

public class GuiCustomTextbox extends GuiTextField implements IGuiElement
{
    protected GuiCustomScreen parentScreen;
    protected IAction action;
    protected boolean trackInput;
    protected boolean isRendered;
    protected long lastRendered;

    public GuiCustomTextbox(GuiCustomScreen parentScreen, int x, int y, int width, int height)
    {
        this(x, y, width, height);
        this.parentScreen = parentScreen;
        this.parentScreen.customTextfieldList.add(this);
        this.trackInput = true;
    }

    public GuiCustomTextbox(int x, int y, int width, int height)
    {
        super(Game.fontRenderer(), x, y, width, height);
        this.xPosition = x;
        this.yPosition = y;
        this.width = width;
        this.height = height;
        this.trackElement();
    }

    @Override
    public void updateElement()
    {
        if (this.isRendered())
        {
            this.setVisible(true);
        }
        else
        {
            this.setVisible(false);
        }
    }

    @Override
    public void drawTextBox()
    {
        super.drawTextBox();
        this.lastRendered = System.currentTimeMillis();
    }
    
    @Override
    public boolean isEnabled()
    {
        return true;
    }
    
    @Override
    public long lastRenderTime()
    {
        return this.lastRendered;
    }
    
    @Override
    public boolean isRendered()
    {
        long currentTime = System.currentTimeMillis();
        
        if (currentTime > 100 && this.lastRenderTime() > 100)
        {
            return currentTime - this.lastRenderTime() < 500;
        }
        
        return false;
    }

    @Override
    public void trackElement()
    {
        GUIElementTracker.instance.track(this);
    }

    @Override
    public void stopTracking()
    {
        GUIElementTracker.instance.stopTracking(this);
    }

    @Override
    public boolean isMouseInElement(Vector2d mousePosition)
    {
        int mouseX = (int) mousePosition.x;
        int mouseY = (int) mousePosition.y;
        
        return this.isEnabled() && this.getVisible() && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }
    
    @Override
    public boolean canTrackInput()
    {
        return this.isEnabled() && trackInput && getVisible();
    }

    @Override
    public boolean setTrackInput(boolean trackInput)
    {
        return this.trackInput = trackInput;
    }

    @Override
    public void mousePressed(Vector2d mousePosition)
    {
        super.mouseClicked((int) mousePosition.x, (int) mousePosition.y, 0);
    }

    @Override
    public void mouseReleased(Vector2d mousePosition)
    {
        ;
    }

    @Override
    public void mouseDragged(Vector2d mousePosition)
    {
        ;
    }

    @Override
    public IAction getAction()
    {
        return action;
    }

    @Override
    public IGuiElement setAction(IAction action)
    {
        this.action = action;
        return this;
    }
}