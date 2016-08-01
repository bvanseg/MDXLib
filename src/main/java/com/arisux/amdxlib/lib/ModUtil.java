package com.arisux.amdxlib.lib;

import java.util.ArrayList;

import com.arisux.amdxlib.lib.game.IMod;
import com.arisux.amdxlib.lib.world.block.Blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

@Deprecated
public class ModUtil
{
    /**
     * Used for easier management of large quantities of Item and Block instances.
     */
    public static abstract class IBHandler
    {
        private IMod mod;
        private ArrayList<Object> objectList = new ArrayList<Object>();

        public IBHandler(IMod mod)
        {
            this.mod = mod;
        }

        public ArrayList<Object> getHandledObjects()
        {
            return objectList;
        }

        public IMod getMod()
        {
            return mod;
        }

        /**
         * Wrapper method for the registerBlock method found in ModEngine. Allows for simplified
         * registration of Blocks. Using this method will result in the Block being automatically assigned
         * a texture location, creative tab, and added to the ArrayList of objects in the specified IBHandler.
         * 
         * Automatically assigned texture IDs are set to the default resource location of Blocks in the
         * mod's domain. Texture names are based off of the Block's unlocalized name.
         * 
         * @param block - The Block instance to register.
         * @param reference - The reference ID to register the block under.
         * @return Returns the Block instances originally provided in the block parameter.
         */
        public Block registerBlock(Block block, String reference)
        {
            return registerBlock(block, reference, this.getMod().tab());
        }

        public Block registerBlock(Block block, String reference, CreativeTabs tab)
        {
            block.setUnlocalizedName(getMod().domain() + reference);
            block.setTextureName((block.getUnlocalizedName()).replace("tile.", ""));
            Blocks.setCreativeTab(block, tab);

            return GameRegistry.registerBlock(block, reference);
        }

        public Item registerItem(Item item, String reference)
        {
            return registerItem(item, reference, true, null);
        }

        /**
         * Wrapper method for the registerItem method found in ModEngine. Allows for simplified
         * registration of Items. Using this method will result in the Item being automatically assigned
         * a texture location, creative tab, and added to the ArrayList of objects in the specified IBHandler.
         * 
         * Automatically assigned texture IDs are set to the default resource location of Items in the
         * mod's domain. Texture names are based off of the Item's unlocalized name.
         * 
         * Unlocalized names are based off of the specified String reference IDs.
         * 
         * @param item - The Block instance to register.
         * @param reference - The reference ID to register the item under.
         * @param visibleOnPrimaryTab - If set to true, the Item will be automatically added to the CreativeTab
         * specified in the IBHandler. If set to false, you may specifiy a different CreativeTab instance for
         * the Item to be added to.
         * @param tab - If specified, the Item will be assigned to the specified CreativeTab. Set to null for
         * no creative tab.
         * @return Returns the Item instances originally provided in the item parameter.
         */
        public Item registerItem(Item item, String reference, boolean visibleOnPrimaryTab, CreativeTabs tab)
        {
            return ModUtil.registerItem(item, reference, this, visibleOnPrimaryTab, tab);
        }

        /**
         * Wrapper method for the registerItem method found in ModEngine. Allows for simplified
         * registration of Items. Using this method will result in the Item being automatically assigned
         * a texture location, and added to the ArrayList of objects in the specified IBHandler.
         * 
         * Automatically assigned texture IDs are set to the default resource location of Items in the
         * mod's domain. Texture names are based off of the Item's unlocalized name.
         * 
         * Unlocalized names are based off of the specified String reference IDs.
         * 
         * @param item - The Block instance to register.
         * @param reference - The reference ID to register the item under.
         * @param visibleOnPrimaryTab - If set to true, the Item will be automatically added to the CreativeTab
         * specified in the IBHandler. If set to false, you may specifiy a different CreativeTab instance for
         * the Item to be added to.
         * @return Returns the Item instances originally provided in the item parameter.
         */
        public Item registerItem(Item item, String reference, boolean visibleOnPrimaryTab)
        {
            return ModUtil.registerItem(item, reference, this, visibleOnPrimaryTab, null);
        }
    }

    /**
     * Wrapper method for the registerBlock method found in GameRegistry. Allows for simplified
     * registration of Blocks. Using this method will result in the Block being automatically assigned
     * a texture location, creative tab, and added to the ArrayList of objects in the specified IBHandler.
     * 
     * Automatically assigned texture IDs are set to the default resource location of Blocks in the
     * mod's domain. Texture names are based off of the Block's unlocalized name.
     * 
     * @param block - The Block instance to register.
     * @param reference - The reference ID to register the block under.
     * @param texture - The path to the texture assigned to this block.
     * @param handler - The IBHandler instance that is registerring this block.
     * @param visibleOnTab - If set true, the Block will automatically be registered to the CreativeTab
     * specified in the IBHandler instance this Block was registered from.
     * @param tab - The CreativeTabs instance this Block will be displayed on.
     * @return Returns the Block instances originally provided in the block parameter.
     */
    public static Block registerBlock(Block block, String reference, String texture, IBHandler handler, boolean visibleOnTab, CreativeTabs tab)
    {
        block.setUnlocalizedName(handler.getMod().domain() + reference);

        if (texture == null)
        {
            block.setTextureName((block.getUnlocalizedName()).replace("tile.", ""));
        }
        else
        {
            block.setTextureName(texture);
        }

        if (tab != null && visibleOnTab)
        {
            block.setCreativeTab(tab);
        }

        if (handler.getHandledObjects() != null)
        {
            handler.getHandledObjects().add(block);
        }

        GameRegistry.registerBlock(block, reference);

        return block;
    }

    /**
     * Wrapper method for the registerItem method found in GameRegistry. Allows for simplified
     * registration of Items. Using this method will result in the Item being automatically assigned
     * a texture location, creative tab, and added to the ArrayList of objects in the specified IBHandler.
     * 
     * Automatically assigned texture IDs are set to the default resource location of Items in the
     * mod's domain. Texture names are based off of the Item's unlocalized name.
     * 
     * Unlocalized names are based off of the specified String reference IDs.
     * 
     * @param item - The Block instance to register.
     * @param reference - The reference ID to register the item under.
     * @param handler - The IBHandler instance that is registerring this item.
     * @param visibleOnPrimaryTab - If set to true, the Item will be automatically added to the CreativeTab
     * specified in the IBHandler. If set to false, you may specifiy a different CreativeTab instance for
     * the Item to be added to.
     * @param tab - If specified, the Item will be assigned to the specified CreativeTab. Set to null for
     * no creative tab.
     * @return Returns the Item instances originally provided in the item parameter.
     */
    public static Item registerItem(Item item, String reference, IBHandler handler, boolean visibleOnPrimaryTab, CreativeTabs tab)
    {
        GameRegistry.registerItem(item, reference);

        item.setUnlocalizedName(handler.getMod().domain() + reference);
        item.setTextureName((item.getUnlocalizedName()).replace("item.", ""));

        if (handler.mod.tab() != null && visibleOnPrimaryTab)
        {
            item.setCreativeTab(handler.mod.tab());
        }
        else if (tab != null)
        {
            item.setCreativeTab(tab);
        }

        if (handler.getHandledObjects() != null)
        {
            handler.getHandledObjects().add(item);
        }

        return item;
    }
}
