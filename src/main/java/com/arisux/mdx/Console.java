package com.arisux.mdx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.arisux.mdx.lib.game.IInitEvent;
import com.arisux.mdx.lib.game.IPostInitEvent;
import com.arisux.mdx.lib.game.IPreInitEvent;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Console implements IPreInitEvent, IInitEvent, IPostInitEvent
{
    public static final Console instance = new Console();
    public static final Logger  logger   = LogManager.getLogger("MDX");

    @Override
    public void pre(FMLPreInitializationEvent event)
    {
        logger.info("Preparing...");
        
        if (!MDXModule.prefetchComplete)
        {
            modificationWarning();
            return;
        }
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        logger.info("Minecraft Development Library X Copyright \u00A9 2016-2017 ASX");
        logger.info("Initializing...");
    }

    @Override
    public void post(FMLPostInitializationEvent event)
    {
        logger.info("Initialized. Running post initialization tasks...");
    }

    public static void modificationWarning()
    {
        logger.warn("Somebody has been tinkering with functionality that shouldn't be tinkered with!");        
    }
}
