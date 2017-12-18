package com.arisux.mdx.lib.game;

import com.arisux.mdx.MDX;
import com.arisux.mdx.lib.game.network.server.PacketCommandBlockScanner;
import com.arisux.mdx.lib.game.network.server.PacketCommandChunkBorders;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class Network extends SimpleNetworkWrapper implements IInitEvent
{
    public static final Network instance      = new Network();
    private int                 descriminator = 0;

    public Network()
    {
        super(MDX.Properties.ID.toUpperCase());
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        this.registerMessage(Side.CLIENT, PacketCommandBlockScanner.class);
        this.registerMessage(Side.CLIENT, PacketCommandChunkBorders.class);
    }

    /**
     * @param side - The side this packet will be sent to.
     * @param packet - The packet being registered.
     */
    @SuppressWarnings("unchecked")
    private <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Side side, Class<?> packet)
    {
        this.registerMessage((Class<? extends IMessageHandler<REQ, REPLY>>) packet, (Class<REQ>) packet, descriminator++, side);
    }
}
