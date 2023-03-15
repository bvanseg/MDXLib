package com.asx.mdx.common.io.commands;

import net.minecraft.entity.player.EntityPlayer;

public interface IClientCommand
{
    public void executeClient(EntityPlayer player, String[] args);
}
