package com.arisux.mdx.lib.game;

import java.util.HashMap;

import com.arisux.mdx.MDX;
import com.arisux.mdx.lib.client.render.ItemIconRenderer;
import com.arisux.mdx.lib.client.render.ItemRenderer;

import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Renderers implements IPostInitEvent
{
    public static Renderers                          INSTANCE       = new Renderers();
    private final HashMap<Item, ItemRenderer<?>>     ITEM_RENDERERS = new HashMap<Item, ItemRenderer<?>>();
    private final HashMap<Item, ItemIconRenderer<?>> ICON_RENDERERS = new HashMap<Item, ItemIconRenderer<?>>();

    public static ModelManager modelManager()
    {
        return Game.minecraft().modelManager;
    }

    public static BlockModelShapes modelProvider()
    {
        return modelManager().getBlockModelShapes();
    }
    
    /**
     * A better way of registering entity renderers.
     */
    public static <E extends Entity, R extends Render<? super E>> void registerRenderer(Class<E> entityClass, Class<R> renderer)
    {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, new IRenderFactory<E>()
        {
            @Override
            public Render<E> createRenderFor(RenderManager m)
            {
                try
                {
                    Render<E> render = (Render<E>) (renderer.getConstructor(RenderManager.class)).newInstance(new Object[] { m });
                    MDX.log().info("Registered entity renderer for " + entityClass + ": " + render);
                    return render;
                }
                catch (Exception e)
                {
                    MDX.log().warn("Failed to construct entity renderer: " + (renderer != null ? renderer.getName() : renderer));
                    e.printStackTrace();
                }

                return null;
            }
        });
    }
    
    /**
     * Working replacement for ItemRenderer, but should only be used in rare cases when
     * the built in rendering system is not sufficient.
     */
    public static void registerItemRenderer(Item item, ItemRenderer<?> renderer)
    {
        if (item != null && item.getRegistryName() == null || renderer == null)
        {
            MDX.log().warn("Failed to register Item Renderer for item: " + item.getRegistryName());
            return;
        }

        if (getItemRenderer(item) == null)
        {
            INSTANCE.ITEM_RENDERERS.put(item, renderer);
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
    }

    public static void registerIcon(Item item)
    {
        ItemIconRenderer<?> renderer = new ItemIconRenderer(item);

        if (item != null && item.getRegistryName() == null || renderer == null)
        {
            MDX.log().warn("Failed to register Item Icon Renderer for item: " + item.getRegistryName());
            return;
        }

        if (getItemRenderer(item) == null)
        {
            INSTANCE.ICON_RENDERERS.put(item, renderer);
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
    }

    public static ItemRenderer<?> getItemRenderer(Item item)
    {
        if (INSTANCE.ITEM_RENDERERS.containsKey(item))
        {
            return INSTANCE.ITEM_RENDERERS.get(item);
        }

        return null;
    }

    public static ItemRenderer<?> getIconRenderer(Item item)
    {
        if (INSTANCE.ICON_RENDERERS.containsKey(item))
        {
            return INSTANCE.ICON_RENDERERS.get(item);
        }

        return null;
    }

    @Override
    public void post(IMod mod, FMLPostInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event)
    {
        for (Item item : ITEM_RENDERERS.keySet())
        {
            if (ICON_RENDERERS.containsKey(item))
            {
                ICON_RENDERERS.remove(item);
            }

            ModelResourceLocation resource = new ModelResourceLocation(item.getRegistryName(), "inventory");
            ItemRenderer<?> itemRenderer = ITEM_RENDERERS.get(item);

            if (itemRenderer != null)
            {
                event.getModelRegistry().putObject(resource, itemRenderer);
            }
            else
            {
                MDX.log().warn(String.format("Error Injecting Item Renderer (%s): %s", itemRenderer, resource));
            }
        }

        for (Item item : ICON_RENDERERS.keySet())
        {
            ModelResourceLocation resource = new ModelResourceLocation(item.getRegistryName(), "inventory");
            ItemRenderer<?> itemRenderer = ICON_RENDERERS.get(item);

            if (itemRenderer != null)
            {
                event.getModelRegistry().putObject(resource, itemRenderer);
            }
            else
            {
                MDX.log().warn(String.format("Error Injecting Item Icon Renderer (%s): %s", itemRenderer, resource));
            }
        }
    }
}
