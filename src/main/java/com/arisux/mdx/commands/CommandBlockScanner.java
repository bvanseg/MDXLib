package com.arisux.mdx.commands;

import com.arisux.mdx.MDX;
import com.arisux.mdx.lib.client.DebugToolsRenderer;
import com.arisux.mdx.lib.client.DebugToolsRenderer.BlockScanner;
import com.arisux.mdx.lib.game.network.IClientCommand;
import com.arisux.mdx.lib.game.network.server.PacketCommandBlockScanner;
import com.arisux.mdx.lib.world.entity.player.Players;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommandBlockScanner extends CommandBase implements IClientCommand
{
    @Override
    public String getName()
    {
        return "blockscanner";
    }

    @Override
    public String getUsage(ICommandSender commandSender)
    {
        commandSender.sendMessage(new TextComponentString("/blockscanner add <range> <color_hex>"));
        return "/blockscanner <enabled/disabled/clear/add> - Block scanner debug tool management command.";
    }
    
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return sender.canUseCommand(2, "");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayer player = Players.getPlayerForCommandSender(sender);

        if (player != null && player instanceof EntityPlayerMP)
        {
            EntityPlayerMP playerMP = (EntityPlayerMP) player;
            MDX.network().sendTo(new PacketCommandBlockScanner(args), playerMP);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void executeClient(EntityPlayer player, String[] args)
    {
        if (args.length >= 1)
        {
            if (args[0].equalsIgnoreCase("enabled") || args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("on"))
            {
                System.out.println("teest");
                BlockScanner.setBlockScannerEnabled(true);
                player.sendMessage(new TextComponentString("Enabled block scanner."));
                return;
            }
            else if (args[0].equalsIgnoreCase("disabled") || args[0].equalsIgnoreCase("false") || args[0].equalsIgnoreCase("off"))
            {
                BlockScanner.setBlockScannerEnabled(false);
                player.sendMessage(new TextComponentString("Disabled block scanner."));
                return;
            }
            else if (args[0].equalsIgnoreCase("clear"))
            {
                BlockScanner.destroyBlockScanners();
                player.sendMessage(new TextComponentString("Cleared block scanner tracking array."));
                return;
            }
            else if (args[0].equalsIgnoreCase("add"))
            {
                ItemStack itemHeld = player.getHeldItemMainhand();

                if (itemHeld != null && itemHeld.getItem() != null && itemHeld.getItem() != Items.AIR)
                {
                    Block block = Block.getBlockFromItem(itemHeld.getItem());

                    if (block != null && block != Blocks.AIR)
                    {
                        int scanRange = 32;
                        int color = 0xFFFF0000;

                        if (args.length >= 2 && !args[1].isEmpty())
                        {
                            scanRange = Integer.parseInt(args[1]);
                        }

                        if (args.length >= 3 && !args[2].isEmpty())
                        {
                            color = Integer.parseInt(args[2], 16);
                        }

                        float r = (color >> 16 & 255) / 255.0F;
                        float g = (color >> 8 & 255) / 255.0F;
                        float b = (color & 255) / 255.0F;

                        BlockScanner.scanForBlock(new BlockScanner.Scannable(block, scanRange, r, g, b, 0.4F));
                        player.sendMessage(new TextComponentString(String.format("Tracking %s with a range of %s and a render color of %s", block.getLocalizedName(), scanRange, color)));
                    }
                }
            }
        }
        else
        {
            player.sendMessage(new TextComponentString("Incorrect amount of arguments! See the command context!"));
        }
    }
}
