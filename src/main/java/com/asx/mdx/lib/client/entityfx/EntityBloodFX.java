package com.asx.mdx.lib.client.entityfx;

import com.asx.mdx.lib.client.util.OpenGL;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityBloodFX extends Particle
{
    private int     bobTimer;
    private boolean glow;

    public EntityBloodFX(World worldIn, double posX, double posY, double posZ, int color, boolean glow)
    {
        super(worldIn, posX, posY, posZ, 0, 0, 0);
        this.particleMaxAge = ((60 * 20) * 3) + ((this.rand.nextInt(30 * 20)));
        this.glow = glow;
        this.particleGravity = 0.06F;
        this.particleScale = (this.rand.nextFloat() * 0.5F + 0.5F);
        this.bobTimer = 40;
        float particleSpread = 0.15F;
        this.motionY = (double) (this.rand.nextFloat() * 0.4F + 0.05F);
        this.motionZ = (double) (this.rand.nextFloat() * particleSpread) - (this.rand.nextFloat() * particleSpread);
        this.motionX = (double) (this.rand.nextFloat() * particleSpread) - (this.rand.nextFloat() * particleSpread);
        this.particleAlpha = ((float) ((color & 0xFF000000) >> 24)) / 255F;;
        this.particleRed = ((float) ((color & 0xFF0000) >> 16)) / 255F;
        this.particleGreen = ((float) ((color & 0xFF00) >> 8)) / 255F;
        this.particleBlue = ((float) (color & 0xFF)) / 255F;

        // this.particleRed = this.particleRed < 0.4F ? 0.4F : this.particleRed;
        // this.particleGreen = this.particleGreen < 0.4F ? 0.4F : this.particleGreen;
        // this.particleBlue = this.particleBlue < 0.4F ? 0.4F : this.particleBlue;
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        this.motionY -= (double) this.particleGravity;

        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.bobTimer-- > 0)
        {
            this.motionX *= 0.02D;
            this.motionY *= 0.02D;
            this.motionZ *= 0.02D;
        }

        if (this.particleMaxAge-- <= 0)
        {
            this.setExpired();
        }

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }

        BlockPos pos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ));
        IBlockState blockstate = this.world.getBlockState(pos);
        Material material = blockstate.getMaterial();

        if (material.isLiquid() || material.isSolid())
        {
            double d0 = (double) ((float) (MathHelper.floor(this.posY) + 1) - BlockLiquid.getLiquidHeightPercent(blockstate.getBlock().getMetaFromState(blockstate)));

            if (this.posY < d0)
            {
                this.setExpired();
            }
        }
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rX, float rZ, float rYZ, float rXY, float rXZ)
    {
        float r = this.particleRed;
        float g = this.particleGreen;
        float b = this.particleBlue;
        float a = this.particleAlpha;

        if (glow)
        {
            OpenGL.disableLightMapping();
            OpenGL.disableLight();
        }

        float u2 = (float) this.particleTextureIndexX / 16.0F;
        float u1 = u2 + 0.0624375F;
        float v2 = (float) this.particleTextureIndexY / 16.0F;
        float v1 = v2 + 0.0624375F;
        float pScale = 0.3F * this.particleScale;

        if (this.particleTexture != null)
        {
            u2 = this.particleTexture.getMinU();
            u1 = this.particleTexture.getMaxU();
            v2 = this.particleTexture.getMinV();
            v1 = this.particleTexture.getMaxV();
        }

        float yOffset = 0.1F;
        float x = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
        float y = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY + yOffset);
        float z = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);

        int brightness = this.getBrightnessForRender(partialTicks);
        int skyLight = brightness >> 16 & 65535;
        int blocKLight = brightness & 65535;
        Vec3d[] vec = new Vec3d[] { new Vec3d((double) (-rX * pScale - rXY * pScale), (double) (-rZ * pScale), (double) (-rYZ * pScale - rXZ * pScale)), new Vec3d((double) (-rX * pScale + rXY * pScale), (double) (rZ * pScale), (double) (-rYZ * pScale + rXZ * pScale)), new Vec3d((double) (rX * pScale + rXY * pScale), (double) (rZ * pScale), (double) (rYZ * pScale + rXZ * pScale)), new Vec3d((double) (rX * pScale - rXY * pScale), (double) (-rZ * pScale), (double) (rYZ * pScale - rXZ * pScale)) };

        if (this.particleAngle != 0.0F)
        {
            float angle = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
            float scale = MathHelper.cos(angle * 0.5F);
            float viewX = MathHelper.sin(angle * 0.5F) * (float) cameraViewDir.x;
            float viewY = MathHelper.sin(angle * 0.5F) * (float) cameraViewDir.y;
            float viewZ = MathHelper.sin(angle * 0.5F) * (float) cameraViewDir.z;
            Vec3d vec3d = new Vec3d((double) viewX, (double) viewY, (double) viewZ);

            for (int v = 0; v < 4; ++v)
            {
                vec[v] = vec3d.scale(2.0D * vec[v].dotProduct(vec3d)).add(vec[v].scale((double) (scale * scale) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(vec[v]).scale((double) (2.0F * scale)));
            }
        }

        buffer.pos((double) x + vec[0].x, (double) y + vec[0].y, (double) z + vec[0].z).tex((double) u1, (double) v1).color(r, g, b, a).lightmap(skyLight, blocKLight).endVertex();
        buffer.pos((double) x + vec[1].x, (double) y + vec[1].y, (double) z + vec[1].z).tex((double) u1, (double) v2).color(r, g, b, a).lightmap(skyLight, blocKLight).endVertex();
        buffer.pos((double) x + vec[2].x, (double) y + vec[2].y, (double) z + vec[2].z).tex((double) u2, (double) v2).color(r, g, b, a).lightmap(skyLight, blocKLight).endVertex();
        buffer.pos((double) x + vec[3].x, (double) y + vec[3].y, (double) z + vec[3].z).tex((double) u2, (double) v1).color(r, g, b, a).lightmap(skyLight, blocKLight).endVertex();
    }

    @Override
    public int getFXLayer()
    {
        return 0;
    }
}
