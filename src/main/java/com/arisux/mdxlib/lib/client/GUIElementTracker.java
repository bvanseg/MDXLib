package com.arisux.mdxlib.lib.client;

import java.util.ArrayList;

import javax.vecmath.Vector2d;

import org.lwjgl.input.Mouse;

import com.arisux.mdxlib.lib.client.gui.IGuiElement;
import com.arisux.mdxlib.lib.client.render.Screen;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class GUIElementTracker
{
    public static final GUIElementTracker instance    = new GUIElementTracker();

    private ArrayList<IGuiElement>               guiElements = new ArrayList<IGuiElement>();
    private boolean                              canRelease;

    @SubscribeEvent
    public void tick(ClientTickEvent event)
    {
        if (event.phase == Phase.START)
        {
            Vector2d mousePosition = Screen.scaledMousePosition();

            for (int x = 0; x < guiElements.size(); x++)
            {
                IGuiElement element = guiElements.get(x);
                
                element.updateElement();

                if (element != null && element.canTrackInput() && element.isMouseInElement(mousePosition))
                {
                    if (Mouse.isButtonDown(0))
                    {
                        if (!canRelease)
                        {
                            element.mousePressed(mousePosition);
                            this.canRelease = true;
                        }
                        else
                        {
                            element.mouseDragged(mousePosition);
                        }
                    }
                    else if (canRelease)
                    {
                        canRelease = false;
                        element.mouseReleased(mousePosition);
                    }
                }
            }
        }
    }

    public void track(IGuiElement element)
    {
        this.stopTracking(element);

        if (!guiElements.contains(element))
        {
            guiElements.add(element);
        }
    }

    public void stopTracking(IGuiElement element)
    {
        if (guiElements.contains(element))
        {
            guiElements.remove(element);
        }
    }
}
