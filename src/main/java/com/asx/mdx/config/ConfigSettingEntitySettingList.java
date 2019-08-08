package com.asx.mdx.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.asx.mdx.MDX;
import com.asx.mdx.lib.util.Game;
import com.asx.mdx.lib.world.entity.Entities;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ConfigSettingEntitySettingList<T> extends ConfigSetting
{
    public ConfigSettingEntitySettingList(IFlexibleConfiguration configuration, Property property)
    {
        super(configuration, property);
    }

    @Override
    public void toggle()
    {
        ;
    }

    @SuppressWarnings("unchecked")
    private T convertStringToValue(String value)
    {
        T val = null;

        if (isStringHexColor(value))
        {
            return (T) (Object) stringToHexColor(value);
        }

        if (isStringInteger(value))
        {
            return (T) (Object) Integer.parseInt(value);
        }

        if (isStringBoolean(value))
        {
            return (T) (Object) Boolean.parseBoolean(value);
        }

        return val;
    }

    private static String convertValueToString(Object value)
    {
        String str = null;

        if (value instanceof Integer && isStringHexColor(value.toString()))
        {
            return hexColorToString((int) value);
        }

        if (isStringInteger(value.toString()))
        {
            return value.toString();
        }

        if (isStringBoolean(value.toString()))
        {
            return value.toString();
        }

        return str;
    }

    public static boolean isStringBoolean(String value)
    {
        try
        {
            Boolean.parseBoolean(value);
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    public static boolean isStringInteger(String value)
    {
        try
        {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException ex)
        {
            return false;
        }
    }

    public static boolean isStringHexColor(String value)
    {
        try
        {
            String hexString = value;

            try
            {
                int intValue = Integer.parseInt(value);
                hexString = Integer.toHexString(intValue);
            }
            catch (Exception e)
            {
                hexString = value;
            }

            Integer.parseUnsignedInt(hexString, 16);
            return true;
        }
        catch (NumberFormatException ex)
        {
            return false;
        }
    }

    public static String hexColorToString(int color)
    {
        return Integer.toHexString(color);
    }

    public static int stringToHexColor(String value)
    {
        String hex = value.replace("0x", "").replace("#", "");
        return Integer.parseUnsignedInt(hex, 16);
    }

    @Override
    public Map<Class<? extends Entity>, T> value()
    {
        Map<Class<? extends Entity>, T> entitySettings = new HashMap<Class<? extends Entity>, T>();

        if (this.property.getString() != null)
        {
            String[] settings = this.property.getString().split(",");

            for (String data : settings)
            {
                if (data != null && !data.isEmpty())
                {
                    String[] dataArray = data.split(";");
                    T val = convertStringToValue(dataArray[1]);

                    Class<? extends Entity> entity = Entities.getRegisteredEntityClass(dataArray[0]);

                    if (entity != null)
                    {
                        entitySettings.put(entity, val);
                    }
                    else
                    {
                        MDX.log().warn("Invalid entity ID entered. Entity type not found! Entity ID: " + dataArray[0]);
                    }
                }
            }
        }

        return entitySettings;
    }

    @Override
    public String getStringValue()
    {
        return entitySettingListForConfig(value());
    }

    public static String entitySettingListForConfig(Map<Class<? extends Entity>, ?> settings)
    {
        StringBuilder builder = new StringBuilder();

        for (Class<? extends Entity> clz : settings.keySet())
        {
            Object value = settings.get(clz);
            EntityEntry entry = EntityRegistry.getEntry(clz);

            if (clz != null && entry.getRegistryName() != null)
            {
                String data = String.format("%s;%s", entry.getRegistryName().toString(), convertValueToString(value));
                boolean last = settings.get(Arrays.asList(settings.keySet().toArray()).get(settings.size() - 1)) == clz;

                if (!last)
                {
                    builder.append(data + ",");
                }
                else
                {
                    builder.append(data);
                }
            }
            else
            {
                if (Game.isDevEnvironment())
                {
                    MDX.log().info("Entity type was null or had a null registry name. This is probably a bug. Entity type: " + clz);
                }
            }
        }

        return builder.toString();
    }
}