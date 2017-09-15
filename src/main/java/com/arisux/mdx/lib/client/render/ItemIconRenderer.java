package com.arisux.mdx.lib.client.render;

import com.arisux.mdx.lib.client.Model;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemIconRenderer<M extends Model> extends ItemRenderer<M>
{
    public static enum IconType
    {
        ITEM("items");

        private String type;

        IconType(String type)
        {
            this.type = type;
        }

        public ResourceLocation iconResourceLocation(Item item)
        {
            ResourceLocation registryName = item.getRegistryName();
            String domain = registryName.getResourceDomain();
            String path = String.format("%s/%s.png", getIconDirectory(), registryName.getResourcePath());
            //System.out.println(String.format("DOMAIN(%s) PATH(%s) RESOURCE(%s)", domain, registryName.getResourcePath(), new ResourceLocation(domain, path)));

            return new ResourceLocation(domain, path);
        }

        public String getIconDirectory()
        {
            return String.format("textures/%s", this.type);
        }
    }

    private ResourceLocation icon;

    public ItemIconRenderer(Item item)
    {
        super(null);
        this.icon = IconType.ITEM.iconResourceLocation(item);
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
    public void renderThirdPersonLeft(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        OpenGL.pushMatrix();
        OpenGL.scale(-0.75F, -0.75F, 1F);
        OpenGL.translate(0.5F, -0.75F, 0F);
        OpenGL.rotate(180F, 0, 1, 0);
        OpenGL.disableStandardItemLighting();
        Draw.bindTexture(this.icon);
        Draw.drawQuad(0, 0, 1, 1, 0, 0F, 1F, 0F, 1F);
        OpenGL.enableStandardItemLighting();
        OpenGL.popMatrix();
    }

    @Override
    public void renderThirdPersonRight(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        OpenGL.pushMatrix();
        OpenGL.scale(-0.75F, -0.75F, 1F);
        OpenGL.translate(0.5F, -0.75F, 0F);
        OpenGL.rotate(180F, 0, 1, 0);
        OpenGL.disableStandardItemLighting();
        Draw.bindTexture(this.icon);
        Draw.drawQuad(0, 0, 1, 1, 0, 0F, 1F, 0F, 1F);
        OpenGL.enableStandardItemLighting();
        OpenGL.popMatrix();
    }

    @Override
    public void renderFirstPersonLeft(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        OpenGL.pushMatrix();
        OpenGL.scale(-0.75F, -0.75F, 1F);
        OpenGL.translate(0.5F, -0.75F, 0F);
        OpenGL.rotate(120F, 0, 1, 0);
        OpenGL.rotate(20F, 0, 0, 1);
        OpenGL.disableStandardItemLighting();
        Draw.bindTexture(this.icon);
        Draw.drawQuad(0, 0, 1, 1, 0, 0F, 1F, 0F, 1F);
        OpenGL.enableStandardItemLighting();
        OpenGL.popMatrix();
    }

    @Override
    public void renderFirstPersonRight(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        OpenGL.pushMatrix();
        OpenGL.scale(-0.75F, -0.75F, 1F);
        OpenGL.translate(0.15F, -0.5F, -0.7F);
        OpenGL.rotate(240F, 0, 1, 0);
        OpenGL.rotate(-20F, 0, 0, 1);
        OpenGL.disableStandardItemLighting();
        Draw.bindTexture(this.icon);
        Draw.drawQuad(0, 0, 1, 1, 0, 0F, 1F, 0F, 1F);
        OpenGL.enableStandardItemLighting();
        OpenGL.popMatrix();
    }

    @Override
    public void renderInInventory(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        OpenGL.pushMatrix();
        OpenGL.scale(-1F, -1F, 1F);
        OpenGL.translate(0.5F, -0.5F, 0F);
        OpenGL.rotate(180F, 0, 1, 0);
        OpenGL.disableStandardItemLighting();
        Draw.bindTexture(this.icon);
        Draw.drawQuad(0, 0, 1, 1, 0, 0F, 1F, 0F, 1F);
        OpenGL.enableStandardItemLighting();
        OpenGL.popMatrix();
    }

    @Override
    public void renderInWorld(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        
    }

    @Override
    public void renderPre(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        ;
    }
}
