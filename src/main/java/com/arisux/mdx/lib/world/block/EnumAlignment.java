package com.arisux.mdx.lib.world.block;

import net.minecraft.util.IStringSerializable;

//TODO: This may have the same functionality as @BlockSide.java
public enum EnumAlignment implements IStringSerializable
{
    TOP("top"), BOTTOM("bottom"), SIDE("side");

    private final String name;

    private EnumAlignment(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return this.name;
    }

    public String getName()
    {
        return this.name;
    }
}