package com.arisux.amdxlib.lib.game;

import cpw.mods.fml.common.ModContainer;

public interface IMod extends IInitEvent, IPreInitEvent, IPostInitEvent
{
    /** Return the mod's ModContainer instance **/
    public ModContainer container();

    /** Return the mod's domain. EXAMPLE: "amdlibx:" **/
    public String domain();
}
