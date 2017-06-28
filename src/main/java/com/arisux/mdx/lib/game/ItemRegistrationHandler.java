package com.arisux.mdx.lib.game;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public abstract class ItemRegistrationHandler<MOD extends IMod> extends RegistrationHandler<MOD>
{
    public ItemRegistrationHandler(MOD mod)
    {
        super(mod);
    }

    public Item register(String identifier, Item item)
    {
        item.setUnlocalizedName(String.format("%s:%s", mod.container().getModId(), identifier));
        GameRegistry.register(item, new ResourceLocation(mod.container().getModId(), identifier));

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            Renderers.registerIcon(item);
        }

        return item;
    }
}
