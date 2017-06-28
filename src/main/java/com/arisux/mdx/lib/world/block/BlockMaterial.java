package com.arisux.mdx.lib.world.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMaterial extends Block
{
    private BlockRenderLayer renderLayer;
    
    public BlockMaterial(Material material)
    {
        super(material);
        this.renderLayer = BlockRenderLayer.SOLID;
        this.setLightOpacity(255);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return this.renderLayer;
    }

    public BlockMaterial setLayer(BlockRenderLayer renderLayer)
    {
        this.renderLayer = renderLayer;
        return this;
    }
}
