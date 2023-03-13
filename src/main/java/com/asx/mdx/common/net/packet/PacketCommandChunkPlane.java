package com.asx.mdx.common.net.packet;

import com.asx.mdx.client.ClientGame;
import com.asx.mdx.internal.CommandHandler;
import com.asx.mdx.common.Game;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketCommandChunkPlane implements IMessage, IMessageHandler<PacketCommandChunkPlane, PacketCommandChunkPlane>
{
    private int size;
    private String[] args;

    public PacketCommandChunkPlane()
    {
        ;
    }

    public PacketCommandChunkPlane(String args[])
    {
        this.size = args.length;
        this.args = args;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.size = buf.readInt();
        this.args = new String[this.size];

        for (int i = this.size - 1; i >= 0; i--)
        {
            this.args[i] = ByteBufUtils.readUTF8String(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.size);
        
        for (int i = this.size - 1; i >= 0; i--)
        {
            ByteBufUtils.writeUTF8String(buf, args[i]);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public PacketCommandChunkPlane onMessage(PacketCommandChunkPlane packet, MessageContext ctx)
    {
        ClientGame.instance.minecraft().addScheduledTask(new Runnable()
        {
            @Override
            public void run()
            {
                CommandHandler.INSTANCE.chunkPlane.executeClient(ClientGame.instance.minecraft().player, packet.args);
            }
        });
        return null;
    }
}
