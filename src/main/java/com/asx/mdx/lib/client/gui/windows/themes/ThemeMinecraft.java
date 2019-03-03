package com.asx.mdx.lib.client.gui.windows.themes;

import com.asx.mdx.lib.client.gui.windows.Window;

public class ThemeMinecraft extends Theme implements ITheme
{
    public ThemeMinecraft(String name)
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
        // RenderLib.drawBlockSide(43, 2, window.getX(), window.getY() - 16, window.getWidth(), 16, window.getWidth() / 50.0F, 0.5F);
    }

    public void drawTitleBar(Window window, int mouseX, int mouseY)
    {
        // RenderLib.drawBlockSide(1, 0, window.getX(), window.getY(), window.getWidth(), window.getHeight(), window.getWidth() / 50.0F, window.getHeight() / 50.0F);
    }

    public void drawCloseButton(Window window, int mouseX, int mouseY)
    {
        // RenderLib.drawBlockSide(46, 2, window.getX() + window.getWidth() - 14, window.getY() - 14, 12, 12);
    }
}
