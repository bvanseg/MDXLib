package com.arisux.mdx.lib.world.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HookedItem extends Item
{
    private String description;

    public Item setDescription(String desc)
    {
        this.description = desc;
        return this;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        if (description != null)
        {
            for (String line : this.description.split("\n"))
            {
                list.add(line);
            }
        }
    }
}
