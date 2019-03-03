package com.asx.mdx.lib.client.gui.windows;

import java.io.IOException;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.asx.mdx.MDX;
import com.asx.mdx.lib.client.util.Draw;
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
    private Window      draggedWindow = null;
    private int         mouseXLast;
    private int         mouseYLast;

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
        if (this.windowapi.getWindowsRegistry().size() > 0)
        {
            ((Window) this.windowapi.getWindowsRegistry().get(0)).keyTyped(par1, par2);
        }
    }

    public Window getTopWindow(int mouseX, int mouseY)
    {
        Window window = null;

        for (Window w : this.windowapi.getWindowsRegistry())
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

        Window top = getTopWindow(mouseX, mouseY);

        if (top != null)
        {
            if ((mouseX > top.getX() + top.getWidth() - 14) && (mouseX < top.getX() + top.getWidth() - 2) && (mouseY < top.getY() - 2) && (mouseY > top.getY() - 14))
            {
                top.onClose();
            }
        }
        Window window;
        for (int x = 0; x < this.windowapi.getWindowsRegistry().size(); x++)
        {
            window = (Window) this.windowapi.getWindowsRegistry().get(x);

            if (top == window)
            {
                this.resetWindow = window;

                if ((mouseX > window.getX()) && (mouseX < window.getX() + window.getWidth()) && (mouseY > window.getY() - 16) && (mouseY < window.getY()))
                {
                    this.draggedWindow = window;
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
            MDX.log().warn("Parent screen returned null.");
        }

        Draw.drawGradientRect(0, 0, Screen.scaledDisplayResolution().getScaledWidth(), Screen.scaledDisplayResolution().getScaledHeight(), -16777216, 16777215);

        GL11.glPushMatrix();

        GL11.glScalef(0.5F, 0.5F, 0.5F);
        this.fontRenderer.drawString("Press 'ESC' to close the window manager. Press 'ALT + W' to open it on Windows/Linux or 'OPTION + W' to open it on OS X.", 10, 11, -65536);

        GL11.glPopMatrix();

        if (this.resetWindow != null)
        {
            this.windowapi.getWindowsRegistry().remove(this.resetWindow);
            this.windowapi.getWindowsRegistry().add(this.windowapi.getWindowsRegistry().size(), this.resetWindow);
            this.resetWindow = null;
        }

        for (int x = this.windowapi.getWindowsRegistry().size() - 1; x >= 0; x--)
        {
            Window window = (Window) this.windowapi.getWindowsRegistry().get(x);

            if (Mouse.isButtonDown(0))
            {
                if (this.draggedWindow == window)
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
                this.draggedWindow = null;
            }
        }

        for (Window window : this.windowapi.getWindowsRegistry())
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
        for (Window window : this.windowapi.getWindowsRegistry())
        {
            window.onButtonPress(b);
        }
    }

    public WindowAPI getWindowAPI()
    {
        return this.windowapi;
    }
}