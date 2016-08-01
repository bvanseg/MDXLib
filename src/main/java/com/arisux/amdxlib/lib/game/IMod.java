package com.arisux.amdxlib.lib.game;

import cpw.mods.fml.common.ModContainer;
import net.minecraft.creativetab.CreativeTabs;

public interface IMod extends IInitEvent, IPreInitEvent, IPostInitEvent
{
    /** Return the mod's ModContainer instance **/
    public ModContainer container();

    /** Return the mod's CreativeTabs object if one exists **/
    public CreativeTabs tab();

    /** Return the mod's domain. EXAMPLE: "airi:" **/
    public String domain();
}
