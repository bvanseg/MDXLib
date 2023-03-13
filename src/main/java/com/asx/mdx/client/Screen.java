package com.asx.mdx.client;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.vecmath.Vector2d;

import com.asx.mdx.client.render.OpenGL;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.asx.mdx.internal.MDX;
import com.asx.mdx.common.Game;

import net.minecraft.client.Minecraft;

public class Screen
{

    /**
     * Compatibility version of the ScaledResolution class. Returns the current game display resolution.
     * @return Returns an instance of the compatibility version of ScaledResolution.
     */
    public static ScaledResolution scaledDisplayResolution()
    {
        return new ScaledResolution(ClientGame.instance.minecraft(), ClientGame.instance.minecraft().displayWidth, ClientGame.instance.minecraft().displayHeight);
    }

    /**
     * @return Returns a Vector2d instance containing the mouse's scaled coordinates in-game.
     */
    public static Vector2d scaledMousePosition()
    {
        final int SCALED_WIDTH = scaledDisplayResolution().getScaledWidth();
        final int SCALED_HEIGHT = scaledDisplayResolution().getScaledHeight();
        final int MOUSE_X = Mouse.getX() * SCALED_WIDTH / ClientGame.instance.minecraft().displayWidth;
        final int MOUSE_Y = SCALED_HEIGHT - Mouse.getY() * SCALED_HEIGHT / ClientGame.instance.minecraft().displayHeight - 1;
        return new Vector2d(MOUSE_X, MOUSE_Y);
    }

    /**
     * @return Returns the current game display width and height as a Dimension
     */
    public static Dimension displayResolution()
    {
        Minecraft mc = ClientGame.instance.minecraft();
        return new Dimension(mc.displayWidth, mc.displayHeight);
    }

    /**
     * @return Returns the mouse location in-game.
     */
    public static Point getMouseLocation()
    {
        ScaledResolution size = scaledDisplayResolution();
        Dimension res = displayResolution();
        return new Point(Mouse.getX() * size.getScaledWidth() / res.width, size.getScaledHeight() - Mouse.getY() * size.getScaledHeight() / res.height - 1);
    }

    /**
     * Saves a screenshot to the specified location. Default folder is the working directory: ".minecraft/"
     * 
     * @param filename - File path and name to save the screenshot at.
     * @param x - x coordinate to start screen capture
     * @param y - y coordinate to start screen capture
     * @param width - Width to capture screen at.
     * @param height - Height to capture screen at.
     */
    public static void saveScreenshot(String filename, int x, int y, int width, int height)
    {
        File file = new File(ClientGame.instance.minecraft().gameDir.getPath());
        MDX.log().info("Saving screenshot to " + file.getPath());
    
        if (!file.exists())
        {
            file.mkdirs();
        }
    
        if (ClientGame.instance.minecraft().ingameGUI != null && Keyboard.isKeyDown(Keyboard.KEY_F3) && Keyboard.isKeyDown(Keyboard.KEY_U))
        {
            try
            {
                OpenGL.readBuffer(GL11.GL_FRONT);
                int bpp = 4;
                ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * bpp);
                OpenGL.readPixels(x, y, width, height, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
    
                String format = "png";
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    
                for (int px = x; px < width; px++)
                {
                    for (int py = y; py < height; py++)
                    {
                        int i = (px + (width * py)) * bpp;
                        int r = pixels.get(i) & 0xFF;
                        int g = pixels.get(i + 1) & 0xFF;
                        int b = pixels.get(i + 2) & 0xFF;
                        image.setRGB(px, height - (py + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
                    }
                }
    
                ImageIO.write(image, format, new File(file, filename));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
