package com.arisux.amdxlib.lib.util;

import java.lang.reflect.Field;

import com.arisux.amdxlib.AMDXLib;
import com.arisux.amdxlib.lib.game.Game;

public class Reflect
{
    public static double getDouble(Object obj, String deobfName, String obfName)
    {
        try
        {
            return ((Double) get(obj, deobfName, obfName)).doubleValue();
        }
        catch (Exception e)
        {
            ;
        }

        return 0D;
    }

    public static float getFloat(Object obj, String deobfName, String obfName)
    {
        try
        {
            return ((Float) get(obj, deobfName, obfName)).floatValue();
        }
        catch (Exception e)
        {
            ;
        }

        return 0F;
    }

    public static int getInt(Object obj, String deobfName, String obfName)
    {
        try
        {
            return ((Integer) get(obj, deobfName, obfName)).intValue();
        }
        catch (Exception e)
        {
            ;
        }

        return 0;
    }

    public static boolean getBoolean(Object obj, String deobfName, String obfName)
    {
        try
        {
            return ((Boolean) get(obj, deobfName, obfName)).booleanValue();
        }
        catch (Exception e)
        {
            ;
        }

        return false;
    }

    public static long getLong(Object obj, String deobfName, String obfName)
    {
        try
        {
            return ((Long) get(obj, deobfName, obfName)).longValue();
        }
        catch (Exception e)
        {
            ;
        }

        return 0L;
    }

    public static byte getByte(Object obj, String deobfName, String obfName)
    {
        try
        {
            return ((Byte) get(obj, deobfName, obfName)).byteValue();
        }
        catch (Exception e)
        {
            ;
        }

        return 0;
    }

    public static String getString(Object obj, String deobfName, String obfName)
    {
        try
        {
            return (String) get(obj, deobfName, obfName);
        }
        catch (Exception e)
        {
            ;
        }

        return "";
    }

    public static void set(Object obj, String deobfName, String obfName, Object value)
    {
        set(obj.getClass(), obj, deobfName, obfName, value);
    }

    public static void set(Class<?> clazz, Object obj, String deobfName, String obfName, Object value)
    {
        String fieldName = Game.isDevEnvironment() ? deobfName : obfName;

        try
        {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        }
        catch (Exception e)
        {
            AMDXLib.log().warn(String.format("Failed setting field %s to %s: %s", fieldName, value, e));
        }
    }

    public static Object get(Object obj, String deobfName, String obfName)
    {
        return get(obj.getClass(), obj, deobfName, obfName);
    }

    public static Object get(Class<?> clazz, Object obj, String deobfName, String obfName)
    {
        String fieldName = Game.isDevEnvironment() ? deobfName : obfName;

        try
        {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        }
        catch (Exception e)
        {
            AMDXLib.log().warn(String.format("Failed getting field %s: %s", fieldName, e));
        }
        return null;
    }
}
