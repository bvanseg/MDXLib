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
    private Property             developerKey;

    @Override
    @EventHandler
    public void pre(FMLPreInitializationEvent evt)
    {
        if (!MDXModule.prefetchComplete)
        {
            Console.modificationWarning();
            return;
        }

        configuration = new Configuration(new File(evt.getModConfigurationDirectory(), "mdx.config"));

        try
        {
            configuration.load();
            developerKey = configuration.get(CATEGORY_STARTUP, "developer_key", "none");
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
    
    public boolean isDeveloperKeyPresent()
    {
        return developerKey != null && developerKey.getString() != null && !developerKey.getString().isEmpty() && developerKey.getString().equals("5e8edd851d2fdfbd7415232c67367cc3");
    }

    public Configuration getConfig()
    {
        return configuration;
    }
}
