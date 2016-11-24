package com.arisux.mdxlib.lib.client.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;

public class GuiCustomScreen extends GuiScreen
{
    public ArrayList<GuiCustomButton> customButtonList = new ArrayList<GuiCustomButton>();
    public ArrayList<GuiCustomTextbox> customTextfieldList = new ArrayList<GuiCustomTextbox>();

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        for (GuiCustomButton button : customButtonList)
        {
            button.mouseReleased(mouseX, mouseY);
        }
    }

    @Override
    public void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        super.drawGradientRect(par1, par2, par3, par4, par5, par6);
    }

    public void setZLevel(float f)
    {
        this.zLevel = f;
    }

    public float getZLevel()
    {
        return this.zLevel;
    }

    public void incZLevel(float f)
    {
        this.zLevel += f;
    }
}