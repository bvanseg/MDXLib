package com.arisux.mdxlib.lib.game;

import java.util.HashMap;

import com.arisux.mdxlib.MDX;
import com.arisux.mdxlib.lib.client.render.ItemRenderer;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Renderers implements IPostInitEvent
{
    public static Renderers                      INSTANCE       = new Renderers();
    private final HashMap<Item, ItemRenderer<?>> ITEM_RENDERERS = new HashMap<Item, ItemRenderer<?>>();

    public static void register(Item item, ItemRenderer<?> renderer)
    {
        if (item != null && item.getRegistryName() == null ||renderer == null)
        {
            MDX.log().warn("Failed to register Item Renderer for item: " + item.getRegistryName());
            return;
        }

        MDX.log().info("Registerring Item Renderer: " + item.getRegistryName());
        INSTANCE.ITEM_RENDERERS.put(item, renderer);
        Game.minecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    public static ItemRenderer<?> getItemRenderer(Item item)
    {
        if (INSTANCE.ITEM_RENDERERS.containsKey(item))
        {
            return INSTANCE.ITEM_RENDERERS.get(item);
        }

        return null;
    }

    @Override
    public void post(FMLPostInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event)
    {
        for (Item item : ITEM_RENDERERS.keySet())
        {
            ModelResourceLocation resource = new ModelResourceLocation(item.getRegistryName(), "inventory");
            ItemRenderer<?> itemRenderer = ITEM_RENDERERS.get(item);

            if (itemRenderer != null)
            {
                event.getModelRegistry().putObject(resource, itemRenderer);
                MDX.log().info("Injecting Item Renderer: " + resource);
            }
            else
            {
                MDX.log().warn(String.format("Error Injecting Item Renderer (%s): %s", itemRenderer, resource));
            }
        }
    }
}
