package com.arisux.mdxlib.lib.game;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public interface IPreInitEvent
{
    @Mod.EventHandler
    public void pre(FMLPreInitializationEvent event);
}
