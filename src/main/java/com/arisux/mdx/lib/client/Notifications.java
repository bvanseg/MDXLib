package com.arisux.mdx.lib.client;

import java.util.ArrayList;

import com.arisux.mdx.Settings;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Notifications
{
    public static final Notifications INSTANCE = new Notifications();

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
        if (notification.allowMultiple() || !notification.allowMultiple() && !Notifications.INSTANCE.queue().contains(notification))
        {
            Notifications.INSTANCE.queue().add(notification);
        }
    }

    public void onStartup()
    {
        System.out.println("okkk");
        if (Settings.INSTANCE.isStartupNotificationEnabled())
        {
            Notifications.sendNotification(new Notification()
            {
                @Override
                public int displayTimeout()
                {
                    return 2000;
                }
                
                @Override
                public String getMessage()
                {
                    return "MDX collects data on startup, by continuing with the data collector enabled you agree to let us collect data that may help us diagnose problems you may have in the future. This is the first time you've used this mod, data will not be collected until the next time the game launches. Data collection can be disabled in the configuration, or by selecting 'Disable' below.";
                }
            });
            Settings.INSTANCE.disableStartupNotification();
            
            Notifications.sendNotification(new Notification()
            {
                @Override
                public String getMessage()
                {
                    return "Notifications may pop up here throughout gameplay. These notifications will explain how certain features of the game work. You can disable these notifications in the settings.";
                }
            });
            Settings.INSTANCE.disableStartupNotification();
        }
    }
}
