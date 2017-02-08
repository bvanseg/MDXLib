package com.arisux.mdxlib.lib.world.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.arisux.mdxlib.MDX;
import com.arisux.mdxlib.lib.world.Pos;
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
import net.minecraft.init.Blocks;
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
    public static Entity getEntityInCoordsRange(World worldObj, Class<? extends Entity> entityClass, Pos data, int range)
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

    public static boolean isPositionSafe(Pos pos, World world)
    {
        if (pos != null && world != null)
        {
            Pos newPos = new Pos(pos.x, pos.y, pos.z);
            Pos newPosBelow = new Pos(pos.x, pos.y - 1, pos.z);

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
    public static Pos getSafePositionAboveBelow(Pos pos, World world)
    {
        Pos newSafePosition = Worlds.getNextSafePositionAbove(pos, world);

        if (newSafePosition == null)
        {
            newSafePosition = Worlds.getNextSafePositionBelow(pos, world);
        }

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
    public static Entity getEntityInCoordsRange(World worldObj, Class<? extends Entity> entityClass, Pos data, int range, int height)
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
    public static Entity getRandomEntityInCoordsRange(World worldObj, Class<? extends Entity> entityClass, Pos data, int range)
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
    public static Entity getRandomEntityInCoordsRange(World worldObj, Class<? extends Entity> entityClass, Pos data, int range, int height)
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
    public static List<? extends Entity> getEntitiesInCoordsRange(World worldObj, Class<? extends Entity> entityClass, Pos data, int range)
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
    public static List<? extends Entity> getEntitiesInCoordsRange(World worldObj, Class<? extends Entity> entityClass, Pos data, int range, int height)
    {
        return worldObj.getEntitiesWithinAABB(entityClass, AxisAlignedBB.getBoundingBox(data.x, data.y, data.z, data.x + 1, data.y + 1, data.z + 1).expand(range * 2, height, range * 2));
    }

    /**
     * @param e1 - The entity that entityLooking is looking for.
     * @param e2 - The entity that is looking for the first entity.
     * @return Returns true if the first Entity can be seen by the second Entity.
     */
    public static boolean canEntityBeSeenBy(Entity e1, Entity e2)
    {
        return rayTraceBlocks(e1, e2) == null;
    }

    public static boolean canEntityBeSeenBy(Entity e, Pos coord)
    {
        return rayTraceBlocks(e, coord) == null;
    }

    public static boolean canCoordBeSeenBy(World world, Pos p1, Pos p2)
    {
        return rayTraceBlocks(world, p1, p2) == null;
    }

    /**
     * @param e1 - The entity that entityLooking is looking for.
     * @param e2 - The entity that is looking for the first entity.
     * @return Returns the MovingObjectPosition hit by the rayTrace.
     */
    public static MovingObjectPosition rayTraceBlocks(Entity e1, Entity e2)
    {
        return e1 != null && e2 != null ? rayTraceBlocks(e1.worldObj, e1.posX, e1.posY + (e1.height / 2), e1.posZ, e2.posX, e2.posY + e2.getEyeHeight(), e2.posZ) : null;
    }

    public static MovingObjectPosition rayTraceBlocks(Entity e, Pos p)
    {
        return e != null && p != null ? rayTraceBlocks(e.worldObj, e.posX, e.posY + (e.height / 2), e.posZ, p.x, p.y, p.z) : null;
    }

    public static MovingObjectPosition rayTraceBlocks(World worldObj, Pos p1, Pos p2)
    {
        return p1 != null && p2 != null ? rayTraceBlocks(worldObj, p1.x, p1.y, p1.z, p2.x, p2.y, p2.z) : null;
    }

    public static MovingObjectPosition rayTraceBlocks(World worldObj, double x1, double y1, double z1, double x2, double y2, double z2)
    {
        return worldObj != null ? rayTraceBlocks(worldObj, Vec3.createVectorHelper(x1, y1, z1), Vec3.createVectorHelper(x2, y2, z2), false, false, false) : null;
    }

    public static MovingObjectPosition rayTraceAll(Entity entity, int reach)
    {
        return rayTraceAll(entity.worldObj, entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch, reach, new ArrayList<Entity>(Arrays.asList(new Entity[] {entity})));
    }

    public static MovingObjectPosition rayTraceAll(World world, double x, double y, double z, float rotationYaw, float rotationPitch, int reach, ArrayList<Entity> exclude)
    {
        Vec3 pos = Vec3.createVectorHelper(x, y, z);
        Vec3 lookVec = getLookVector(rotationYaw, rotationPitch);

        Entity entityHit = null;
        Vec3 hitVec = null;
        Vec3 posHit = null;

        if (lookVec != null)
        {
            posHit = pos.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);
            List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1F, y + 1F, z + 1F).addCoord(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach).expand(1.0F, 1.0F, 1.0F));

            for (Entity e : entities)
            {
                if (e != null && e.canBeCollidedWith() && !exclude.contains(e))
                {
                    float size = e.getCollisionBorderSize();
                    AxisAlignedBB box = e.boundingBox.expand(size, size, size);
                    MovingObjectPosition movobjpos = box.calculateIntercept(pos, posHit);
                    
                    entityHit = e;

                    if (movobjpos == null)
                    {
                        hitVec = pos;
                    }
                    else
                    {
                        hitVec = movobjpos.hitVec;
                    }
                }
            }
        }

        if (entityHit != null && hitVec != null)
        {
            return new MovingObjectPosition(entityHit, hitVec);
        }

        if (posHit != null)
        {
            MovingObjectPosition blockHitVec = rayTraceBlocks(world, pos, posHit, true, true, true);

            if (blockHitVec != null)
            {
                return blockHitVec;
            }
        }

        return null;
    }
    
    public static MovingObjectPosition rayTraceBlocks(World world, Pos pos, Pos pos2, boolean hitLiquid, boolean ignoreBlocksWithoutBoundingBox, boolean returnLastUncollidableBlock)
    {
        return rayTraceBlocks(world, Vec3.createVectorHelper(pos.x, pos.y, pos.z), Vec3.createVectorHelper(pos2.x, pos2.y, pos2.z), hitLiquid, ignoreBlocksWithoutBoundingBox, returnLastUncollidableBlock);
    }
    
    public static MovingObjectPosition rayTraceBlocks(World world, Vec3 pos, Vec3 pos2, boolean hitLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock)
    {
        if (!Double.isNaN(pos.xCoord) && !Double.isNaN(pos.yCoord) && !Double.isNaN(pos.zCoord))
        {
            if (!Double.isNaN(pos2.xCoord) && !Double.isNaN(pos2.yCoord) && !Double.isNaN(pos2.zCoord))
            {
                int posX = MathHelper.floor_double(pos.xCoord);
                int posY = MathHelper.floor_double(pos.yCoord);
                int posZ = MathHelper.floor_double(pos.zCoord);
                Block posBlock = world.getBlock(posX, posY, posZ);
                int posMeta = world.getBlockMetadata(posX, posY, posZ);

                if ((!ignoreBlockWithoutBoundingBox || posBlock.getCollisionBoundingBoxFromPool(world, posX, posY, posZ) != null) && posBlock.canStopRayTrace(posMeta, hitLiquid))
                {
                    MovingObjectPosition obj = posBlock.collisionRayTrace(world, posX, posY, posZ, pos, pos2);

                    if (obj != null)
                    {
                        return obj;
                    }
                }

                MovingObjectPosition movObjPos = null;
                int dist = 200;

                while (dist-- >= 0)
                {
                    if (Double.isNaN(pos.xCoord) || Double.isNaN(pos.yCoord) || Double.isNaN(pos.zCoord))
                    {
                        return null;
                    }

                    if (posX == pos2.xCoord && posY == pos2.yCoord && posZ == pos2.zCoord)
                    {
                        return returnLastUncollidableBlock ? movObjPos : null;
                    }

                    boolean endX = true;
                    boolean endY = true;
                    boolean endZ = true;
                    double distX = 999.0D;
                    double distY = 999.0D;
                    double distZ = 999.0D;

                    if (pos2.xCoord > posX)
                    {
                        distX = (double)posX + 1.0D;
                    }
                    else if (pos2.xCoord < posX)
                    {
                        distX = (double)posX + 0.0D;
                    }
                    else
                    {
                        endX = false;
                    }

                    if (pos2.yCoord > posY)
                    {
                        distY = (double)posY + 1.0D;
                    }
                    else if (pos2.yCoord < posY)
                    {
                        distY = (double)posY + 0.0D;
                    }
                    else
                    {
                        endY = false;
                    }

                    if (pos2.zCoord > posZ)
                    {
                        distZ = (double)posZ + 1.0D;
                    }
                    else if (pos2.zCoord < posZ)
                    {
                        distZ = (double)posZ + 0.0D;
                    }
                    else
                    {
                        endZ = false;
                    }

                    double dX = 999.0D;
                    double dY = 999.0D;
                    double dZ = 999.0D;
                    double displacementX = pos2.xCoord - pos.xCoord;
                    double displacementY = pos2.yCoord - pos.yCoord;
                    double displacementZ = pos2.zCoord - pos.zCoord;

                    if (endX)
                    {
                        dX = (distX - pos.xCoord) / displacementX;
                    }

                    if (endY)
                    {
                        dY = (distY - pos.yCoord) / displacementY;
                    }

                    if (endZ)
                    {
                        dZ = (distZ - pos.zCoord) / displacementZ;
                    }

                    byte side;

                    if (dX < dY && dX < dZ)
                    {
                        if (pos2.xCoord > posX)
                        {
                            side = 4;
                        }
                        else
                        {
                            side = 5;
                        }

                        pos.xCoord = distX;
                        pos.yCoord += displacementY * dX;
                        pos.zCoord += displacementZ * dX;
                    }
                    else if (dY < dZ)
                    {
                        if (pos2.yCoord > posY)
                        {
                            side = 0;
                        }
                        else
                        {
                            side = 1;
                        }

                        pos.xCoord += displacementX * dY;
                        pos.yCoord = distY;
                        pos.zCoord += displacementZ * dY;
                    }
                    else
                    {
                        if (pos2.zCoord > posZ)
                        {
                            side = 2;
                        }
                        else
                        {
                            side = 3;
                        }

                        pos.xCoord += displacementX * dZ;
                        pos.yCoord += displacementY * dZ;
                        pos.zCoord = distZ;
                    }

                    Vec3 posNew = Vec3.createVectorHelper(pos.xCoord, pos.yCoord, pos.zCoord);
                    posX = (int)(posNew.xCoord = (double)MathHelper.floor_double(pos.xCoord));

                    if (side == 5)
                    {
                        --posX;
                        ++posNew.xCoord;
                    }

                    posY = (int)(posNew.yCoord = (double)MathHelper.floor_double(pos.yCoord));

                    if (side == 1)
                    {
                        --posY;
                        ++posNew.yCoord;
                    }

                    posZ = (int)(posNew.zCoord = (double)MathHelper.floor_double(pos.zCoord));

                    if (side == 3)
                    {
                        --posZ;
                        ++posNew.zCoord;
                    }

                    Block newPosBlock = world.getBlock(posX, posY, posZ);
                    int newPosMeta = world.getBlockMetadata(posX, posY, posZ);

                    if (!ignoreBlockWithoutBoundingBox || newPosBlock.getCollisionBoundingBoxFromPool(world, posX, posY, posZ) != null)
                    {
                        if (newPosBlock.canStopRayTrace(newPosMeta, hitLiquid))
                        {
                            MovingObjectPosition obj = newPosBlock.collisionRayTrace(world, posX, posY, posZ, pos, pos2);

                            if (obj != null)
                            {
                                return obj;
                            }
                        }
                        else
                        {
                            movObjPos = new MovingObjectPosition(posX, posY, posZ, side, pos, false);
                        }
                    }
                }

                return returnLastUncollidableBlock ? movObjPos : null;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * @param partialTicks - The partial tick time or amount of partial ticks between each CPU tick.
     * @return an interpolated look vector used for ray tracing.
     */
    public static Vec3 getLookVector(float rotationYaw, float rotationPitch)
    {
        return getLookVector(rotationYaw, rotationPitch, 0F, 0F, 1.0F);
    }

    /**
     * @param partialTicks - The partial tick time or amount of partial ticks between each CPU tick.
     * @return an interpolated look vector used for ray tracing.
     */
    public static Vec3 getLookVector(float rotationYaw, float rotationPitch, float prevRotationYaw, float prevRotationPitch, float partialTicks)
    {
        float f1;
        float f2;
        float f3;
        float f4;

        if (partialTicks == 1.0F)
        {
            f1 = MathHelper.cos(-rotationYaw * 0.017453292F - (float) Math.PI);
            f2 = MathHelper.sin(-rotationYaw * 0.017453292F - (float) Math.PI);
            f3 = -MathHelper.cos(-rotationPitch * 0.017453292F);
            f4 = MathHelper.sin(-rotationPitch * 0.017453292F);
            return Vec3.createVectorHelper((double) (f2 * f3), (double) f4, (double) (f1 * f3));
        }
        else
        {
            f1 = prevRotationPitch + (rotationPitch - prevRotationPitch) * partialTicks;
            f2 = prevRotationYaw + (rotationYaw - prevRotationYaw) * partialTicks;
            f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
            f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
            float f5 = -MathHelper.cos(-f1 * 0.017453292F);
            float f6 = MathHelper.sin(-f1 * 0.017453292F);
            return Vec3.createVectorHelper((double) (f4 * f5), (double) f6, (double) (f3 * f5));
        }
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

    public static String getEntityRegistrationId(Class<? extends Entity> c)
    {
        return (String) EntityList.classToStringMapping.get(c);
    }

    public static Pos getSafeLocationAround(Entity toCheck, Pos around)
    {
        ArrayList<Pos> potentialLocations = com.arisux.mdxlib.lib.world.block.Blocks.getCoordDataInRangeIncluding((int) around.x, (int) around.y, (int) around.z, 2, toCheck.worldObj, Blocks.air);

        for (Pos potentialLocation : potentialLocations)
        {
            Block blockAt = potentialLocation.getBlock(toCheck.worldObj);
            Block blockBelow = potentialLocation.add(0, -1, 0).getBlock(toCheck.worldObj);

            if (blockAt != null && blockBelow != null)
            {
                if (blockAt == Blocks.air)
                {
                    if (isGround(blockBelow))
                    {
                        double entityWidth = toCheck.boundingBox.maxX - toCheck.boundingBox.minX;
                        double entityHeight = toCheck.boundingBox.maxY - toCheck.boundingBox.minY;
                        double entityDepth = toCheck.boundingBox.maxZ - toCheck.boundingBox.minZ;

                        entityWidth = entityWidth < 1 ? 1 : entityWidth;
                        entityDepth = entityDepth < 1 ? 1 : entityDepth;
                        entityHeight = entityHeight < 1 ? 1 : entityHeight;

                        Block blockAbove = potentialLocation.add(0, entityHeight, 0).getBlock(toCheck.worldObj);

                        if (isSafe(blockAbove))
                        {
                            Block blockToLeft = potentialLocation.add(-entityWidth, 0, 0).getBlock(toCheck.worldObj);
                            Block blockToRight = potentialLocation.add(entityWidth, 0, 0).getBlock(toCheck.worldObj);
                            Block blockInFront = potentialLocation.add(0, 0, entityDepth).getBlock(toCheck.worldObj);
                            Block blockInBack = potentialLocation.add(0, 0, -entityDepth).getBlock(toCheck.worldObj);

                            if (isSafe(blockToLeft) && isSafe(blockToRight) && isSafe(blockInFront) && isSafe(blockInBack))
                            {
                                return potentialLocation;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static boolean isGround(Block block)
    {
        return block.getMaterial().isSolid() && block != Blocks.air;
    }

    public static boolean isSafe(Block block)
    {
        return block == Blocks.air;
    }
}
