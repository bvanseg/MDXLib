package com.arisux.amdxlib.lib.world;

import java.util.Iterator;

public class CoordSelectionIterator implements Iterator<CoordData>
{
    private CoordData min;
    private CoordData max;

    private int curX;
    private int curY;
    private int curZ;

    public CoordSelectionIterator(CoordData min, CoordData max)
    {
        this.min = min;
        this.max = max;
        this.curX = (int) min.posX;
        this.curY = (int) min.posY;
        this.curZ = (int) min.posZ;
    }

    @Override
    public boolean hasNext()
    {
        return this.curX <= this.max.posX && this.curY <= this.max.posY && this.curZ <= this.max.posZ;
    }

    @Override
    public CoordData next()
    {
        CoordData coord = this.hasNext() ? new CoordData(this.curX, this.curY, this.curZ) : null;

        this.curX++;

        if (this.curX > this.max.posX)
        {
            this.curX = (int) this.min.posX;
            this.curY++;

            if (this.curY > this.max.posY)
            {
                this.curY = (int) this.min.posY;
                this.curZ++;
            }
        }

        return coord;
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}