package com.asx.mdx.commands;

import com.asx.mdx.lib.world.Dimension;
import com.asx.mdx.lib.world.entity.player.Players;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldProvider;

public class CommandTeleportDimension extends CommandBase
{
    @Override
    public String getName()
    {
        return "tpdim";
    }

    @Override
    public String getUsage(ICommandSender commandSender)
    {
        return "Teleport the player to the dimension with the target dimension ID.";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender commandSender, String[] args)
    {
        EntityPlayer player = Players.getPlayerForCommandSender(commandSender);

        if (args.length == 0)
        {
            commandSender.sendMessage(new TextComponentString(String.format("Unable to transport player %s to dimension, no dimension ID provided.", player.getName())));
        }

        if (args.length >= 1)
        {
            Integer dimensionId = Integer.parseInt(args[0]);
            WorldProvider providerForDim = server.getWorld(dimensionId).provider;

            if (player instanceof EntityPlayerMP)
            {
                EntityPlayerMP playerMP = (EntityPlayerMP) player;

                if (providerForDim != null)
                {
                    commandSender.sendMessage(new TextComponentString(String.format("Transporting to dimension %s", providerForDim.getDimensionType().getName())));
                    Dimension.transferEntityTo(playerMP, dimensionId);
                }
            }
        }
    }
}
