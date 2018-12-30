package com.arisux.mdx.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;

public class Util
{
    public static String[] parseTypeperfDataIntoArray(String data)
    {
        return data.split(",");
    }

    public static String arrayToJson(String[] data)
    {
        return new Gson().toJson(data);
    }
    
    public static String toJson(Object o)
    {
        return new Gson().toJson(o);
    }

    public static String stripTypeperf(BufferedReader out) throws IOException
    {
        String output = "";
        String line = "";
        int i = 0;

        while ((line = out.readLine()) != null)
        {
            if (i == 2)
            {
                output = output + line;
            }
            i++;
        }

        return output;
    }

    public static String prepSingleValueDataTypeperf(BufferedReader reader) throws IOException
    {
        String text = stripTypeperf(reader);
        text = text.substring(text.indexOf(",") + 1, text.length());
        text = text.replaceAll("\"", "");

        return text;
    }

    public static String getSingleValueFromProcessOutput(Process p) throws IOException
    {
        InputStreamReader is = new InputStreamReader(p.getInputStream());
        BufferedReader reader = new BufferedReader(is);
        String text = prepSingleValueDataTypeperf(reader);

        if (WebModule.isVerbose())
        {
            System.out.println(text);
        }

        reader.close();

        return text;
    }
}
