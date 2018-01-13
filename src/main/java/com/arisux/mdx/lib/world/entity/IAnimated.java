package com.arisux.mdx.lib.world.entity;

public interface IAnimated
{
    public static final Animation NO_ANIMATION = Animation.create(0);

    public int getAnimationTick();

    public void setAnimationTick(int tick);

    public Animation getAnimation();

    public void setAnimation(Animation animation);

    public Animation[] getAnimations();
}
