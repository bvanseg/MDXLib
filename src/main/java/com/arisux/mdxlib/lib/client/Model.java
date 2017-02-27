package com.arisux.mdxlib.lib.client;

import com.arisux.mdxlib.MDX;
import com.arisux.mdxlib.lib.game.Game;
import com.arisux.mdxlib.lib.util.MDXMath;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class Model extends ModelBase
{
    public static final float DEFAULT_SCALE = 1F / 16F;

    /**
     * Set the width and height of this ModelBaseExtension's texture.
     * 
     * @param textureWidth - The texture width in pixels
     * @param textureHeight - The texture height in pixels
     */
    public void setTextureDimensions(int textureWidth, int textureHeight)
    {
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    /**
     * Set the rotation angles of the specified ModelRenderer instance.
     */
    public void setRotation(ModelRenderer model, float rotateAngleX, float rotateAngleY, float rotateAngleZ)
    {
        model.rotateAngleX = rotateAngleX;
        model.rotateAngleY = rotateAngleY;
        model.rotateAngleZ = rotateAngleZ;
    }

    public static void draw(ModelRenderer modelRenderer)
    {
        modelRenderer.render(DEFAULT_SCALE);
    }

    public static void draw(ModelRenderer[] group)
    {
        for (ModelRenderer child : group)
        {
            draw(child);
        }
    }

    public void render()
    {
        this.render(null);
    }

    /**
     * The entity render method from ModelBase with correct parameter mappings. Calls the base render method.
     * 
     * @param entity - The Entity instance being rendered.
     * @param swing - The arm swing progress of the Entity being rendered.
     * @param swingPrev - The previous tick's arm swing progress of the Entity being rendered.
     * @param idle - The idle arm swing progress of the Entity being rendered.
     * @param headYaw - The head rotation yaw of the Entity being rendered.
     * @param headPitch - The head rotation pitch of the Entity being rendered.
     * @param scale - The scale this model will render at.
     */
    public void render(Object obj)
    {
        ;
    }

    /**
    * The entity render method from ModelBase with correct parameter mappings. Calls the base render method.
    *
    * @param entity - The Entity instance being rendered.
    * @param swing - The arm swing progress of the Entity being rendered.
    * @param swingPrev - The previous tick's arm swing progress of the Entity being rendered.
    * @param idle - The idle arm swing progress of the Entity being rendered.
    * @param headYaw - The head rotation yaw of the Entity being rendered.
    * @param headPitch - The head rotation pitch of the Entity being rendered.
    * @param scale - The scale this model will render at.
    */
    @Override
    public void render(Entity entity, float swing, float swingPrev, float idle, float headYaw, float headPitch, float scale)
    {
        this.render(entity);
    }

    /**
    * The standard setRotationAngles method from ModelBase with correct parameter mappings. Calls the superclass method.
    *
    * @param swing - The arm swing progress of the Entity being rendered.
    * @param swingPrev - The previous tick's arm swing progress of the Entity being rendered.
    * @param idle - The idle arm swing progress of the Entity being rendered.
    * @param headYaw - The head rotation yaw of the Entity being rendered.
    * @param headPitch - The head rotation pitch of the Entity being rendered.
    * @param scale - The scale this model will render at.
    * @param entity - The Entity instance being rendered.
    */
    @Override
    public void setRotationAngles(float swing, float swingPrev, float idle, float headYaw, float headPitch, float scale, Entity entity)
    {
        ;
    }

    /**
    * The standard setLivingAnimations method from ModelBase with correct parameter mappings. Calls the superclass method.
    *
    * @param entityLiving - The EntityLiving instance currently being rendered.
    * @param swingProgress - The arm swing progress of the Entity being rendered.
    * @param swingProgressPrev - The previous tick's arm swing progress of the Entity being rendered.
    * @param renderPartialTicks - Render partial ticks
    */
    @Override
    public void setLivingAnimations(EntityLivingBase entityLiving, float swingProgress, float swingProgressPrev, float renderPartialTicks)
    {
        ;
    }

    /**
     * Creates an array or group of ModelRenderers.
     * 
     * @param children - The ModelRenderer instances we're adding to this group.
     * @return The array or group created.
     */
    public static ModelRenderer[] group(ModelRenderer... children)
    {
        return children;
    }

    /**
     * Constructs a standard ModelBase instance from the specified class.
     * 
     * @param modelClass - A class extending ModelBase which will be instantaniated. 
     * @return Instance of the class specified in the modelClass parameter.
     */
    public static ModelBase createModelBase(Class<? extends ModelBase> modelClass)
    {
        try
        {
            return (modelClass.getConstructor()).newInstance(new Object[] {});
        }
        catch (Exception e)
        {
            MDX.log().warn("Error creating new model instance.");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Constructs a ModelBaseExtension instance from the specified class.
     * 
     * @param modelClass - A class extending ModelBaseExtension which will be instantaniated. 
     * @return Instance of the class specified in the modelClass parameter.
     */
    public static Model createExtendedModelBase(Class<? extends Model> modelClass)
    {
        try
        {
            return (modelClass.getConstructor()).newInstance(new Object[] {});
        }
        catch (Exception e)
        {
            MDX.log().warn("Error creating new model instance.");
            e.printStackTrace();
        }

        return null;
    }

    public static ModelBase getMainModel(RenderLivingBase<EntityLivingBase> renderer)
    {
        return MDX.access().getMainModel(renderer);
    }

    public static float getIdleProgress(EntityLivingBase base)
    {
        return base.ticksExisted + Game.partialTicks();
    }

    public static float getSwingProgress(EntityLivingBase base)
    {
        return base.limbSwing - base.limbSwingAmount * (1.0F - Game.partialTicks());
    }

    public static float getSwingProgressPrev(EntityLivingBase base)
    {
        return base.prevLimbSwingAmount + (base.limbSwingAmount - base.prevLimbSwingAmount) * Game.partialTicks();
    }

    public static float getHeadYaw(EntityLivingBase base)
    {
        float yawOffset = MDXMath.interpolateRotation(base.prevRenderYawOffset, base.renderYawOffset, Game.partialTicks());
        float yawHead = MDXMath.interpolateRotation(base.prevRotationYawHead, base.rotationYawHead, Game.partialTicks());
        return yawHead - yawOffset;
    }

    public static float getHeadPitch(EntityLivingBase base)
    {
        return (base.prevRotationPitch + (base.rotationPitch - base.prevRotationPitch) * Game.partialTicks());
    }

    public static float idleProgress(Object o)
    {
        if (o != null && o instanceof EntityLivingBase)
        {
            return getIdleProgress((EntityLivingBase) o);
        }

        return 0F;
    }

    public static float swingProgress(Object o)
    {
        if (o != null && o instanceof EntityLivingBase)
        {
            return getSwingProgress((EntityLivingBase) o);
        }

        return 0F;
    }

    public static float swingProgressPrev(Object o)
    {
        if (o != null && o instanceof EntityLivingBase)
        {
            return getSwingProgressPrev((EntityLivingBase) o);
        }

        return 0F;
    }

    public static float headYaw(Object o)
    {
        if (o != null && o instanceof EntityLivingBase)
        {
            return getHeadYaw((EntityLivingBase) o);
        }

        return 0F;
    }

    public static float headPitch(Object o)
    {
        if (o != null && o instanceof EntityLivingBase)
        {
            return getHeadPitch((EntityLivingBase) o);
        }

        return 0F;
    }
}
