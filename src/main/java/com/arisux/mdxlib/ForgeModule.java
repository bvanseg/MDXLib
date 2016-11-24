package com.arisux.mdxlib;

import com.arisux.mdxlib.lib.client.GuiElementTrackingModule;
import com.arisux.mdxlib.lib.client.Notification;
import com.arisux.mdxlib.lib.client.NotifierModule;
import com.arisux.mdxlib.lib.game.Game;
import com.arisux.mdxlib.lib.game.IdentityRemapModule;
import com.arisux.mdxlib.lib.world.StructureGenerationHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = MDX.MODID, version = MDX.VERSION)
public class ForgeModule
{
    private static MDX amdxlib = new MDX();

    public static MDX amdxlib()
    {
        return amdxlib;
    }

    @EventHandler
    public void pre(FMLPreInitializationEvent event)
    {
        Console.preInit();
        
        Game.registerEventHandler(StructureGenerationHandler.instance);
        Game.registerEventHandler(IdentityRemapModule.instance);

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            Game.registerEventHandler(NotifierModule.instance);
            Game.registerEventHandler(GuiElementTrackingModule.instance);
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        Console.copyright();
        Console.init();
    }

    @EventHandler
    public void post(FMLPostInitializationEvent event)
    {
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
}
