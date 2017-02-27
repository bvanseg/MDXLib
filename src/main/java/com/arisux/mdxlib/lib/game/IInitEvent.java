package com.arisux.mdxlib.lib.game;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public interface IInitEvent
{
    @Mod.EventHandler
    public void init(FMLInitializationEvent event);
}
