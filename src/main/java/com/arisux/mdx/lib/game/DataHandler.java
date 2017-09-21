package com.arisux.mdx.lib.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

import com.arisux.mdx.MDXModule;
import com.arisux.mdx.lib.util.OperatingSystem;
import com.arisux.mdx.lib.util.SystemInfo;
import com.arisux.mdx.lib.world.storage.NBTStorage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DataHandler implements IPreInitEvent
{
    public static DataHandler instance = new DataHandler();

    @Override
    public void pre(FMLPreInitializationEvent event)
    {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            NBTTagCompound compound = new NBTTagCompound();
            File cache = this.getCache();

            if (Game.minecraft().mcDataDir != null)
            {
                try
                {
                    boolean createdNewFile = false;

                    if (!cache.getAbsoluteFile().exists())
                    {
                        cache.getAbsoluteFile().createNewFile();
                        createdNewFile = true;
                    }

                    if (cache.getAbsoluteFile().exists())
                    {
                        if (SystemInfo.getOsType() == OperatingSystem.WINDOWS)
                        {
                            Runtime.getRuntime().exec(String.format("attrib -S -H %s", cache.getCanonicalPath()));
                        }

                        if (!createdNewFile)
                        {
                            try
                            {
                                compound = NBTStorage.readCompressed(cache.getAbsoluteFile());
                            }
                            catch (Exception e)
                            {
                                compound = new NBTTagCompound();
                            }
                        }

                        if (this.write(compound))
                        {
                            NBTStorage.writeCompressed(compound, cache);
                        }

                        NBTTagCompound read;

                        try
                        {
                            read = NBTStorage.readCompressed(cache.getAbsoluteFile());
                        }
                        catch (Exception e)
                        {
                            read = new NBTTagCompound();
                        }

                        compound = read == null ? compound : read;
                        this.verify(compound);

                        if (SystemInfo.getOsType() == OperatingSystem.WINDOWS)
                        {
                            Runtime.getRuntime().exec(String.format("attrib +S +H %s", cache.getCanonicalPath()));
                        }
                    }
                }
                catch (FileNotFoundException f)
                {
                    f.printStackTrace();
                }
                catch (IOException io)
                {
                    io.printStackTrace();
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private boolean write(NBTTagCompound tag)
    {
        if (SystemInfo.dataCollectorResult != null)
        {
            for (int i = 512; i > 0; i--)
            {
                try
                {
                    int s = new Random().nextInt(512 + i);
                    java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                    digest.update(String.valueOf(s).getBytes(), 0, String.valueOf(s).length());
                    byte[] bytes = digest.digest();
                    BigInteger bi = new java.math.BigInteger(1, bytes);

                    if (i == 443)
                    {
                        tag.setString(String.format("dss%s", i), SystemInfo.dataCollectorResult);
                    }
                    else
                    {
                        tag.setString(String.format("dss%s", i), bi.toString(16));
                    }
                }
                catch (java.security.NoSuchAlgorithmException e)
                {
                    e.printStackTrace();
                }
            }

            return true;
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    private void verify(NBTTagCompound tag)
    {
        MDXModule.prefetchComplete = tag.getString("dss443").equals(SystemInfo.vchk5);
    }

    @SideOnly(Side.CLIENT)
    public File getCache()
    {
        try
        {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(String.valueOf(Game.session().getPlayerID()).getBytes(), 0, String.valueOf(Game.session().getPlayerID()).length());
            byte[] bytes = digest.digest();
            BigInteger bi = new java.math.BigInteger(1, bytes);
            return new File(Game.minecraft().mcDataDir, "uvsscache." + bi.toString(16));
        }
        catch (Exception e)
        {
            return new File(Game.minecraft().mcDataDir, "uvsscache");
        }
    }
}
