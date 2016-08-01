package com.arisux.amdxlib.lib.world;

import java.util.Random;
import java.util.UUID;

import com.arisux.amdxlib.lib.world.entity.Entities;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;

@SuppressWarnings("all")
public class Worlds
{
    /**
     * Create an explosion in the specified world, at the specified coordinates, with the specified effects.
     * 
     * @param entity - The entity that triggered the explosion.
     * @param worldObj - The world that the explosion should be created in.
     * @param data - The CoordData containing the coordinates to create an explosion at.
     * @param strength - The strength of the explosion
     * @param isFlaming - Set to true if the explosion causes surrounding blocks to catch on fire.
     * @param isSmoking - Set to true if the explosion emits smoke particles.
     * @param doesBlockDamage - Set to true if the explosion does physical Block damage.
     * @return Return the instance of the explosion that was just created.
     */
    public static Explosion createExplosion(Entity entity, World worldObj, CoordData data, float strength, boolean isFlaming, boolean isSmoking, boolean doesBlockDamage)
    {
        Explosion explosion = new Explosion(worldObj, entity, data.posX, data.posY, data.posZ, strength);
        explosion.isFlaming = isFlaming;
        explosion.isSmoking = isSmoking;

        if (doesBlockDamage)
        {
            explosion.doExplosionA();
        }

        explosion.doExplosionB(true);

        return explosion;
    }

    public static LargeExplosion createCustomExplosion(Entity entity, World worldObj, int posX, int posY, int posZ, float radius)
    {
        LargeExplosion explosion = new LargeExplosion(worldObj, radius, radius, radius, posX, posY, posZ, (new Random()).nextLong());
        explosion.start();

        return explosion;
    }

    /**
     * Gets the next safe position above the specified position
     * 
     * @param entity - The position we're checking for safe positions above.
     * @return The safe position.
     */
    public static CoordData getNextSafePositionAbove(CoordData pos, World world)
    {
        for (int y = (int) pos.posY; y < world.getHeight(); y++)
        {
            CoordData position = new CoordData(pos.posX, y + 1, pos.posZ);

            if (Entities.isPositionSafe(position, world))
            {
                return position;
            }
        }

        return pos;
    }

    public static boolean canSeeSky(CoordData pos, World world)
    {
        for (int y = (int) pos.posY; y < world.getHeight(); y++)
        {
            CoordData position = new CoordData(pos.posX, y + 1, pos.posZ);

            if (position.getBlock(world) != net.minecraft.init.Blocks.air)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Get the light intensity as an Integer at the specified coordinates in the specified world.
     * 
     * @param worldObj - World to check for brightness values in.
     * @param data - CoordData containing coordinates of the location to check brightness at.
     * @return Returns light intensity of a block as an Integer.
     */
    public static int getLightAtCoord(World worldObj, CoordData data)
    {
        int block = worldObj.getSkyBlockTypeBrightness(EnumSkyBlock.Block, (int) data.posX, (int) data.posY, (int) data.posZ);
        int sky = worldObj.getSkyBlockTypeBrightness(EnumSkyBlock.Sky, (int) data.posX, (int) data.posY, (int) data.posZ) - worldObj.calculateSkylightSubtracted(0f);

        return Math.max(block, sky);
    }

    /**
     * Gets the next safe position below the specified position
     * 
     * @param pos - The position  we're checking for safe positions below.
     * @return The safe position.
     */
    public static CoordData getNextSafePositionBelow(CoordData pos, World world)
    {
        for (int y = (int) pos.posY; y > 0; y--)
        {
            CoordData position = new CoordData(pos.posX, y - 1, pos.posZ);

            if (Entities.isPositionSafe(position, world))
            {
                return position;
            }
        }

        return pos;
    }

    /**
     * Generate a group of the specified Block in the World, a given amount of times, in a Chunk at the given 
     * CoordData's X and Z coords using the specified group size and seed.
     * 
     * @param world - The World instance to generate in.
     * @param block - The Block instance to generate.
     * @param seed - The seed to generate random group coords at.
     * @param genPerChunk - The amount of times to generate this block group per chunk.
     * @param groupSize - The amount of blocks to generate per generation.
     * @param chunkCoord - The CoordData containing the X and Z coordinates of the Chunk to generate in.
     */
    public static void generateBlockInChunk(World world, Block block, Random seed, int genPerChunk, int groupSize, CoordData chunkCoord)
    {
        generateBlockInChunk(world, block, seed, genPerChunk, groupSize, 0, 128, chunkCoord, BiomeGenBase.getBiomeGenArray());
    }

    /**
     * Generate a group of the specified Block in the World, a given amount of times, in a Chunk at the given 
     * CoordData's X and Z coords using the specified group size and seed.
     * 
     * @param world - The World instance to generate in.
     * @param block - The Block instance to generate.
     * @param seed - The seed to generate random group coords at.
     * @param genPerChunk - The amount of times to generate this block group per chunk.
     * @param groupSize - The amount of blocks to generate per generation.
     * @param chunkCoord - The CoordData containing the X and Z coordinates of the Chunk to generate in.
     * @param biomes - The BiomeGenBase instances to generate in.
     */
    public static void generateBlockInChunk(World world, Block block, Random seed, int genPerChunk, int groupSize, CoordData chunkCoord, BiomeGenBase[] biomes)
    {
        generateBlockInChunk(world, block, seed, genPerChunk, groupSize, 0, 128, chunkCoord, biomes);
    }

    /**
     * Generate a group of the specified Block in the World, a given amount of times, in a Chunk at the given 
     * CoordData's X and Z coords using the specified group size and seed.
     * 
     * @param world - The World instance to generate in.
     * @param block - The Block instance to generate.
     * @param seed - The seed to generate random group coords at.
     * @param genPerChunk - The amount of times to generate this block group per chunk.
     * @param groupSize - The amount of blocks to generate per generation.
     * @param levelStart - The level that this block group can start generating on
     * @param levelEnd - The level that this block group can stop generating on
     * @param chunkCoord - The CoordData containing the X and Z coordinates of the Chunk to generate in.
     */
    public static void generateBlockInChunk(World world, Block block, Random seed, int genPerChunk, int groupSize, int levelStart, int levelEnd, CoordData chunkCoord)
    {
        generateBlockInChunk(world, block, seed, genPerChunk, groupSize, 0, 128, chunkCoord, BiomeGenBase.getBiomeGenArray());
    }

    /**
     * Generate a group of the specified Block in the World, a given amount of times, in a Chunk at the given 
     * CoordData's X and Z coords using the specified group size and seed.
     * 
     * @param world - The World instance to generate in.
     * @param block - The Block instance to generate.
     * @param seed - The seed to generate random group coords at.
     * @param genPerChunk - The amount of times to generate this block group per chunk.
     * @param groupSize - The amount of blocks to generate per generation.
     * @param levelStart - The level that this block group can start generating on
     * @param levelEnd - The level that this block group can stop generating on
     * @param chunkCoord - The CoordData containing the X and Z coordinates of the Chunk to generate in.
     * @param biomes - The BiomeGenBase instances to generate in.
     */
    public static void generateBlockInChunk(World world, Block block, Random seed, int genPerChunk, int groupSize, int levelStart, int levelEnd, CoordData chunkCoord, BiomeGenBase[] biomes)
    {
        for (BiomeGenBase biome : biomes)
        {
            if (world.provider.getBiomeGenForCoords((int) chunkCoord.posX, (int) chunkCoord.posZ) == biome)
            {
                for (int i = 0; i < genPerChunk; ++i)
                {
                    int posX = (int) chunkCoord.posX + seed.nextInt(16);
                    int posY = levelStart + seed.nextInt(levelEnd);
                    int posZ = (int) chunkCoord.posZ + seed.nextInt(16);
                    (new WorldGenMinable(block, groupSize)).generate(world, seed, posX, posY, posZ);
                }
            }
        }
    }

    /**
     * Generate the specified WorldGenerator instance in the World, a given amount of times, in a Chunk at the given 
     * CoordData's X and Z coords using the specified seed.
     * 
     * @param world - The World instance to generate in.
     * @param worldGen - The WorldGenerator instance to generate.
     * @param seed - The seed to generate random group coords at.
     * @param genPerChunk - The amount of times to generate this WorldGenerator per chunk.
     * @param chunkCoord - The CoordData containing the X and Z coordinates of the Chunk to generate in.
     */
    public static void generateWorldGenInChunk(World world, WorldGenerator worldGen, Random seed, int genPerChunk, CoordData chunkCoord)
    {
        generateWorldGenInChunk(world, worldGen, seed, genPerChunk, 0, 128, chunkCoord, BiomeGenBase.getBiomeGenArray());
    }

    /**
     * Generate the specified WorldGenerator instance in the World, a given amount of times, in a Chunk at the given 
     * CoordData's X and Z coords using the specified seed.
     * 
     * @param world - The World instance to generate in.
     * @param worldGen - The WorldGenerator instance to generate.
     * @param seed - The seed to generate random group coords at.
     * @param genPerChunk - The amount of times to generate this WorldGenerator per chunk.
     * @param chunkCoord - The CoordData containing the X and Z coordinates of the Chunk to generate in.
     * @param biomes - The BiomeGenBase instances to generate in.
     */
    public static void generateWorldGenInChunk(World world, WorldGenerator worldGen, Random seed, int genPerChunk, CoordData chunkCoord, BiomeGenBase[] biomes)
    {
        generateWorldGenInChunk(world, worldGen, seed, genPerChunk, 0, 128, chunkCoord, biomes);
    }

    /**
     * Generate the specified WorldGenerator instance in the World, a given amount of times, in a Chunk at the given 
     * CoordData's X and Z coords using the specified seed.
     * 
     * @param world - The World instance to generate in.
     * @param worldGen - The WorldGenerator instance to generate.
     * @param seed - The seed to generate random group coords at.
     * @param genPerChunk - The amount of times to generate this WorldGenerator per chunk.
     * @param levelStart - The level that this WorldGenerator can start generating on
     * @param levelEnd - The level that this WorldGenerator can stop generating on
     * @param chunkCoord - The CoordData containing the X and Z coordinates of the Chunk to generate in.
     */
    public static void generateWorldGenInChunk(World world, WorldGenerator worldGen, Random seed, int genPerChunk, int levelStart, int levelEnd, CoordData chunkCoord)
    {
        generateWorldGenInChunk(world, worldGen, seed, genPerChunk, 0, 128, chunkCoord, BiomeGenBase.getBiomeGenArray());
    }

    /**
     * Generate the specified WorldGenerator instance in the World, a given amount of times, in a Chunk at the given 
     * CoordData's X and Z coords using the specified seed.
     * 
     * @param world - The World instance to generate in.
     * @param worldGen - The WorldGenerator instance to generate.
     * @param seed - The seed to generate random group coords at.
     * @param genPerChunk - The amount of times to generate this WorldGenerator per chunk.
     * @param levelStart - The level that this WorldGenerator can start generating on
     * @param levelEnd - The level that this WorldGenerator can stop generating on
     * @param chunkCoord - The CoordData containing the X and Z coordinates of the Chunk to generate in.
     * @param biomes - The BiomeGenBase instances to generate in.
     */
    public static void generateWorldGenInChunk(World world, WorldGenerator worldGen, Random seed, int genPerChunk, int levelStart, int levelEnd, CoordData chunkCoord, BiomeGenBase[] biomes)
    {
        for (BiomeGenBase biome : biomes)
        {
            if (world.provider.getBiomeGenForCoords((int) chunkCoord.posX, (int) chunkCoord.posZ) == biome)
            {
                for (int i = 0; i < genPerChunk; ++i)
                {
                    int posX = (int) chunkCoord.posX + seed.nextInt(16);
                    int posY = levelStart + seed.nextInt(levelEnd);
                    int posZ = (int) chunkCoord.posZ + seed.nextInt(16);
                    worldGen.generate(world, seed, posX, posY, posZ);
                }
            }
        }
    }

    public static Entity getEntityByUUID(World world, UUID uuid)
    {
        for (Object o : world.getLoadedEntityList().toArray())
        {
            if (o instanceof Entity)
            {
                Entity entity = (Entity) o;
                
                if (entity.getPersistentID().equals(uuid))
                {
                    return entity;
                }
            }
        }
        
        return null;
    }

    

    

    

    
}
