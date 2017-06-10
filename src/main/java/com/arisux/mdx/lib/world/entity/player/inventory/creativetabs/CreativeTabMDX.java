package com.arisux.mdx.lib.world.entity.player.inventory.creativetabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class CreativeTabMDX extends CreativeTabs
{
    public static final CreativeTabs INSTANCE = new CreativeTabMDX();

    public CreativeTabMDX()
    {
        super("MDX");
    }

    @Override
    public Item getTabIconItem()
    {
        return Items.REPEATER;
    }
}
