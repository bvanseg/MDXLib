package com.arisux.amdxlib.lib.game;

import java.lang.reflect.Method;

import com.arisux.amdxlib.lib.util.Reflect;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;

public class Access
{
    @SideOnly(Side.CLIENT)
    public float getRenderPartialTicks()
    {
        return ((Timer) Reflect.get(Minecraft.class, Game.minecraft(), "timer", "field_71428_T")).renderPartialTicks;
    }

    @SideOnly(Side.CLIENT)
    public int getRightClickDelayTimer()
    {
        return Reflect.getInt(Game.minecraft(), "rightClickDelayTimer", "field_71467_ac");
    }

    @SideOnly(Side.CLIENT)
    public void setRightClickDelayTimer(int value)
    {
        Reflect.set(Minecraft.class, Game.minecraft(), "rightClickDelayTimer", "field_71467_ac", value);
    }

    @SideOnly(Side.CLIENT)
    public Session getSession()
    {
        return (Session) Reflect.get(Minecraft.class, Game.minecraft(), "session", "field_71449_j");
    }

    @SideOnly(Side.CLIENT)
    public void setEquippedProgress(float value)
    {
        Reflect.set(ItemRenderer.class, Game.minecraft().entityRenderer.itemRenderer, "equippedProgress", "field_78454_c", value);
    }

    @SideOnly(Side.CLIENT)
    public float getTorchFlickerX()
    {
        return Reflect.getFloat(Game.minecraft().entityRenderer, "torchFlickerX", "field_78514_e");
    }

    @SideOnly(Side.CLIENT)
    public void setTorchFlickerX(float value)
    {
        Reflect.set(EntityRenderer.class, Game.minecraft().entityRenderer, "torchFlickerX", "field_78514_e", value);
    }

    @SideOnly(Side.CLIENT)
    public float getBossColorModifier()
    {
        return Reflect.getFloat(Game.minecraft().entityRenderer, "bossColorModifier", "field_82831_U");
    }

    @SideOnly(Side.CLIENT)
    public void setBossColorModifier(float value)
    {
        Reflect.set(EntityRenderer.class, Game.minecraft().entityRenderer, "bossColorModifier", "field_82831_U", value);
    }

    @SideOnly(Side.CLIENT)
    public float getBossColorModifierPrev()
    {
        return Reflect.getFloat(Game.minecraft().entityRenderer, "bossColorModifierPrev", "field_82832_V");
    }

    @SideOnly(Side.CLIENT)
    public void getBossColorModifierPrev(float value)
    {
        Reflect.set(EntityRenderer.class, Game.minecraft().entityRenderer, "bossColorModifierPrev", "field_82832_V", value);
    }

    @SideOnly(Side.CLIENT)
    public int[] getLightmapColors()
    {
        return (int[]) Reflect.get(EntityRenderer.class, Game.minecraft().entityRenderer, "lightmapColors", "field_78504_Q");
    }

    @SideOnly(Side.CLIENT)
    public DynamicTexture getLightmapTexture()
    {
        return (DynamicTexture) Reflect.get(EntityRenderer.class, Game.minecraft().entityRenderer, "lightmapTexture", "field_78513_d");
    }

    @SideOnly(Side.CLIENT)
    public boolean isLightmapUpdateNeeded()
    {
        return Reflect.getBoolean(Game.minecraft().entityRenderer, "lightmapUpdateNeeded", "field_78536_aa");
    }

    @SideOnly(Side.CLIENT)
    public void setLightmapUpdateNeeded(boolean value)
    {
        Reflect.set(EntityRenderer.class, Game.minecraft().entityRenderer, "lightmapUpdateNeeded", "field_78536_aa", value);
    }

    public void setMoveHelper(EntityLiving living, EntityMoveHelper moveHelper)
    {
        Reflect.set(EntityLiving.class, living, "moveHelper", "field_70765_h", moveHelper);
    }

    public void setNavigator(EntityLiving living, PathNavigate navigator)
    {
        Reflect.set(EntityLiving.class, living, "navigator", "field_70699_by", navigator);
    }

    public void setLookHelper(EntityLiving living, EntityLookHelper lookHelper)
    {
        Reflect.set(EntityLiving.class, living, "lookHelper", "field_70749_g", lookHelper);
    }

    public double getMoveHelperPosX(EntityMoveHelper moveHelper)
    {
        return (Double) Reflect.get(EntityMoveHelper.class, moveHelper, "posX", "field_75646_b");
    }

    public double getMoveHelperPosY(EntityMoveHelper moveHelper)
    {
        return (Double) Reflect.get(EntityMoveHelper.class, moveHelper, "posY", "field_75647_c");
    }

    public double getMoveHelperPosZ(EntityMoveHelper moveHelper)
    {
        return (Double) Reflect.get(EntityMoveHelper.class, moveHelper, "posZ", "field_75644_d");
    }

    public double getMoveHelperSpeed(EntityMoveHelper moveHelper)
    {
        return (Double) Reflect.get(EntityMoveHelper.class, moveHelper, "speed", "field_75645_e");
    }

    public double getLookHelperPosX(EntityLookHelper lookHelper)
    {
        return (Double) Reflect.get(EntityLookHelper.class, lookHelper, "posX", "field_75656_e");
    }

    public double getLookHelperPosY(EntityLookHelper lookHelper)
    {
        return (Double) Reflect.get(EntityLookHelper.class, lookHelper, "posY", "field_75653_f");
    }

    public double getLookHelperPosZ(EntityLookHelper lookHelper)
    {
        return (Double) Reflect.get(EntityLookHelper.class, lookHelper, "posZ", "field_75654_g");
    }

    public boolean getLookHelperIsLooking(EntityLookHelper lookHelper)
    {
        return (Boolean) Reflect.get(EntityLookHelper.class, lookHelper, "isLooking", "field_75655_d");
    }

    public float getBlockResistance(Block block)
    {
        return (Float) Reflect.get(Block.class, block, "blockResistance", "field_149781_w");
    }

    public float getBlockHardness(Block block)
    {
        return (Float) Reflect.get(Block.class, block, "blockHardness", "field_149782_v");
    }

    public String getBlockTextureName(Block block)
    {
        return (String) Reflect.get(Block.class, block, "textureName", "field_149768_d");
    }

    public ModelBase getMainModel(RendererLivingEntity renderLiving)
    {
        return (ModelBase) Reflect.get(RendererLivingEntity.class, renderLiving, "mainModel", "field_77045_g");
    }

    public ResourceLocation getEntityTexture(Render render, Entity entity)
    {
        try
        {
            Method getEntityTexture = Render.class.getDeclaredMethod(Game.isDevEnvironment() ? "getEntityTexture" : "func_110775_a", Entity.class);
            getEntityTexture.setAccessible(true);
            return (ResourceLocation) getEntityTexture.invoke(render, new Object[] { entity });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
