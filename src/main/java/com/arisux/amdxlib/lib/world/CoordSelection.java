package com.arisux.amdxlib.lib.world;

import java.util.Iterator;

import net.minecraft.util.AxisAlignedBB;

public class CoordSelection implements Iterable<CoordData>
{
    private CoordData pos1;
    private CoordData pos2;

    public CoordSelection(CoordData pos1, CoordData pos2)
    {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public static CoordSelection areaFromSize(CoordData coord, int[] size)
    {
        if (size[0] <= 0 || size[1] <= 0 || size[2] <= 0)
        {
            throw new IllegalArgumentException();
        }

        return new CoordSelection(coord, new CoordData(coord.posX + size[0] - 1, coord.posY + size[1] - 1, coord.posZ + size[2] - 1));
    }

    public CoordData getPos1()
    {
        return pos1;
    }

    public void setPos1(CoordData point1)
    {
        this.pos1 = point1;
    }

    public CoordData getPos2()
    {
        return pos2;
    }

    public void setPos2(CoordData point2)
    {
        this.pos2 = point2;
    }

    public CoordData min()
    {
        return pos1.min(pos2);
    }

    public CoordData max()
    {
        return pos1.max(pos2);
    }

    public double[] areaSize()
    {
        return new double[] { max().subtract(min()).posX + 1, max().subtract(min()).posY + 1, max().subtract(min()).posZ + 1 };
    }

    public boolean contains(CoordData coord)
    {
        return coord.posX >= min().posX && coord.posY >= min().posY && coord.posZ >= min().posZ && coord.posX <= max().posX && coord.posY <= max().posY && coord.posZ <= max().posZ;
    }

    public AxisAlignedBB asAxisAlignedBB()
    {
        return AxisAlignedBB.getBoundingBox(min().posX, min().posY, min().posZ, max().posX, max().posY, max().posZ);
    }

    @Override
    public Iterator<CoordData> iterator()
    {
        return new CoordSelectionIterator(min(), max());
    }
}