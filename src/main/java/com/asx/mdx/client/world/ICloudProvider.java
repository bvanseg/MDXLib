package com.asx.mdx.client.world;

import com.asx.mdx.client.ClientGame;
import com.asx.mdx.client.render.model.texture.Texture;
import com.asx.mdx.common.Game;
import com.asx.mdx.common.math.MDXMath;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    
    @SideOnly(Side.CLIENT)
    public Texture getCloudTexture();
    
    public default double getCloudMovementX(World world, float cloudTicksPrev, float cloudTicks)
    {
        return MDXMath.interpolateRotation(cloudTicksPrev, cloudTicks, ClientGame.instance.partialTicks());
    }
    
    public default double getCloudMovementZ(World world, float cloudTicksPrev, float cloudTicks)
    {
        return 0;
    }
    
    public boolean areCloudsApplicableTo(WorldProvider provider);
}
