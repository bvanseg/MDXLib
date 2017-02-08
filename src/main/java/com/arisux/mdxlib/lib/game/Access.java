package com.arisux.mdxlib.lib.game;

import java.io.DataInput;
import java.io.DataOutput;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;

public class Access
{
    @SideOnly(Side.CLIENT)
    public float getRenderPartialTicks()
    {
        return Game.minecraft().timer.renderPartialTicks;
    }

    @SideOnly(Side.CLIENT)
    public int getRightClickDelayTimer()
    {
        return Game.minecraft().rightClickDelayTimer;
    }

    @SideOnly(Side.CLIENT)
    public void setRightClickDelayTimer(int value)
    {
        Game.minecraft().rightClickDelayTimer = value;
    }

    @SideOnly(Side.CLIENT)
    public Session getSession()
    {
        return Game.minecraft().session;
    }

    @SideOnly(Side.CLIENT)
    public void setEquippedProgress(float value)
    {
        Game.minecraft().entityRenderer.itemRenderer.equippedProgress = value;
    }

    @SideOnly(Side.CLIENT)
    public float getTorchFlickerX()
    {
        return Game.minecraft().entityRenderer.torchFlickerX;
    }

    @SideOnly(Side.CLIENT)
    public float getTorchFlickerY()
    {
        return Game.minecraft().entityRenderer.torchFlickerY;
    }

    @SideOnly(Side.CLIENT)
    public void setTorchFlickerX(float value)
    {
        Game.minecraft().entityRenderer.torchFlickerX = value;
    }

    @SideOnly(Side.CLIENT)
    public void setTorchFlickerYX(float value)
    {
        Game.minecraft().entityRenderer.torchFlickerY = value;
    }

    @SideOnly(Side.CLIENT)
    public float getBossColorModifier()
    {
        return Game.minecraft().entityRenderer.bossColorModifier;
    }

    @SideOnly(Side.CLIENT)
    public void setBossColorModifier(float value)
    {
        Game.minecraft().entityRenderer.bossColorModifier = value;
    }

    @SideOnly(Side.CLIENT)
    public float getBossColorModifierPrev()
    {
        return Game.minecraft().entityRenderer.bossColorModifierPrev;
    }

    @SideOnly(Side.CLIENT)
    public void getBossColorModifierPrev(float value)
    {
        Game.minecraft().entityRenderer.bossColorModifierPrev = value;
    }

    @SideOnly(Side.CLIENT)
    public int[] getLightmapColors()
    {
        return Game.minecraft().entityRenderer.lightmapColors;
    }

    @SideOnly(Side.CLIENT)
    public DynamicTexture getLightmapTexture()
    {
        return Game.minecraft().entityRenderer.lightmapTexture;
    }

    @SideOnly(Side.CLIENT)
    public boolean isLightmapUpdateNeeded()
    {
        return Game.minecraft().entityRenderer.lightmapUpdateNeeded;
    }

    @SideOnly(Side.CLIENT)
    public void setLightmapUpdateNeeded(boolean value)
    {
        Game.minecraft().entityRenderer.lightmapUpdateNeeded = value;
    }

    public void setMoveHelper(EntityLiving living, EntityMoveHelper moveHelper)
    {
        living.moveHelper = moveHelper;
    }

    public void setNavigator(EntityLiving living, PathNavigate navigator)
    {
        living.navigator = navigator;
    }

    public void setLookHelper(EntityLiving living, EntityLookHelper lookHelper)
    {
        living.lookHelper = lookHelper;
    }

    public double getMoveHelperPosX(EntityMoveHelper moveHelper)
    {
        return moveHelper.posX;
    }

    public double getMoveHelperPosY(EntityMoveHelper moveHelper)
    {
        return moveHelper.posY;
    }

    public double getMoveHelperPosZ(EntityMoveHelper moveHelper)
    {
        return moveHelper.posZ;
    }

    public double getMoveHelperSpeed(EntityMoveHelper moveHelper)
    {
        return moveHelper.speed;
    }

    public double getLookHelperPosX(EntityLookHelper lookHelper)
    {
        return lookHelper.posX;
    }

    public double getLookHelperPosY(EntityLookHelper lookHelper)
    {
        return lookHelper.posY;
    }

    public double getLookHelperPosZ(EntityLookHelper lookHelper)
    {
        return lookHelper.posZ;
    }

    public boolean getLookHelperIsLooking(EntityLookHelper lookHelper)
    {
        return lookHelper.isLooking;
    }

    public float getBlockResistance(Block block)
    {
        return block.blockResistance;
    }

    public float getBlockHardness(Block block)
    {
        return block.blockHardness;
    }

    public String getBlockTextureName(Block block)
    {
        return block.textureName;
    }

    @SideOnly(Side.CLIENT)
    public ModelBase getMainModel(RendererLivingEntity renderLiving)
    {
        return renderLiving.mainModel;
    }

    private static final MethodHandle CompressedStreamTools_writeTag;
    private static final MethodHandle CompressedStreamTools_readTag;

    static
    {
        try
        {
            Method method = null;
            
            method = CompressedStreamTools.class.getDeclaredMethod(Game.isDevEnvironment() ? "writeTag" : "func_150663_a", NBTBase.class, DataOutput.class);
            method.setAccessible(true);
            CompressedStreamTools_writeTag = MethodHandles.publicLookup().unreflect(method);

            method = CompressedStreamTools.class.getDeclaredMethod(Game.isDevEnvironment() ? "func_152455_a" : "func_152455_a", DataInput.class, int.class, NBTSizeTracker.class);
            method.setAccessible(true);
            CompressedStreamTools_readTag = MethodHandles.publicLookup().unreflect(method);
        }
        catch (Exception exception)
        {
            throw new RuntimeException();
        }

    }

    /**
     * Provides access to CompressedStreamTools.writeTag(NBTBase, DataOutput)
     */
    public static void writeTag(NBTBase base, DataOutput dataoutput)
    {
        try
        {
            Access.CompressedStreamTools_writeTag.invokeExact(base, dataoutput);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Provides access to CompressedStreamTools.readTag(DataInput, int, NBTSizeTracker)
     */
    public static NBTBase readTag(DataInput datainput, int depth, NBTSizeTracker sizeTracker)
    {
        try
        {
            return (NBTBase) Access.CompressedStreamTools_readTag.invokeExact(datainput, depth, sizeTracker);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
        
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static class ClientAccess
    {
        private static final MethodHandle getEntityTexture;

        static
        {
            try
            {
                Method method = Render.class.getDeclaredMethod(Game.isDevEnvironment() ? "getEntityTexture" : "func_110775_a", Entity.class);
                method.setAccessible(true);
                getEntityTexture = MethodHandles.publicLookup().unreflect(method);
            }
            catch (Exception exception)
            {
                throw new RuntimeException();
            }

        }
    }

    @SideOnly(Side.CLIENT)
    public ResourceLocation getEntityTexture(Render render, Entity entity)
    {
        try
        {
            return ((ResourceLocation) ClientAccess.getEntityTexture.invokeExact(render, (Entity) entity));
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
