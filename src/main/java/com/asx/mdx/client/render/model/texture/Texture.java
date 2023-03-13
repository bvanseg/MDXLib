package com.asx.mdx.client.render.model.texture;

import com.asx.mdx.client.render.Draw;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Texture extends ResourceLocation
{
    public Texture(ResourceLocation resource)
    {
        this(resource.getNamespace(), resource.getPath());
    }

    public Texture(String location)
    {
        super(location);
    }

    public Texture(String domain, String location)
    {
        super(domain, location);
    }

    @SideOnly(Side.CLIENT)
    public void bind()
    {
        Draw.bindTexture(this);
    }
}
