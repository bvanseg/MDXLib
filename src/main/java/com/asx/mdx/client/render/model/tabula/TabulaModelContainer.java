package com.asx.mdx.client.render.model.tabula;

import java.util.ArrayList;
import java.util.List;

public class TabulaModelContainer
{
    private String                         modelName;
    private String                         authorName;

    private int                            projVersion;
    private String[]                       metadata;

    private double[]                       scale      = new double[] { 1.0, 1.0, 1.0 };

    private int                            textureWidth;
    private int                            textureHeight;

    private List<TabulaCubeGroupContainer> cubeGroups = new ArrayList<>();
    private List<TabulaCubeContainer>      cubes      = new ArrayList<>();
    private List<TabulaAnimationContainer> anims      = new ArrayList<>();
    private int                            cubeCount;

    public TabulaModelContainer(String name, String author, int textureWidth, int textureHeight, List<TabulaCubeContainer> cubes, int projectVersion)
    {
        this.modelName = name;
        this.authorName = author;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.cubes = cubes;
        this.cubes.forEach(this::incrementCubeCount);
        this.projVersion = projectVersion;
    }

    private void incrementCubeCount(TabulaCubeContainer cube)
    {
        this.cubeCount++;
        cube.getChildren().forEach(this::incrementCubeCount);
    }

    public String getName()
    {
        return this.modelName;
    }

    public String getAuthor()
    {
        return this.authorName;
    }

    public int getProjectVersion()
    {
        return this.projVersion;
    }

    public String[] getMetadata()
    {
        return this.metadata;
    }

    public int getTextureWidth()
    {
        return this.textureWidth;
    }

    public int getTextureHeight()
    {
        return this.textureHeight;
    }

    public double[] getScale()
    {
        return this.scale;
    }

    public List<TabulaCubeGroupContainer> getCubeGroups()
    {
        return this.cubeGroups;
    }

    public List<TabulaCubeContainer> getCubes()
    {
        return this.cubes;
    }

    public List<TabulaAnimationContainer> getAnimations()
    {
        return this.anims;
    }

    public int getCubeCount()
    {
        return this.cubeCount;
    }

    public ArrayList<TabulaCubeContainer> getAllCubes()
    {
        return getAllCubes(null, null);
    }

    private ArrayList<TabulaCubeContainer> getAllCubes(TabulaCubeContainer parent, ArrayList<TabulaCubeContainer> list)
    {
        if (list == null)
        {
            list = new ArrayList<TabulaCubeContainer>();
        }

        for (TabulaCubeContainer p : (parent == null ? this.getCubes() : parent.getChildren()))
        {
            list.add(p);

            if (p.getChildren().size() > 0)
            {
                getAllCubes(p, list);
            }
        }

        return list;
    }

    public TabulaCubeContainer getCubeByName(String name)
    {
        for (TabulaCubeContainer c : this.getAllCubes())
        {
            if (c.getName().equals(name))
            {
                return c;
            }
        }

        return null;
    }
}
