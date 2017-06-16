package com.arisux.mdx.lib.game;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public abstract class BlockRegistrationHandler<MOD extends IMod> extends RegistrationHandler<MOD>
{
    public BlockRegistrationHandler(MOD mod)
    {
        super(mod);
    }

    public Block register(String identifier, Block block)
    {
        block.setUnlocalizedName(String.format("%s:%s", mod.container().getModId(), identifier));

        GameRegistry.register(block, new ResourceLocation(mod.container().getModId(), identifier));
        GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            Item item = Item.getItemFromBlock(block);
            ModelResourceLocation modelResource = new ModelResourceLocation(item.getRegistryName(), "inventory");
            ModelLoader.setCustomModelResourceLocation(item, 0, modelResource);
        }

        return block;
    }
}
