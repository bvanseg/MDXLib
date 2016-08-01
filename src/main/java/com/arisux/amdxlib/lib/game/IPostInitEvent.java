package com.arisux.amdxlib.lib.game;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

public interface IPostInitEvent
{
    @Mod.EventHandler
    public void post(FMLPostInitializationEvent event);
}
