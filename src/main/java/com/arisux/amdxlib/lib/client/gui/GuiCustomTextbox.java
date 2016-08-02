package com.arisux.amdxlib.lib.client.gui;

import javax.vecmath.Vector2d;

import com.arisux.amdxlib.lib.client.render.Screen;
import com.arisux.amdxlib.lib.game.Game;

import net.minecraft.client.gui.GuiTextField;

public class GuiCustomTextbox extends GuiTextField implements IGuiElement
{
    public GuiCustomScreen parentScreen;
    private IAction action;
    private long lastDrawTime;

    public GuiCustomTextbox(GuiCustomScreen parentScreen, int x, int y, int width, int height)
    {
        this(x, y, width, height);
        this.parentScreen = parentScreen;
        this.parentScreen.customTextfieldList.add(this);
    }

    public GuiCustomTextbox(int x, int y, int width, int height)
    {
        super(Game.fontRenderer(), x, y, width, height);
        this.xPosition = x;
        this.yPosition = y;
        this.width = width;
        this.height = height;
    }

    public long getLastDrawTime()
    {
        return lastDrawTime;
    }

    @Override
    public void drawTextBox()
    {
        super.drawTextBox();
        this.lastDrawTime = System.currentTimeMillis();
    }

    @Override
    public boolean isActive()
    {
        Vector2d mousePosition = Screen.scaledMousePosition();
        int mouseX = (int) mousePosition.x;
        int mouseY = (int) mousePosition.y;
        return mouseX >= (xPosition) && mouseX <= (xPosition + width) && mouseY >= (yPosition) && mouseY <= (yPosition + height);
    }

    @Override
    public void mousePressed(Vector2d mousePosition)
    {
        ;
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