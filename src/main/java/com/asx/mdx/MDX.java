package com.asx.mdx;

import org.apache.logging.log4j.Logger;

import com.asx.mdx.core.Access;
import com.asx.mdx.core.PacketHandler;
import com.asx.mdx.lib.client.Renderers;
import com.asx.mdx.lib.client.gui.notifications.Notifications;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MDX
{
    public static class Properties
    {
        public static final String NAME = "MDXLib";
        public static final String ID = "mdxlib";
        public static final String DOMAIN = ID + ":";
        public static final String VERSION = "@VERSION@";
    }

    /** Provides access to core Minecraft methods that have restricted access **/
    private static Access access;

    public MDX()
    {
        access = new Access();
    }

    public static MDX instance()
    {
        return MDXModule.instance();
    }

    public static Access access()
    {
        return access;
    }

    public static Console console()
    {
        return Console.INSTANCE;
    }

    public static Logger log()
    {
        return Console.LOGGER;
    }

    public static Settings settings()
    {
        return Settings.INSTANCE;
    }

    public static PacketHandler network()
    {
        return PacketHandler.instance;
    }

    @SideOnly(Side.CLIENT)
    public static Notifications notifications()
    {
        return Notifications.INSTANCE;
    }

    @SideOnly(Side.CLIENT)
    public static Renderers renders()
    {
        return Renderers.INSTANCE;
    }
}
