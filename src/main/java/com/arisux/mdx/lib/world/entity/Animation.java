package com.arisux.mdx.lib.world.entity;

public class Animation
{
    private int duration;

    private Animation(int duration)
    {
        this.duration = duration;
    }

    public static Animation create(int duration)
    {
        return new Animation(duration);
    }

    public int getDuration()
    {
        return this.duration;
    }
}