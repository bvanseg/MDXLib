package com.arisux.mdx;

import java.io.File;

import com.arisux.mdx.lib.game.IPreInitEvent;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Settings implements IPreInitEvent
{
    public static final Settings instance         = new Settings();
    private Configuration        configuration;
    private final String         CATEGORY_STARTUP = "startup";
    private Property             startupNotification;

    @Override
    @EventHandler
    public void pre(FMLPreInitializationEvent evt)
    {
        configuration = new Configuration(new File(evt.getModConfigurationDirectory(), "mdx.config"));

        try
        {
            configuration.load();
            startupNotification = configuration.get(CATEGORY_STARTUP, "startup_notification", true);
        }
        finally
        {
            configuration.save();
        }
    }

    public void disableStartupNotification()
    {
        startupNotification.set(false);
        configuration.save();
    }

    public boolean isStartupNotificationEnabled()
    {
        return startupNotification.getBoolean();
    }
    
    public Configuration getConfig()
    {
        return configuration;
    }
}
