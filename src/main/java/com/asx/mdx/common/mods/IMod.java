package com.asx.mdx.common.mods;

import net.minecraftforge.fml.common.ModContainer;

public interface IMod
{
    /** Return the mod's ModContainer instance **/
    public ModContainer container();
}
