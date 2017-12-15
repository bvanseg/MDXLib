package com.arisux.mdx.lib.client.render.world;

import com.arisux.mdx.lib.client.render.Texture;

import net.minecraft.world.World;

public interface ICloudProvider
{
    public float getCloudMovementSpeed(World world);
    
    public Texture getCloudTexture();
    
    public float getCloudMovementDirection(World world);
}
