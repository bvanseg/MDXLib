package com.arisux.amdxlib.lib.world.block;

import java.util.ArrayList;
import java.util.Arrays;

import com.arisux.amdxlib.AMDXLib;
import com.arisux.amdxlib.lib.world.CoordData;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;

public class Blocks
{
    public static ArrayList<CoordData> getCoordDataInRange(int posX, int posY, int posZ, int range)
    {
        ArrayList<CoordData> data = new ArrayList<CoordData>();

        for (int x = posX - range; x < posX + range * 2; x++)
        {
            for (int y = posY - range; y < posY + range * 2; y++)
            {
                for (int z = posZ - range; z < posZ + range * 2; z++)
                {
                    data.add(new CoordData(x, y, z));
                }
            }
        }

        return data;
    }

    public static ArrayList<CoordData> getCoordDataInRangeForBlocks(int posX, int posY, int posZ, int range, World world, Block... types)
    {
        ArrayList<CoordData> data = new ArrayList<CoordData>();

        for (int x = posX - range; x < posX + range * 2; x++)
        {
            for (int y = posY - range; y < posY + range * 2; y++)
            {
                for (int z = posZ - range; z < posZ + range * 2; z++)
                {
                    CoordData coordData = new CoordData(x, y, z);
                    Block block = coordData.getBlock(world);

                    if (Arrays.asList(types).contains(block))
                    {
                        data.add(coordData);
                    }
                }
            }
        }

        return data;
    }

    public static ArrayList<CoordData> getCoordDataInRangeForBlocksExcluding(int posX, int posY, int posZ, int range, World world, Block... types)
    {
        ArrayList<CoordData> data = new ArrayList<CoordData>();

        for (int x = posX - range; x < posX + range * 2; x++)
        {
            for (int y = posY - range; y < posY + range * 2; y++)
            {
                for (int z = posZ - range; z < posZ + range * 2; z++)
                {
                    CoordData coordData = new CoordData(x, y, z);
                    Block block = coordData.getBlock(world);

                    if (!Arrays.asList(types).contains(block))
                    {
                        data.add(coordData);
                    }
                }
            }
        }

        return data;
    }

    public static String getDomain(Block block)
    {
        String domain = "minecraft:";

        if (block.getUnlocalizedName().contains(":"))
        {
            domain = (block.getUnlocalizedName().split(":")[0] + ":").replace("tile.", "");
        }

        return domain;
    }

    public static void setCreativeTab(Block block, CreativeTabs tab)
    {
        if (tab != null)
        {
            block.setCreativeTab(tab);
        }
    }

    public static float getBlockResistance(Block blockParent)
    {
        return AMDXLib.access().getBlockResistance(blockParent);
    }

    public static float getBlockHardness(Block blockParent)
    {
        return AMDXLib.access().getBlockResistance(blockParent);
    }
}