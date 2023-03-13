package com.asx.mdx.client.render.item;

import java.util.Collections;
import java.util.List;

import javax.vecmath.Matrix4f;

import com.asx.mdx.client.ClientGame;
import com.asx.mdx.client.render.OpenGL;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import com.asx.mdx.client.render.model.MapModelTexture;
import com.asx.mdx.client.render.model.Model;
import com.asx.mdx.common.Game;
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
import net.minecraftforge.client.model.ModelLoader.White;

public abstract class ItemRenderer<M extends Model> implements IBakedModel
{
    protected MapModelTexture<M>                        model;
    protected static final Minecraft                    MC    = Minecraft.getMinecraft();
    private ItemRenderList<M>                           overrides;
    private final Pair<? extends IBakedModel, Matrix4f> SELF_PAIR;
    private static List<BakedQuad>                      quads = Collections.emptyList();
    // protected ResourceLocation resource;
    // protected ModelBase model;
    protected ItemStack                                 stack;
    protected EntityLivingBase                          entity;
    protected TextureAtlasSprite                        sprite;

    public static class ItemRenderList<M extends Model> extends ItemOverrideList
    {
        public ItemRenderList()
        {
            super(Lists.<ItemOverride>newArrayList());
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

    public ItemRenderer(MapModelTexture<M> model)
    {
        this.sprite = White.INSTANCE;
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
        /** Keep track of previously bound texture ID **/
        int previousTexture = GlStateManager.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        this.renderPre(this.stack, this.entity, type);

        switch (type)
        {
            case FIRST_PERSON_LEFT_HAND:
            {
                OpenGL.pushMatrix();
                this.renderFirstPersonLeft(this.stack, this.entity, type);
                OpenGL.popMatrix();
            }
                break;
            case FIRST_PERSON_RIGHT_HAND:
            {
                OpenGL.pushMatrix();
                this.renderFirstPersonRight(this.stack, this.entity, type);
                OpenGL.popMatrix();
            }
                break;
            case GUI:
            {
                OpenGL.pushMatrix();
                GlStateManager.resetColor();
                OpenGL.enableStandardItemLighting();
                this.renderInInventory(this.stack, this.entity, type);
                OpenGL.popMatrix();
            }
                break;
            case THIRD_PERSON_LEFT_HAND:
            {
                OpenGL.pushMatrix();
                this.renderThirdPersonLeft(this.stack, this.entity, type);
                OpenGL.popMatrix();
            }
                break;
            case THIRD_PERSON_RIGHT_HAND:
            {
                OpenGL.pushMatrix();
                this.renderThirdPersonRight(this.stack, this.entity, type);
                OpenGL.popMatrix();
            }
                break;
            case GROUND:
            {
                OpenGL.pushMatrix();
                this.renderInWorld(this.stack, this.entity, type);
                OpenGL.popMatrix();
            }
                break;

            default:
                break;
        }

        this.renderPost(this.stack, this.entity, type);
        /** Re-bind previous texture ID to prevent issues **/
        GlStateManager.bindTexture(previousTexture);
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
        return this.sprite;
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

    public MapModelTexture<M> getModel()
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
        return entity == ClientGame.instance.minecraft().getRenderViewEntity() && ClientGame.instance.minecraft().gameSettings.thirdPersonView == 0 && (!(ClientGame.instance.minecraft().currentScreen instanceof GuiInventory) && !(ClientGame.instance.minecraft().currentScreen instanceof GuiContainerCreative) || ClientGame.instance.renderManager().playerViewY != 180.0F);
    }

    public float getIconRotation()
    {
        return -45F;
        // return ClientGame.instance.minecraft().world.getTotalWorldTime() % 360 + ClientGame.instance.partialTicks();
    }
}
