package com.arisux.amdxlib.lib.game;

import java.util.ArrayList;
import java.util.List;

import com.arisux.amdxlib.AMDXLib;
import com.arisux.amdxlib.lib.world.entity.player.inventory.Inventories;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.Session;
import net.minecraftforge.common.MinecraftForge;

public class Game
{
    @SideOnly(Side.CLIENT)
    public static Minecraft minecraft()
    {
        return Minecraft.getMinecraft();
    }

    @SideOnly(Side.CLIENT)
    public static FontRenderer fontRenderer()
    {
        return Game.minecraft().fontRendererObj;
    }

    @SideOnly(Side.CLIENT)
    public static Session session()
    {
        return AMDXLib.access().getSession();
    }

    @SideOnly(Side.CLIENT)
    public static void setRightClickDelayTimer(int i)
    {
        AMDXLib.access().setRightClickDelayTimer(i);        
    }

    @SideOnly(Side.CLIENT)
    public static void setEquippedProgress(float f)
    {
        AMDXLib.access().setEquippedProgress(f);
    }

    /**
     * Returns if the current Minecraft installation is running 
     * in a development environment or normal environment.
     * 
     * @return Returns true if in a dev environment. Returns false if other.
     */
    public static boolean isDevEnvironment()
    {
        return (Boolean) net.minecraft.launchwrapper.Launch.blackboard.get("fml.deobfuscatedEnvironment");
    }
    
    public static void registerEventHandler(Object handler)
    {
        FMLCommonHandler.instance().bus().register(handler);
        MinecraftForge.EVENT_BUS.register(handler);
    }

    /**
     * Finds all IRecipe instances registered to a specific Item or Block instance.
     * 
     * @param obj - Item or Block instance to scan for recipes.
     * @return All found instances of IRecipes registered to the specified Item or Block.
     */
    @SuppressWarnings("unchecked")
    public static List<IRecipe> getRecipes(Object obj)
    {
        ItemStack stack = Inventories.newStack(obj);
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        List<IRecipe> foundRecipes = new ArrayList<IRecipe>();
    
        if (stack != null)
        {
            for (IRecipe recipe : recipes)
            {
                if (recipe != null && recipe.getRecipeOutput() != null && recipe.getRecipeOutput().getItem() == stack.getItem())
                {
                    foundRecipes.add(recipe);
                }
            }
        }
    
        return foundRecipes;
    }

    /**
     * Finds the first IRecipe instance registered to a specific Item or Block instance.
     * 
     * @param obj - Item or Block instance to scan for recipes.
     * @return First found instance of an IRecipe registered to the specified Item or Block.
     */
    @SuppressWarnings("unchecked")
    public static IRecipe getRecipe(Object obj)
    {
        ItemStack stack = Inventories.newStack(obj);
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
    
        if (stack != null)
        {
            for (IRecipe recipe : recipes)
            {
                if (recipe != null && recipe.getRecipeOutput() != null && recipe.getRecipeOutput().getItem() == stack.getItem())
                {
                    return recipe;
                }
            }
        }
    
        return null;
    }

    /**
     * Wrapper method for the registerKeyBinding method found in ClientRegistry. Allows for
     * more efficient registration of a KeyBinding. 
     * 
     * @param keyName - Name of the KeyBinding to be registered.
     * @param key - Integer assigned to each individual keyboard key in the Keyboard class.
     * @param keyGroup - Group of KeyBindings to assign this KeyBinding to.
     * @return The KeyBinding Instance created from the provided parameters.
     */
    @SideOnly(Side.CLIENT)
    public static KeyBinding registerKeybinding(String keyName, int key, String keyGroup)
    {
        KeyBinding keybind = new KeyBinding(String.format("key.%s", keyName), key, keyGroup);
        ClientRegistry.registerKeyBinding(keybind);
        return keybind;
    }

    /** 
     * Retrieve the ModContainer instance for a mod with the specified ID.
     * 
     * @param id - ID of the mod retrieving an instance from.
     * @return An instance of ModContainer that is assigned to this ID.
     */
    public static ModContainer getModContainer(String id)
    {
        for (ModContainer container : Loader.instance().getModList())
        {
            if (container.getModId().equalsIgnoreCase(id))
            {
                return container;
            }
        }
    
        return null;
    }

    public static final String getAnnotatedModId(Class<?> clazz)
    {
        if (clazz.isAnnotationPresent(Mod.class))
        {
            Mod mod = clazz.getAnnotation(Mod.class);
    
            return mod.modid();
        }
    
        return null;
    }
}
