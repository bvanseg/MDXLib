package com.arisux.mdx.lib.client.render.world;

import com.arisux.mdx.lib.client.render.Texture;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public interface IStormProvider
{
    public boolean isStormActive(World world);
    
    public float getStormDensity();
    
    public int getStormSize();
    
    public boolean  isStormVisibleInBiome(Biome biome);
    
    public float getStormDownfallSpeed();
    
    public float getStormWindSpeed();
    
    public boolean doesLightingApply();
    
    public float getStormDirection();
    
    public Texture getStormTexture(World world, Biome biome);
}