package com.asx.mdx.server;

import com.asx.mdx.common.Game;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A class designed to provide easy access to a variety of Minecraft core methods, some
 * of which may naturally have restricted access. The names of these methods will never change.
 */
public class ServerGame extends Game
{
    public static final ServerGame instance = new ServerGame();

    protected ServerGame() { super(); }
    public MinecraftServer server()
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }
}
