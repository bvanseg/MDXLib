package com.asx.mdx.internal;

import com.asx.mdx.common.io.commands.CommandBlockScanner;
import com.asx.mdx.common.io.commands.CommandBlockUpdate;
import com.asx.mdx.common.io.commands.CommandChunkBorders;
import com.asx.mdx.common.io.commands.CommandChunkPlane;
import com.asx.mdx.common.io.commands.CommandGenerate;
import com.asx.mdx.common.io.commands.CommandKeyframeDifferenceCalculator;
import com.asx.mdx.common.io.commands.CommandTeleportDimension;
import com.asx.mdx.common.mods.IInitEvent;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;

public class CommandHandler implements IInitEvent
{
    public static final CommandHandler INSTANCE = new CommandHandler();

    public CommandGenerate             generate;
    public CommandBlockUpdate          blockUpdate;
    public CommandBlockScanner         blockScanner;
    public CommandChunkBorders         chunkBorders;
    public CommandChunkPlane           chunkPlane;

    @Override
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(this.generate = new CommandGenerate());
        event.registerServerCommand(this.blockUpdate = new CommandBlockUpdate());
        event.registerServerCommand(this.blockScanner = new CommandBlockScanner());
        event.registerServerCommand(this.chunkBorders = new CommandChunkBorders());
        event.registerServerCommand(this.chunkPlane = new CommandChunkPlane());
        event.registerServerCommand(new CommandTeleportDimension());

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            event.registerServerCommand(new CommandKeyframeDifferenceCalculator());
        }
    }
}
