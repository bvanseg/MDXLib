package com.arisux.mdx.lib.client.render.world;

import com.arisux.mdx.lib.client.render.Texture;
import com.arisux.mdx.lib.game.Game;
import com.arisux.mdx.lib.util.MDXMath;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

public interface ICloudProvider
{
    public float getCloudMovementSpeed(World world);
    
    public default float getMaxCloudSpeedDuringStorm()
    {
        return 12F;
    }
    
    public default float getMaxNormalCloudSpeed()
    {
        return 2F;
    }
    
    public Texture getCloudTexture();
    
    public default double getCloudMovementX(World world, float cloudTicksPrev, float cloudTicks)
    {
        return MDXMath.interpolateRotation(cloudTicksPrev, cloudTicks, Game.partialTicks());
    }
    
    public default double getCloudMovementZ(World world, float cloudTicksPrev, float cloudTicks)
    {
        return 0;
    }
    
    public boolean areCloudsApplicableTo(WorldProvider provider);
    
    /** Use this instead of vanilla's WorldProvider.getWeatherRenderer() due to incorrect render order **/
    public IStormProvider getStormProvider();
}
