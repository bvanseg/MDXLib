package com.arisux.mdx;

import com.arisux.mdx.MDX.Properties;
import com.arisux.mdx.lib.client.GUIElementTracker;
import com.arisux.mdx.lib.client.NotifierModule;
import com.arisux.mdx.lib.client.render.model.DummyModelLoader;
import com.arisux.mdx.lib.game.CommandHandler;
import com.arisux.mdx.lib.game.Game;
import com.arisux.mdx.lib.game.IdentityRemapModule;
import com.arisux.mdx.lib.util.Remote;
import com.arisux.mdx.lib.util.SystemInfo;
import com.arisux.mdx.lib.world.StructureGenerationHandler;

import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Properties.ID, version = Properties.VERSION)
public class MDXModule
{
    private static MDX     instance = new MDX();
    private static boolean enabled  = true;

    public static MDX instance()
    {
        return instance;
    }

    @EventHandler
    public void pre(FMLPreInitializationEvent event)
    {
        this.enable();

        if (!enabled)
        {
            return;
        }

        SystemInfo.instance.runtimeTasks();
        MDX.console().pre(event);
        MDX.settings().pre(event);
        Game.registerEventHandler(StructureGenerationHandler.instance);

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            ModelLoaderRegistry.registerLoader(DummyModelLoader.INSTANCE);
            Game.registerEventHandler(NotifierModule.instance);
            Game.registerEventHandler(GUIElementTracker.instance);
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if (!enabled)
        {
            return;
        }

        MDX.console().init(event);
        CommandHandler.instance.init(event);
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        if (!enabled)
        {
            return;
        }

        CommandHandler.instance.onServerStarting(event);
    }

    @EventHandler
    public void post(FMLPostInitializationEvent event)
    {
        if (!enabled)
        {
            return;
        }

        MDX.console().post(event);
        MDX.renders().post(event);
        MDX.notifications().onStartup();
    }

    @EventHandler
    public void onLoadMissingMapping(FMLMissingMappingsEvent event)
    {
        if (!enabled)
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
                MDXModule.enabled = false;
            }
        }
    }

    public static boolean enabled()
    {
        return enabled;
    }
}
