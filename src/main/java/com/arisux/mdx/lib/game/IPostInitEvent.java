package com.arisux.mdx.lib.game;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public interface IPostInitEvent
{
    @Mod.EventHandler
    public void post(IMod mod, FMLPostInitializationEvent event);
}
