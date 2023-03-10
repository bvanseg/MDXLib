package com.asx.mdx.common.minecraft.tile;

import net.minecraft.util.EnumFacing;

public interface IRotatableXAxis
{
    public EnumFacing getRotationXAxis();

    public void setRotationXAxis(EnumFacing facing);
}