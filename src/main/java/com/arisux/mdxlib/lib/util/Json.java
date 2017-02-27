package com.arisux.mdxlib.lib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.Level;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import net.minecraftforge.fml.common.FMLLog;


public class Json
{
    public static JsonElement parseJsonFromFile(File pathToJson)
    {
        try
        {
            return parseJsonFromStream(new FileInputStream(pathToJson));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonElement parseJsonFromStream(InputStream stream)
    {
        try
        {
            InputStreamReader reader = new InputStreamReader(stream);
            JsonParser parser = new JsonParser();
            JsonElement rootElement = parser.parse(reader);

            if (rootElement.isJsonArray())
            {
                for (JsonElement json : rootElement.getAsJsonArray())
                {
                    return json;
                }
            }
        }
        catch (Exception e)
        {
            FMLLog.log(Level.ERROR, e, "The stream could not be parsed as valid JSON.");
            e.printStackTrace();
        }
        return null;
    }
}
