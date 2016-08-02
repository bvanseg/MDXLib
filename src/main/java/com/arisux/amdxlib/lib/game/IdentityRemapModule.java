package com.arisux.amdxlib.lib.game;

import com.arisux.amdxlib.AMDXLib;
import com.arisux.amdxlib.lib.game.ModIdentityMap.IdentityMap;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;

public class IdentityRemapModule
{
    public static final IdentityRemapModule instance = new IdentityRemapModule();
    
    @EventHandler
    public void onLoadMissingMapping(FMLMissingMappingsEvent event)
    {
        AMDXLib.log().warn("Invalid mappings found, searching for new mappings...");

        for (MissingMapping mapping : event.getAll())
        {
            for (ModIdentityMap mod : AMDXLib.getRemappedMods())
            {
                if (mapping.name.contains(mod.getInvalidIdentity()))
                {
                    try
                    {
                        if (Class.forName(mod.getClassLocation()) != null)
                        {
                            AMDXLib.replaceMapping(mapping, mod.getInvalidIdentity() + ":", mod.getValidIdentity() + ":");
                        }
                    }
                    catch (ClassNotFoundException e)
                    {
                        AMDXLib.log().warn("Invalid mappings were detected, but the mod targetted for the new mappings is not present: " + mod.getClassLocation());
                    }
                }
            }

            for (IdentityMap newMapping : AMDXLib.getRemappedIdentities())
            {
                if (mapping.name.contains(newMapping.getInvalidIdentity()))
                {
                    try
                    {
                        if (Class.forName(newMapping.getModIdentityMap().getClassLocation()) != null)
                        {
                            AMDXLib.replaceMapping(mapping, newMapping.getInvalidIdentity(), newMapping.getValidIdentity());
                        }
                    }
                    catch (ClassNotFoundException e)
                    {
                        AMDXLib.log().warn("Invalid mappings were detected, but the mod targetted for the new mappings is not present: " + newMapping.getModIdentityMap().getClassLocation());
                    }
                }
            }
        }
    }
}
