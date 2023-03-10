package com.asx.mdx.common.io.commands;

import com.asx.mdx.common.minecraft.Chat;
import com.asx.mdx.common.minecraft.Pos;
import com.asx.mdx.common.minecraft.structure.SchematicLoader;
import com.asx.mdx.common.minecraft.structure.Structure;
import com.asx.mdx.common.minecraft.structure.StructureGenerationHandler;
import com.asx.mdx.common.minecraft.entity.player.Players;
import com.asx.mdx.common.minecraft.storage.Schematic;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandGenerate extends CommandBase
{
    @Override
    public String getName()
    {
        return "genschematic";
    }

    @Override
    public String getUsage(ICommandSender commandSender)
    {
        return "/genschematic <name> [x] [y] [z]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        final EntityPlayer PLAYER = Players.getPlayerForCommandSender(sender);

        if (args.length == 1 || args.length == 4)
        {
            String schematicTargetName = args[0];

            for (Schematic schematic : SchematicLoader.getSchematicRegistry())
            {
                String schematicFileName = schematic.getFile().getName();
                final String SCHEMATIC_NAME = schematicFileName.replace(".schematic", "");

                if (schematicTargetName.equals(SCHEMATIC_NAME))
                {
                    Pos data = null;

                    if (args.length == 1)
                    {
                        data = new Pos(PLAYER.posX, PLAYER.posY, PLAYER.posZ);
                    }
                    else if (args.length == 4)
                    {
                        double posX = Double.parseDouble(args[1]);
                        double posY = Double.parseDouble(args[2]);
                        double posZ = Double.parseDouble(args[3]);
                        data = new Pos(posX, posY, posZ);
                    }
                    

                    WorldServer worldServer = server.getWorld(PLAYER.dimension);
                    Structure structure = new Structure(schematic, worldServer, data) {
                        @Override
                        public String getName()
                        {
                            return SCHEMATIC_NAME;
                        }

                        @Override
                        public void onProcessing()
                        {
                            ;
                        }

                        @Override
                        public void onProcessingComplete()
                        {
                            PLAYER.sendMessage(Chat.component("Generation of " + this.getName() + " completed."));
                        }
                    };

                    StructureGenerationHandler.addStructureToQueue(structure);
                    sender.sendMessage(Chat.component("Started generation of " + SCHEMATIC_NAME));
                }
            }
        }
    }
}
