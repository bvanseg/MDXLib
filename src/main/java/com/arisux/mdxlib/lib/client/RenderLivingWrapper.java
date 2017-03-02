package com.arisux.mdxlib.lib.client;

import com.arisux.mdxlib.lib.game.Game;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class RenderLivingWrapper<T extends EntityLiving> extends RenderLiving<T>
{
    protected TexturedModel<? extends Model> model;
    
    public RenderLivingWrapper(TexturedModel<? extends Model> model)
    {
        this(model, 0F);
    }
    
    public RenderLivingWrapper(TexturedModel<? extends Model> model, float shadowSize)
    {
        super(Game.minecraft().getRenderManager(), model.getModel(), shadowSize);
        this.model = model;
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity)
    {
        return this.model.getTexture();
    }
    
    public TexturedModel<? extends Model> getModelTexMap()
    {
        return model;
    }
}
