package com.asx.mdx.lib.client.entityfx;

import static org.lwjgl.opengl.GL11.GL_ONE;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.asx.mdx.lib.client.Resources;
import com.asx.mdx.lib.client.util.OpenGL;
import com.asx.mdx.lib.util.Game;
import com.asx.mdx.lib.world.Pos;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityFXElectricArc extends Particle
{
    private static final ResourceLocation PARTICLES = new ResourceLocation("textures/particle/particles.png");

    private Random                        rand;
    private int                           color;
    private int                           tessellation;
    private float                         rotYaw;
    private float                         rotPitch;
    private float                         density;
    private double                        targetX;
    private double                        targetY;
    private double                        targetZ;
    private double                        displacement;
    private double                        complexity;
    public boolean                        useBlend;
    public boolean                        useLighting;

    public Entity                         targetEntity;
    public Pos                            targetOffset;
    public Entity                         originEntity;
    public Pos                            originOffset;

    public EntityFXElectricArc(World world, double x, double y, double z, double targetX, double targetY, double targetZ, int age)
    {
        this(world, x, y, z, targetX, targetY, targetZ, age, 1.6D, 0.1D, 0.1F, 0xFFAA99FF);
    }

    public EntityFXElectricArc(World world, double x, double y, double z, double targetX, double targetY, double targetZ, int age, int color)
    {
        this(world, x, y, z, targetX, targetY, targetZ, age, 1.6D, 0.1D, 0.1F, color);
    }

    public EntityFXElectricArc(World world, double x, double y, double z, double targetX, double targetY, double targetZ, int age, double displacement, double complexity, float density, int color)
    {
        super(world, x, y, z);
        this.rand = new Random();
        this.tessellation = 2;
        this.particleMaxAge = age;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.displacement = displacement;
        this.complexity = complexity;
        this.density = density;
        this.color = color;
        this.useBlend = true;
        this.useLighting = false;
        this.targetOffset = new Pos(0, 0, 0);
        this.originOffset = new Pos(0, 0, 0);
        this.changeDirection((float) (this.posX - this.targetX), (float) (this.posY - this.targetY), (float) (this.posZ - this.targetZ));
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rX, float rZ, float rYZ, float rXY, float rXZ)
    {
        if (this.originEntity != null)
        {
            this.posX = this.originEntity.getPositionVector().x;
            this.posY = this.originEntity.getPositionVector().y;
            this.posZ = this.originEntity.getPositionVector().z;

            if (this.originEntity instanceof EntityPlayerSP)
            {
                EntityPlayerSP player = (EntityPlayerSP) this.originEntity;
                RayTraceResult result = player.rayTrace(2.0D, Game.partialTicks());

                if (result != null)
                {
                    this.posX = result.hitVec.x;
                    this.posY = result.hitVec.y;
                    this.posZ = result.hitVec.z;
                }
            }
        }

        if (this.targetEntity != null)
        {
            this.targetX = this.targetEntity.getPositionVector().x;
            this.targetY = this.targetEntity.getPositionVector().y;
            this.targetZ = this.targetEntity.getPositionVector().z;

            if (this.originEntity instanceof EntityPlayerSP)
            {
                EntityPlayerSP player = (EntityPlayerSP) this.originEntity;
                RayTraceResult result = player.rayTrace(2.0D, Game.partialTicks());

                if (result != null)
                {
                    this.targetX = result.hitVec.x;
                    this.targetY = result.hitVec.y;
                    this.targetZ = result.hitVec.z;
                }
            }
        }

        double x = (posX + this.originOffset.x);
        double y = (posY + this.originOffset.y);
        double z = (posZ + this.originOffset.z);
        double tX = (targetX + this.targetOffset.x);
        double tY = (targetY + this.targetOffset.y);
        double tZ = (targetZ + this.targetOffset.z);

        Resources.BLANK.bind();
        this.drawArc(buffer, x, y, z, tX, tY, tZ, displacement, complexity, density);
    }

    public EntityFXElectricArc setTessellation(int tessellation)
    {
        this.tessellation = tessellation;
        return this;
    }

    private void changeDirection(float x, float y, float z)
    {
        double variance = MathHelper.sqrt(x * x + z * z);
        this.rotYaw = ((float) (Math.atan2(x, z) * 180.0D / Math.PI));
        this.rotPitch = ((float) (Math.atan2(y, variance) * 180.0D / Math.PI));
    }

    private void drawArc(BufferBuilder buffer, double originX, double originY, double originZ, double targetX, double targetY, double targetZ, double displacement, double complexity, float density)
    {
        double x = (originX);
        double y = (originY);
        double z = (originZ);
        double tX = (targetX);
        double tY = (targetY);
        double tZ = (targetZ);

        if (displacement < complexity)
        {
            float rx = (float) (x - tX);
            float ry = (float) (y - tY);
            float rz = (float) (z - tZ);

            this.changeDirection(rx, ry, rz);

            OpenGL.pushMatrix();
            OpenGL.translate((float) (x - interpPosX), (float) (y - interpPosY), (float) (z - interpPosZ));

            if (this.useBlend)
            {
                OpenGL.enableBlend();
                OpenGL.blendFunc(GL11.GL_DST_ALPHA, GL11.GL_CURRENT_BIT);
            }

            OpenGL.disableCullFace();
            OpenGL.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            OpenGL.rotate(180.0F + this.rotYaw, 0.0F, 0.0F, -1.0F);
            OpenGL.rotate(this.rotPitch, 1.0F, 0.0F, 0.0F);

            if (!this.useLighting)
            {
//                OpenGL.disableLightMapping();
                OpenGL.disableLight();
            }

            double vX1 = density * -0.15;
            double vX2 = density * -0.15 * 1.0;
            double vY2 = MathHelper.sqrt(rx * rx + ry * ry + rz * rz);
            double vY1 = 0.0D;

            int a = (color >> 24 & 255);
            int r = (color >> 16 & 255);
            int g = (color >> 8 & 255);
            int b = (color & 255);
            
            for (int i2 = 0; i2 < tessellation; i2++)
            {
                GlStateManager.rotate((360F / tessellation) / 2, 0.0F, 1.0F, 0.0F);
                OpenGL.color(r / 255F, g / 255F, b / 255F, a / 255F);
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                buffer.pos(vX2, vY2, 0).color(255, 255, 255, 255).endVertex();
                buffer.pos(vX1, vY1, 0).color(255, 255, 255, 255).endVertex();
                buffer.pos(-vX1, vY1, 0).color(255, 255, 255, 255).endVertex();
                buffer.pos(-vX2, vY2, 0).color(255, 255, 255, 255).endVertex();
                Tessellator.getInstance().draw();
            }

            if (!this.useLighting)
            {
                OpenGL.enableLight();
//                OpenGL.enableLightMapping();
            }

            OpenGL.color(1.0F, 1.0F, 1.0F, 1.0F);
            OpenGL.enableCullFace();

            if (this.useBlend)
            {
                OpenGL.disableBlend();
            }

            OpenGL.popMatrix();
        }
        else
        {
            double splitX = (tX + x) / 2;
            double splitY = (tY + y) / 2;
            double splitZ = (tZ + z) / 2;
            splitX += (rand.nextFloat() - 0.5) * displacement;
            splitY += (rand.nextFloat() - 0.5) * displacement;
            splitZ += (rand.nextFloat() - 0.5) * displacement;
            drawArc(buffer, x, y, z, splitX, splitY, splitZ, displacement / 2, complexity, density);
            drawArc(buffer, tX, tY, tZ, splitX, splitY, splitZ, displacement / 2, complexity, density);
        }
    }

    @Override
    public void onUpdate()
    {
        if (this.particleAge++ > this.particleMaxAge)
        {
            this.setExpired();
        }
    }

    @Override
    public int getFXLayer()
    {
        return 3;
    }
}
