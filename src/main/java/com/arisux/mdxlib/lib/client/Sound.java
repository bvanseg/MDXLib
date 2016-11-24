package com.arisux.mdxlib.lib.client;

import com.arisux.mdxlib.lib.world.CoordData;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class Sound
{
    private String  key;
    private float   volume;
    private float   pitch;
    private boolean distanceDelay;

    public Sound(String key)
    {
        this(key, 1F, 1F);
    }

    public Sound(String key, float volume)
    {
        this(key, volume, 1F);
    }

    public Sound(String key, float volume, float pitch)
    {
        this.key = key;
        this.volume = volume;
        this.pitch = pitch;
        this.distanceDelay = true;
    }

    public Sound setDistanceDelay(boolean distanceDelay)
    {
        this.distanceDelay = distanceDelay;
        return this;
    }

    public String getKey()
    {
        return key;
    }

    public float getVolume()
    {
        return volume;
    }
    
    private void setReflectedVolume(Float volume)
    {
        this.volume = volume;
    }

    public Sound setVolume(float volume)
    {
        this.volume = volume;
        return this;
    }

    public float getPitch()
    {
        return pitch;
    }

    public Sound setPitch(float pitch)
    {
        this.pitch = pitch;
        return this;
    }

    @Override
    public String toString()
    {
        return this.getKey();
    }

    public void playSound(Entity entity)
    {
        playSound(entity, volume, pitch);
    }

    public void playSound(Entity entity, float volume, float pitch)
    {
        entity.playSound(key, volume, pitch);
    }

    public void playSound(World world, CoordData data)
    {
        playSound(world, (int) data.x, (int) data.y, (int) data.z, volume, pitch);
    }

    public void playSound(World world, CoordData data, float volume, float pitch)
    {
        playSound(world, (int) data.x, (int) data.y, (int) data.z, volume, pitch);
    }

    public void playSound(World world, double posX, double posY, double posZ)
    {
        playSound(world, (int) posX, (int) posY, (int) posZ, volume, pitch);
    }

    public void playSound(World world, int posX, int posY, int posZ)
    {
        playSound(world, posX, posY, posZ, volume, pitch);
    }

    public void playSound(World world, double posX, double posY, double posZ, float volume, float pitch)
    {
        playSound(world, (int) posX, (int) posY, (int) posZ, volume, pitch, distanceDelay);
    }

    public void playSound(World world, int posX, int posY, int posZ, float volume, float pitch)
    {
        playSound(world, posX, posY, posZ, volume, pitch, distanceDelay);
    }

    public void playSound(World world, int posX, int posY, int posZ, float volume, float pitch, boolean distanceDelay)
    {
        world.playSound(posX, posY, posZ, key, volume, pitch, distanceDelay);
    }
}
