package com.arisux.mdx.lib.world.block;

import java.util.List;

import com.arisux.mdx.lib.client.render.Matrix3;
import com.arisux.mdx.lib.client.render.Vertex;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockShape extends Block
{
    public static enum Shape
    {
        SLOPE(0), CORNER(1), INVERTED_CORNER(2), RIDGE(3), SMART_RIDGE(4), INVERTED_RIDGE(5), SMART_INVERTED_RIDGE(6);

        private int               id;

        public final static Shape ridges[]                 = { RIDGE, SMART_RIDGE
        };
        public final static Shape ridgesOrSlopes[]         = { RIDGE, SMART_RIDGE, SLOPE, CORNER, INVERTED_CORNER
        };
        public final static Shape invertedRidges[]         = { INVERTED_RIDGE, SMART_INVERTED_RIDGE
        };
        public final static Shape invertedRidgesOrSlopes[] = { INVERTED_RIDGE, SMART_INVERTED_RIDGE, SLOPE, INVERTED_CORNER
        };

        private Shape(int id)
        {
            this.id = id;
        }

        public int getId()
        {
            return id;
        }
    }

    private Shape shape;
    private int   textureSide;
    private Block textureBlock;

    public BlockShape(Shape shape)
    {
        super(Material.GROUND);
        this.shape = shape;
        this.textureSide = 0;
        this.setLightOpacity(1);
    }

    public BlockShape(Material material, Shape shape)
    {
        super(material);
        this.shape = shape;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[0])
        {
            @Override
            protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties, ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties)
            {
                return new StateImplementation(block, properties)
                {
                    @Override
                    public EnumBlockRenderType getRenderType()
                    {
                        // AliensVsPredator.renderTypes().RENDER_TYPE_SHAPED
                        return EnumBlockRenderType.MODEL;
                    }

                    @Override
                    public boolean isOpaqueCube()
                    {
                        return false;
                    }

                    @Override
                    public void addCollisionBoxToList(World world, BlockPos pos, AxisAlignedBB clip, List<AxisAlignedBB> list, Entity entity)
                    {
                        super.addCollisionBoxToList(world, pos, clip, list, entity);
                        IBlockState state = world.getBlockState(pos);
                        int data = state.getBlock().getMetaFromState(state);
                        Matrix3 rot = Matrix3.rotations[data >> 2];
                        Vertex org = new Vertex(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

                        this.addBox(-0.5, 0.5, -0.5, 0.0, -0.5, 0.5, rot, org, clip, list);

                        if (shape.getId() == 0 || shape.getId() == 2)
                        {
                            this.addBox(-0.5, 0.5, 0.0, 0.5, 0.0, 0.5, rot, org, clip, list);
                        }
                        if (shape.getId() == 1)
                        {
                            this.addBox(-0.5, 0.0, 0.0, 0.5, 0.0, 0.5, rot, org, clip, list);
                        }
                        if (shape.getId() == 2)
                        {
                            this.addBox(-0.5, 0.0, 0.0, 0.5, -0.5, 0.0, rot, org, clip, list);
                        }
                    }

                    @SuppressWarnings({ "rawtypes", "unchecked" })
                    public void addBox(double x0, double x1, double y0, double y1, double z0, double z1, Matrix3 rot, Vertex org, AxisAlignedBB clip, List list)
                    {
                        Vertex p0 = rot.mul(x0, y0, z0).add(org);
                        Vertex p1 = rot.mul(x1, y1, z1).add(org);

                        if (p0.x < p1.x)
                        {
                            x0 = p0.x;
                            x1 = p1.x;
                        }
                        else
                        {
                            x0 = p1.x;
                            x1 = p0.x;
                        }
                        if (p0.y < p1.y)
                        {
                            y0 = p0.y;
                            y1 = p1.y;
                        }
                        else
                        {
                            y0 = p1.y;
                            y1 = p0.y;
                        }
                        if (p0.z < p1.z)
                        {
                            z0 = p0.z;
                            z1 = p1.z;
                        }
                        else
                        {
                            z0 = p1.z;
                            z1 = p0.z;
                        }
                        AxisAlignedBB box = new AxisAlignedBB(x0, y0, z0, x1, y1, z1);

                        if (box != null && clip.intersectsWith(box))
                        {
                            list.add(box);
                        }
                    }
                };
            }
        };
    }

    public void setIconsFromBlock(Block block)
    {
        this.textureBlock = block;
    }

    public Block getTextureBlock()
    {
        return textureBlock;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return true;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        world.setBlockState(pos, this.getStateFromMeta(placementRotation(placer)), 3);
    }

    public static int placementRotation(EntityLivingBase player)
    {
        int meta = (MathHelper.floor((player.rotationYaw * 4.0 / 360.0) + 0.5)) & 3;
        meta = meta | ((player.rotationPitch < 0 ? 1 : 0) << 2);

        return meta;
    }

    public Shape getShape()
    {
        return this.shape;
    }

    public int getTextureSide()
    {
        return this.textureSide;
    }

    public BlockShape setTextureSide(int textureSide)
    {
        this.textureSide = textureSide;
        return this;
    }
}
