package com.arisux.mdxlib.lib.client;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderLivingWrapper extends RenderLiving
{
    protected TexturedModel<? extends Model> model;
    
    public RenderLivingWrapper(TexturedModel<? extends Model> model)
    {
        this(model, 0F);
    }
    
    public RenderLivingWrapper(TexturedModel<? extends Model> model, float shadowSize)
    {
        super(model.getModel(), shadowSize);
        this.model = model;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.model.getTexture();
    }
    
    public TexturedModel<? extends Model> getModelTexMap()
    {
        return model;
    }
}
