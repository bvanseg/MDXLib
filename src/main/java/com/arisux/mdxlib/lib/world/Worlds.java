package com.arisux.mdxlib.lib.world;

import java.util.Random;
import java.util.UUID;

import com.arisux.mdxlib.lib.world.entity.Entities;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
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
    public static Explosion createExplosion(Entity entity, World worldObj, Pos data, float strength, boolean isFlaming, boolean isSmoking, boolean doesBlockDamage)
    {
        Explosion explosion = new Explosion(worldObj, entity, data.x, data.y, data.z, strength);
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
    public static Pos getNextSafePositionAbove(Pos pos, World world)
    {
        for (int y = (int) pos.y; y < world.getHeight(); y++)
        {
            Pos position = new Pos(pos.x, y + 1, pos.z);

            if (Entities.isPositionSafe(position, world))
            {
                return position;
            }
        }

        return pos;
    }

    public static boolean canSeeSky(Pos pos, World world)
    {
        for (int y = (int) pos.y; y < world.getHeight(); y++)
        {
            Pos position = new Pos(pos.x, y + 1, pos.z);

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
    public static int getLightAtCoord(World worldObj, Pos data)
    {
        int block = worldObj.getSkyBlockTypeBrightness(EnumSkyBlock.Block, (int) data.x, (int) data.y, (int) data.z);
        int sky = worldObj.getSkyBlockTypeBrightness(EnumSkyBlock.Sky, (int) data.x, (int) data.y, (int) data.z) - worldObj.calculateSkylightSubtracted(0f);

        return Math.max(block, sky);
    }

    /**
     * Gets the next safe position below the specified position
     * 
     * @param pos - The position  we're checking for safe positions below.
     * @return The safe position.
     */
    public static Pos getNextSafePositionBelow(Pos pos, World world)
    {
        for (int y = (int) pos.y; y > 0; y--)
        {
            Pos position = new Pos(pos.x, y - 1, pos.z);

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
     * @param generator - The WorldGenerator instance to generate.
     * @param seed - The seed to generate random group coords at.
     * @param genPerChunk - The amount of times to generate this block group per chunk.
     * @param chunkCoord - The CoordData containing the X and Z coordinates of the Chunk to generate in.
     */
    public static void generateInChunk(World world, WorldGenerator generator, Random seed, int genPerChunk, Pos chunkCoord)
    {
        generateInChunk(world, generator, seed, genPerChunk, 0, 128, chunkCoord);
    }

    /**
     * Generate a group of the specified Block in the World, a given amount of times, in a Chunk at the given 
     * CoordData's X and Z coords using the specified group size and seed.
     * 
     * @param world - The World instance to generate in.
     * @param generator - The WorldGenerator instance to generate.
     * @param seed - The seed to generate random group coords at.
     * @param genPerChunk - The amount of times to generate this block group per chunk.
     * @param levelStart - The level that this block group can start generating on
     * @param levelEnd - The level that this block group can stop generating on
     * @param chunkCoord - The CoordData containing the X and Z coordinates of the Chunk to generate in.
     */
    public static void generateInChunk(World world, WorldGenerator generator, Random seed, int genPerChunk, int levelStart, int levelEnd, Pos chunkCoord)
    {
        for (int i = 0; i < genPerChunk; ++i)
        {
            int posX = (int) chunkCoord.x + seed.nextInt(16);
            int posY = levelStart + seed.nextInt(levelEnd);
            int posZ = (int) chunkCoord.z + seed.nextInt(16);
            generator.generate(world, seed, posX, posY, posZ);
        }
    }

    /**
     * Generate a group of the specified Block in the World, a given amount of times, in a Chunk at the given 
     * CoordData's X and Z coords using the specified group size and seed.
     * 
     * @param world - The World instance to generate in.
     * @param generator - The WorldGenerator instance to generate.
     * @param seed - The seed to generate random group coords at.
     * @param genPerChunk - The amount of times to generate this block group per chunk.
     * @param chunkCoord - The CoordData containing the X and Z coordinates of the Chunk to generate in.
     * @param biomes - The BiomeGenBase instances to generate in.
     */
    public static void generateInBiome(World world, WorldGenerator generator, Random seed, int genPerChunk, Pos chunkCoord, BiomeGenBase[] biomes)
    {
        generateInBiome(world, generator, seed, genPerChunk, 0, 128, chunkCoord, biomes);
    }

    /**
     * Generate a group of the specified Block in the World, a given amount of times, in a Chunk at the given 
     * CoordData's X and Z coords using the specified group size and seed.
     * 
     * @param world - The World instance to generate in.
     * @param generator - The WorldGenerator instance to generate.
     * @param seed - The seed to generate random group coords at.
     * @param genPerChunk - The amount of times to generate this block group per chunk.
     * @param levelStart - The level that this block group can start generating on
     * @param levelEnd - The level that this block group can stop generating on
     * @param chunkCoord - The CoordData containing the X and Z coordinates of the Chunk to generate in.
     * @param biomes - The BiomeGenBase instances to generate in.
     */
    public static void generateInBiome(World world, WorldGenerator generator, Random seed, int genPerChunk, int levelStart, int levelEnd, Pos chunkCoord, BiomeGenBase[] biomes)
    {
        for (BiomeGenBase biome : biomes)
        {
            if (world.provider.getBiomeGenForCoords((int) chunkCoord.x, (int) chunkCoord.z) == biome)
            {
                generateInChunk(world, generator, seed, genPerChunk, levelStart, levelEnd, chunkCoord);
            }
        }
    }

    public static Entity getEntityByUUID(World world, UUID uuid)
    {
        for (Object o : world.loadedEntityList.toArray())
        {
            if (o instanceof Entity)
            {
                Entity entity = (Entity) o;
                
                if (entity.getUniqueID().equals(uuid))
                {
                    return entity;
                }
            }
        }
        
        return null;
    }

    public static UUID uuidFromNBT(NBTTagCompound nbt, String key)
    {
        return uuidFromSignature(nbt.getString(key));
    }

    public static UUID uuidFromSignature(String signature)
    {
        if (signature != null && signature.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}"))
        {
            return UUID.fromString(signature);
        }

        return null;
    }
}
