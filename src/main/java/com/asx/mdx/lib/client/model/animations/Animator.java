package com.asx.mdx.lib.client.model.animations;

import java.util.HashMap;

import com.asx.mdx.lib.client.util.Transform;
import com.asx.mdx.lib.util.Game;
import com.asx.mdx.lib.world.entity.animations.Animation;
import com.asx.mdx.lib.world.entity.animations.IAnimated;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Animator
{
    private IAnimated                         entity;
    private boolean                           correct;
    private int                               tick;
    private int                               tickPrev;
    private HashMap<ModelRenderer, Transform> transforms;
    private HashMap<ModelRenderer, Transform> transformsPrev;

    private Animator()
    {
        this.tick = 0;
        this.correct = false;
        this.transforms = new HashMap<>();
        this.transformsPrev = new HashMap<>();
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
        this.tick = this.tickPrev = 0;
        this.correct = false;
        this.entity = entity;
        this.transforms.clear();
        this.transformsPrev.clear();
    }

    public boolean setAnimation(Animation animation)
    {
        this.tick = this.tickPrev = 0;
        this.correct = this.entity.getAnimation() == animation;

        return this.correct;
    }

    public void startKeyframe(int duration)
    {
        if (!this.correct)
        {
            return;
        }

        this.tickPrev = this.tick;
        this.tick += duration;
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
        if (!this.correct)
        {
            return;
        }

        this.getTransform(box).addRotation(x, y, z);
    }

    public void move(ModelRenderer box, float x, float y, float z)
    {
        if (!this.correct)
        {
            return;
        }

        this.getTransform(box).addOffset(x, y, z);
    }

    private Transform getTransform(ModelRenderer box)
    {
        Transform t = this.transforms.get(box);

        if (t == null)
        {
            t = new Transform();
            this.transforms.put(box, t);
        }

        return t;
    }

    public void endKeyframe()
    {
        this.endKeyframe(false);
    }

    private void endKeyframe(boolean stationary)
    {
        if (!this.correct)
        {
            return;
        }

        int animationTick = this.entity.getAnimationTick();

        if (animationTick >= this.tickPrev && animationTick < this.tick)
        {
            if (stationary)
            {
                for (ModelRenderer box : this.transformsPrev.keySet())
                {
                    Transform t = this.transformsPrev.get(box);

                    box.rotateAngleX += t.getRotationX();
                    box.rotateAngleY += t.getRotationY();
                    box.rotateAngleZ += t.getRotationZ();
                    box.rotationPointX += t.getOffsetX();
                    box.rotationPointY += t.getOffsetY();
                    box.rotationPointZ += t.getOffsetZ();
                }
            }
            else
            {
                float tick = (animationTick - this.tickPrev + Game.partialTicks()) / (this.tick - this.tickPrev);
                float inc = MathHelper.sin((float) (tick * Math.PI / 2.0F)), dec = 1.0F - inc;

                for (ModelRenderer box : this.transformsPrev.keySet())
                {
                    Transform t = this.transformsPrev.get(box);

                    box.rotateAngleX += dec * t.getRotationX();
                    box.rotateAngleY += dec * t.getRotationY();
                    box.rotateAngleZ += dec * t.getRotationZ();
                    box.rotationPointX += dec * t.getOffsetX();
                    box.rotationPointY += dec * t.getOffsetY();
                    box.rotationPointZ += dec * t.getOffsetZ();
                }

                for (ModelRenderer box : this.transforms.keySet())
                {
                    Transform t = this.transforms.get(box);

                    box.rotateAngleX += inc * t.getRotationX();
                    box.rotateAngleY += inc * t.getRotationY();
                    box.rotateAngleZ += inc * t.getRotationZ();
                    box.rotationPointX += inc * t.getOffsetX();
                    box.rotationPointY += inc * t.getOffsetY();
                    box.rotationPointZ += inc * t.getOffsetZ();
                }
            }
        }

        if (!stationary)
        {
            this.transformsPrev.clear();
            this.transformsPrev.putAll(this.transforms);
            this.transforms.clear();
        }
    }
}