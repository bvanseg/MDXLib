package com.arisux.mdxlib.config;

import net.minecraftforge.common.config.Property;

public class ConfigSettingGraphics extends ConfigSetting
{
    public ConfigSettingGraphics(IFlexibleConfiguration configuration, Property property)
    {
        super(configuration, property);
    }

    @Override
    public void toggle()
    {
        this.property().set(this.value().next().ordinal());
    }

    @Override
    public GraphicsSetting value()
    {
        return GraphicsSetting.level(property().getInt());
    }
}