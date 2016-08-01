package com.arisux.amdxlib.lib.world.tile;

import com.arisux.amdxlib.lib.world.CoordData;

import net.minecraft.tileentity.TileEntity;

public class TileEntities
{
    /**
     * Move the specified TileEntity to the specified CoordData coordinates.
     * 
     * @param tileEntity - The TileEntity to change the position of.
     * @param coord - The CoordData instance containing the new set of coordinates.
     */
    public static void setTileEntityPosition(TileEntity tileEntity, CoordData coord)
    {
        tileEntity.xCoord = (int) coord.posX;
        tileEntity.yCoord = (int) coord.posY;
        tileEntity.zCoord = (int) coord.posZ;
    }

    /**
     * Move the specified TileEntity by the specified amount of coordinates.
     * 
     * @param tileEntity - The TileEntity to be moved.
     * @param coord - The CoordData instance containing the coordinates to add.
     */
    public static void moveTileEntity(TileEntity tileEntity, CoordData coord)
    {
        setTileEntityPosition(tileEntity, (new CoordData(tileEntity)).add(coord));
    }

}