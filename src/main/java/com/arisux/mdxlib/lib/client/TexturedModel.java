package com.arisux.mdxlib.lib.client;

import com.arisux.mdxlib.MDX;
import com.arisux.mdxlib.lib.client.render.Texture;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TexturedModel<MODEL extends Model>
{
    private Texture texture;
    private MODEL model;
    
    public TexturedModel(MODEL model, Texture texture)
    {
        this.model = model;
        this.texture = texture;
    }

    public MODEL getModel()
    {
        return this.model;
    }

    public Texture getTexture()
    {
        return this.texture;
    }
    
    public void bindTexture()
    {
        this.getTexture().bind();
    }
    
    public void drawStandaloneModel()
    {
        this.drawStandaloneModel(null);
    }
    
    public void drawStandaloneModel(Object o)
    {
        this.getModel().render(o);
    }
    
    public void draw()
    {
        this.draw(null);
    }
    
    public void draw(Object o)
    {
        if (this.model != null && this.texture != null)
        {
            this.bindTexture();
            this.drawStandaloneModel(o);
        }
    }
    
    public TexturedModel<MODEL> clone()
    {
        try
        {
            Model newModel = this.model.getClass().getConstructor().newInstance(new Object[] {});
            TexturedModel<MODEL> texturedModel = new TexturedModel<MODEL>((MODEL) newModel, this.texture);
            MDX.log().info(String.format("[%s->%s] [%s->%s]", this.model, newModel, this, texturedModel));
            return texturedModel;
        }
        catch (Exception e)
        {
            MDX.log().warn("Failed to clone model: " + e);
            e.printStackTrace();
        }
        
        return null;
    }
}
