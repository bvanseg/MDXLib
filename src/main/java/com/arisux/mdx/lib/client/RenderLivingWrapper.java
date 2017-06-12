package com.arisux.mdx.lib.client;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class RenderLivingWrapper<T extends EntityLiving, MODEL extends Model> extends RenderLiving<T>
{
    protected TexturedModel<MODEL> model;
    
    public RenderLivingWrapper(RenderManager m, TexturedModel<MODEL> model)
    {
        this(m, model, 0F);
    }
    
    public RenderLivingWrapper(RenderManager m, TexturedModel<MODEL> model, float shadowSize)
    {
        super(m, model.getModel(), shadowSize);
        this.model = model;
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity)
    {
        return this.model.getTexture();
    }
    
    public TexturedModel<MODEL> getModel()
    {
        return model;
    }
}
