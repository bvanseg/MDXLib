package com.asx.mdx.client;

import com.asx.mdx.common.Game;
import com.asx.mdx.internal.MDX;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.Session;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A class designed to provide easy access to a variety of Minecraft core methods, some
 * of which may naturally have restricted access. The names of these methods will never change.
 */
public class ClientGame extends Game
{
    public static final ClientGame instance = new ClientGame();

    private ClientGame() { super(); }

    /** A wrapper method to get the Minecraft instance. **/
    @SideOnly(Side.CLIENT)
    public Minecraft minecraft()
    {
        return Minecraft.getMinecraft();
    }

    /** A wrapper method for the RenderManager instance. **/
    @SideOnly(Side.CLIENT)
    public RenderManager renderManager()
    {
        return minecraft().getRenderManager();
    }

    /** A wrapper method for the FontRenderer instance. **/
    @SideOnly(Side.CLIENT)
    public FontRenderer fontRenderer()
    {
        return this.minecraft().fontRenderer;
    }

    /** Easy access to the partialTickTime variable. **/
    @SideOnly(Side.CLIENT)
    public float partialTicks()
    {
        return Animation.getPartialTickTime();
    }

    /** Access to the Session instance. Please do not abuse this. **/
    @SideOnly(Side.CLIENT)
    public Session session()
    {
        return MDX.access().getSession();
    }

    /** Easy way to set the right click delay timer variable. **/
    @SideOnly(Side.CLIENT)
    public void setRightClickDelayTimer(int i)
    {
        MDX.access().setRightClickDelayTimer(i);
    }

    /** Easy way to set the current equipped progress variable. **/
    @SideOnly(Side.CLIENT)
    public void setEquippedProgress(float f)
    {
        MDX.access().setEquippedProgress(f);
    }
}
