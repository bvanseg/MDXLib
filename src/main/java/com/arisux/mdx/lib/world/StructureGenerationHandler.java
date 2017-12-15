package com.arisux.mdx.lib.world;

import java.util.ArrayList;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


public class StructureGenerationHandler
{
    public static final StructureGenerationHandler INSTANCE = new StructureGenerationHandler();
    private ArrayList<Structure> structuresInQueue = new ArrayList<Structure>();

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void serverTick(TickEvent.ServerTickEvent event)
    {
        ArrayList<Structure> structures = (ArrayList<Structure>) structuresInQueue.clone();

        for (Structure structure : structures)
        {
            if (structure.process())
            {
                this.structuresInQueue.remove(structure);
            }
        }
    }
    
    public static void addStructureToQueue(Structure structure)
    {
        getStructuresInQueue().add(structure);
    }

    public static ArrayList<Structure> getStructuresInQueue()
    {
        return INSTANCE.structuresInQueue;
    }
}
