package com.arisux.mdxlib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Console
{
    public static Logger logger = LogManager.getLogger("MDX");;

    public static void copyright()
    {
        logger.info("Copyright(C) 2016-2017 Arisux Technology Group");
    }
    
    public static void preInit()
    {
        logger.info("Preparing to initialize...");
    }
    
    public static void init()
    {
        logger.info("Initializing...");
    }
    
    public static void postInit()
    {
        logger.info("Initialized. Running post initialization tasks...");
    }
    
    public static void postInitComplete()
    {
        logger.info("Initialization complete.");
    }
}
