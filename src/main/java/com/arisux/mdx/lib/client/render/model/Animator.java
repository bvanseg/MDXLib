package com.arisux.mdx.lib.client.render.model;

import java.util.HashMap;

import com.arisux.mdx.lib.client.render.Transform;
import com.arisux.mdx.lib.game.Game;
import com.arisux.mdx.lib.world.entity.Animation;
import com.arisux.mdx.lib.world.entity.IAnimated;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Animator
{
    private int                               tempTick;
    private int                               prevTempTick;
    private boolean                           correctAnimation;
    private IAnimated                         entity;
    private HashMap<ModelRenderer, Transform> transformMap;
    private HashMap<ModelRenderer, Transform> prevTransformMap;

    private Animator()
    {
        this.tempTick = 0;
        this.correctAnimation = false;
        this.transformMap = new HashMap<>();
        this.prevTransformMap = new HashMap<>();
    }

    public static Animator create()
    {
        return new Animator();
    }

    public IAnimated getEntity()
    {
        return this.entity;
    }

    public void update(IAnimated entity)
    {
        this.tempTick = this.prevTempTick = 0;
        this.correctAnimation = false;
        this.entity = entity;
        this.transformMap.clear();
        this.prevTransformMap.clear();
    }

    public boolean setAnimation(Animation animation)
    {
        this.tempTick = this.prevTempTick = 0;
        this.correctAnimation = this.entity.getAnimation() == animation;

        return this.correctAnimation;
    }

    public void startKeyframe(int duration)
    {
        if (!this.correctAnimation)
        {
            return;
        }

        this.prevTempTick = this.tempTick;
        this.tempTick += duration;
    }

    public void setStaticKeyframe(int duration)
    {
        this.startKeyframe(duration);
        this.endKeyframe(true);
    }

    public void resetKeyframe(int duration)
    {
        this.startKeyframe(duration);
        this.endKeyframe();
    }

    public void rotate(ModelRenderer box, float x, float y, float z)
    {
        if (!this.correctAnimation)
        {
            return;
        }

        this.getTransform(box).addRotation(x, y, z);
    }

    public void move(ModelRenderer box, float x, float y, float z)
    {
        if (!this.correctAnimation)
        {
            return;
        }

        this.getTransform(box).addOffset(x, y, z);
    }

    private Transform getTransform(ModelRenderer box)
    {
        Transform transform = this.transformMap.get(box);

        if (transform == null)
        {
            transform = new Transform();
            this.transformMap.put(box, transform);
        }

        return transform;
    }

    public void endKeyframe()
    {
        this.endKeyframe(false);
    }

    private void endKeyframe(boolean stationary)
    {
        if (!this.correctAnimation)
        {
            return;
        }

        int animationTick = this.entity.getAnimationTick();

        if (animationTick >= this.prevTempTick && animationTick < this.tempTick)
        {
            if (stationary)
            {
                for (ModelRenderer box : this.prevTransformMap.keySet())
                {
                    Transform transform = this.prevTransformMap.get(box);

                    box.rotateAngleX += transform.getRotationX();
                    box.rotateAngleY += transform.getRotationY();
                    box.rotateAngleZ += transform.getRotationZ();
                    box.rotationPointX += transform.getOffsetX();
                    box.rotationPointY += transform.getOffsetY();
                    box.rotationPointZ += transform.getOffsetZ();
                }
            }
            else
            {
                float tick = (animationTick - this.prevTempTick + Game.partialTicks()) / (this.tempTick - this.prevTempTick);
                float inc = MathHelper.sin((float) (tick * Math.PI / 2.0F)), dec = 1.0F - inc;

                for (ModelRenderer box : this.prevTransformMap.keySet())
                {
                    Transform transform = this.prevTransformMap.get(box);

                    box.rotateAngleX += dec * transform.getRotationX();
                    box.rotateAngleY += dec * transform.getRotationY();
                    box.rotateAngleZ += dec * transform.getRotationZ();
                    box.rotationPointX += dec * transform.getOffsetX();
                    box.rotationPointY += dec * transform.getOffsetY();
                    box.rotationPointZ += dec * transform.getOffsetZ();
                }

                for (ModelRenderer box : this.transformMap.keySet())
                {
                    Transform transform = this.transformMap.get(box);

                    box.rotateAngleX += inc * transform.getRotationX();
                    box.rotateAngleY += inc * transform.getRotationY();
                    box.rotateAngleZ += inc * transform.getRotationZ();
                    box.rotationPointX += inc * transform.getOffsetX();
                    box.rotationPointY += inc * transform.getOffsetY();
                    box.rotationPointZ += inc * transform.getOffsetZ();
                }
            }
        }

        if (!stationary)
        {
            this.prevTransformMap.clear();
            this.prevTransformMap.putAll(this.transformMap);
            this.transformMap.clear();
        }
    }
}