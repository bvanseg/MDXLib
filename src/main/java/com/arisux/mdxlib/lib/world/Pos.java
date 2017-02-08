package com.arisux.mdxlib.lib.world;

import java.util.ArrayList;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class Pos
{
    public double  x;
    public double  y;
    public double  z;
    private IStorable stored;
    
    public static interface IStorable
    {
        ;
    }
    
    public static class BlockDataStore implements IStorable
    {
        public String blockid;
        public byte metadata;
        
        public BlockDataStore(Block block, byte metadata)
        {
            this(identity(block), metadata);
        }
        
        public BlockDataStore(UniqueIdentifier uid, byte metadata)
        {
            this(uid.toString(), metadata);
        }
        
        public BlockDataStore(String blockid, byte metadata)
        {
            this.blockid = blockid;
            this.metadata = metadata;
        }

        public static UniqueIdentifier identity(Block block)
        {
            return GameRegistry.findUniqueIdentifierFor(block);
        }
    }

    public Pos(Entity entity)
    {
        this(Math.round(entity.posX), Math.round(entity.posY), Math.round(entity.posZ));
    }

    public Pos(TileEntity tileEntity)
    {
        this(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
    }

    public Pos(double posX, double posY, double posZ)
    {
        this.x = posX;
        this.y = posY;
        this.z = posZ;
    }

    public Pos(long posX, long posY, long posZ)
    {
        this((double) posX, (double) posY, (double) posZ);
    }

    public Pos(float posX, float posY, float posZ)
    {
        this((double) posX, (double) posY, (double) posZ);
    }

    public Pos(int posX, int posY, int posZ)
    {
        this((double) posX, (double) posY, (double) posZ);
    }
    
    public Pos store(IStorable store)
    {
        this.stored = store;
        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o != null && o instanceof Pos)
        {
            Pos test = (Pos) o;

            if (this == test || test.x == this.x && test.y == this.y && test.z == this.z)
            {
                return true;
            }
        }

        return false;
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
        return world.getBlock((int) this.x, (int) this.y, (int) this.z);
    }

    public int getBlockMetadata(World world)
    {
        return world != null ? world.getBlockMetadata((int) this.x, (int) this.y, (int) this.z) : 0;
    }

    public TileEntity getTileEntity(World world)
    {
        return world.getTileEntity((int) this.x, (int) this.y, (int) this.z);
    }

    public Pos min(Pos data)
    {
        return new Pos(Math.min(this.x, data.x), Math.min(this.y, data.y), Math.min(this.z, data.z));
    }

    public Pos max(Pos data)
    {
        return new Pos(Math.max(this.x, data.x), Math.max(this.y, data.y), Math.max(this.z, data.z));
    }

    public Pos add(Pos data)
    {
        return new Pos(this.x + data.x, this.y + data.y, this.z + data.z);
    }

    public Pos add(double posX, double posY, double posZ)
    {
        return this.add(new Pos(posX, posY, posZ));
    }

    public Pos subtract(Pos data)
    {
        return new Pos(this.max(data).x - this.min(data).x, this.max(data).y - this.min(data).y, this.max(data).z - this.min(data).z);
    }

    public Pos subtract(double posX, double posY, double posZ)
    {
        return this.add(new Pos(posX, posY, posZ));
    }

    public Pos offsetX(double amount)
    {
        this.x = this.x + amount;
        return this;
    }

    public Pos offsetY(double amount)
    {
        this.y = this.y + amount;
        return this;
    }

    public Pos offsetZ(double amount)
    {
        this.z = this.z + amount;
        return this;
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
        
        if (this.stored != null)
        {
            if (this.stored instanceof IStorable)
            {
                if (this.stored instanceof BlockDataStore)
                {
                    BlockDataStore blockdata = (BlockDataStore) this.stored;
                    dataTag.setString(labelId, blockdata.toString());
                }
            }
        }

        dataTag.setDouble(labelX, this.x);
        dataTag.setDouble(labelY, this.y);
        dataTag.setDouble(labelZ, this.z);

        return dataTag;
    }

    public Pos readFromNBT(NBTTagCompound nbt)
    {
        return this.readFromNBT(nbt, "Id", "PosX", "PosY", "PosZ");
    }

    public Pos readFromNBT(NBTTagCompound nbt, String labelId, String labelX, String labelY, String labelZ)
    {
        return readFromNBT(nbt, labelId, labelX, labelY, labelZ, "Meta");
    }
    
    public Pos readFromNBT(NBTTagCompound nbt, String labelId, String labelX, String labelY, String labelZ, String labelMeta)
    {
        return new Pos(nbt.getInteger(labelX), nbt.getInteger(labelY), nbt.getInteger(labelZ)).store(new BlockDataStore(nbt.getString(labelId), nbt.getByte(labelMeta)));
    }

    @Override
    public String toString()
    {
        return String.format("CoordData[%s, %s, %s]/Object[%s]", this.x, this.y, this.z, this.stored);
    }

    public boolean isAnySurfaceEmpty(World world)
    {
        return isAnySurfaceNextTo(world, net.minecraft.init.Blocks.air);
    }

    public boolean isAnySurfaceNextTo(World world, Block block)
    {
        Pos up = this.add(0, 1, 0);
        Pos down = this.add(0, -1, 0);
        Pos left = this.add(-1, 0, 0);
        Pos right = this.add(1, 0, 0);
        Pos front = this.add(0, 0, -1);
        Pos back = this.add(0, 0, 1);

        return up.getBlock(world) == block || down.getBlock(world) == block || left.getBlock(world) == block || right.getBlock(world) == block || front.getBlock(world) == block || back.getBlock(world) == block;
    }

    public Pos findSafePosAround(World world)
    {
        Pos pos = this;
        Pos up = pos.add(0, 1, 0);
        Pos down = pos.add(0, -1, 0);
        Pos left = pos.add(-1, 0, 0);
        Pos right = pos.add(1, 0, 0);
        Pos front = pos.add(0, 0, -1);
        Pos frontLeft = pos.add(-1, 0, -1);
        Pos frontRight = pos.add(1, 0, -1);
        Pos back = pos.add(0, 0, 1);
        Pos backLeft = pos.add(-1, 0, 1);
        Pos backRight = pos.add(1, 0, 1);

        if (pos.getBlock(world) != net.minecraft.init.Blocks.air)
        {
            if (left.getBlock(world) == net.minecraft.init.Blocks.air)
                pos = left;
            else if (right.getBlock(world) == net.minecraft.init.Blocks.air)
                pos = right;
            else if (front.getBlock(world) == net.minecraft.init.Blocks.air)
                pos = front;
            else if (frontLeft.getBlock(world) == net.minecraft.init.Blocks.air)
                pos = frontLeft;
            else if (frontRight.getBlock(world) == net.minecraft.init.Blocks.air)
                pos = frontRight;
            else if (back.getBlock(world) == net.minecraft.init.Blocks.air)
                pos = left;
            else if (backLeft.getBlock(world) == net.minecraft.init.Blocks.air)
                pos = backLeft;
            else if (backRight.getBlock(world) == net.minecraft.init.Blocks.air)
                pos = backRight;
            else if (up.getBlock(world) == net.minecraft.init.Blocks.air)
                pos = up;
            else if (down.getBlock(world) == net.minecraft.init.Blocks.air)
                pos = down;
        }

        return pos.add(0.5, 0.0, 0.5);
    }

    public Pos writeToBuffer(ByteBuf buf)
    {
        buf.writeDouble(this.x());
        buf.writeDouble(this.y());
        buf.writeDouble(this.z());
        return this;
    }
    
    public Pos readFromBuffer(ByteBuf buf)
    {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        return this;
    }

    public Pos divide(int i)
    {
        this.x = this.x / i;
        this.y = this.y / i;
        this.z = this.z / i;

        return this;
    }

    public Pos half()
    {
        return this.divide(2);
    }

    public Pos remainder(int i)
    {
        this.x = this.x % i;
        this.y = this.y % i;
        this.z = this.z % i;

        return this;
    }
    
    public double distanceFrom(Pos coord)
    {
        return distance(coord.x, coord.y, coord.z, this.x, this.y, this.z);
    }

    public double distanceSqFrom(Pos coord)
    {
        return distanceSq(coord.x, coord.y, coord.z, this.x, this.y, this.z);
    }

    public double distanceFrom(Entity entity)
    {
        return distance(entity.posX, entity.posY, entity.posZ, this.x, this.y, this.z);
    }

    public double distanceSqFrom(Entity entity)
    {
        return distanceSq(entity.posX, entity.posY, entity.posZ, this.x, this.y, this.z);
    }
    
    /**
     * STATIC ACCESS METHODS
     */
    
    /**
     * Generates an arraylist of equally segmented coodinates between the two specified coordinates.
     * (x, y, z) = (x1 + (sectionIndex / sectionsMax) * (x2 - x1), y1 + (sectionIndex / sectionsMax) * (y2 - y1), z1 + (sectionIndex / sectionsMax) * (z2 - z1))
     * 
     * @param p1 - Point 1, the starting point of the line segment.
     * @param p2 - Point 2, the end point of the line segment.
     * @param sections - The amount of times this line segment will be split.
     * @return The coodinates of each point that the line segment was split at.
     */
    public static ArrayList<Pos> getPointsBetween(Pos p1, Pos p2, int sections)
    {
        ArrayList<Pos> points = new ArrayList<Pos>();

        for (int section = sections; section > 0; section--)
        {
            float s = ((float) section / sections);
            double x = p1.x + (s * (p2.x - p1.x));
            double y = p1.y + (s * (p2.y - p1.y));
            double z = p1.z + (s * (p2.z - p1.z));

            points.add(new Pos(x, y, z));
        }

        return points;
    }
    
    public static double distance(Pos p1, Pos p2)
    {
        return distance(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z);
    }
    
    public static double distanceSq(Pos p1, Pos p2)
    {
        return distanceSq(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z);
    }

    public static double distance(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        return (double) MathHelper.sqrt_double(distanceSq(x1, y1, z1, x2, y2, z2));
    }
    
    public static double distanceSq(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        double x = x1 - x2;
        double y = y1 - y2;
        double z = z1 - z2;
        return x * x + y * y + z * z;
    }
}
