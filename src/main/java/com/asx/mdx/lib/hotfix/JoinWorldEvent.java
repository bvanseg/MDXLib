package com.asx.mdx.lib.hotfix;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class JoinWorldEvent
{
    public static final JoinWorldEvent INSTANCE = new JoinWorldEvent();

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (!(event.getEntity() instanceof EntityPlayer) && event.getEntity() instanceof EntityCreature)
        {
            ((EntityCreature) event.getEntity()).tasks.addTask(1, new EntityAIWanderPatch(((EntityCreature) event.getEntity()), 0.8D));
        }
    }
}
