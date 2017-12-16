package com.arisux.mdx.lib.client.render.world;

import com.arisux.mdx.lib.client.render.Texture;
import com.arisux.mdx.lib.game.Game;
import com.arisux.mdx.lib.util.MDXMath;

import net.minecraft.world.World;

public interface ICloudProvider
{
    public float getCloudMovementSpeed(World world);
    
    public Texture getCloudTexture();
    
    public default double getCloudMovementX(World world, float cloudTicksPrev, float cloudTicks)
    {
        return MDXMath.interpolateRotation(cloudTicksPrev, cloudTicks, Game.partialTicks());
    }
    
    public default double getCloudMovementZ(World world, float cloudTicksPrev, float cloudTicks)
    {
        return 0;
    }
}
