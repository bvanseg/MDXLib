package com.asx.mdx.config;

import java.util.ArrayList;

import com.asx.mdx.MDX;
import com.asx.mdx.lib.util.Game;
import com.asx.mdx.lib.world.entity.Entities;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ConfigSettingEntityTypeList extends ConfigSetting
{
    public ConfigSettingEntityTypeList(IFlexibleConfiguration configuration, Property property)
    {
        super(configuration, property);
    }

    @Override
    public void toggle()
    {
        ;
    }

    @Override
    public ArrayList<Class<? extends Entity>> value()
    {
        ArrayList<Class<? extends Entity>> classes = new ArrayList<Class<? extends Entity>>();

        if (this.property.getString() != null)
        {
            String[] entities = this.property.getString().split(",");

            for (String id : entities)
            {
                if (id != null && !id.isEmpty())
                {
                    Class<? extends Entity> entity = Entities.getRegisteredEntityClass(id);

                    if (entity != null)
                    {
                        classes.add(entity);
                    }
                    else
                    {
                        MDX.log().warn("Invalid entity ID entered. Entity type not found! Entity ID: " + id);
                    }
                }
            }
        }

        return classes;
    }

    @Override
    public String getStringValue()
    {
        return entityRegistryListForConfig(value());
    }

    public static String entityRegistryListForConfig(ArrayList<Class<? extends Entity>> entities)
    {
        StringBuilder builder = new StringBuilder();

        for (Class<? extends Entity> clz : entities)
        {
            EntityEntry entry = EntityRegistry.getEntry(clz);
            
            if (clz != null && entry.getRegistryName() != null)
            {
                String id = entry.getRegistryName().toString();
                boolean last = entities.get(entities.size() - 1) == clz;

                if (!last)
                {
                    builder.append(id + ",");
                }
                else
                {
                    builder.append(id);
                }
            }
            else
            {
                if (Game.isDevEnvironment())
                {
                    MDX.log().info("Entity type was null or had a null registry name. This is probably a bug. Entity type: " + clz);
                }
            }
        }

        return builder.toString();
    }
}