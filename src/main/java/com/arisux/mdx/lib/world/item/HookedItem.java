package com.arisux.mdx.lib.world.item;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HookedItem extends Item
{
    public HookedItem()
    {
        super();
    }

    public Item setDescription(String desc)
    {
        return this;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        String descriptionKey = String.format("%s.desc", this.getUnlocalizedName());

        if (descriptionKey != null)
        {
            String[] lines = I18n.format(descriptionKey).split("/n");

            for (String line : lines)
            {
                if (!line.equals(descriptionKey))
                {
                    list.add(line);
                }
            }
        }
    }
}
