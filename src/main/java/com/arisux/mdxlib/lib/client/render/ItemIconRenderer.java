package com.arisux.mdxlib.lib.client.render;

import com.arisux.mdxlib.MDX;
import com.arisux.mdxlib.lib.client.Model;
import com.arisux.mdxlib.lib.game.Game;
import com.arisux.mdxlib.lib.world.block.BlockShape;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemIconRenderer<M extends Model> extends ItemRenderer<M>
{
    public static enum IconType
    {
        ITEM("items"), BLOCK("blocks");

        private String type;

        IconType(String type)
        {
            this.type = type;
        }

        public ResourceLocation newResource(Item item)
        {
            String domain = item.getRegistryName().getResourceDomain();
            return new ResourceLocation(domain, getTextureLocation(item));
        }

        public String getTextureLocation(Item item)
        {
            ResourceLocation resource = item.getRegistryName();
            Block block = Block.getBlockFromItem(item);
            
            if (block != null)
            {
                if (block instanceof BlockShape)
                {
                    BlockShape shape = (BlockShape) block;
                    resource = shape.getTextureBlock().getRegistryName();
                }
            }
            
            return String.format("%s\\%s.png", getTextureLocation(), resource.getResourcePath());
        }

        public String getTextureLocation()
        {
            return String.format("textures\\%s", this.type);
        }

        public static IconType getType(Item item)
        {
            return Block.getBlockFromItem(item) == null ? ITEM : BLOCK;
        }
    }

    private RenderBlock      renderBlock;
    private IconType         type;
    private ResourceLocation resource;

    public ItemIconRenderer(Item item)
    {
        super(null);
        MDX.log().info("Item Icon Renderer registration attempt for item: " + item.getRegistryName());
        this.type = IconType.getType(item);
        this.resource = this.type.newResource(item);
        this.renderBlock = new RenderBlock();
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

    }

    @Override
    public void renderThirdPersonRight(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {

    }

    @Override
    public void renderFirstPersonLeft(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {

    }

    @Override
    public void renderFirstPersonRight(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {

    }

    @Override
    public void renderInInventory(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        switch (type)
        {
            case ITEM:
                this.renderItemInInventory(itemstack, entity, cameraTransformType);
                break;
            case BLOCK:
                this.renderBlockInInventory(itemstack, entity, cameraTransformType);
                break;
            default:
                break;
        }
    }

    public void renderItemInInventory(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        OpenGL.pushMatrix();
        OpenGL.scale(-1F, -1F, 1F);
        OpenGL.translate(0.5F, -0.5F, 0F);
        OpenGL.rotate(180F, 0, 1, 0);
        OpenGL.disableStandardItemLighting();
        Draw.bindTexture(resource);
        Draw.drawQuad(0, 0, 1, 1, 0, 0F, 1F, 0F, 1F);
        OpenGL.enableStandardItemLighting();
        OpenGL.popMatrix();
    }

    public void renderBlockInInventory(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        OpenGL.pushMatrix();
        float scale = 0.7F;
        OpenGL.scale(-scale, scale, scale);
        OpenGL.rotate(-50F, 1F, -1F, 0F);
//        OpenGL.rotate((Minecraft.getMinecraft().world.getWorldTime() % 360) * 2 + Game.partialTicks(), -1F, 0.5F, 0F);
        OpenGL.translate(-0.5F, -0.5F, -0.5F);
        OpenGL.disableStandardItemLighting();
        Draw.bindTexture(resource);
        renderBlock.renderBlock(Block.getBlockFromItem(itemstack.getItem()).getDefaultState());
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
