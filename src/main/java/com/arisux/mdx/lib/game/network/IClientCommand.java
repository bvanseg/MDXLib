package com.arisux.mdx.lib.game.network;

import net.minecraft.entity.player.EntityPlayer;

public interface IClientCommand
{
    public void executeClient(EntityPlayer player, String[] args);
}
