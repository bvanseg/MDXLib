package com.arisux.amdxlib.lib.client.gui;

import java.util.ArrayList;

import javax.vecmath.Vector2d;

import com.arisux.amdxlib.lib.game.Game;

public class GuiDropdownMenu extends GuiCustomButton implements IGuiElement
{
    ArrayList<GuiCustomButton> options = new ArrayList<GuiCustomButton>();

    public GuiDropdownMenu(ArrayList<GuiCustomButton> buttonList, int id, int xPosition, int yPosition, int width, int height, String displayString, IAction action)
    {
        super(buttonList, id, xPosition, yPosition, width, height, displayString, action);
    }

    @Override
    public void mousePressed(Vector2d mousePosition)
    {
        super.mousePressed(Game.minecraft(), (int) mousePosition.x, (int) mousePosition.y);
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