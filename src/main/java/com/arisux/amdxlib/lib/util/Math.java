package com.arisux.amdxlib.lib.util;

public class Math
{
    public static final double PHI = 1.618033988749894D;
    public static final double PI = java.lang.Math.PI;
    public static final double TO_DEG = 57.29577951308232D;
    public static final double TO_RAD = 0.017453292519943D;
    public static final double SQRT2 = 1.414213562373095D;

    public static double[] SIN_TABLE = new double[65536];

    static
    {
        for (int i = 0; i < 65536; ++i)
        {
            SIN_TABLE[i] = java.lang.Math.sin(i / 65536D * 2 * Math.PI);
        }

        SIN_TABLE[0] = 0;
        SIN_TABLE[16384] = 1;
        SIN_TABLE[32768] = 0;
        SIN_TABLE[49152] = 1;
    }

    public static double sin(double d)
    {
        return SIN_TABLE[(int) ((float) d * 10430.378F) & 65535];
    }

    public static double cos(double d)
    {
        return SIN_TABLE[(int) ((float) d * 10430.378F + 16384.0F) & 65535];
    }

    public static float approachLinear(float a, float b, float max)
    {
        return (a > b) ? (a - b < max ? b : a - max) : (b - a < max ? b : a + max);
    }

    public static double approachLinear(double a, double b, double max)
    {
        return (a > b) ? (a - b < max ? b : a - max) : (b - a < max ? b : a + max);
    }

    public static float interpolate(float a, float b, float d)
    {
        return a + (b - a) * d;
    }

    public static double interpolate(double a, double b, double d)
    {
        return a + (b - a) * d;
    }

    public static double approachExp(double a, double b, double ratio)
    {
        return a + (b - a) * ratio;
    }

    public static double approachExp(double a, double b, double ratio, double cap)
    {
        double d = (b - a) * ratio;
        if (java.lang.Math.abs(d) > cap)
            d = java.lang.Math.signum(d) * cap;
        return a + d;
    }

    public static double retreatExp(double a, double b, double c, double ratio, double kick)
    {
        double d = (java.lang.Math.abs(c - a) + kick) * ratio;
        if (d > java.lang.Math.abs(b - a))
            return b;
        return a + java.lang.Math.signum(b - a) * d;
    }

    public static double clip(double value, double min, double max)
    {
        if (value > max)
            value = max;
        if (value < min)
            value = min;
        return value;
    }

    public static boolean between(double a, double x, double b)
    {
        return a <= x && x <= b;
    }

    public static int approachExpI(int a, int b, double ratio)
    {
        int r = (int) java.lang.Math.round(approachExp(a, b, ratio));
        return r == a ? b : r;
    }

    public static int retreatExpI(int a, int b, int c, double ratio, int kick)
    {
        int r = (int) java.lang.Math.round(retreatExp(a, b, c, ratio, kick));
        return r == a ? b : r;
    }

    public static int floor_double(double d)
    {
        return net.minecraft.util.MathHelper.floor_double(d);
    }

    public static int roundAway(double d)
    {
        return (int) (d < 0 ? java.lang.Math.floor(d) : java.lang.Math.ceil(d));
    }

    public static int compare(int a, int b)
    {
        return a == b ? 0 : a < b ? -1 : 1;
    }

    public static int compare(double a, double b)
    {
        return a == b ? 0 : a < b ? -1 : 1;
    }
}
