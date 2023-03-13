package com.asx.mdx.internal;

import java.io.File;

import com.asx.mdx.common.Game;
import com.asx.mdx.common.mods.IPreInitEvent;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Settings implements IPreInitEvent
{
    public static final Settings INSTANCE         = new Settings();
    private Configuration        configuration;
    private final String         CATEGORY_STARTUP = "startup";
    private Property             startupNotification;
    private Property             webServerEnabled;
    private Property             webserverPort;

    @Override
    @EventHandler
    public void pre(FMLPreInitializationEvent evt)
    {
        configuration = new Configuration(new File(evt.getModConfigurationDirectory(), "mdx.config"));

        try
        {
            configuration.load();
            startupNotification = configuration.get(CATEGORY_STARTUP, "startup_notification", true);
            webServerEnabled = configuration.get(CATEGORY_STARTUP, "webserver", true);
            webserverPort = configuration.get(CATEGORY_STARTUP, "webserver_port", 7761);
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
    
    public boolean isWebServerEnabled()
    {
        return webServerEnabled.getBoolean();
    }
    
    public int getWebserverPort()
    {
        return Game.isDevEnvironment() ? 7762 : webserverPort.getInt();
    }

    public Configuration getConfig()
    {
        return configuration;
    }
}
