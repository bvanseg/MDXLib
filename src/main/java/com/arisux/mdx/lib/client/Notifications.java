package com.arisux.mdx.lib.client;

import java.util.ArrayList;

import com.arisux.mdx.Console;
import com.arisux.mdx.MDXModule;
import com.arisux.mdx.Settings;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Notifications
{
    public static final Notifications instance = new Notifications();

    /** A queue for notifications that will be displayed     on screen **/
    private ArrayList<Notification>   queue;

    public Notifications()
    {
        this.queue = new ArrayList<Notification>();
    }

    public ArrayList<Notification> queue()
    {
        return queue;
    }

    public static void sendNotification(Notification notification)
    {
        if (notification.allowMultiple() || !notification.allowMultiple() && !Notifications.instance.queue().contains(notification))
        {
            Notifications.instance.queue().add(notification);
        }
    }

    public void onStartup()
    {
        if (Settings.instance.isStartupNotificationEnabled())
        {
            Notifications.sendNotification(new Notification()
            {
                @Override
                public String getMessage()
                {
                    return "Notifications may pop up here throughout gameplay. These notifications will explain how certain features of the game work. You can disable these notifications in the settings.";
                }
            });
            Settings.instance.disableStartupNotification();
        }
    }
}
