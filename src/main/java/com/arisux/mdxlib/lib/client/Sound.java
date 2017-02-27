package com.arisux.mdxlib.lib.client;

import com.arisux.mdxlib.lib.world.Pos;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Sound
{
    private ResourceLocation location;
    private SoundEvent       event;
    private boolean          delay;

    public Sound(ResourceLocation location)
    {
        this(location, 1F, 1F);
    }

    public Sound(ResourceLocation location, float volume)
    {
        this(location, volume, 1F);
    }

    public Sound(ResourceLocation location, float volume, float pitch)
    {
        this.location = location;
        this.delay = true;
    }

    public void register(String soundName)
    {
        this.event = GameRegistry.register(new SoundEvent(this.location).setRegistryName(this.location));
    }

    public Sound setDistanceDelay(boolean delay)
    {
        this.delay = delay;
        return this;
    }

    public SoundEvent event()
    {
        return event;
    }

    @Deprecated
    public float getVolume()
    {
        return 1F;
    }

    @Deprecated
    private void setReflectedVolume(Float volume)
    {
    }

    @Deprecated
    public Sound setVolume(float volume)
    {
        return this;
    }

    @Deprecated
    public float getPitch()
    {
        return 1F;
    }

    @Deprecated
    public Sound setPitch(float pitch)
    {
        return this;
    }

    @Override
    public String toString()
    {
        return this.event.getRegistryName().toString();
    }

    @Deprecated
    public void playSound(Entity entity)
    {
        playSound(entity, 1F, 1F);
    }

    public void playSound(Entity entity, float volume, float pitch)
    {
        entity.playSound(event, volume, pitch);
    }

    @Deprecated
    public void playSound(World world, BlockPos pos)
    {
        playSound(world, (int) pos.getX(), (int) pos.getY(), (int) pos.getZ(), 1F, 1F);
    }

    public void playSound(World world, BlockPos pos, float volume, float pitch)
    {
        playSound(world, (int) pos.getX(), (int) pos.getY(), (int) pos.getZ(), volume, pitch);
    }

    @Deprecated
    public void playSound(World world, Pos pos)
    {
        playSound(world, (int) pos.x, (int) pos.y, (int) pos.z, 1F, 1F);
    }

    public void playSound(World world, Pos pos, float volume, float pitch)
    {
        playSound(world, (int) pos.x, (int) pos.y, (int) pos.z, volume, pitch);
    }

    @Deprecated
    public void playSound(World world, double x, double y, double z)
    {
        playSound(world, (int) x, (int) y, (int) z, 1F, 1F);
    }

    @Deprecated
    public void playSound(World world, int x, int y, int z)
    {
        playSound(world, x, y, z, 1F, 1F);
    }

    public void playSound(World world, double x, double y, double z, float volume, float pitch)
    {
        playSound(world, (int) x, (int) y, (int) z, volume, pitch, delay);
    }

    public void playSound(World world, int x, int y, int z, float volume, float pitch)
    {
        playSound(world, x, y, z, volume, pitch, delay);
    }

    public void playSound(World world, int x, int y, int z, float volume, float pitch, boolean distanceDelay)
    {
        world.playSound(x, y, z, this.event, SoundCategory.NEUTRAL, volume, pitch, distanceDelay);
    }
}
