package com.arisux.amdxlib.lib.world;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CoordData
{
    public double x, y, z;
    public int meta;
    public Block block;
    public UniqueIdentifier identifier;

    public CoordData(Entity entity)
    {
        this(Math.round(entity.posX), Math.round(entity.posY), Math.round(entity.posZ));
    }

    public CoordData(TileEntity tileEntity)
    {
        this(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, tileEntity.getWorld() != null ? tileEntity.getWorld().getBlock(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) : null, tileEntity.getWorld() != null ? tileEntity.getWorld().getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) : 0);
    }

    public CoordData(double posX, double posY, double posZ, Block block)
    {
        this(posX, posY, posZ, block, 0);
    }

    public CoordData(double posX, double posY, double posZ, Block block, double meta)
    {
        this(posX, posY, posZ, GameRegistry.findUniqueIdentifierFor(block));
        this.block = block;
    }

    public CoordData(double posX, double posY, double posZ, String id)
    {
        this(posX, posY, posZ, id, 0);
    }

    public CoordData(double posX, double posY, double posZ, String id, double meta)
    {
        this(posX, posY, posZ, new UniqueIdentifier(id), 0);
    }

    public CoordData(double posX, double posY, double posZ, UniqueIdentifier identifier)
    {
        this(posX, posY, posZ, identifier, 0);
    }

    public CoordData(double posX, double posY, double posZ, UniqueIdentifier identifier, int meta)
    {
        this.identifier = identifier;
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.meta = meta;
    }

    public CoordData(double posX, double posY, double posZ)
    {
        this.x = posX;
        this.y = posY;
        this.z = posZ;
    }

    public CoordData(long posX, long posY, long posZ)
    {
        this.x = (double) posX;
        this.y = (double) posY;
        this.z = (double) posZ;
    }

    public CoordData(float posX, float posY, float posZ)
    {
        this.x = (double) posX;
        this.y = (double) posY;
        this.z = (double) posZ;
    }

    public CoordData(int posX, int posY, int posZ)
    {
        this.x = (double) posX;
        this.y = (double) posY;
        this.z = (double) posZ;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || this != o || !(o instanceof CoordData) || o instanceof CoordData && ((CoordData) o).x != this.x || o instanceof CoordData && ((CoordData) o).y != this.y || o instanceof CoordData && ((CoordData) o).z != this.z)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = (int) this.x;
        result = 31 * result + (int) this.y;
        result = 31 * result + (int) this.z;
        return result;
    }

    public double x()
    {
        return x;
    }

    public double y()
    {
        return y;
    }

    public double z()
    {
        return z;
    }

    public Block getBlock(World world)
    {
        return getBlock(world, false);
    }

    public Block getBlock(World world, boolean force)
    {
        return this.block == null && world != null || force ? world.getBlock((int) this.x, (int) this.y, (int) this.z) : this.block;
    }

    public int getBlockMetadata(World world)
    {
        return this.meta == 0 && world != null ? world.getBlockMetadata((int) this.x, (int) this.y, (int) this.z) : 0;
    }

    public TileEntity getTileEntity(World world)
    {
        return world.getTileEntity((int) this.x, (int) this.y, (int) this.z);
    }

    public CoordData min(CoordData data)
    {
        return new CoordData(Math.min(this.x, data.x), Math.min(this.y, data.y), Math.min(this.z, data.z));
    }

    public CoordData max(CoordData data)
    {
        return new CoordData(Math.max(this.x, data.x), Math.max(this.y, data.y), Math.max(this.z, data.z));
    }

    public CoordData add(CoordData data)
    {
        return new CoordData(this.x + data.x, this.y + data.y, this.z + data.z);
    }

    public CoordData add(double posX, double posY, double posZ)
    {
        return this.add(new CoordData(posX, posY, posZ));
    }

    public CoordData subtract(CoordData data)
    {
        return new CoordData(this.max(data).x - this.min(data).x, this.max(data).y - this.min(data).y, this.max(data).z - this.min(data).z);
    }

    public CoordData subtract(double posX, double posY, double posZ)
    {
        return this.add(new CoordData(posX, posY, posZ));
    }

    public CoordData offsetX(double amount)
    {
        this.x = this.x + amount;
        return this;
    }

    public CoordData offsetY(double amount)
    {
        this.y = this.y + amount;
        return this;
    }

    public CoordData offsetZ(double amount)
    {
        this.z = this.z + amount;
        return this;
    }

    public void set(CoordData data)
    {
        this.identifier = data.identifier;
        this.block = data.block;
        this.x = data.x;
        this.y = data.y;
        this.z = data.z;
    }

    public UniqueIdentifier identity()
    {
        return this.identity(null);
    }

    public UniqueIdentifier identity(World world)
    {
        return this.identifier == null ? GameRegistry.findUniqueIdentifierFor(this.getBlock(world)) : this.identifier;
    }

    public NBTTagCompound writeToNBT()
    {
        return this.writeToNBT(null);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        return this.writeToNBT(nbt, "Id", "PosX", "PosY", "PosZ");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt, String labelId, String labelX, String labelY, String labelZ)
    {
        NBTTagCompound dataTag = nbt == null ? new NBTTagCompound() : nbt;

        dataTag.setString(labelId, String.format("%s:%s", this.identity().modId, this.identity().name));
        dataTag.setDouble(labelX, this.x);
        dataTag.setDouble(labelY, this.y);
        dataTag.setDouble(labelZ, this.z);

        return dataTag;
    }

    public CoordData readFromNBT(NBTTagCompound nbt)
    {
        return this.readFromNBT(nbt, "Id", "PosX", "PosY", "PosZ");
    }

    public CoordData readFromNBT(NBTTagCompound nbt, String labelId, String labelX, String labelY, String labelZ)
    {
        return new CoordData(nbt.getInteger(labelX), nbt.getInteger(labelY), nbt.getInteger(labelZ), nbt.getString(labelId));
    }

    @Override
    public String toString()
    {
        return String.format("CoordData/Coords[%s, %s, %s]/Block[%s:%s]/Object[%s]", this.x, this.y, this.z, this.block, this.meta, this.getClass());
    }

    public boolean isAnySurfaceVisible(World world)
    {
        Block air = net.minecraft.init.Blocks.air;
        
        CoordData up = this.add(0, 1, 0);
        CoordData down = this.add(0, -1, 0);
        CoordData left = this.add(-1, 0, 0);
        CoordData right = this.add(1, 0, 0);
        CoordData front = this.add(0, 0, -1);
        CoordData back = this.add(0, 0, 1);

        return up.getBlock(world) == air || down.getBlock(world) == air || left.getBlock(world) == air || right.getBlock(world) == air || front.getBlock(world) == air || back.getBlock(world) == air;
    }

    public CoordData findSafePosAround(World world)
    {
        CoordData pos = this;
        CoordData up = pos.add(0, 1, 0);
        CoordData down = pos.add(0, -1, 0);
        CoordData left = pos.add(-1, 0, 0);
        CoordData right = pos.add(1, 0, 0);
        CoordData front = pos.add(0, 0, -1);
        CoordData frontLeft = pos.add(-1, 0, -1);
        CoordData frontRight = pos.add(1, 0, -1);
        CoordData back = pos.add(0, 0, 1);
        CoordData backLeft = pos.add(-1, 0, 1);
        CoordData backRight = pos.add(1, 0, 1);

        if (pos.getBlock(world) != net.minecraft.init.Blocks.air)
        {
            if (left.getBlock(world) == net.minecraft.init.Blocks.air)
            {
                pos = left;
            }
            else if (right.getBlock(world) == net.minecraft.init.Blocks.air)
            {
                pos = right;
            }
            else if (front.getBlock(world) == net.minecraft.init.Blocks.air)
            {
                pos = front;
            }
            else if (frontLeft.getBlock(world) == net.minecraft.init.Blocks.air)
            {
                pos = frontLeft;
            }
            else if (frontRight.getBlock(world) == net.minecraft.init.Blocks.air)
            {
                pos = frontRight;
            }
            else if (back.getBlock(world) == net.minecraft.init.Blocks.air)
            {
                pos = left;
            }
            else if (backLeft.getBlock(world) == net.minecraft.init.Blocks.air)
            {
                pos = backLeft;
            }
            else if (backRight.getBlock(world) == net.minecraft.init.Blocks.air)
            {
                pos = backRight;
            }
            else if (up.getBlock(world) == net.minecraft.init.Blocks.air)
            {
                pos = up;
            }
            else if (down.getBlock(world) == net.minecraft.init.Blocks.air)
            {
                pos = down;
            }
        }

        return pos.add(0.5, 0.0, 0.5);
    }

    public static CoordData fromBytes(ByteBuf buf)
    {
        return new CoordData(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    public void toBytes(ByteBuf buf)
    {
        buf.writeDouble(this.x());
        buf.writeDouble(this.y());
        buf.writeDouble(this.z());
    }
}