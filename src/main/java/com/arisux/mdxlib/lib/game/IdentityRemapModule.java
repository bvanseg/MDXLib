package com.arisux.mdxlib.lib.game;

import com.arisux.mdxlib.MDX;
import com.arisux.mdxlib.lib.game.ModIdentityMap.IdentityMap;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;

public class IdentityRemapModule
{
    public static final IdentityRemapModule instance = new IdentityRemapModule();
    
    @EventHandler
    public void onLoadMissingMapping(FMLMissingMappingsEvent event)
    {
        MDX.log().warn("Invalid mappings found, searching for new mappings...");

        for (MissingMapping mapping : event.getAll())
        {
            for (ModIdentityMap mod : MDX.getRemappedMods())
            {
                if (mapping.name.contains(mod.getInvalidIdentity()))
                {
                    try
                    {
                        if (Class.forName(mod.getClassLocation()) != null)
                        {
                            MDX.replaceMapping(mapping, mod.getInvalidIdentity() + ":", mod.getValidIdentity() + ":");
                        }
                    }
                    catch (ClassNotFoundException e)
                    {
                        MDX.log().warn("Invalid mappings were detected, but the mod targetted for the new mappings is not present: " + mod.getClassLocation());
                    }
                }
            }

            for (IdentityMap newMapping : MDX.getRemappedIdentities())
            {
                if (mapping.name.contains(newMapping.getInvalidIdentity()))
                {
                    try
                    {
                        if (Class.forName(newMapping.getModIdentityMap().getClassLocation()) != null)
                        {
                            MDX.replaceMapping(mapping, newMapping.getInvalidIdentity(), newMapping.getValidIdentity());
                        }
                    }
                    catch (ClassNotFoundException e)
                    {
                        MDX.log().warn("Invalid mappings were detected, but the mod targetted for the new mappings is not present: " + newMapping.getModIdentityMap().getClassLocation());
                    }
                }
            }
        }
    }
}
