package com.arisux.mdxlib.lib.world.block.util;

import java.util.ArrayList;
import java.util.Arrays;

import com.arisux.mdxlib.lib.game.Game;
import com.arisux.mdxlib.lib.world.block.BlockShape;
import com.arisux.mdxlib.lib.world.block.BlockShape.Shape;
import com.arisux.mdxlib.lib.world.block.Blocks;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;

public class ShapedBlocks
{
    public static class ShapedBlockRegistration
    {
        private Block[] array;

        public ShapedBlockRegistration(Block[] array)
        {
            this.array = array;
        }

        public ShapedBlockRegistration setCreativeTab(CreativeTabs tab)
        {
            for (Block block : new ArrayList<Block>(Arrays.asList(this.array)))
            {
                block.setCreativeTab(tab);
            }
            
            return this;
        }
        
        public Block[] array()
        {
            return this.array;
        }
    }
    
    public static ShapedBlockRegistration construct(Block block)
    {
        return construct(block, block);
    }

    public static ShapedBlockRegistration construct(Block blockParent, Block textureBlock)
    {
        BlockShape blockSlope = new BlockShape(Shape.SLOPE);
        BlockShape blockCorner = new BlockShape(Shape.CORNER);
        BlockShape blockInvertedCorner = new BlockShape(Shape.INVERTED_CORNER);
        BlockShape blockRidge = new BlockShape(Shape.RIDGE);
        BlockShape blockInvertedRidge = new BlockShape(Shape.INVERTED_RIDGE);
        BlockShape blockSmartInvertedRidge = new BlockShape(Shape.SMART_INVERTED_RIDGE);
        BlockShape blockSmartRidge = new BlockShape(Shape.SMART_RIDGE);

        applyPropertiesToShapedBlock(blockSlope, blockParent, textureBlock);
        applyPropertiesToShapedBlock(blockCorner, blockParent, textureBlock);
        applyPropertiesToShapedBlock(blockInvertedCorner, blockParent, textureBlock);
        applyPropertiesToShapedBlock(blockRidge, blockParent, textureBlock);
        applyPropertiesToShapedBlock(blockInvertedRidge, blockParent, textureBlock);
        applyPropertiesToShapedBlock(blockSmartInvertedRidge, blockParent, textureBlock);
        applyPropertiesToShapedBlock(blockSmartRidge, blockParent, textureBlock);

        Block[] array = new Block[] { blockParent, blockSlope, blockCorner, blockInvertedCorner, blockRidge, blockInvertedRidge, blockSmartInvertedRidge, blockSmartRidge };
        return new ShapedBlockRegistration(array);
    }

    public static void register(String modid, Block[] blocks, String identifier)
    {
        for (Block block : new ArrayList<Block>(Arrays.asList(blocks)))
        {
            String shapedIdentifier = identifier;

            if (block instanceof BlockShape)
            {
                switch (((BlockShape) block).getShape())
                {
                    case SLOPE:
                        shapedIdentifier = String.format("%s.slope", identifier);
                        break;

                    case CORNER:
                        shapedIdentifier = String.format("%s.corner", identifier);
                        break;

                    case INVERTED_CORNER:
                        shapedIdentifier = String.format("%s.invertedcorner", identifier);
                        break;

                    case RIDGE:
                        shapedIdentifier = String.format("%s.ridge", identifier);
                        break;

                    case INVERTED_RIDGE:
                        shapedIdentifier = String.format("%s.invertedridge", identifier);
                        break;

                    case SMART_INVERTED_RIDGE:
                        shapedIdentifier = String.format("%s.smartinvertedridge", identifier);
                        break;

                    case SMART_RIDGE:
                        shapedIdentifier = String.format("%s.smartridge", identifier);
                        break;
                }
            }

            Game.register(modid, block, shapedIdentifier);
        }
    }

    public static void applyPropertiesToShapedBlock(BlockShape shaped, Block blockParent, Block textureBlock)
    {
        shaped.setIconsFromBlock(textureBlock);

        if (blockParent != null)
        {
            shaped.setResistance(Blocks.getBlockResistance(blockParent));
            shaped.setHardness(Blocks.getBlockHardness(blockParent));
            shaped.setLightOpacity(blockParent.getDefaultState().getLightOpacity());
        }
    }
}
