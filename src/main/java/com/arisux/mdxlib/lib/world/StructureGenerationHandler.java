package com.arisux.mdxlib.lib.world;

import java.util.ArrayList;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class StructureGenerationHandler
{
    public static final StructureGenerationHandler instance = new StructureGenerationHandler();
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
        return instance.structuresInQueue;
    }
}
