package com.arisux.mdxlib.lib.world.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class NBTStorage
{
    /**
     * Copies an ArrayList of Strings into an NBTTagList of Strings.
     * 
     * @param list - The ArrayList to be converted.
     * @return NBTTagList created from the ArrayList.
     */
    public static NBTTagList newStringNBTList(ArrayList<String> list)
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (int j = 0; j < list.size(); ++j)
        {
            String s1 = list.get(j);
            nbttaglist.appendTag(new NBTTagString(s1));
        }

        return nbttaglist;
    }

    /**
     * Copies an ArrayList of NBTTagCompounds into an NBTTagList of NBTTagCompounds.
     * 
     * @param list - The ArrayList to be converted.
     * @return NBTTagList created from the ArrayList.
     */
    public static NBTTagList newCompoundNBTList(ArrayList<NBTTagCompound> list)
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (int j = 0; j < list.size(); ++j)
        {
            NBTTagCompound nbt = list.get(j);
            nbttaglist.appendTag(nbt);
        }

        return nbttaglist;
    }

    /**
     * Writes an uncompressed NBTTagCompound to the specified File
     * 
     * @param nbt - The NBTTagCompound to be written.
     * @param file - The File to write to.
     */
    public static void write(NBTTagCompound nbt, File file)
    {
        try
        {
            CompressedStreamTools.write(nbt, file);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @param file - The File to read an NBTTagCompound from.
     * @return The uncompressed NBTTagCompound being read from the specified File.
     */
    public static NBTTagCompound read(File file)
    {
        NBTTagCompound nbtTagCompound = null;

        try
        {
            nbtTagCompound = CompressedStreamTools.read(file);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return nbtTagCompound;
    }

    /**
     * Writes a compressed NBTTagCompound to the specified File
     * 
     * @param nbt - The NBTTagCompound to be written.
     * @param file - The File to write to.
     * @throws IOException 
     */
    public static void writeCompressed(NBTTagCompound nbt, File file) throws IOException
    {
        FileOutputStream stream = new FileOutputStream(file);

        CompressedStreamTools.writeCompressed(nbt, stream);

        if (stream != null)
        {
            stream.close();
        }
    }

    /**
     * @param file - The File to read an NBTTagCompound from.
     * @return The compressed NBTTagCompound being read from the specified File.
     * @throws IOException 
     */
    public static NBTTagCompound readCompressed(File file) throws IOException
    {
        NBTTagCompound nbtTagCompound = null;
        FileInputStream stream = new FileInputStream(file);

        nbtTagCompound = CompressedStreamTools.readCompressed(stream);

        if (stream != null)
        {
            stream.close();
        }

        return nbtTagCompound;
    }
}