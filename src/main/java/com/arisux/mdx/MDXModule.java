package com.arisux.mdx;

import com.arisux.mdx.MDX.Properties;
import com.arisux.mdx.lib.client.GUIElementTracker;
import com.arisux.mdx.lib.client.NotifierModule;
import com.arisux.mdx.lib.client.render.model.DummyModelLoader;
import com.arisux.mdx.lib.game.CommandHandler;
import com.arisux.mdx.lib.game.DataHandler;
import com.arisux.mdx.lib.game.Game;
import com.arisux.mdx.lib.game.IdentityRemapModule;
import com.arisux.mdx.lib.util.SystemInfo;
import com.arisux.mdx.lib.world.StructureGenerationHandler;

import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = Properties.ID, version = Properties.VERSION)
public class MDXModule
{
    private static MDX    instance         = new MDX();
    public static boolean prefetchComplete = false;

    public static MDX instance()
    {
        return instance;
    }

    @EventHandler
    public void pre(FMLPreInitializationEvent event)
    {
        SystemInfo.INSTANCE.runtimeTasks();
        DataHandler.instance.pre(event);

        if (!prefetchComplete)
        {
            return;
        }

        MDX.console().pre(event);
        MDX.settings().pre(event);
        Game.registerEventHandler(StructureGenerationHandler.INSTANCE);
    }

    @SideOnly(Side.CLIENT)
    @EventHandler
    public void preClient(FMLPreInitializationEvent event)
    {
        ModelLoaderRegistry.registerLoader(DummyModelLoader.INSTANCE);
        Game.registerEventHandler(NotifierModule.instance);
        Game.registerEventHandler(GUIElementTracker.INSTANCE);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if (!prefetchComplete)
        {
            return;
        }

        MDX.console().init(event);
        MDX.network().init(event);
        CommandHandler.INSTANCE.init(event);
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        if (!prefetchComplete)
        {
            return;
        }

        CommandHandler.INSTANCE.onServerStarting(event);
    }

    @EventHandler
    public void post(FMLPostInitializationEvent event)
    {
        if (!prefetchComplete)
        {
            return;
        }

        MDX.console().post(event);
    }

    @SideOnly(Side.CLIENT)
    @EventHandler
    public void postClient(FMLPostInitializationEvent event)
    {
        if (!prefetchComplete)
        {
            return;
        }

        MDX.renders().post(event);
        MDX.notifications().onStartup();
    }

    @EventHandler
    public void onLoadMissingMapping(FMLMissingMappingsEvent event)
    {
        if (!prefetchComplete)
        {
            return;
        }

        IdentityRemapModule.INSTANCE.onLoadMissingMapping(event);
    }

    public static boolean prefetchComplete()
    {
        return prefetchComplete;
    }
}
