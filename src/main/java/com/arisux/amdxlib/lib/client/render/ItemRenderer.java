package com.arisux.amdxlib.lib.client.render;

import com.arisux.amdxlib.lib.client.Model;
import com.arisux.amdxlib.lib.client.TexturedModel;
import com.arisux.amdxlib.lib.client.PlayerResourceStorage;
import com.arisux.amdxlib.lib.game.Game;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public abstract class ItemRenderer implements IItemRenderer
{
    protected Minecraft                             mc = Game.minecraft();
    private TexturedModel<? extends Model> modelTexMap;
    protected PlayerResourceStorage                 resourceStorage;
    private boolean                                 rendersInFirstPerson;
    private boolean                                 rendersInThirdPerson;
    private boolean                                 rendersInInventory;
    private boolean                                 rendersInWorld;

    public ItemRenderer(TexturedModel<? extends Model> modelTexMap)
    {
        this.resourceStorage = new PlayerResourceStorage();
        this.modelTexMap = modelTexMap;
        this.rendersInFirstPerson = true;
        this.rendersInThirdPerson = true;
        this.rendersInInventory = true;
        this.rendersInWorld = true;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        switch (type)
        {
            case EQUIPPED:
                return rendersInThirdPerson;

            case EQUIPPED_FIRST_PERSON:
                return rendersInFirstPerson;

            case INVENTORY:
                return rendersInInventory;

            case ENTITY:
                return rendersInWorld;

            default:
                return false;
        }
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        try
        {
            switch (type)
            {
                case EQUIPPED:
                    OpenGL.pushMatrix();
                    this.renderThirdPerson(item, data);
                    OpenGL.popMatrix();
                    break;
                case EQUIPPED_FIRST_PERSON:
                    OpenGL.pushMatrix();
                    this.renderFirstPerson(item, data);
                    OpenGL.popMatrix();
                    break;
                case INVENTORY:
                    OpenGL.pushMatrix();
                    OpenGL.rotate(-45, 1, 0, 0);
                    OpenGL.rotate(180, 0, 1, 0);
                    OpenGL.translate(-16, 0, 0);
                    this.renderInInventory(item, data);
                    OpenGL.popMatrix();
                    break;
                case ENTITY:
                    OpenGL.pushMatrix();
                    this.renderInWorld(item, data);
                    OpenGL.popMatrix();
                    break;
                default:
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void renderThirdPerson(ItemStack item, Object... data)
    {
        ;
    }

    public void renderFirstPerson(ItemStack item, Object... data)
    {
        ;
    }

    public void renderInInventory(ItemStack item, Object... data)
    {
        ;
    }

    public void renderInWorld(ItemStack item, Object... data)
    {
        ;
    }

    public ItemRenderer setRendersInThirdPerson(boolean rendersInThirdPerson)
    {
        this.rendersInThirdPerson = rendersInThirdPerson;
        return this;
    }

    public ItemRenderer setRendersInFirstPerson(boolean rendersInFirstPerson)
    {
        this.rendersInFirstPerson = rendersInFirstPerson;
        return this;
    }

    public ItemRenderer setRendersInInventory(boolean rendersInInventory)
    {
        this.rendersInInventory = rendersInInventory;
        return this;
    }

    public ItemRenderer setRendersInWorld(boolean rendersInWorld)
    {
        this.rendersInWorld = rendersInWorld;
        return this;
    }

    public TexturedModel<? extends Model> getModelTexMap()
    {
        return modelTexMap;
    }

    public Model getModel()
    {
        return this.getModelTexMap().getModel();
    }

    public Texture getTexture()
    {
        return this.getModelTexMap().getTexture();
    }

    public boolean firstPersonRenderCheck(Object o)
    {
        return o == mc.renderViewEntity && mc.gameSettings.thirdPersonView == 0 && (!(mc.currentScreen instanceof GuiInventory) && !(mc.currentScreen instanceof GuiContainerCreative));
    }
}
