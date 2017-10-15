package com.arisux.mdx.lib.client;

import com.arisux.mdx.MDX;
import com.arisux.mdx.lib.client.render.Texture;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Resources
{
    public static final Texture BLANK = new Texture(MDX.Properties.ID, "textures/misc/blank.png");
}
