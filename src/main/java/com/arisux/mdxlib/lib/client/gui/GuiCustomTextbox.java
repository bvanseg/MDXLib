package com.arisux.mdxlib.lib.client.gui;

import com.arisux.mdxlib.lib.game.Game;

import net.minecraft.client.gui.GuiTextField;

public class GuiCustomTextbox extends GuiTextField
{
    public GuiCustomScreen parentScreen;
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
}