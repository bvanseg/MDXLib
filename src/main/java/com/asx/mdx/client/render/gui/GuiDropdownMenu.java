package com.asx.mdx.client.render.gui;

import java.util.ArrayList;

import javax.vecmath.Vector2d;

import com.asx.mdx.client.ClientGame;
import com.asx.mdx.common.Game;

public class GuiDropdownMenu extends GuiCustomButton
{
    private ArrayList<GuiCustomButton> options = new ArrayList<GuiCustomButton>();

    public GuiDropdownMenu(int id, int xPosition, int yPosition, int width, int height, String displayString)
    {
        super(id, xPosition, yPosition, width, height, displayString);
    }

    @Override
    public void mousePressed(Vector2d mousePosition)
    {
        super.mousePressed(ClientGame.instance.minecraft(), (int) mousePosition.x, (int) mousePosition.y);
        this.drawOptions();
    }

    public void drawOptions()
    {
        if (options.size() > 0)
        {
            for (GuiCustomButton button : options)
            {
                button.drawButton();
            }
        }
    }
}