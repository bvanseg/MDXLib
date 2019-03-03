package com.asx.mdx.lib.client.gui.windows;

import java.util.ArrayList;

import com.asx.mdx.MDX;

public abstract class Window implements IWindow
{
    protected WindowManager                                 manager;
    protected int                                           xPos;
    protected int                                           yPos;
    protected int                                           width;
    protected int                                           height;
    private String                                          title;
    private String                                          id;
    protected ArrayList<net.minecraft.client.gui.GuiButton> buttonList = new ArrayList<net.minecraft.client.gui.GuiButton>();

    public Window(String id, WindowManager manager, String title, int xPos, int yPos, int width, int height)
    {
        this.id = id;
        this.manager = manager;
        this.title = title;
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;

        MDX.windows().addWindow(this);
    }

    public abstract void drawWindowContents();

    public abstract void onButtonPress(net.minecraft.client.gui.GuiButton paramGuiButton);

    public abstract void keyTyped(char paramChar, int paramInt);

    public void onClose()
    {
        this.manager.getWindowAPI().getWindows().remove(this);
    }

    public void setPosition(int x, int y)
    {
        this.xPos = x;
        this.yPos = y;
    }

    public void setDimensions(int w, int h)
    {
        this.width = w;
        this.height = h;
    }

    public void setTitle(String t)
    {
        this.title = t;
    }

    public int getX()
    {
        return this.xPos;
    }

    public int getY()
    {
        return this.yPos;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public String getTitle()
    {
        return this.title;
    }

    public ArrayList<net.minecraft.client.gui.GuiButton> getButtonList()
    {
        return this.buttonList;
    }

    public boolean isTopWindow()
    {
        if (this.manager.getWindowAPI().getWindows().size() > 0)
        {
            return ((Window) this.manager.getWindowAPI().getWindows().get(0)).equals(this);
        }

        return false;
    }

    public String getID()
    {
        return this.id;
    }
}