package com.arisux.mdx.lib.game;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public abstract class RegistrationHandler<MOD extends IMod> implements IPreInitEvent, IInitEvent, IPostInitEvent
{
    protected MOD mod;
    
    public RegistrationHandler(MOD mod)
    {
        this.mod = mod;
    }

    @Override
    @Mod.EventHandler
    public void pre(FMLPreInitializationEvent event)
    {
        ;
    }

    @Override
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        ;
    }
    
    @Override
    @Mod.EventHandler
    public void post(FMLPostInitializationEvent event)
    {
        ;
    }
}
