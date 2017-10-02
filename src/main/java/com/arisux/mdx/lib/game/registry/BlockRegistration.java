package com.arisux.mdx.lib.game.registry;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public abstract class BlockRegistration
{
    protected Block  block;
    protected String registryName;

    public BlockRegistration(String registryName, Block block)
    {
        this.registryName = registryName;
        this.block = block;
    }

    public Block applyModifiers(Block block)
    {
        return block;
    }

    public ItemBlock applyModifiers(ItemBlock itemblock)
    {
        return itemblock;
    }

    public Block registerBlock(RegistryEvent.Register<Block> event)
    {
        this.block = this.applyModifiers(block);
        block.setRegistryName(registryName);
        block.setUnlocalizedName(block.getRegistryName().toString());
        event.getRegistry().register(this.block);

        return this.block;
    }

    public ItemBlock registerItemBlock(RegistryEvent.Register<Item> event)
    {
        ItemBlock itemblock = this.applyModifiers(new ItemBlock(block));
        itemblock.setRegistryName(registryName);
        itemblock.setUnlocalizedName(itemblock.getRegistryName().toString());
        event.getRegistry().register(itemblock);

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            ModelResourceLocation modelResource = new ModelResourceLocation(itemblock.getRegistryName(), "inventory");
            ModelLoader.setCustomModelResourceLocation(itemblock, 0, modelResource);
        }

        return itemblock;
    }
}