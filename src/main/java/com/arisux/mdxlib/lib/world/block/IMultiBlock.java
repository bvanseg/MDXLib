package com.arisux.mdxlib.lib.world.block;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface IMultiBlock
{
    public BlockPos[] defaultSet();

    public default BlockPos[] setFor(EnumFacing facing)
    {
        switch (facing)
        {
            case NORTH:
                return north();
            case SOUTH:
                return south();
            case EAST:
                return east();
            case WEST:
                return west();
            default:
                return defaultSet();
        }
    }

    public default BlockPos[] north()
    {
        if (defaultSet() != null)
        {
            BlockPos[] set = defaultSet();
            BlockPos[] newSet = new BlockPos[set.length];
            int idx = 0;

            for (BlockPos pos : set)
            {
                newSet[idx++] = invert(pos);
            }

            return newSet;
        }
        return null;
    }

    public default BlockPos[] south()
    {
        return defaultSet();
    }

    public default BlockPos[] east()
    {
        if (defaultSet() != null)
        {
            BlockPos[] set = defaultSet();
            BlockPos[] newSet = new BlockPos[set.length];
            int idx = 0;

            for (BlockPos pos : set)
            {
                newSet[idx++] = flip(invert(pos));
            }

            return newSet;
        }
        return null;
    }

    public default BlockPos[] west()
    {
        if (defaultSet() != null)
        {
            BlockPos[] set = defaultSet();
            BlockPos[] newSet = new BlockPos[set.length];
            int idx = 0;

            for (BlockPos pos : set)
            {
                newSet[idx++] = flip(pos);
            }

            return newSet;
        }
        return null;
    }

    public static BlockPos flip(BlockPos pos)
    {
        return new BlockPos(pos.getZ(), pos.getY(), pos.getX());
    }

    public static BlockPos invert(BlockPos pos)
    {
        return new BlockPos(-pos.getX(), pos.getY(), -pos.getZ());
    }
}
