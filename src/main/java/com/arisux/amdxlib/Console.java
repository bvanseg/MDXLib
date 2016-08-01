package com.arisux.amdxlib;

import java.util.logging.Logger;

public class Console
{
    public static final Logger logger = Logger.getLogger("AMDXLib");
    
    public static void copyright()
    {
        Console.logger.info("[AMDXLib] Copyright(C) 2016-2017 Arisux Technology Group");
    }
    
    public static void preInit()
    {
        Console.logger.info("[AMDXLib] Preparing to initialize...");
    }
    
    public static void init()
    {
        Console.logger.info("[AMDXLib] Initializing...");
    }
    
    public static void postInit()
    {
        Console.logger.info("[AMDXLib] Initialized. Running post initialization tasks...");
    }
    
    public static void postInitComplete()
    {
        Console.logger.info("[AMDXLib] Initialization complete.");
    }
}
