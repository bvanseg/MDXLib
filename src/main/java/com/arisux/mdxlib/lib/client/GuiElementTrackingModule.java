package com.arisux.mdxlib.lib.client;

import java.util.ArrayList;

import javax.vecmath.Vector2d;

import org.lwjgl.input.Mouse;

import com.arisux.mdxlib.lib.client.gui.GuiElement;
import com.arisux.mdxlib.lib.client.render.Screen;
import com.arisux.mdxlib.lib.game.Game;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.MouseInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;

public class GuiElementTrackingModule
{
    public static final GuiElementTrackingModule instance    = new GuiElementTrackingModule();

    private ArrayList<GuiElement>                guiElements = new ArrayList<GuiElement>();

    @SubscribeEvent
    public void tick(ClientTickEvent event)
    {
        Vector2d mousePosition = Screen.scaledMousePosition();

        for (int x = 0; x < guiElements.size(); x++)
        {
            GuiElement element = guiElements.get(x);

            if (element != null)
            {
                handleButtonInput(null, element, mousePosition);

                // if (element instanceof GuiCustomTextbox)
                // {
                // GuiCustomTextbox textbox = (GuiCustomTextbox) element;
                //
                // if (textbox != null && Mouse.isButtonDown(0))
                // {
                // if (textbox.isActive())
                // {
                // textbox.setFocused(true);
                // }
                // else
                // {
                // textbox.setFocused(false);
                // }
                // }
                // }
            }
        }
    }

    private static void handleButtonInput(MouseInputEvent event, GuiElement element, Vector2d mousePosition)
    {
        if (!Game.minecraft().inGameHasFocus && element.isActive() && element.isMouseInElement(mousePosition))
        {
            while (Mouse.next())
            {
                if (Mouse.getEventButton() > -1)
                {
                    if (Mouse.getEventButtonState())
                    {
                        element.mousePressed(mousePosition);
                    }
                    else
                    {
                        element.mouseReleased(mousePosition);
                    }
                }
            }

            if (Mouse.isButtonDown(0) && (Mouse.getDX() != 0 || Mouse.getDY() != 0))
            {
                element.mouseDragged(mousePosition);
            }
        }
    }

    public void add(GuiElement element)
    {
        this.remove(element);

        if (!guiElements.contains(element))
        {
            guiElements.add(element);
        }
    }

    public void remove(GuiElement element)
    {
        if (guiElements.contains(element))
        {
            guiElements.remove(element);
        }
    }
}
