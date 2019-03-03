package com.asx.mdx.lib.client.gui.windows.themes;

import org.lwjgl.opengl.GL11;

import com.asx.mdx.MDX;
import com.asx.mdx.lib.client.gui.windows.Window;
import com.asx.mdx.lib.client.gui.windows.WindowManager;
import com.asx.mdx.lib.util.Game;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class Theme implements ITheme
{
    private String themeName;

    public Theme(String name)
    {
        this.themeName = name;
    }

    public void drawWindow(Window window, int mouseX, int mouseY)
    {
        drawBackground(window, mouseX, mouseY);
        drawTitleBar(window, mouseX, mouseY);
        drawCloseButton(window, mouseX, mouseY);
        drawContents(window, mouseX, mouseY);
    }

    public void drawBackground(Window window, int mouseX, int mouseY)
    {
        ;
    }

    public void drawTitleBar(Window window, int mouseX, int mouseY)
    {
        ;
    }

    public void drawCloseButton(Window window, int mouseX, int mouseY)
    {
        ;
    }

    public void drawContents(Window window, int mouseX, int mouseY)
    {
        int x;

        int y;

        if (getWindowManager() != null)
        {
            getWindowManager().drawCenteredString(Game.minecraft().fontRenderer, window.getTitle(), window.getX() + window.getWidth() / 2, window.getY() - 12, 16777215);
            window.drawWindowContents();

            x = -1;
            y = -1;

            if (getWindowManager().getTopWindow(mouseX, mouseY) == window)
            {
                x = mouseX;
                y = mouseY;
            }

            for (GuiButton button : window.getButtonList())
            {
                button.drawButton(Game.minecraft(), x, y, Game.partialTicks());
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
            }

        }
        else
        {
            MDX.log().info("Window Manager returned null.");
        }
    }

    public String getName()
    {
        return this.themeName;
    }

    public void setName(String themeName)
    {
        this.themeName = themeName;
    }

    public WindowManager getWindowManager()
    {
        return MDX.windows().getWindowManager();
    }
}