package com.arisux.mdxlib.lib.world.tile;

import com.arisux.mdxlib.lib.world.Pos;

import net.minecraft.tileentity.TileEntity;

public class TileEntities
{
    /**
     * Move the specified TileEntity to the specified CoordData coordinates.
     * 
     * @param tileEntity - The TileEntity to change the position of.
     * @param coord - The CoordData instance containing the new set of coordinates.
     */
    public static void setTileEntityPosition(TileEntity tileEntity, Pos coord)
    {
        tileEntity.xCoord = (int) coord.x;
        tileEntity.yCoord = (int) coord.y;
        tileEntity.zCoord = (int) coord.z;
    }

    /**
     * Move the specified TileEntity by the specified amount of coordinates.
     * 
     * @param tileEntity - The TileEntity to be moved.
     * @param coord - The CoordData instance containing the coordinates to add.
     */
    public static void moveTileEntity(TileEntity tileEntity, Pos coord)
    {
        setTileEntityPosition(tileEntity, (new Pos(tileEntity)).add(coord));
    }

}