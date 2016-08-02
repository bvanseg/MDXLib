package com.arisux.amdxlib.lib.client.gui;

import javax.vecmath.Vector2d;

import com.arisux.amdxlib.lib.client.render.Draw;
import com.arisux.amdxlib.lib.game.Game;

import net.minecraft.client.Minecraft;

public class GuiCustomSlider extends GuiCustomButton implements IGuiElement
{
    public String label;
    public float sliderValue = 1.0F;
    public float sliderMaxValue = 1.0F;
    public boolean dragging = false;
    public int sliderVariable;
    public int sliderButtonColor = 0xFF00AAFF;

    public GuiCustomSlider(int id, int x, int y, String label, float startingValue, float maxValue)
    {
        super(id, x, y, 150, 20, label, null);
        this.sliderValue = startingValue;
        this.sliderMaxValue = maxValue;
        this.label = label;
    }

    @Override
    public int getHoverState(boolean par1)
    {
        return 0;
    }

    @Override
    public void mouseDragged(Minecraft minecraft, int mouseX, int mouseY)
    {
        super.mouseDragged(minecraft, mouseX, mouseY);

        if (this.visible)
        {
            if (this.dragging)
            {
                this.displayString = label + ": " + (int) (sliderValue * sliderMaxValue);
                this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);

                if (this.sliderValue < 0.0F)
                {
                    this.sliderValue = 0.0F;
                }

                if (this.sliderValue > 1.0F)
                {
                    this.sliderValue = 1.0F;
                }

                this.sliderVariable = (int) (this.sliderValue * this.sliderMaxValue);
            }
        }
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        super.drawButton(mc, mouseX, mouseY);

        if (this.visible)
        {
            Draw.drawRectWithOutline(this.xPosition - 1, this.yPosition - 1, this.width + 2, this.height + 2, 1, 0x00000000, 0xAAFFFFFF);
            Draw.drawRect(this.xPosition + (int) (this.sliderValue * (this.width - 8)), this.yPosition, 8, this.height, sliderButtonColor);
        }
    }

    @Override
    public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY)
    {
        if (super.mousePressed(minecraft, mouseX, mouseY))
        {
            this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);

            if (this.sliderValue < 0.0F)
            {
                this.sliderValue = 0.0F;
            }

            if (this.sliderValue > 1.0F)
            {
                this.sliderValue = 1.0F;
            }

            this.dragging = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY)
    {
        this.dragging = false;
    }

    @Override
    public void mousePressed(Vector2d mousePosition)
    {
        this.mousePressed(Game.minecraft(), (int) mousePosition.x, (int) mousePosition.y);
    }

    @Override
    public void mouseReleased(Vector2d mousePosition)
    {
        this.mouseReleased((int) mousePosition.x, (int) mousePosition.y);
    }

    @Override
    public void mouseDragged(Vector2d mousePosition)
    {
        this.mouseDragged(Game.minecraft(), (int) mousePosition.x, (int) mousePosition.y);
    }
}