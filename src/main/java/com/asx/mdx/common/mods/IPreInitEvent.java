package com.asx.mdx.common.mods;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IPreInitEvent
{
    @Mod.EventHandler
    public void pre(FMLPreInitializationEvent event);
}
