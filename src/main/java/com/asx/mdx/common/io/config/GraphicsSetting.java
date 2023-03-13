package com.asx.mdx.common.io.config;

import net.minecraftforge.common.config.Property;

public enum GraphicsSetting
{
    LOW(),
    MEDIUM(),
    HIGH(),
    ULTRA();

    public static GraphicsSetting level(Property property)
    {
        return level(property.getInt());
    }

    public static GraphicsSetting level(int i)
    {
        for (GraphicsSetting gs : values())
        {
            if (gs.ordinal() == i)
            {
                return gs;
            }
        }

        return GraphicsSetting.MEDIUM;
    }
    
    public GraphicsSetting next()
    {
        return GraphicsSetting.next(this);
    }

    public static GraphicsSetting next(GraphicsSetting setting)
    {
        if (setting == GraphicsSetting.LOW)
        {
            return GraphicsSetting.MEDIUM;
        }

        if (setting == GraphicsSetting.MEDIUM)
        {
            return GraphicsSetting.HIGH;
        }

        if (setting == GraphicsSetting.HIGH)
        {
            return GraphicsSetting.ULTRA;
        }

        return GraphicsSetting.LOW;
    }
}