package com.arisux.mdxlib.lib.world.entity;

import java.util.List;
import java.util.Random;

import com.arisux.mdxlib.MDX;
import com.arisux.mdxlib.lib.world.CoordData;
import com.arisux.mdxlib.lib.world.Worlds;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Entities
{
    /**
     * Get the first Entity instance of the specified class type found, 
     * within specified range, at the specified world coordinates.
     * 
     * @param worldObj - World instance to scan for entities in
     * @param entityClass - Entity class type to scan for.
     * @param data - The CoordData containing the coordinates to start scanning at.
     * @param range - Range of blocks to scan within.
     * @return First Entity instance found using the specified parameters.
     */
    public static Entity getEntityInCoordsRange(World worldObj, Class<? extends Entity> entityClass, CoordData data, int range)
    {
        return getEntityInCoordsRange(worldObj, entityClass, data, range, 16);
    }

    /**
     * Checks if the specified position is safe for an entity to spawn at.
     * 
     * @param pos - The position we are checking.
     * @param world - The world instance we are checking in.
     * @return true if the position is safe.
     */

    public static boolean isPositionSafe(CoordData pos, World world)
    {
        if (pos != null && world != null)
        {
            CoordData newPos = new CoordData(pos.x, pos.y, pos.z);
            CoordData newPosBelow = new CoordData(pos.x, pos.y - 1, pos.z);

            return newPosBelow.getBlock(world) != net.minecraft.init.Blocks.air && newPos.getBlock(world) == net.minecraft.init.Blocks.air;
        }

        return false;
    }

    /**
     * Gets a safe position for the entity to spawn at from the given position.
     * 
     * @param pos - The position that we should check around.
     * @param world - The world we're checking for a safe position in.
     * @return The safe position.
     */
    public static CoordData getSafePosition(CoordData pos, World world)
    {
        CoordData newSafePosition = Worlds.getNextSafePositionAbove(pos, world);

        if (newSafePosition == null)
        {
            newSafePosition = Worlds.getNextSafePositionBelow(pos, world);
        }

        // if (!(isPositionSafe(newSafePosition, world)))
        // {
        // Random rand = new Random();
        // newSafePosition = getSafePosition(new CoordData(rand.nextInt(30000), rand.nextInt(256), rand.nextInt(30000)), world);
        // }

        return newSafePosition;
    }

    /**
     * Get the first Entity instance of the specified class type found, 
     * within specified range, at the specified world coordinates, within specified height.
     * 
     * @param worldObj - World instance to scan for entities in
     * @param entityClass - Entity class type to scan for.
     * @param data - The CoordData containing the coordinates to start scanning at.
     * @param range - Range of blocks to scan within.
     * @param height - Height to scan for entities within
     * @return First Entity instance found using the specified parameters.
     */
    public static Entity getEntityInCoordsRange(World worldObj, Class<? extends Entity> entityClass, CoordData data, int range, int height)
    {
        List<? extends Entity> entities = getEntitiesInCoordsRange(worldObj, entityClass, data, range, height);

        return entities.size() >= 1 ? (Entity) entities.get(worldObj.rand.nextInt(entities.size())) : null;
    }

    /**
     * Get a random Entity instance of the specified class type found, 
     * within specified range, at the specified world coordinates, within specified height.
     * 
     * @param worldObj - World instance to scan for entities in
     * @param entityClass - Entity class type to scan for.
     * @param data - The CoordData containing the coordinates to start scanning at.
     * @param range - Range of blocks to scan within.
     * @return First Entity instance found using the specified parameters.
     */
    public static Entity getRandomEntityInCoordsRange(World worldObj, Class<? extends Entity> entityClass, CoordData data, int range)
    {
        return getRandomEntityInCoordsRange(worldObj, entityClass, data, range, 16);
    }

    /**
     * Get a random Entity instance of the specified class type found, 
     * within specified range, at the specified world coordinates, within specified height.
     * 
     * @param worldObj - World instance to scan for entities in
     * @param entityClass - Entity class type to scan for.
     * @param data - The CoordData containing the coordinates to start scanning at.
     * @param range - Range of blocks to scan within.
     * @param height - Height to scan for entities within
     * @return First Entity instance found using the specified parameters.
     */
    public static Entity getRandomEntityInCoordsRange(World worldObj, Class<? extends Entity> entityClass, CoordData data, int range, int height)
    {
        List<? extends Entity> entities = getEntitiesInCoordsRange(worldObj, entityClass, data, range, height);

        return entities.size() > 1 ? (Entity) entities.get((new Random()).nextInt(entities.size())) : null;
    }

    /**
     * Gets all Entity instances of the specified class type found, 
     * within specified range, at the specified world coordinates, within specified height.
     * 
     * @param worldObj - World instance to scan for entities in
     * @param entityClass - Entity class type to scan for.
     * @param data - The CoordData containing the coordinates to start scanning at.
     * @param range - Range of blocks to scan within.
     * @return All the Entity instances found using the specified parameters.
     */
    public static List<? extends Entity> getEntitiesInCoordsRange(World worldObj, Class<? extends Entity> entityClass, CoordData data, int range)
    {
        return getEntitiesInCoordsRange(worldObj, entityClass, data, range, 16);
    }

    /**
     * Gets all Entity instances of the specified class type found, 
     * within specified range, at the specified world coordinates, within specified height.
     * 
     * @param worldObj - World instance to scan for entities in
     * @param entityClass - Entity class type to scan for.
     * @param data - The CoordData containing the coordinates to start scanning at.
     * @param range - Range of blocks to scan within.
     * @param height - Height to scan for entities within
     * @return All the Entity instances found using the specified parameters.
     */
    @SuppressWarnings("unchecked")
    public static List<? extends Entity> getEntitiesInCoordsRange(World worldObj, Class<? extends Entity> entityClass, CoordData data, int range, int height)
    {
        return worldObj.getEntitiesWithinAABB(entityClass, AxisAlignedBB.getBoundingBox(data.x, data.y, data.z, data.x + 1, data.y + 1, data.z + 1).expand(range * 2, height, range * 2));
    }

    /**
     * @param entity - The entity that entityLooking is looking for.
     * @param entityLooking - The entity that is looking for the first entity.
     * @return Returns true if the first Entity can be seen by the second Entity.
     */
    public static boolean canEntityBeSeenBy(Entity entity, Entity entityLooking)
    {
        return rayTrace(entity, entityLooking) == null;
    }

    public static boolean canCoordBeSeenBy(Entity entity, CoordData coord)
    {
        return rayTrace(entity, coord) == null;
    }

    /**
     * @param entity - The entity that entityLooking is looking for.
     * @param entityLooking - The entity that is looking for the first entity.
     * @return Returns the MovingObjectPosition hit by the rayTrace.
     */
    public static MovingObjectPosition rayTrace(Entity entity, Entity entityLooking)
    {
        return entity != null && entityLooking != null && entity.worldObj != null ? entity.worldObj.rayTraceBlocks(Vec3.createVectorHelper(entity.posX, entity.posY + (entity.height / 2), entity.posZ), Vec3.createVectorHelper(entityLooking.posX, entityLooking.posY + entityLooking.getEyeHeight(), entityLooking.posZ)) : null;
    }

    public static MovingObjectPosition rayTrace(Entity entity, CoordData coord)
    {
        return entity != null && coord != null && entity.worldObj != null ? entity.worldObj.rayTraceBlocks(Vec3.createVectorHelper(entity.posX, entity.posY + (entity.height / 2), entity.posZ), Vec3.createVectorHelper(coord.x, coord.y, coord.z)) : null;
    }

    public static MovingObjectPosition rayTrace(EntityLivingBase player, int reach)
    {
        Vec3 pos = player.getPosition(1F);
        Vec3 entityLook = player.getLook(1F);

        Entity pointedEntity = null;
        Vec3 hitVec = null;
        Vec3 posReach = null;

        if (entityLook != null)
        {
            posReach = pos.addVector(entityLook.xCoord * reach, entityLook.yCoord * reach, entityLook.zCoord * reach);

            List<Entity> entities = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.addCoord(entityLook.xCoord * reach, entityLook.yCoord * reach, entityLook.zCoord * reach).expand(1.0F, 1.0F, 1.0F));

            for (Entity listEntity : entities)
            {
                if (listEntity.canBeCollidedWith())
                {
                    float borderSize = listEntity.getCollisionBorderSize();
                    AxisAlignedBB axisalignedbb = listEntity.boundingBox.expand(borderSize, borderSize, borderSize);
                    MovingObjectPosition movingObjPos = axisalignedbb.calculateIntercept(pos, posReach);

                    if (axisalignedbb.isVecInside(pos))
                    {
                        pointedEntity = listEntity;
                        hitVec = movingObjPos == null ? pos : movingObjPos.hitVec;
                    }
                    else if (movingObjPos != null)
                    {
                        if (listEntity == player.ridingEntity && !listEntity.canRiderInteract())
                        {
                            pointedEntity = listEntity;
                            hitVec = movingObjPos.hitVec;
                        }
                        else
                        {
                            pointedEntity = listEntity;
                            hitVec = movingObjPos.hitVec;
                        }
                    }
                }
            }
        }

        if (pointedEntity != null && hitVec != null)
        {
            return new MovingObjectPosition(pointedEntity, hitVec);
        }

        if (posReach != null)
        {
            MovingObjectPosition blockHitVec = player.worldObj.rayTraceBlocks(pos, posReach, true, true, true);

            if (blockHitVec != null)
            {
                return blockHitVec;
            }
        }

        return null;
    }

    public static MovingObjectPosition rayTrace(Entity entity, int reach)
    {
        Vec3 pos = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
        Vec3 entityLook = entity.getLookVec();

        Entity pointedEntity = null;
        Vec3 hitVec = null;
        Vec3 posReach = null;

        if (entityLook != null)
        {
            posReach = pos.addVector(entityLook.xCoord * reach, entityLook.yCoord * reach, entityLook.zCoord * reach);

            List<Entity> entities = entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, entity.boundingBox.addCoord(entityLook.xCoord * reach, entityLook.yCoord * reach, entityLook.zCoord * reach).expand(1.0F, 1.0F, 1.0F));

            for (Entity listEntity : entities)
            {
                if (listEntity.canBeCollidedWith())
                {
                    float borderSize = listEntity.getCollisionBorderSize();
                    AxisAlignedBB axisalignedbb = listEntity.boundingBox.expand(borderSize, borderSize, borderSize);
                    MovingObjectPosition movingObjPos = axisalignedbb.calculateIntercept(pos, posReach);

                    if (axisalignedbb.isVecInside(pos))
                    {
                        pointedEntity = listEntity;
                        hitVec = movingObjPos == null ? pos : movingObjPos.hitVec;
                    }
                    else if (movingObjPos != null)
                    {
                        if (listEntity == entity.ridingEntity && !listEntity.canRiderInteract())
                        {
                            pointedEntity = listEntity;
                            hitVec = movingObjPos.hitVec;
                        }
                        else
                        {
                            pointedEntity = listEntity;
                            hitVec = movingObjPos.hitVec;
                        }
                    }
                }
            }
        }

        if (pointedEntity != null && hitVec != null)
        {
            return new MovingObjectPosition(pointedEntity, hitVec);
        }

        if (posReach != null)
        {
            MovingObjectPosition blockHitVec = entity.worldObj.rayTraceBlocks(pos, posReach, true, true, true);

            if (blockHitVec != null)
            {
                return blockHitVec;
            }
        }

        return null;
    }

    /**
     * Constructs a new Entity instance from the specified class name in the specified world.
     * 
     * @param worldObj - World to construct the Entity instance in.
     * @param name - String name of the entity class of which will be constructed.
     * @return Entity instance constructed using this method.
     */
    @SuppressWarnings("unchecked")
    public static Entity constructEntityViaClasspath(World worldObj, String name)
    {
        try
        {
            return constructEntity(worldObj, (Class<? extends Entity>) Class.forName(name));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Constructs a new Entity instance from the specified class in the specified world.
     * 
     * @param worldObj - World to construct the Entity instance in.
     * @param c - The entity class of which will be constructed.
     * @return Entity instance constructed using this method.
     */
    public static Entity constructEntity(World worldObj, Class<? extends Entity> c)
    {
        if (worldObj == null)
        {
            MDX.log().warn("World object null while attempting to construct entity.");
            return null;
        }
        
        if (c == null)
        {
            MDX.log().warn("Entity class null while attempting to construct entity.");
            return null;
        }
        
        try
        {
            return (c.getConstructor(World.class)).newInstance(new Object[] { worldObj });
        }
        catch (Exception e)
        {
            MDX.log().warn("Failed to construct entity: " + (c != null ? c.getName() : c));
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Causes the facer entity to face the faced entity.
     * 
     * @param facer - The Entity that is going to be facing the faced entity.
     * @param faced - The Entity that is going to be faced.
     * @param maxYaw - The max rotation yaw that the facer can rotate.
     * @param maxPitch - The max rotation pitch that the facer can rotate.
     */
    public static void faceEntity(Entity facer, Entity faced, float maxYaw, float maxPitch)
    {
        double xDistance = faced.posX - facer.posX;
        double zDistance = faced.posZ - facer.posZ;
        double yDistance;

        if (faced instanceof EntityLivingBase)
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase) faced;
            yDistance = entitylivingbase.posY + entitylivingbase.getEyeHeight() - (facer.posY + facer.getEyeHeight());
        }
        else
        {
            yDistance = (faced.boundingBox.minY + faced.boundingBox.maxY) / 2.0D - (facer.posY + facer.getEyeHeight());
        }

        double d3 = MathHelper.sqrt_double(xDistance * xDistance + zDistance * zDistance);
        float f2 = (float) (Math.atan2(zDistance, xDistance) * 180.0D / Math.PI) - 90.0F;
        float f3 = (float) (-(Math.atan2(yDistance, d3) * 180.0D / Math.PI));
        facer.rotationPitch = updateRotation(facer.rotationPitch, f3, maxPitch);
        facer.rotationYaw = updateRotation(facer.rotationYaw, f2, maxYaw);
    }

    /**
     * @param currentRotation - The current rotation value.
     * @param targetRotation - The target rotation value.
     * @param maxChange - The maximum rotation change allowed.
     * @return Amount of rotation that is results based on the current, target, and max rotation.
     */
    public static float updateRotation(float currentRotation, float targetRotation, float maxChange)
    {
        float newRotation = MathHelper.wrapAngleTo180_float(targetRotation - currentRotation);
        return currentRotation + (newRotation > maxChange ? maxChange : newRotation < -maxChange ? -maxChange : maxChange);
    }

    /**
     * Apply a block collision for the provided Entity instance.
     * @param entity - The entity to apply a collision for
     */
    public static void applyCollision(Entity entity)
    {
        int minX = MathHelper.floor_double(entity.boundingBox.minX + 0.001D);
        int minY = MathHelper.floor_double(entity.boundingBox.minY + 0.001D);
        int minZ = MathHelper.floor_double(entity.boundingBox.minZ + 0.001D);
        int maxX = MathHelper.floor_double(entity.boundingBox.maxX - 0.001D);
        int maxY = MathHelper.floor_double(entity.boundingBox.maxY - 0.001D);
        int maxZ = MathHelper.floor_double(entity.boundingBox.maxZ - 0.001D);

        if (entity.worldObj.checkChunksExist(minX, minY, minZ, maxX, maxY, maxZ))
        {
            for (int x = minX; x <= maxX; ++x)
            {
                for (int y = minY; y <= maxY; ++y)
                {
                    for (int z = minZ; z <= maxZ; ++z)
                    {
                        Block block = entity.worldObj.getBlock(x, y, z);

                        try
                        {
                            block.onEntityCollidedWithBlock(entity.worldObj, x, y, z, entity);
                        }
                        catch (Throwable throwable)
                        {
                            MDX.log().warn("Exception while handling entity collision with block.");
                        }
                    }
                }
            }
        }
    }

    public static boolean isInLava(Entity entity)
    {
        return isInMaterial(entity, Material.lava);
    }

    public static boolean isInWater(Entity entity)
    {
        return isInMaterial(entity, Material.water);
    }

    public static boolean isInMaterial(Entity entity, Material material)
    {
        if (entity != null && entity.worldObj != null && entity.getBoundingBox() != null)
        {
            return entity.worldObj.isMaterialInBB(entity.getBoundingBox().expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), material);
        }

        return false;
    }
    
    public static void setMoveHelper(EntityLiving living, EntityMoveHelper moveHelper)
    {
        MDX.access().setMoveHelper(living, moveHelper);
    }

    public static void setNavigator(EntityLiving living, PathNavigate navigator)
    {
        MDX.access().setNavigator(living, navigator);
    }

    public static void setLookHelper(EntityLiving living, EntityLookHelper lookHelper)
    {
        MDX.access().setLookHelper(living, lookHelper);
    }

    /**
     * @param render - The Render instance of the Entity to obtain a ResourceLocation from.
     * @param entity - The Entity to obtain the ResourceLocation for.
     * @return The ResourceLocation of the Entity.
     */
    @SideOnly(Side.CLIENT)
    public static ResourceLocation getEntityTexture(Render render, Entity entity)
    {
        return MDX.access().getEntityTexture(render, entity);
    }
    
    public static Class<? extends Entity> getRegisteredEntityClass(String entityId)
    {
        return (Class<? extends Entity>) EntityList.stringToClassMapping.get(entityId);
    }
    
    public static String getEntityRegistrationId(Entity entity)
    {
        return getEntityRegistrationId(entity.getClass());
    }
    
    public static String getEntityRegistrationId(Class <? extends Entity> c)
    {
        return (String) EntityList.classToStringMapping.get(c);
    }
}