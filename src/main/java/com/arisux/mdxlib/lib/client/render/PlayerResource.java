package com.arisux.mdxlib.lib.client.render;

import com.arisux.mdxlib.lib.world.entity.player.Players;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class PlayerResource
{
    private String name, uuid;
    private ResourceLocation resource;

    public PlayerResource(String username)
    {
        this.name = username;
        this.uuid = username;

        Thread uuidThread = new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                uuid = Players.getUUID(name);
            }
        };
        uuidThread.start();
    }

    public String playerName()
    {
        return name;
    }

    public String playerUUID()
    {
        return uuid;
    }

    public ResourceLocation get()
    {
        return this.resource;
    }

    public ResourceLocation store(ResourceLocation resource)
    {
        return this.resource = resource;
    }
}
