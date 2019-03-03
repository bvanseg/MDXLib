package com.asx.mdx.lib.client.gui.windows.themes;

import com.asx.mdx.lib.client.gui.windows.Window;
import com.asx.mdx.lib.client.gui.windows.WindowManager;
import com.asx.mdx.lib.client.util.Draw;

public class ThemeDefault extends Theme implements ITheme
{
    public ThemeDefault(String name)
    {
        super(name);
        setName(name);
    }

    public void drawWindow(Window window, int mouseX, int mouseY)
    {
        super.drawWindow(window, mouseX, mouseY);
    }

    public void drawBackground(Window window, int mouseX, int mouseY)
    {
        Draw.drawGradientRect(window.getX(), window.getY(), window.getWidth(), window.getHeight(), -13421773, -576017750);
    }

    public void drawTitleBar(Window window, int mouseX, int mouseY)
    {
        getWindowManager();
        WindowManager.drawRect(window.getX(), window.getY() - 16, window.getX() + window.getWidth(), window.getY(), -577136231);
    }

    public void drawCloseButton(Window window, int mouseX, int mouseY)
    {
        Draw.drawString("x", window.getX() + window.getWidth() - 12, window.getY() - 14, 16777215, false);
        Draw.drawRect(window.getX() + window.getWidth() - 17, window.getY() - 16, 15, 14, -2012147439);
    }
}