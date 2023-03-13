package com.asx.mdx.common.io.config;

import java.util.ArrayList;

import com.asx.mdx.internal.MDX;
import com.asx.mdx.common.Game;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Property;

public class ConfigSettingBiomeList extends ConfigSetting
{
    public ConfigSettingBiomeList(IFlexibleConfiguration configuration, Property property)
    {
        super(configuration, property);
    }

    @Override
    public void toggle()
    {
        ;
    }

    @Override
    public ArrayList<Biome> value()
    {
        ArrayList<Biome> biomeList = new ArrayList<Biome>();

        if (this.property.getString() != null)
        {
            String[] biomeNames = this.property.getString().split(",");

            for (String id : biomeNames)
            {
                if (id != null && !id.isEmpty())
                {
                    Biome biome = getRegisteredBiome(id);

                    if (biome != null)
                    {
                        biomeList.add(biome);
                    }
                    else
                    {
                        MDX.log().warn("Invalid biome ID entered. Biome not found! Biome ID: " + id);
                    }
                }
            }
        }

        return biomeList;
    }

    @Override
    public String getStringValue()
    {
        return biomeIdListForConfig(value());
    }

    private static Biome getRegisteredBiome(String id)
    {
        Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(id));

        if (biome == null)
        {
            throw new IllegalStateException("Invalid Biome requested: " + id);
        }
        else
        {
            return biome;
        }
    }

    public static String biomeIdListForConfig(ArrayList<Biome> biomes)
    {
        StringBuilder builder = new StringBuilder();

        for (Biome b : biomes)
        {
            if (b != null && b.getRegistryName() != null)
            {
                String id = b.getRegistryName().toString();
                boolean last = biomes.get(biomes.size() - 1) == b;

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
                if (Game.instance.isDevEnvironment())
                {
                    MDX.log().info("Biome was null or had a null registry name. This is probably a bug. Biome: " + b);
                }
            }
        }

        return builder.toString();
    }
}