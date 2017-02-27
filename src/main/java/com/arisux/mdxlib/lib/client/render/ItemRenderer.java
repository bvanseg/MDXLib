package com.arisux.mdxlib.lib.client.render;

import java.util.Collections;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;

public abstract class ItemRenderer implements IPerspectiveAwareModel, IBakedModel
{
    protected static final Minecraft                    mc    = Minecraft.getMinecraft();
    private ItemRenderList                              overrides;
    private final Pair<? extends IBakedModel, Matrix4f> selfPair;
    private static List<BakedQuad>                      quads = Collections.emptyList();
    protected ResourceLocation                          resource;
    protected ModelBase                                 model;
    protected ItemStack                                 stack;
    protected EntityLivingBase                          entity;

    public static class ItemRenderList extends ItemOverrideList
    {
        public ItemRenderList()
        {
            super(Lists.<ItemOverride> newArrayList());
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity)
        {
            if (originalModel instanceof ItemRenderer)
            {
                ItemRenderer model = (ItemRenderer) originalModel;
                model.setItemstack(stack);
                model.setEntity(entity);
            }

            return super.handleItemState(originalModel, stack, world, entity);
        }
    }

    public ItemRenderer(ModelBase model, ResourceLocation resource)
    {
        this.overrides = new ItemRenderList();
        this.selfPair = Pair.of(this, null);
        this.model = model;
        this.resource = resource;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType type)
    {
        GL11.glPushMatrix();
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
        GL11.glPopMatrix();

        return selfPair;
    }

    public void renderPre(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        ;
    }

    public void renderPost(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        ;
    }

    public abstract void renderThirdPersonLeft(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType);

    public abstract void renderThirdPersonRight(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType);

    public abstract void renderFirstPersonLeft(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType);

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

    public ModelBase getModel()
    {
        return model;
    }

    public ResourceLocation getResourceLocation()
    {
        return resource;
    }

    public void setResourceLocation(ResourceLocation resource)
    {
        this.resource = resource;
    }

    private void setItemstack(ItemStack stack)
    {
        this.stack = stack;
    }

    private void setEntity(EntityLivingBase entity)
    {
        this.entity = entity;
    }
}
