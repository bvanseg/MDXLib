package com.arisux.mdxlib;

import com.arisux.mdxlib.lib.client.GUIElementTracker;
import com.arisux.mdxlib.lib.client.Notification;
import com.arisux.mdxlib.lib.client.NotifierModule;
import com.arisux.mdxlib.lib.game.Game;
import com.arisux.mdxlib.lib.game.IdentityRemapModule;
import com.arisux.mdxlib.lib.util.Remote;
import com.arisux.mdxlib.lib.util.SystemInfo;
import com.arisux.mdxlib.lib.world.StructureGenerationHandler;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = MDX.MODID, version = MDX.VERSION)
public class MDXModule
{
    private static MDX        instance   = new MDX();
    public static boolean     enable     = true;
    private final SystemInfo systemInfo = new SystemInfo();

    public static MDX instance()
    {
        return instance;
    }

    @EventHandler
    public void pre(FMLPreInitializationEvent event)
    {
        this.enable();

        if (!enable)
        {
            return;
        }

        systemInfo.runtimeTasks();
        Console.preInit();
        Settings.instance.pre(event);
        Game.registerEventHandler(StructureGenerationHandler.instance);

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            Game.registerEventHandler(NotifierModule.instance);
            Game.registerEventHandler(GUIElementTracker.instance);
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if (!enable)
        {
            return;
        }

        Console.copyright();
        Console.init();
    }

    @EventHandler
    public void post(FMLPostInitializationEvent event)
    {
        if (!enable)
        {
            return;
        }

        Console.postInit();
        Console.postInitComplete();

        if (Settings.instance.isStartupNotificationEnabled())
        {
            MDX.sendNotification(new Notification()
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

    @EventHandler
    public void onLoadMissingMapping(FMLMissingMappingsEvent event)
    {
        if (!enable)
        {
            return;
        }

        IdentityRemapModule.instance.onLoadMissingMapping(event);
    }

    private void enable()
    {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            if (Remote.authorized())
            {
                MDXModule.enable = false;
            }
        }
    }
}
