package com.arisux.mdxlib;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.Logger;

import com.arisux.mdxlib.lib.client.Notification;
import com.arisux.mdxlib.lib.client.render.wavefront.TriangulatedWavefrontModel;
import com.arisux.mdxlib.lib.game.Access;
import com.arisux.mdxlib.lib.game.ModIdentityMap;
import com.arisux.mdxlib.lib.game.ModIdentityMap.IdentityMap;
import com.arisux.mdxlib.lib.world.storage.Schematic;

import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompressedStreamTools;

public class MDX
{
    public static final String                          MODID   = "mdxlib";
    public static final String                          VERSION = "1.0.0";

    /** Provides access to core Minecraft methods that have restricted access **/
    private static Access                               access;

    /** The registry that contains all of the triangulated wavefront models currently registered **/
    private HashMap<String, TriangulatedWavefrontModel> modelRegistry;

    /** The registry that contains all currently registered schematics **/
    private ArrayList<Schematic>                        schematicRegistry;

    /** A queue for notifications that will be displayed     on screen **/
    private static ArrayList<Notification>              notificationsInQueue;

    /** Entire mods that will be remapped **/
    private static ArrayList<ModIdentityMap>            remappedMods;

    /** Individual IDs that will be remapped **/
    private static ArrayList<IdentityMap>               remappedIdentities;

    public MDX()
    {
        access = new Access();
        modelRegistry = new HashMap<String, TriangulatedWavefrontModel>();
        schematicRegistry = new ArrayList<Schematic>();
        notificationsInQueue = new ArrayList<Notification>();
        remappedMods = new ArrayList<ModIdentityMap>();
        remappedIdentities = new ArrayList<IdentityMap>();
    }

    public static MDX instance()
    {
        return ForgeModule.instance();
    }

    public static Access access()
    {
        return access;
    }

    public static Logger log()
    {
        return Console.logger;
    }

    public static void sendNotification(Notification notification)
    {
        if (notification.allowMultiple() || !notification.allowMultiple() && !MDX.getNotificationsInQueue().contains(notification))
        {
            notificationsInQueue.add(notification);
        }
    }

    public static ArrayList<Notification> getNotificationsInQueue()
    {
        return notificationsInQueue;
    }

    /**
     * @return - A registry that containst instances of all registered wavefront models.
     */
    public static HashMap<String, TriangulatedWavefrontModel> getModelRegistry()
    {
        return MDX.instance().modelRegistry;
    }

    /**
     * @return - The File instance of the models directory.
     */
    public static File getModelsDirectory()
    {
        return new File("models");
    }

    /**
     * Extract a wavefront model from the provided URL to the provided File path and then load it.
     * Note: The model MUST be triangulated.
     * 
     * @param c - The main class of the mod we are loading models for.
     * @param modid - The mod id of the mod we are loading models for.
     * @param model - The name of the model we are loading.
     * @param assetsPath - The location of the model in the assets.
     * @return - The model that was loaded. Null if the model was not loaded.
     */
    public static TriangulatedWavefrontModel loadWavefrontModel(Class<?> c, String modid, String model, String assetsPath)
    {
        File baseDir = new File(MDX.getModelsDirectory(), String.format("%s/", modid));
        File path = new File(baseDir, model);

        if (!MDX.getModelsDirectory().exists())
        {
            MDX.getModelsDirectory().mkdirs();
        }

        if (!baseDir.exists())
        {
            baseDir.mkdirs();
        }

        try
        {
            URL urlOBJ = c.getResource(assetsPath + ".obj");
            File fileOBJ = new File(path.getAbsolutePath() + ".obj");
            URL urlMTL = c.getResource(assetsPath + ".mtl");
            File fileMTL = new File(path.getAbsolutePath() + ".mtl");

            if (!fileOBJ.exists())
            {
                FileUtils.copyURLToFile(urlOBJ, fileOBJ);
                MDX.log().info(String.format("Extracted wavefront model: %s", fileOBJ.getAbsoluteFile().getPath()));
            }

            if (!fileMTL.exists())
            {
                FileUtils.copyURLToFile(urlMTL, fileMTL);
                MDX.log().info(String.format("Extracted wavefront texture: %s", fileMTL.getAbsoluteFile().getPath()));
            }
        }
        catch (Exception e)
        {
            MDX.log().info(String.format("Error while extracting %s from %s: %s", path, assetsPath, e));
            e.printStackTrace();
        }

        return loadWavefrontModel(modid, path);
    }

    /**
     * Load a wavefront model from the provided path. 
     * Note: The model MUST be triangulated.
     * 
     * @param modid - The mod id of the mod we are loading models for.
     * @param path - The path we are extracting to and loading the model from.
     * @return - The model that was loaded. Null if the model was not loaded.
     */
    public static TriangulatedWavefrontModel loadWavefrontModel(String modid, File path)
    {
        TriangulatedWavefrontModel model = new TriangulatedWavefrontModel();

        if (model.load(modid, path.getAbsolutePath()))
        {
            String tag = path.getAbsolutePath().replaceAll(".obj", "").replaceAll(".OBJ", "");
            tag = tag.substring(tag.lastIndexOf('/') + 1, tag.length());
            MDX.getModelRegistry().put(tag, model);

            MDX.log().info("[WavefrontAPI] Loaded wavefront model: " + path);
        }
        else
        {
            MDX.log().info("[WavefrontAPI] Unable to load wavefront model: " + path);
        }

        return model;
    }

    /**
     * Get the instance of the model with the specified name.
     * 
     * @param modelName - The name of the model we are getting.
     * @return The instance of the model that was found.
     */
    public static TriangulatedWavefrontModel getWavefrontModel(String modelName)
    {
        return MDX.getModelRegistry().get(modelName);
    }

    public static ArrayList<Schematic> getSchematicRegistry()
    {
        return MDX.instance().schematicRegistry;
    }

    /**
     * @return - The File instance of the schematics directory.
     */
    public static File getSchematicsDirectory()
    {
        return new File("./", "schematics");
    }

    /**
     * @return - A Collection of Files containing File instances to each schematic
     * found in the schematics directory.
     */
    public static Collection<File> getSchematicsInDirectory()
    {
        return FileUtils.listFiles(MDX.getSchematicsDirectory(), new String[] { "schematic" }, true);
    }

    /**
     * @return - An Array of Strings containing the names of all schematics in
     * the schematics directory.
     */
    public static String[] getSchematicNames()
    {
        Collection<File> files = MDX.getSchematicsInDirectory();
        String[] filenames = new String[files.size()];
        int i = 0;

        for (File file : files)
        {
            filenames[i++] = FilenameUtils.getBaseName(file.getName());
        }

        return filenames;
    }

    /**
     * Load a schematic from the schematics directory by using a direct File path.
     * 
     * @param path - The File path we are loading the schematic from.
     * @return - The schematic that was loaded. Null if the schematic was not loaded.
     */
    public static Schematic loadSchematic(File path)
    {
        try
        {
            Schematic schematic = new Schematic(path, CompressedStreamTools.readCompressed(new FileInputStream(path)));
            MDX.getSchematicRegistry().add(schematic);

            return schematic;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Load a schematic from the schematics directory simply by using its name.
     * 
     * @param name - The name of the schematic we are loading.
     * @return - The schematic that was loaded. Null if the schematic was not loaded.
     */
    public static Schematic loadSchematic(String name)
    {
        name = FilenameUtils.getExtension(name).length() == 0 ? name + ".schematic" : name;

        for (File file : MDX.getSchematicsInDirectory())
        {
            if (file.getPath().endsWith(name) && name.endsWith(file.getName()))
            {
                return MDX.loadSchematic(file);
            }
        }

        return null;
    }

    /**
     * Extract a schematic from the provided URL to the provided File path and then load it.
     * The schematic will not be extracted if it is already present in the provided path.
     * 
     * @param path - The path we are extracting to and loading the schematic from.
     * @param resource - The URL path we are extracting the resource from.
     * @return - The schematic that was loaded. Null if the schematic was not loaded.
     */
    public static Schematic loadSchematic(File path, URL resource)
    {
        if (!MDX.getSchematicsDirectory().exists())
        {
            MDX.getSchematicsDirectory().mkdirs();
        }

        if (!path.exists())
        {
            try
            {
                FileUtils.copyURLToFile(resource, path);
                MDX.log().info(String.format("Extracted %s", path.getAbsoluteFile().getPath()));
            }
            catch (Exception e)
            {
                MDX.log().info(String.format("Error while extracting %s: %s", path, e));
            }
        }

        return MDX.loadSchematic(path);
    }

    public static void registerRemappedEntity(Class<? extends Entity> entityClass, String invalidId)
    {
        EntityList.stringToClassMapping.put(invalidId, entityClass);
    }

    public static void registerRemappedMod(String oldID, String newID, String modClassLocation)
    {
        MDX.getRemappedMods().add(new ModIdentityMap(oldID, newID, modClassLocation));
    }

    public static void registerMappingInfo(String oldID, String newID, String modId)
    {
        MDX.getRemappedIdentities().add(new IdentityMap(oldID, newID, new ModIdentityMap(modId)));
    }

    public static void replaceMapping(MissingMapping mapping, String oldID, String newID)
    {
        String newName = (mapping.name).replace(oldID, newID);

        /** Check for and replace missing item mappings **/
        if (mapping.type == GameRegistry.Type.ITEM)
        {
            MDX.log().info("Converting item mapping [" + mapping.name + "@" + mapping.id + "] -> [" + newName + "@" + mapping.id + "]");

            Item item = (Item) Item.itemRegistry.getObject(newName);

            if (item != null)
            {
                mapping.remap(item);
            }
            else
            {
                MDX.log().warn("Error converting item mapping [" + mapping.name + "@" + mapping.id + "]");
            }
        }

        /** Check for and replace missing block mappings **/
        if (mapping.type == GameRegistry.Type.BLOCK)
        {
            Block block = (Block) Block.blockRegistry.getObject(newName);

            MDX.log().info("Converting block mapping [" + mapping.name + "@" + mapping.id + "] -> [" + newName + "@" + mapping.id + "]");

            if (block != null)
            {
                mapping.remap(block);
            }
            else
            {
                MDX.log().warn("Error converting block mapping. [" + mapping.name + "@" + mapping.id + "]");
            }
        }
    }

    public static ArrayList<IdentityMap> getRemappedIdentities()
    {
        return remappedIdentities;
    }

    public static ArrayList<ModIdentityMap> getRemappedMods()
    {
        return remappedMods;
    }
}
