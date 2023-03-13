package com.asx.mdx.common.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.asx.mdx.client.ClientGame;
import com.asx.mdx.common.Game;
import com.asx.mdx.common.mods.IInitEvent;
import com.asx.mdx.common.mods.IPreInitEvent;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class JsonResourceGenerator implements IPreInitEvent, IInitEvent
{
    private File   baseDirectory;
    private File   defaultsDirectory;
    private File   domainDir;
    private String modid;

    public static enum ResourceType
    {
        BLOCKSTATE("blockstate", "blockstate.json"), BLOCKSTATE_FORGE_CUBE("blockstate.forge.cube", "blockstate_forge_cube.json"), ITEM_MODEL("model.item", "itemmodel.json"), BLOCK_MODEL("model.block", "blockmodel.json");

        private String id;
        private String defaultName;
        private String defaultJson;
        private File   defaultJsonFile;

        ResourceType(String id, String defaultName)
        {
            this.id = id;
            this.defaultName = defaultName;
        }

        public String getId()
        {
            return id;
        }

        public String getDefaultJson()
        {
            return defaultJson;
        }

        public void loadDefault(File defaultDir) throws IOException
        {
            this.defaultJsonFile = new File(defaultDir, defaultName);
            this.defaultJson = readFile(this.defaultJsonFile.getPath(), Charset.defaultCharset());
        }
    }

    public JsonResourceGenerator(String modid)
    {
        this.modid = modid;
        this.baseDirectory = new File(ClientGame.instance.instance.minecraft().gameDir, "jsongen");
        this.defaultsDirectory = new File(baseDirectory, "default");
        this.domainDir = new File(baseDirectory, modid);
    }

    @Override
    public void init(FMLInitializationEvent event)
    {

    }

    @Override
    public void pre(FMLPreInitializationEvent event)
    {
        try
        {
            for (ResourceType type : ResourceType.values())
            {
                type.loadDefault(defaultsDirectory);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (!domainDir.exists())
        {
            domainDir.mkdirs();
        }
    }

    public void generateAll(String identifier)
    {
        if (domainDir.exists())
        {
            for (ResourceType type : ResourceType.values())
            {
                generate(type, identifier);
            }
        }
    }

    public void generate(ResourceType type, String identifier)
    {
        if (domainDir.exists())
        {
            createResource(type, identifier);
        }
    }

    private void createResource(ResourceType type, String id)
    {
        File typeDir = new File(domainDir, type.getId());

        if (!typeDir.exists())
        {
            typeDir.mkdirs();
        }

        File resource = new File(typeDir, String.format("%s.json", id));
        String newJson = type.getDefaultJson();

        newJson = newJson.replace("%DOMAIN%", this.modid);
        newJson = newJson.replace("%MODEL%", id);
        newJson = newJson.replace("%TEXTURE%", id);

        try
        {
            resource.createNewFile();

            if (resource.exists())
            {
                PrintWriter out = new PrintWriter(resource);
                out.println(newJson);
                out.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static String readFile(String path, Charset encoding) throws IOException
    {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        return new String(bytes, encoding);
    }
}
