package com.asx.mdx.common.net.packet;

import com.asx.mdx.client.ClientGame;
import com.asx.mdx.common.Game;
import com.asx.mdx.common.minecraft.entity.animations.IAnimated;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketAnimationPause implements IMessage, IMessageHandler<PacketAnimationPause, PacketAnimationPause>
{
    private int     entityID;
    private boolean pause;

    public PacketAnimationPause()
    {
        ;
    }

    public PacketAnimationPause(int entityID, boolean pause)
    {
        this.entityID = entityID;
        this.pause = pause;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public PacketAnimationPause onMessage(PacketAnimationPause packet, MessageContext ctx)
    {
        ClientGame.instance.minecraft().addScheduledTask(new Runnable() {
            @Override
            public void run()
            {
                IAnimated animated = (IAnimated) ClientGame.instance.minecraft().player.world.getEntityByID(packet.entityID);

                if (animated != null)
                {
                    if (packet.pause)
                    {
                        animated.pauseAnimation();
                    }
                    else
                    {
                        animated.playAnimation();
                    }
                }
            }
        });
        return null;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf)
    {
        this.entityID = byteBuf.readInt();
        this.pause = byteBuf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf byteBuf)
    {
        byteBuf.writeInt(this.entityID);
        byteBuf.writeBoolean(this.pause);
    }
}