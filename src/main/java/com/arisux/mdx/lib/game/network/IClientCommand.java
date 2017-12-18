package com.arisux.mdx.lib.game.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public interface IClientCommand
{
    public void executeClient(Minecraft minecraft, EntityPlayer player, String[] args);
}
