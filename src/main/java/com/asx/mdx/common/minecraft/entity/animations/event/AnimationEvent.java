package com.asx.mdx.common.minecraft.entity.animations.event;

import com.asx.mdx.common.minecraft.entity.animations.Animation;
import com.asx.mdx.common.minecraft.entity.animations.IAnimated;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class AnimationEvent<T extends Entity & IAnimated> extends Event
{
    protected Animation animation;
    private T           entity;

    AnimationEvent(T entity, Animation animation)
    {
        this.entity = entity;
        this.animation = animation;
    }

    public T getEntity()
    {
        return this.entity;
    }

    public Animation getAnimation()
    {
        return this.animation;
    }

    @Cancelable
    public static class Start<T extends Entity & IAnimated> extends AnimationEvent<T>
    {
        public Start(T entity, Animation animation)
        {
            super(entity, animation);
        }

        public void setAnimation(Animation animation)
        {
            this.animation = animation;
        }
    }

    public static class Tick<T extends Entity & IAnimated> extends AnimationEvent<T>
    {
        protected int tick;

        public Tick(T entity, Animation animation, int tick)
        {
            super(entity, animation);
            this.tick = tick;
        }

        public int getTick()
        {
            return this.tick;
        }
    }
}
