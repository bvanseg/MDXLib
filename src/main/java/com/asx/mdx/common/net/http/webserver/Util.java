package com.asx.mdx.common.net.http.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;

public class Util
{
    public static String arrayToJson(String[] data)
    {
        return new Gson().toJson(data);
    }

    public static String toJson(Object o)
    {
        return new Gson().toJson(o);
    }

    public static String getFormattedOutputFromProcess(Process p) throws IOException
    {
        InputStreamReader is = new InputStreamReader(p.getInputStream());
        BufferedReader reader = new BufferedReader(is);
        TypePerfOutputMapping mapping = new TypePerfOutputMapping(reader);
        String json = mapping.toJson();

        if (WebModule.isVerbose())
        {
            System.out.println(json);
        }

        reader.close();

        return json;
    }
}
