package com.arisux.mdx.lib.game;

import java.util.ArrayList;

import com.arisux.mdx.Console;
import com.arisux.mdx.MDX;
import com.arisux.mdx.MDXModule;
import com.arisux.mdx.lib.game.ModIdentityMap.IdentityMap;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class IdentityRemapModule
{
    public static final IdentityRemapModule instance = new IdentityRemapModule();

    /** Entire mods that will be remapped **/
    private static ArrayList<ModIdentityMap> remappedMods;

    /** Individual IDs that will be remapped **/
    private static ArrayList<IdentityMap>    remappedIdentities;
    
    public IdentityRemapModule()
    {
        remappedMods = new ArrayList<ModIdentityMap>();
        remappedIdentities = new ArrayList<IdentityMap>();
    }

    public static void registerRemappedEntity(Class<? extends Entity> entityClass, String invalidId)
    {
        EntityList.NAME_TO_CLASS.put(invalidId, entityClass);
    }

    public static void registerRemappedMod(String oldID, String newID, String modClassLocation)
    {
        IdentityRemapModule.getRemappedMods().add(new ModIdentityMap(oldID, newID, modClassLocation));
    }

    public static void registerMappingInfo(String oldID, String newID, String modId)
    {
        IdentityRemapModule.getRemappedIdentities().add(new IdentityMap(oldID, newID, new ModIdentityMap(modId)));
    }

    public static void replaceMapping(MissingMapping mapping, String domain, String oldID, String newID)
    {
        ResourceLocation newName = new ResourceLocation(domain, (mapping.name).replace(mapping.name, newID));

        /** Check for and replace missing item mappings **/
        if (mapping.type == GameRegistry.Type.ITEM)
        {
            MDX.log().info("Converting item mapping [" + mapping.name + "@" + mapping.id + "] -> [" + newName + "@" + mapping.id + "]");

            Item item = (Item) Item.REGISTRY.getObject(newName);

            if (item != null)
            {
                mapping.remap(item);
            }
            else
            {
                MDX.log().warn("Error converting item mapping [" + mapping.name + "@" + mapping.id + "]");
            }
        }

        /** Check for and replace missing block mappings **/
        if (mapping.type == GameRegistry.Type.BLOCK)
        {
            Block block = (Block) Block.REGISTRY.getObject(newName);

            MDX.log().info("Converting block mapping [" + mapping.name + "@" + mapping.id + "] -> [" + newName + "@" + mapping.id + "]");

            if (block != null)
            {
                mapping.remap(block);
            }
            else
            {
                MDX.log().warn("Error converting block mapping. [" + mapping.name + "@" + mapping.id + "]");
            }
        }
    }

    public static ArrayList<IdentityMap> getRemappedIdentities()
    {
        return remappedIdentities;
    }

    public static ArrayList<ModIdentityMap> getRemappedMods()
    {
        return remappedMods;
    }
    
    public void onLoadMissingMapping(FMLMissingMappingsEvent event)
    {
        if (!MDXModule.prefetchComplete)
        {
            Console.modificationWarning();
            return;
        }
        
        MDX.log().warn("Invalid mappings found, searching for new mappings...");

        for (MissingMapping mapping : event.getAll())
        {
            for (ModIdentityMap mod : IdentityRemapModule.getRemappedMods())
            {
                if (mapping.name.contains(mod.getInvalidIdentity()))
                {
                    try
                    {
                        if (Class.forName(mod.getClassLocation()) != null)
                        {
                            IdentityRemapModule.replaceMapping(mapping, mod.getValidIdentity(), mod.getInvalidIdentity() + ":", mod.getValidIdentity() + ":");
                        }
                    }
                    catch (ClassNotFoundException e)
                    {
                        MDX.log().warn("Invalid mappings were detected, but the mod targetted for the new mappings is not present: " + mod.getClassLocation());
                    }
                }
            }

            for (IdentityMap newMapping : IdentityRemapModule.getRemappedIdentities())
            {
                if (mapping.name.contains(newMapping.getInvalidIdentity()))
                {
                    try
                    {
                        if (Class.forName(newMapping.getModIdentityMap().getClassLocation()) != null)
                        {
                            IdentityRemapModule.replaceMapping(mapping, newMapping.modIdentityMap.getValidIdentity(), newMapping.getInvalidIdentity(), newMapping.getValidIdentity());
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
