package com.arisux.mdx.commands;

import java.util.ArrayList;

import com.arisux.mdx.lib.world.Pos;
import com.arisux.mdx.lib.world.entity.player.Players;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class CommandBlockUpdate extends CommandBase
{
    @Override
    public String getName()
    {
        return "blockupdate";
    }

    @Override
    public String getUsage(ICommandSender commandSender)
    {
        return "Triggers block updates on all blocks in the specified radius.";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayer player = Players.getPlayerForCommandSender(sender);

        if (args.length == 1)
        {
            int radius = Integer.parseInt(args[0]);
            int updates = triggerBlockUpdates(player.world, radius, player.posX, player.posY, player.posZ);

            player.sendMessage(new TextComponentString(String.format("Triggered a block update on %s block(s) in a radius of %s", updates, radius)));
        }
    }
    
    public static int triggerBlockUpdates(World world, int radius, double posX, double posY, double posZ)
    {
        int counter = 0;

        ArrayList<Pos> positions = com.arisux.mdx.lib.world.block.Blocks.getCoordDataInRange((int) posX, (int) posY, (int) posZ, radius);

        for (Pos pos : positions)
        {
            BlockPos blockPos = pos.blockPos();
            IBlockState state = world.getBlockState(blockPos);

            if (state.getBlock() != Blocks.AIR)
            {
                world.notifyBlockUpdate(blockPos, state, state, 4);
                world.updateBlockTick(blockPos, state.getBlock(), 0, 1);
                counter++;
            }
        }
        
        return counter;
    }
}
