package com.arisux.amdxlib.lib.game;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public interface IInitEvent
{
    @Mod.EventHandler
    public void init(FMLInitializationEvent event);
}
