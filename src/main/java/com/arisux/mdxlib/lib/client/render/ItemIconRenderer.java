package com.arisux.mdxlib.lib.client.render;

import com.arisux.mdxlib.lib.client.Model;
import com.arisux.mdxlib.lib.client.TexturedModel;

public abstract class ItemIconRenderer<M extends Model> extends ItemRenderer<M>
{
    public ItemIconRenderer(TexturedModel<M> model)
    {
        super(model);
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return true;
    }

    @Override
    public boolean isGui3d()
    {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return false;
    }
}
