package com.arisux.amdxlib.lib.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;

import com.arisux.amdxlib.AMDXLib;

import cpw.mods.fml.common.FMLLog;

public class Jars
{
    /**
     * Extracts a file with specified location from the specified 
     * mod's java archive to the specified file instance.
     * 
     * @param modClass - Mod class to retrieve files from.
     * @param filePath - File path to retrieve a file from.
     * @param to - File instance that the information will be saved to.
     * @throws IOException
     */
    @SuppressWarnings("unused")
    private static void copyFileFromJar(Class<?> modClass, String filePath, File to) throws IOException
    {
        AMDXLib.log().info(String.format("Extracting %s from %s jar", filePath, modClass.getSimpleName()));
        URL url = modClass.getResource(filePath);
        FileUtils.copyURLToFile(url, to);
    }

    public static ArrayList<JarEntry> getZipEntriesInJar(JarFile jar)
    {
        return Collections.list(jar.entries());
    }

    public static FileInputStream getFileInputStreamFromJar(JarFile jar, File pathToFile)
    {
        try
        {
            ZipEntry zipEntry = jar.getEntry(pathToFile.toString());

            if (zipEntry != null)
            {
                return (FileInputStream) jar.getInputStream(zipEntry);
            }
        }
        catch (Exception e)
        {
            FMLLog.log(Level.WARN, e, "Jar %s failed to read properly, it will be ignored", jar.getName());
        }

        return null;
    }

    public static File extract(File compressedFile, File extractionDir)
    {
        if (!extractionDir.exists())
        {
            extractionDir.mkdir();
        }

        ZipFile zip = null;

        try
        {
            zip = new ZipFile(compressedFile);

            Enumeration<? extends ZipEntry> e = zip.entries();

            while (e.hasMoreElements())
            {
                ZipEntry entry = e.nextElement();

                File destinationPath = new File(extractionDir, entry.getName());

                if (!destinationPath.exists())
                {
                    destinationPath.getParentFile().mkdirs();
                }

                if (entry.isDirectory())
                {
                    continue;
                }
                else
                {
                    BufferedInputStream inputStream = new BufferedInputStream(zip.getInputStream(entry));

                    int b;
                    byte buffer[] = new byte[1024];

                    FileOutputStream fileOutputStream = new FileOutputStream(destinationPath);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, 1024);

                    while ((b = inputStream.read(buffer, 0, 1024)) != -1)
                    {
                        bufferedOutputStream.write(buffer, 0, b);
                    }

                    bufferedOutputStream.close();
                    inputStream.close();
                    AMDXLib.log().info("Extracted: " + destinationPath);
                }
            }
        }
        catch (Exception e)
        {
            AMDXLib.log().warn(String.format("Error extracting %s: %s", compressedFile.getAbsolutePath(), e));
            e.printStackTrace();
        }
        finally
        {
            if (zip != null)
            {
                try
                {
                    zip.close();
                }
                catch (Exception e)
                {
                    AMDXLib.log().warn(String.format("Error closing compressed file: %s", e));
                    e.printStackTrace();
                }
            }
        }

        return extractionDir;
    }
}
