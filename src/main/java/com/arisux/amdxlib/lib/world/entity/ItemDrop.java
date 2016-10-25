package com.arisux.amdxlib.lib.world.entity;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class ItemDrop
{
    private ItemStack itemstack;
    private int rate;
    
    /**
     * Created to better manage drop rates of itemstacks from entities. Drops can be managed in a single location versus per entity.
     * 
     * @param itemstack - The ItemStack instance this drop will consist of.
     * @param rate - The rate at which this item will drop. Entering 5 will result in the ItemStack dropping 5% of the time.
     */
    public ItemDrop(ItemStack itemstack, int rate)
    {
        this.itemstack = itemstack;
        this.rate = rate;
    }
    
    public ItemDrop tryDrop(Entity entity)
    {
        if (new Random().nextInt(100 / this.rate) == 0)
        {
            entity.entityDropItem(itemstack, 0F);
        }
        
        return this;
    }
    
    public ItemStack getItemstack()
    {
        return itemstack;
    }
    
    public int getRate()
    {
        return rate;
    }
}