package com.arisux.mdxlib.lib.world;

import com.arisux.mdxlib.MDX;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

public class Dimension
{
    protected String                         name;
    protected String                         suffix;
    protected Class<? extends WorldProvider> provider;
    protected boolean                        keepLoaded;
    private int                              id;
    private DimensionType                    type;
    private boolean                          registered;

    public Dimension(String name, String suffix, Class<? extends WorldProvider> provider, boolean keepLoaded)
    {
        this.name = name;
        this.suffix = suffix;
        this.provider = provider;
        this.keepLoaded = keepLoaded;
    }

    public Dimension register()
    {
        if (!this.registered)
        {
            this.id = DimensionManager.getNextFreeDimId();
            this.type = DimensionType.register(this.name, this.suffix, this.id, this.provider, this.keepLoaded);
            DimensionManager.registerDimension(this.id, this.type);
            this.registered = true;
        }
        else
        {
            MDX.log().warn("Attempted to register dimension with name %s more than once. Registration attempt blocked.", name);
        }
        return this;
    }

    public DimensionType getType()
    {
        return type;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getSuffix()
    {
        return suffix;
    }

    public Class<? extends WorldProvider> getProvider()
    {
        return provider;
    }

    public boolean shouldKeepLoaded()
    {
        return keepLoaded;
    }
}
