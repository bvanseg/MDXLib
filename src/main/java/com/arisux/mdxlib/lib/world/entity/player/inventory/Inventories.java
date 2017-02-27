package com.arisux.mdxlib.lib.world.entity.player.inventory;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Inventories
{
    public static void dropItemsInAt(IInventory inv, World world, double xCoord, double yCoord, double zCoord)
    {
        if (inv != null)
        {
            for (byte i = 0; i < inv.getSizeInventory(); i++)
            {
                ItemStack stack = inv.getStackInSlot(i);

                if (stack != null)
                {
                    EntityItem entity = new EntityItem(world, xCoord + world.rand.nextFloat(), yCoord + world.rand.nextFloat(), zCoord + world.rand.nextFloat(), stack);
                    float velocity = 0.05F;

                    entity.motionX = (-0.5F + world.rand.nextFloat()) * velocity;
                    entity.motionY = (4F + world.rand.nextFloat()) * velocity;
                    entity.motionZ = (-0.5F + world.rand.nextFloat()) * velocity;

                    world.spawnEntityInWorld(entity);
                }
            }
        }
    }

    /**
     * Get the slot id of the specified item in the specified inventory.
     * 
     * @param item - The item we are getting the slot id of.
     * @param inventory - The inventory of which we are searching in.
     * @return
     */
    public static int getSlotForItemIn(Item item, InventoryPlayer inventory)
    {
        for (int id = 0; id < inventory.getSizeInventory(); ++id)
        {
            if (inventory.getStackInSlot(id) != null && inventory.getStackInSlot(id).getItem() == item)
            {
                return id;
            }
        }

        return -1;
    }

    /**
     * @param items - List of Items to choose a random Item from.
     * @return an ItemStack instance instantaniated from a random Item chosen from the provided Item Array.
     */
    public static ItemStack randomItemStackFromArray(Item[] items)
    {
        return randomItemStackFromArray(items, new Random());
    }

    /**
     * @param items - List of Items to choose a random Item from.
     * @param rand - Random instance to use
     * @return an ItemStack instance instantaniated from a random Item chosen from the provided Item Array.
     */
    public static ItemStack randomItemStackFromArray(Item[] items, Random rand)
    {
        return newStack(items[rand.nextInt(items.length)]);
    }

    /** 
     * @param obj - Item or Block instance 
     * @return A new ItemStack instance of the specified Object
     **/
    public static ItemStack newStack(Object obj)
    {
        return newStack(obj, 1);
    }

    /** 
     * @param obj - Item or Block instance 
     * @param amount - Amount of Items in this ItemStack
     * @return A new ItemStack instance of the specified Object
     **/
    public static ItemStack newStack(Object obj, int amount)
    {
        return obj instanceof Block ? new ItemStack((Block) obj, amount) : obj instanceof Item ? new ItemStack((Item) obj, amount) : null;
    }

    /** 
     * @param stack - Instance of ItemStack 
     * @return the Item instance of the specified ItemStack
     **/
    public static Item getItem(ItemStack stack)
    {
        return stack.getItem();
    }

    /**
     * @param stack - Instance of ItemStack 
     * @return the Block instance of the specified ItemStack
     **/
    public static Block getBlock(ItemStack stack)
    {
        return Block.getBlockFromItem(stack.getItem());
    }

    /**
     * Consumes a single item of the specified type from the specified player's inventory.
     * Only comsumes an item if the player is in survival mode.
     * 
     * @param player - The player to consume an item from.
     * @param item - The item to consume.
     */
    public static boolean consumeItem(EntityPlayer player, Item item)
    {
        return consumeItem(player, item, false);
    }

    /**
     * Consumes a single item of the specified type from the specified player's inventory.
     * Only comsumes an item if the player is in survival mode, unless forced.
     * 
     * @param player - The player to consume an item from.
     * @param item - The item to consume.
     * @param force - Forces an item to be consumed, regardless of gamemode.
     */
    public static boolean consumeItem(EntityPlayer player, Item item, boolean force)
    {
        if (!player.capabilities.isCreativeMode || force)
        {
            int i = getSlotFor(player.inventory, item);

            if (i < 0)
            {
                return false;
            }
            else
            {
                if (--player.inventory.mainInventory[i].stackSize <= 0)
                {
                    player.inventory.mainInventory[i] = null;
                }

                return true;
            }
        }

        return true;
    }
    
    public static int getSlotFor(InventoryPlayer inventory, Item item)
    {
        for (int i = 0; i < inventory.mainInventory.length; ++i)
        {
            if (inventory.mainInventory[i] != null && inventory.mainInventory[i].getItem() == item)
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns the ItemStack residing in the specified player's helm slot.
     * 
     * @param player - The player to return an ItemStack from
     * @return An ItemStack residing in the helm slot.
     */
    public static ItemStack getHelmSlotItemStack(EntityPlayer player)
    {
        return player.inventory.armorItemInSlot(3);
    }

    /**
     * Returns the ItemStack residing in the specified player's chestplate slot.
     * 
     * @param player - The player to return an ItemStack from
     * @return An ItemStack residing in the chestplate slot.
     */
    public static ItemStack getChestSlotItemStack(EntityPlayer player)
    {
        return player.inventory.armorItemInSlot(2);
    }

    /**
     * Returns the ItemStack residing in the specified player's leggings slot.
     * 
     * @param player - The player to return an ItemStack from
     * @return An ItemStack residing in the leggings slot.
     */
    public static ItemStack getLegsSlotItemStack(EntityPlayer player)
    {
        return player.inventory.armorItemInSlot(1);
    }

    /**
     * Returns the ItemStack residing in the specified player's boots slot.
     * 
     * @param player - The player to return an ItemStack from
     * @return An ItemStack residing in the boots slot.
     */
    public static ItemStack getBootSlotItemStack(EntityPlayer player)
    {
        return player.inventory.armorItemInSlot(0);
    }

    public static int getAmountOfItemPlayerHas(Item item, EntityPlayer player)
    {
        int amount = 0;

        for (ItemStack stack : player.inventory.mainInventory)
        {
            if (stack != null && stack.getItem() == item)
            {
                amount += stack.stackSize;
            }
        }

        return amount;
    }
}