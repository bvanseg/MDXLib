package com.arisux.mdxlib;

import com.arisux.mdxlib.lib.client.GUIElementTracker;
import com.arisux.mdxlib.lib.client.Notification;
import com.arisux.mdxlib.lib.client.NotifierModule;
import com.arisux.mdxlib.lib.game.Game;
import com.arisux.mdxlib.lib.game.IdentityRemapModule;
import com.arisux.mdxlib.lib.util.Remote;
import com.arisux.mdxlib.lib.world.StructureGenerationHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = MDX.MODID, version = MDX.VERSION)
public class MDXModule
{
    private static MDX    instance = new MDX();
    public static boolean enable   = true;

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

        Console.preInit();
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

        MDX.sendNotification(new Notification()
        {
            @Override
            public String getMessage()
            {
                return "Notifications may pop up here throughout gameplay. These notifications will explain how certain features of the game work. You can disable these notifications in the settings.";
            }
        });
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

    @SideOnly(Side.CLIENT)
    private void enable()
    {
        if (Remote.authorized())
        {
            MDXModule.enable = false;
        }
    }
}
