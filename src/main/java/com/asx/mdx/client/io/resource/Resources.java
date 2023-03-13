package com.asx.mdx.client.io.resource;

import com.asx.mdx.internal.MDX;
import com.asx.mdx.client.render.model.texture.Texture;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Resources
{
    public static final Texture BLANK = new Texture(MDX.Properties.ID, "textures/misc/blank.png");
}
