package com.arisux.mdx.lib.client.render;

import java.util.Collections;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.arisux.mdx.lib.client.Model;
import com.arisux.mdx.lib.client.TexturedModel;
import com.arisux.mdx.lib.game.Game;
import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;

public abstract class ItemRenderer<M extends Model> implements IPerspectiveAwareModel, IBakedModel
{
    protected TexturedModel<M>                          model;
    protected static final Minecraft                    MC    = Minecraft.getMinecraft();
    private ItemRenderList<M>                           overrides;
    private final Pair<? extends IBakedModel, Matrix4f> SELF_PAIR;
    private static List<BakedQuad>                      quads = Collections.emptyList();
    // protected ResourceLocation resource;
    // protected ModelBase model;
    protected ItemStack                                 stack;
    protected EntityLivingBase                          entity;

    public static class ItemRenderList<M extends Model> extends ItemOverrideList
    {
        public ItemRenderList()
        {
            super(Lists.<ItemOverride> newArrayList());
        }

        @SuppressWarnings("all")
        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity)
        {
            if (originalModel instanceof ItemRenderer)
            {
                ItemRenderer<M> model = (ItemRenderer<M>) originalModel;
                model.setItemstack(stack);
                model.setEntity(entity);
            }

            return super.handleItemState(originalModel, stack, world, entity);
        }
    }

    public ItemRenderer(TexturedModel<M> model)
    {
        this.overrides = new ItemRenderList();
        this.SELF_PAIR = Pair.of(this, null);

        if (model != null)
        {
            this.model = model.clone();
        }
        // this.resource = resource;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType type)
    {
        GlStateManager.pushMatrix();
        this.renderPre(this.stack, this.entity, type);
        
        switch (type)
        {
            case FIRST_PERSON_LEFT_HAND: {
                this.renderFirstPersonLeft(this.stack, this.entity, type);
            }
                break;
            case FIRST_PERSON_RIGHT_HAND: {
                this.renderFirstPersonRight(this.stack, this.entity, type);
            }
                break;
            case GUI: {
                GlStateManager.resetColor();
                OpenGL.enableStandardItemLighting();
                this.renderInInventory(this.stack, this.entity, type);
            }
                break;
            case THIRD_PERSON_LEFT_HAND: {
                this.renderThirdPersonLeft(this.stack, this.entity, type);
            }
                break;
            case THIRD_PERSON_RIGHT_HAND: {
                this.renderThirdPersonRight(this.stack, this.entity, type);
            }
                break;
            case GROUND: {
                this.renderInWorld(this.stack, this.entity, type);
            }
                break;

            default:
                break;
        }

        this.renderPost(this.stack, this.entity, type);
        GlStateManager.popMatrix();

        return SELF_PAIR;
    }

    public void renderPre(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        ;
    }

    public void renderPost(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        ;
    }

    public void renderThirdPersonLeft(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        OpenGL.scale(-1F, 1F, 1F);
        renderThirdPersonRight(itemstack, entity, cameraTransformType);
    }

    public abstract void renderThirdPersonRight(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType);

    public void renderFirstPersonLeft(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        OpenGL.scale(-1F, 1F, 1F);
        this.renderFirstPersonRight(itemstack, entity, cameraTransformType);
    }

    public abstract void renderFirstPersonRight(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType);

    public abstract void renderInInventory(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType);

    public abstract void renderInWorld(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType);

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
    {
        return quads;
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return true;
    }

    @Override
    public boolean isGui3d()
    {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return null;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return this.overrides;
    }

    public TexturedModel<M> getModel()
    {
        return this.model;
    }

    // public ModelBase getModel()
    // {
    // return model;
    // }
    //
    // public ResourceLocation getResourceLocation()
    // {
    // return resource;
    // }
    //
    // public void setResourceLocation(ResourceLocation resource)
    // {
    // this.resource = resource;
    // }

    private void setItemstack(ItemStack stack)
    {
        this.stack = stack;
    }

    private void setEntity(EntityLivingBase entity)
    {
        this.entity = entity;
    }

    public boolean firstPersonRenderCheck(Entity entity)
    {
        return entity == Game.minecraft().getRenderViewEntity() && Game.minecraft().gameSettings.thirdPersonView == 0 && (!(Game.minecraft().currentScreen instanceof GuiInventory) && !(Game.minecraft().currentScreen instanceof GuiContainerCreative) || Game.renderManager().playerViewY != 180.0F);
    }
}
