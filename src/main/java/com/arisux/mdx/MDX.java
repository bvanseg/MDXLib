package com.arisux.mdx;

import org.apache.logging.log4j.Logger;

import com.arisux.mdx.lib.client.Notifications;
import com.arisux.mdx.lib.game.Access;
import com.arisux.mdx.lib.game.Renderers;

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
        return Console.instance;
    }

    public static Logger log()
    {
        return Console.logger;
    }

    public static Settings settings()
    {
        return Settings.instance;
    }

    public static Notifications notifications()
    {
        return Notifications.instance;
    }

    public static Renderers renders()
    {
        return Renderers.instance;
    }
}
