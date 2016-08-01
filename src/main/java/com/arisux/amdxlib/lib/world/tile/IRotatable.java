package com.arisux.amdxlib.lib.world.tile;

import net.minecraftforge.common.util.ForgeDirection;

public interface IRotatable
{
    public ForgeDirection getDirection();

    public void setDirection(ForgeDirection facing);
}
