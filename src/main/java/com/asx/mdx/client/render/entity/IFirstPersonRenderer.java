package com.asx.mdx.client.render.entity;

import net.minecraftforge.fml.common.eventhandler.Event;

public interface IFirstPersonRenderer
{
    public void renderFirstPerson(Event event, float partialTicks);
}