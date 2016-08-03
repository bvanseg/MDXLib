package com.arisux.amdxlib.lib.client;

import com.arisux.amdxlib.AMDXLib;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NotifierModule
{
    public static NotifierModule instance            = new NotifierModule();
    public static Notification   currentNotification = null;

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event)
    {
        if (currentNotification == null && AMDXLib.getNotificationsInQueue().size() > 0)
        {
            currentNotification = AMDXLib.getNotificationsInQueue().get(0);
        }

        if (System.currentTimeMillis() % 2 == 0)
        {
            if (currentNotification != null && currentNotification.tick() >= currentNotification.displayTimeout())
            {
                currentNotification.timeoutAction();
            }
        }
    }

    @SubscribeEvent
    public void render(TickEvent.RenderTickEvent event)
    {
        if (currentNotification != null)
        {
            currentNotification.preDraw();
        }
    }
}
