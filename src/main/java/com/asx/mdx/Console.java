package com.asx.mdx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.asx.mdx.core.mods.IInitEvent;
import com.asx.mdx.core.mods.IPostInitEvent;
import com.asx.mdx.core.mods.IPreInitEvent;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Console implements IPreInitEvent, IInitEvent, IPostInitEvent
{
    public static final Console INSTANCE = new Console();
    public static final Logger  LOGGER   = LogManager.getLogger("MDX");

    @Override
    public void pre(FMLPreInitializationEvent event)
    {
        LOGGER.info("Preparing...");
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        LOGGER.info("Minecraft Development Library X Copyright \u00A9 2016-2019 ASX");
        LOGGER.info("Initializing...");
    }

    @Override
    public void post(FMLPostInitializationEvent event)
    {
        LOGGER.info("Initialized. Running post initialization tasks...");
    }

    public static void modificationWarning()
    {
        LOGGER.warn("Somebody has been tinkering with functionality that shouldn't be tinkered with!");        
    }
}
