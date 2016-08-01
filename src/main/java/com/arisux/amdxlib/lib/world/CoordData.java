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
    public double posX, posY, posZ;
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
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.meta = meta;
    }

    public CoordData(double posX, double posY, double posZ)
    {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public CoordData(long posX, long posY, long posZ)
    {
        this.posX = (double) posX;
        this.posY = (double) posY;
        this.posZ = (double) posZ;
    }

    public CoordData(float posX, float posY, float posZ)
    {
        this.posX = (double) posX;
        this.posY = (double) posY;
        this.posZ = (double) posZ;
    }

    public CoordData(int posX, int posY, int posZ)
    {
        this.posX = (double) posX;
        this.posY = (double) posY;
        this.posZ = (double) posZ;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || this != o || !(o instanceof CoordData) || o instanceof CoordData && ((CoordData) o).posX != this.posX || o instanceof CoordData && ((CoordData) o).posY != this.posY || o instanceof CoordData && ((CoordData) o).posZ != this.posZ)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = (int) this.posX;
        result = 31 * result + (int) this.posY;
        result = 31 * result + (int) this.posZ;
        return result;
    }

    public double x()
    {
        return posX;
    }

    public double y()
    {
        return posY;
    }

    public double z()
    {
        return posZ;
    }

    public Block getBlock(World world)
    {
        return getBlock(world, false);
    }

    public Block getBlock(World world, boolean force)
    {
        return this.block == null && world != null || force ? world.getBlock((int) this.posX, (int) this.posY, (int) this.posZ) : this.block;
    }

    public int getBlockMetadata(World world)
    {
        return this.meta == 0 && world != null ? world.getBlockMetadata((int) this.posX, (int) this.posY, (int) this.posZ) : 0;
    }

    public TileEntity getTileEntity(World world)
    {
        return world.getTileEntity((int) this.posX, (int) this.posY, (int) this.posZ);
    }

    public CoordData min(CoordData data)
    {
        return new CoordData(Math.min(this.posX, data.posX), Math.min(this.posY, data.posY), Math.min(this.posZ, data.posZ));
    }

    public CoordData max(CoordData data)
    {
        return new CoordData(Math.max(this.posX, data.posX), Math.max(this.posY, data.posY), Math.max(this.posZ, data.posZ));
    }

    public CoordData add(CoordData data)
    {
        return new CoordData(this.posX + data.posX, this.posY + data.posY, this.posZ + data.posZ);
    }

    public CoordData add(double posX, double posY, double posZ)
    {
        return this.add(new CoordData(posX, posY, posZ));
    }

    public CoordData subtract(CoordData data)
    {
        return new CoordData(this.max(data).posX - this.min(data).posX, this.max(data).posY - this.min(data).posY, this.max(data).posZ - this.min(data).posZ);
    }

    public CoordData subtract(double posX, double posY, double posZ)
    {
        return this.add(new CoordData(posX, posY, posZ));
    }

    public CoordData offsetX(double amount)
    {
        this.posX = this.posX + amount;
        return this;
    }

    public CoordData offsetY(double amount)
    {
        this.posY = this.posY + amount;
        return this;
    }

    public CoordData offsetZ(double amount)
    {
        this.posZ = this.posZ + amount;
        return this;
    }

    public void set(CoordData data)
    {
        this.identifier = data.identifier;
        this.block = data.block;
        this.posX = data.posX;
        this.posY = data.posY;
        this.posZ = data.posZ;
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
        dataTag.setDouble(labelX, this.posX);
        dataTag.setDouble(labelY, this.posY);
        dataTag.setDouble(labelZ, this.posZ);

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
        return String.format("CoordData/Coords[%s, %s, %s]/Block[%s:%s]/Object[%s]", this.posX, this.posY, this.posZ, this.block, this.meta, this.getClass());
    }

    public boolean isAnySurfaceVisible(World world)
    {
        CoordData up = this.add(0, 1, 0);
        CoordData down = this.add(0, -1, 0);
        CoordData left = this.add(-1, 0, 0);
        CoordData right = this.add(1, 0, 0);
        CoordData front = this.add(0, 0, -1);
        CoordData back = this.add(0, 0, 1);

        return up.getBlock(world) == net.minecraft.init.Blocks.air || down.getBlock(world) == net.minecraft.init.Blocks.air || left.getBlock(world) == net.minecraft.init.Blocks.air || right.getBlock(world) == net.minecraft.init.Blocks.air || front.getBlock(world) == net.minecraft.init.Blocks.air || back.getBlock(world) == net.minecraft.init.Blocks.air;
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