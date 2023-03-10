package com.asx.mdx.common.io.config;

import net.minecraftforge.common.config.Property;

public class ConfigSettingDouble extends ConfigSetting
{
    public ConfigSettingDouble(IFlexibleConfiguration configuration, Property property)
    {
        super(configuration, property);
    }

    @Override
    public void toggle()
    {
        return;
    }

    @Override
    public Double value()
    {
        return property().getDouble();
    }
}