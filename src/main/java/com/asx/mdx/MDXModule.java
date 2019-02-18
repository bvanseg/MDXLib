package com.asx.mdx;

import com.asx.mdx.MDX.Properties;
import com.asx.mdx.core.CommandHandler;
import com.asx.mdx.lib.client.DebugToolsRenderer;
import com.asx.mdx.lib.client.gui.GUIElementTracker;
import com.asx.mdx.lib.client.gui.notifications.NotifierModule;
import com.asx.mdx.lib.client.model.loaders.DummyModelLoader;
import com.asx.mdx.lib.util.Game;
import com.asx.mdx.lib.util.system.SystemInfo;
import com.asx.mdx.lib.world.StructureGenerationHandler;
import com.asx.mdx.webserver.WebModule;

import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = Properties.ID, version = Properties.VERSION)
public class MDXModule
{
    private static MDX instance = new MDX();

    public static MDX instance()
    {
        return instance;
    }

    static
    {
        ;
    }

    @EventHandler
    public void pre(FMLPreInitializationEvent event)
    {
        WebModule.startWebServer();
        MDX.console().pre(event);
        MDX.settings().pre(event);
        SystemInfo.INSTANCE.runtimeTasks();
        Game.registerEventHandler(StructureGenerationHandler.INSTANCE);
    }

    @SideOnly(Side.CLIENT)
    @EventHandler
    public void preClient(FMLPreInitializationEvent event)
    {
        ModelLoaderRegistry.registerLoader(DummyModelLoader.INSTANCE);
        Game.registerEventHandler(NotifierModule.instance);
        Game.registerEventHandler(GUIElementTracker.INSTANCE);
        Game.registerEventHandler(DebugToolsRenderer.instance);
        MDX.renders().pre(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MDX.console().init(event);
        MDX.network().init(event);
        CommandHandler.INSTANCE.init(event);
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        CommandHandler.INSTANCE.onServerStarting(event);
    }

    @EventHandler
    public void post(FMLPostInitializationEvent event)
    {
        MDX.console().post(event);
    }

    @SideOnly(Side.CLIENT)
    @EventHandler
    public void postClient(FMLPostInitializationEvent event)
    {
        MDX.notifications().onStartup();
    }
}
