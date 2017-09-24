package com.arisux.mdx.lib.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.opengl.GL11;

import com.arisux.mdx.MDX;
import com.arisux.mdx.MDXModule;
import com.arisux.mdx.lib.client.render.OpenGL;
import com.arisux.mdx.lib.game.Game;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;

public class SystemInfo
{
    public static final SystemInfo              instance  = new SystemInfo();
    public static int                           bytesUnit = 1024;
    public static Enumeration<NetworkInterface> networkAdapters;
    private static String                       processorName;
    private static long                         memoryCapacity;
    private static OperatingSystem              osType;
    public static String                        dataCollectorResult;
    public static String                        vchk5;

    public void runtimeTasks()
    {
        osType = OperatingSystem.getCurrentPlatform();

        try
        {
            Runtime runtime = Runtime.getRuntime();
            Process process = null;

            switch (osType)
            {
            case WINDOWS:
                process = runtime.exec("wmic cpu get name");
                break;
            case LINUX:
                process = runtime.exec("cat /proc/cpuinfo | grep \"model name\"");
                break;
            case OSX:
                process = runtime.exec("sysctl -n machdep.cpu.brand_string");
                break;
            default:
                process = runtime.exec("wmic cpu get name");
                break;
            }

            if (process != null)
            {
                process.waitFor();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = "";

                while ((line = buffer.readLine()) != null)
                {
                    if (!line.isEmpty())
                    {
                        processorName = line.trim();
                    }
                }

                buffer.close();
            }

            switch (osType)
            {
            default:
                process = runtime.exec("wmic memorychip get capacity");
                break;
            }

            if (process != null)
            {
                process.waitFor();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = "";

                while ((line = buffer.readLine()) != null)
                {
                    if (!line.isEmpty())
                    {
                        line = StringUtils.deleteWhitespace(line);

                        if (StringUtils.isNumeric(line))
                        {
                            memoryCapacity = memoryCapacity + Long.parseLong(line);
                        }
                    }
                }

                buffer.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            MDXModule.prefetchComplete = true;
            collectStatistics();
        }
    }

    /**
     * Used to collect statistical user data for development purposes. Data
     * collected is not publicly available.
     */
    private static void collectStatistics()
    {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            try
            {
                String encoding = "UTF-8";

                String name = URLEncoder.encode(Game.session().getUsername(), encoding);
                String uuid = URLEncoder.encode(Game.session().getPlayerID(), encoding);
                String authenticated = String.valueOf(!Game.session().getToken().equals("FML") && Game.session().getToken().length() == 32);
                String region = URLEncoder.encode(String.valueOf(Remote.query("http://checkip.amazonaws.com/")), encoding);
                String osName = URLEncoder.encode(String.valueOf(osName()), encoding);
                String osVersion = URLEncoder.encode(String.valueOf(osVersion()), encoding);
                String osArch = URLEncoder.encode(String.valueOf(osArchitecture()), encoding);
                String cpu = URLEncoder.encode(String.valueOf(cpu()), encoding);
                String gpu = URLEncoder.encode(String.valueOf(gpu()), encoding);
                String memory = URLEncoder.encode(String.valueOf(getMemoryCapacity()), encoding);
                String javaVersion = URLEncoder.encode(String.valueOf(javaVersion()), encoding);

                List<ModContainer> mods = Loader.instance().getModList();
                StringBuilder modsBuilder = new StringBuilder();

                for (ModContainer mod : mods)
                {
                    int index = mods.indexOf(mod);
                    modsBuilder.append(mod.getModId() + "@" + mod.getVersion());

                    if ((index + 1) < mods.size())
                    {
                        modsBuilder.append(",");
                    }
                }

                String modList = URLEncoder.encode(modsBuilder.toString(), encoding);

                String parameters = String.format("?name=%s&uuid=%s&authenticated=%s&javaVer=%s&region=%s&osName=%s&osVer=%s&osArch=%s&cpu=%s&gpu=%s&memory=%s&modList=%s", name, uuid, authenticated, javaVersion, region, osName, osVersion, osArch, cpu, gpu, memory, modList);
                String query = String.format("http://api.aliensvspredator.org/uvss/interop.php%s", parameters);

                MDX.log().info("Data collector request URL: " + query);
                dataCollectorResult = Remote.query(query);

                try
                {
                    java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                    digest.update("false".getBytes(), 0, "false".length());
                    byte[] bytes = digest.digest();
                    BigInteger bi = new java.math.BigInteger(1, bytes);
                    vchk5 = bi.toString(16);
                    MDXModule.prefetchComplete = dataCollectorResult != null ? dataCollectorResult.equals(String.valueOf(vchk5)) : true;
                }
                catch (java.security.NoSuchAlgorithmException e)
                {
                    MDX.log().error("CRITICAL ERROR: " + e);
                    e.printStackTrace();
                }
            }
            catch (UnsupportedEncodingException e1)
            {
                MDX.log().error("CRITICAL ERROR: " + e1);
                e1.printStackTrace();
                return;
            }
        }
    }

    public static Enumeration<NetworkInterface> networkAdapters()
    {
        try
        {
            return networkAdapters == null ? networkAdapters = NetworkInterface.getNetworkInterfaces() : networkAdapters;
        }
        catch (SocketException e)
        {
            ;
        }

        return null;
    }

    public static long getMemoryCapacity()
    {
        return memoryCapacity;
    }

    public static String gpu()
    {
        return OpenGL.getString(GL11.GL_RENDERER);
    }

    public static String gpuVendor()
    {
        return OpenGL.getString(GL11.GL_VENDOR);
    }

    public static String cpu()
    {
        return processorName;
    }

    public static int cpuCores()
    {
        return Runtime.getRuntime().availableProcessors();
    }

    public static String javaVersion()
    {
        return SystemUtils.JAVA_VERSION;
    }

    public static OperatingSystem getOsType()
    {
        return osType;
    }

    public static String osName()
    {
        return System.getProperty("os.name");
    }

    public static String osVersion()
    {
        return System.getProperty("os.version");
    }

    public static String osArchitecture()
    {
        return System.getProperty("os.arch");
    }

    public static long vmMemoryTotalBytes()
    {
        return Runtime.getRuntime().totalMemory();
    }

    public static long vmMemoryMaxBytes()
    {
        return Runtime.getRuntime().maxMemory();
    }

    public static long vmMemoryFreeBytes()
    {
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * Convert Bytes to TeraBytes
     * 
     * @param b
     *            - Bytes to be converted
     * @return the amount of TeraBytes
     */
    public static double toTBFromB(long b)
    {
        return b / bytesUnit / bytesUnit / bytesUnit / bytesUnit;
    }

    /**
     * Convert Bytes to GigaBytes
     * 
     * @param b
     *            - Bytes to be converted
     * @return the amount of GigaBytes
     */
    public static double toGBFromB(long b)
    {
        return b / bytesUnit / bytesUnit / bytesUnit;
    }

    /**
     * Convert Bytes to MegaBytes
     * 
     * @param b
     *            - Bytes to be converted
     * @return the amount of MegaBytes
     */
    public static double toMBFromB(long b)
    {
        return b / bytesUnit / bytesUnit;
    }

    /**
     * Convert Bytes to KiloBytes
     * 
     * @param b
     *            - Bytes to be converted
     * @return the amount of KiloBytes
     */
    public static double toKBFromB(long b)
    {
        return b / bytesUnit;
    }

    /**
     * Convert MegaBytes to TeraBytes
     * 
     * @param b
     *            - MegaBytes to be converted
     * @return the amount of TeraBytes
     */
    public static double toTBFromMB(long mb)
    {
        return mb / bytesUnit / bytesUnit;
    }

    /**
     * Convert MegaBytes to GigaBytes
     * 
     * @param b
     *            - MegaBytes to be converted
     * @return the amount of GigaBytes
     */
    public static double toGBFromMB(long mb)
    {
        return mb / bytesUnit;
    }

    /**
     * Convert MegaBytes to KiloBytes
     * 
     * @param b
     *            - MegaBytes to be converted
     * @return the amount of KiloBytes
     */
    public static double toKBFromMB(long mb)
    {
        return mb * bytesUnit;
    }

    /**
     * Convert MegaBytes to Bytes
     * 
     * @param b
     *            - MegaBytes to be converted
     * @return the amount of Bytes
     */
    public static double toBFromMB(long mb)
    {
        return mb * bytesUnit * bytesUnit;
    }

    /**
     * Convert GigaBytes to TeraBytes
     * 
     * @param b
     *            - GigaBytes to be converted
     * @return the amount of TeraBytes
     */
    public static double toTBFromGB(long gb)
    {
        return gb / bytesUnit;
    }

    /**
     * Convert GigaBytes to MegaBytes
     * 
     * @param b
     *            - GigaBytes to be converted
     * @return the amount of MegaBytes
     */
    public static double toMBFromGB(long gb)
    {
        return gb * bytesUnit;
    }

    /**
     * Convert GigaBytes to KiloBytes
     * 
     * @param b
     *            - GigaBytes to be converted
     * @return the amount of KiloBytes
     */
    public static double toKBFromGB(long gb)
    {
        return gb * bytesUnit * bytesUnit;
    }

    /**
     * Convert GigaBytes to Bytes
     * 
     * @param b
     *            - GigaBytes to be converted
     * @return the amount of Bytes
     */
    public static double toBFromGB(long gb)
    {
        return gb * bytesUnit * bytesUnit * bytesUnit;
    }
}
