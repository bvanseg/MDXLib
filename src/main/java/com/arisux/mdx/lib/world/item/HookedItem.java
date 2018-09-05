package com.arisux.mdx.lib.world.item;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
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
    
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        String descriptionKey = String.format("%s.desc", this.getUnlocalizedName());

        if (descriptionKey != null)
        {
            String[] lines = I18n.format(descriptionKey).split("/n");

            for (String line : lines)
            {
                if (!line.equals(descriptionKey))
                {
                    tooltip.add(line);
                }
            }
        }
    }
}
