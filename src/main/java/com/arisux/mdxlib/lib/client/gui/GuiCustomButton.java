package com.arisux.mdxlib.lib.client.gui;

import javax.vecmath.Vector2d;

import org.lwjgl.input.Mouse;

import com.arisux.mdxlib.lib.client.render.Draw;
import com.arisux.mdxlib.lib.client.render.OpenGL;
import com.arisux.mdxlib.lib.client.render.Screen;
import com.arisux.mdxlib.lib.game.Game;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public class GuiCustomButton extends GuiElement
{
    public int fontColor;
    public int baseColor;
    public int overlayColorNormal;
    public int overlayColorHover;
    public int overlayColorPressed;
    public float scale;
    public boolean fontShadow;
    public ElementAlignment textAlignment;

    public GuiCustomButton(int id, int xPosition, int yPosition, int width, int height, String displayString)
    {
        super(id, xPosition, yPosition, width, height, displayString);
        this.scale = 1F;
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = id;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.displayString = displayString;
        this.fontColor = 0xFFFFFFFF;
        this.baseColor = 0xEE000000;
        this.overlayColorNormal = 0x44000000;
        this.overlayColorHover = 0x00000000;
        this.overlayColorPressed = 0x66000000;
        this.textAlignment = ElementAlignment.CENTER;
        this.trackElement();
        this.fontShadow = true;
    }

    public void drawButton()
    {
        this.drawButton(1);
    }

    public void drawButton(float scale)
    {
        this.drawButton(Game.minecraft(), (int) (Screen.scaledMousePosition().x * scale), (int) (Screen.scaledMousePosition().y * scale));
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        super.drawButton(mc, mouseX, mouseY);
        
        if (this.visible)
        {
            this.hovered = mouseX >= this.xPosition * scale && mouseY >= this.yPosition * scale && mouseX < (this.xPosition + this.width) * scale && mouseY < (this.yPosition + this.height) * scale;
            int state = this.getHoverState(this.hovered);
            int overlayColor = state == 2 ? (Mouse.isButtonDown(0) ? overlayColorPressed : overlayColorHover) : overlayColorNormal;

            OpenGL.color(1.0F, 1.0F, 1.0F, 1.0F);
            OpenGL.enableBlend();
            Draw.drawRect(this.xPosition, this.yPosition, this.width, this.height, baseColor);
            Draw.drawRect(this.xPosition, this.yPosition, this.width, this.height, overlayColor);

            this.mouseDragged(mc, mouseX, mouseY);

            if (this.textAlignment == ElementAlignment.CENTER)
            {
                Draw.drawStringAlignCenter(this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, fontColor, fontShadow);
            }
            else if (this.textAlignment == ElementAlignment.LEFT)
            {
                Draw.drawString(this.displayString, this.xPosition + 4, this.yPosition + (this.height - 8) / 2, fontColor, fontShadow);
            }
            else if (this.textAlignment == ElementAlignment.RIGHT)
            {
                Draw.drawStringAlignRight(this.displayString, this.xPosition + this.width - 4, this.yPosition + (this.height - 8) / 2, fontColor, fontShadow);
            }

            OpenGL.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
    
    @Override
    public void mousePressed(Vector2d mousePosition)
    {
        super.mousePressed(Game.minecraft(), (int) mousePosition.x, (int) mousePosition.y);
    }

    @Override
    public void mouseReleased(Vector2d mousePosition)
    {
        super.mouseReleased((int) mousePosition.x, (int) mousePosition.y);

        if (this.isMouseInElement(mousePosition))
        {
            if (this.getAction() != null)
            {
                this.getAction().perform(this);
            }
        }
    }

    @Override
    public void mouseDragged(Vector2d mousePosition)
    {
        super.mouseDragged(Game.minecraft(), (int) mousePosition.x, (int) mousePosition.y);
    }

    public GuiCustomButton setAlignment(ElementAlignment alignment)
    {
        this.textAlignment = alignment;
        return this;
    }

    @Override
    public void setX(int x)
    {
        this.xPosition = x;
    }

    @Override
    public void setY(int y)
    {
        this.yPosition = y;
    }

    @Override
    public void setWidth(int width)
    {
        this.width = width;
    }

    @Override
    public void setHeight(int height)
    {
        this.height = height;
    }
}