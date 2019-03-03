package com.asx.mdx.lib.client.gui.windows;

import java.io.IOException;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.asx.mdx.MDX;
import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.client.util.ScaledResolution;
import com.asx.mdx.lib.client.util.Screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WindowManager extends GuiScreen
{
    private WindowAPI   windowapi;
    protected GuiScreen parentScreen;
    private Window      resetWindow;
    private Window      draggingWindow      = null;
    private Window      lastActiveWindow        = null;
    private int         mouseXLast;
    private int         mouseYLast;
    private boolean     resizeEnabled       = false;
    private boolean     resizeIgnoreBorders = false;

    public WindowManager(WindowAPI windowapi, GuiScreen parentScreen)
    {
        this.windowapi = windowapi;
        this.parentScreen = parentScreen;
    }

    public void initGui()
    {
        this.buttonList.clear();
    }

    protected void keyTyped(char par1, int par2)
    {
        if (par2 == 1)
        {
            Minecraft.getMinecraft().currentScreen = this.parentScreen;
        }
        if (this.windowapi.getWindows().size() > 0)
        {
            ((Window) this.windowapi.getWindows().get(0)).keyTyped(par1, par2);
        }
    }

    public Window getTopWindow(int mouseX, int mouseY)
    {
        Window window = null;

        for (Window w : this.windowapi.getWindows())
        {
            if ((mouseX > w.getX()) && (mouseX < w.getX() + w.getWidth()) && (mouseY > w.getY() - 16) && (mouseY < w.getY() + w.getHeight()))
            {
                window = w;
            }
        }
        
        return window;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int buttonId) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, buttonId);

        Window active = lastActiveWindow = getTopWindow(mouseX, mouseY);

        /** close button **/
        if (active != null)
        {
            if ((mouseX > active.getX() + active.getWidth() - 14) && (mouseX < active.getX() + active.getWidth() - 2) && (mouseY < active.getY() - 2) && (mouseY > active.getY() - 14))
            {
                active.onClose();
            }

            /** Maximize Button **/
            int maximizeButtonSize = 16;
            int maximizeButtonX = active.getX() + active.getWidth() - (maximizeButtonSize * 2);
            int maximizeButtonY = active.getY() - maximizeButtonSize;

            if (mouseX > maximizeButtonX && mouseX < maximizeButtonX + maximizeButtonSize && mouseY > maximizeButtonY && mouseY < maximizeButtonY + maximizeButtonSize)
            {
                ScaledResolution res = Screen.scaledDisplayResolution();
                active.setPosition(0, 0 + 16);
                active.setDimensions(res.getScaledWidth(), res.getScaledHeight() - 16);
            }

            /** Resize Button **/
            int resizeButtonSize = 16;
            int resizeButtonX = active.getX() + active.getWidth() - (resizeButtonSize * 3);
            int resizeButtonY = active.getY() - resizeButtonSize;

            if (mouseX > resizeButtonX && mouseX < resizeButtonX + resizeButtonSize && mouseY > resizeButtonY && mouseY < resizeButtonY + resizeButtonSize)
            {
                this.setResizeEnabled(!this.isResizeEnabled());
            }
        }

        for (int x = 0; x < this.windowapi.getWindows().size(); x++)
        {
            Window window = (Window) this.windowapi.getWindows().get(x);

            if (active == window)
            {
                this.resetWindow = window;

                if ((mouseX > window.getX()) && (mouseX < window.getX() + window.getWidth()) && (mouseY > window.getY() - 16) && (mouseY < window.getY()))
                {
                    this.draggingWindow = window;
                    this.lastActiveWindow = window;
                    this.mouseXLast = mouseX;
                    this.mouseYLast = mouseY;
                }

                for (GuiButton button : window.getButtonList())
                {
                    if (button.mousePressed(this.mc, mouseX, mouseY))
                    {
                        button.playPressSound(this.mc.getSoundHandler());
                        window.onButtonPress(button);
                    }
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if (this.parentScreen != null)
        {
            this.parentScreen.drawScreen(mouseX, mouseY, partialTicks);
        }
        else
        {
            // MDX.log().warn("Parent screen returned null.");
        }

        Window active = getTopWindow(mouseX, mouseY);

        if (MDX.windows().getWindowManager().isResizeEnabled())
        {
            if (Mouse.isButtonDown(0))
            {
                if (active != null)
                {
                    /** Resize Zone Button **/
                    int zoneButtonSize = 10;
                    int zoneButtonX = active.getX() + active.getWidth() - (zoneButtonSize);
                    int zoneButtonY = active.getY() + active.getHeight() - (zoneButtonSize);

                    if (mouseX > zoneButtonX && mouseX < zoneButtonX + zoneButtonSize && mouseY > zoneButtonY && mouseY < zoneButtonY + zoneButtonSize)
                    {
                        int newWidth = mouseX - active.getX() + 5;
                        int newHeight = mouseY - active.getY() + 5;

                        resizeIgnoreBorders = true;
                        active.setDimensions(newWidth, newHeight);
                    }
                }

                if (lastActiveWindow != null && resizeIgnoreBorders)
                {
                    int newWidth = mouseX - lastActiveWindow.getX() + 5;
                    int newHeight = mouseY - lastActiveWindow.getY() + 5;

                    lastActiveWindow.setDimensions(newWidth, newHeight);
                }
            }
            else
            {
                resizeIgnoreBorders = false;
            }
        }

        Draw.drawGradientRect(0, 0, Screen.scaledDisplayResolution().getScaledWidth(), Screen.scaledDisplayResolution().getScaledHeight(), 0xEF000000, 0xBB000000);

        GL11.glPushMatrix();

        GL11.glScalef(0.5F, 0.5F, 0.5F);
        this.fontRenderer.drawString("Press 'ESC' to close the window manager. Press 'ALT + W' to open it on Windows/Linux or 'OPTION + W' to open it on mac OS.", 10, 11, 0x55FFFFFF);

        GL11.glPopMatrix();

        if (this.resetWindow != null)
        {
            this.windowapi.getWindows().remove(this.resetWindow);
            this.windowapi.getWindows().add(this.windowapi.getWindows().size(), this.resetWindow);
            this.resetWindow = null;
        }

        for (int x = this.windowapi.getWindows().size() - 1; x >= 0; x--)
        {
            Window window = (Window) this.windowapi.getWindows().get(x);

            if (Mouse.isButtonDown(0))
            {
                if (this.draggingWindow == window)
                {
                    int diffX = mouseX - this.mouseXLast;
                    int diffY = mouseY - this.mouseYLast;

                    window.setPosition(window.getX() + diffX, window.getY() + diffY);

                    this.mouseXLast = mouseX;
                    this.mouseYLast = mouseY;
                }
            }
            else
            {
                this.draggingWindow = null;
            }
        }

        for (Window window : this.windowapi.getWindows())
        {
            this.windowapi.drawWindow(window, mouseX, mouseY);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void setWorldAndResolution(Minecraft mc, int width, int height)
    {
        if (this.parentScreen != null)
        {
            this.parentScreen.setWorldAndResolution(mc, width, height);
        }

        super.setWorldAndResolution(mc, width, height);
    }

    protected void actionPerformed(GuiButton b)
    {
        for (Window window : this.windowapi.getWindows())
        {
            window.onButtonPress(b);
        }
    }

    public void setResizeEnabled(boolean resizeEnabled)
    {
        this.resizeEnabled = resizeEnabled;
    }

    public boolean isResizeEnabled()
    {
        return resizeEnabled;
    }

    public GuiScreen getParentScreen()
    {
        return parentScreen;
    }

    public void setParentScreen(GuiScreen parentScreen)
    {
        this.parentScreen = parentScreen;
    }

    public WindowAPI getWindowAPI()
    {
        return this.windowapi;
    }

    public Window getActiveWindow()
    {
        return lastActiveWindow;
    }

    public Window getDraggingWindow()
    {
        return draggingWindow;
    }
}