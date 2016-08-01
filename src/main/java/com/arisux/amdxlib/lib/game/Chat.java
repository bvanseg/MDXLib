package com.arisux.amdxlib.lib.game;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class Chat
{
    public static String format(String message)
    {
        return message.replaceAll("&", "\u00A7");
    }

    public static ChatComponentText component(String message)
    {
        return new ChatComponentText(format(message));
    }

    public static void sendTo(EntityPlayer player, String message)
    {
        player.addChatMessage(component(message));
    }

    public static void sendTo(EntityPlayer player, ChatComponentText component)
    {
        player.addChatMessage(component);
    }

    public static void sendTo(ICommandSender sender, ChatComponentText component)
    {
        sender.addChatMessage(component);
    }
}
