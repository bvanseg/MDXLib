package com.asx.mdx.common.net.http.webserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import com.asx.mdx.internal.Settings;
import com.asx.mdx.common.net.http.webserver.RequestHandler.CommandRequestHandler;
import com.asx.mdx.common.net.http.webserver.RequestHandler.StandardRequestHandler;
import com.google.common.io.ByteStreams;
import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class WebModule implements Runnable
{
    public static Thread                     webServerThread;

    public static final File                 WEB_ROOT             = new File(".");
    public static final String               DEFAULT_FILE         = "index.html";
    public static final String               METHOD_NOT_SUPPORTED = "not_supported.html";

    private static final boolean             verbose              = false;
    private static boolean                   isRunning            = false;

    private Socket                           connection;

    private static ArrayList<RequestHandler> REQUESTS             = new ArrayList<RequestHandler>();

    public WebModule(Socket c)
    {
        connection = c;
        REQUESTS.add(new CommandRequestHandler("/sys/stat/cpu/totalusage", "typeperf -sc 1 \"\\Processor Information(_total)\\% Processor Utility\""));
        REQUESTS.add(new CommandRequestHandler("/sys/stat/cpu/coreusage", "typeperf -sc 1 \"\\Processor Information(*)\\% Processor Utility\""));
        REQUESTS.add(new CommandRequestHandler("/sys/stat/cpu/corefrequency", "typeperf -sc 1 \"\\Processor Information(*)\\Processor Frequency\""));
        REQUESTS.add(new CommandRequestHandler("/sys/stat/memory/total", "typeperf -sc 1 \"\\NUMA Node Memory(*)\\Total MBytes\""));
        REQUESTS.add(new CommandRequestHandler("/sys/stat/memory/available", "typeperf -sc 1 \"\\NUMA Node Memory(*)\\Available MBytes\""));
        REQUESTS.add(new CommandRequestHandler("/sys/stat/disk/usage", "typeperf -sc 1 \"\\PhysicalDisk(*)\\% Disk Time\""));
        REQUESTS.add(new CommandRequestHandler("/sys/stat/disk/readbps", "typeperf -sc 1 \"\\PhysicalDisk(*)\\Disk Read Bytes/sec\""));
        REQUESTS.add(new CommandRequestHandler("/sys/stat/disk/writebps", "typeperf -sc 1 \"\\PhysicalDisk(*)\\Disk Write Bytes/sec\""));
        REQUESTS.add(new CommandRequestHandler("/sys/stat/power/milliwatts", "typeperf -sc 1 \"\\Power Meter(*)\\Power\""));
        REQUESTS.add(new CommandRequestHandler("/sys/stat/gpu/usage", "typeperf -sc 1 \"\\GPU Engine(*)\\Utilization Percentage\""));
        REQUESTS.add(new StandardRequestHandler("/game/world/players/", new RequestHandler.IDataHandler() {
            @Override
            public Object getData()
            {
                MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

                if (server != null && server.getPlayerList() != null)
                {
                    ArrayList<ArrayList<String>> playersList = new ArrayList<ArrayList<String>>();

                    for (GameProfile profile : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOnlinePlayerProfiles())
                    {
                        ArrayList<String> information = new ArrayList<String>();

                        EntityPlayerMP playerMP = server.getPlayerList().getPlayerByUsername(profile.getName());
                        information.add(profile.getName());
                        information.add(profile.getId().toString());
                        information.add(playerMP.getDisplayNameString());
                        information.add(playerMP.getPlayerIP());
                        information.add(String.valueOf(playerMP.getHealth()));
                        information.add(String.valueOf(playerMP.getMaxHealth()));
                        information.add(String.valueOf(playerMP.getServerWorld().provider.getDimensionType().getName()));

                        playersList.add(information);
                    }

                    return Util.toJson(playersList);
                }

                return null;
            }
        }));
    }

    public static ArrayList<RequestHandler> REQUESTS()
    {
        return REQUESTS;
    }

    public static void startWebServer()
    {
        if (Settings.INSTANCE.isWebServerEnabled())
        {
            isRunning = true;
            webServerThread = new Thread() {
                public void run()
                {
                    ServerSocket socket = null;

                    try
                    {
                        int port = Settings.INSTANCE.getWebserverPort();
                        socket = new ServerSocket(port);
                        System.out.println("MDXLib Internal Web Server started.\nListening for connections on port " + port + "...\n");

                        while (isRunning)
                        {
                            WebModule web = new WebModule(socket.accept());
                            Thread thread = new Thread(web);
                            thread.start();
                        }

                    }
                    catch (IOException e)
                    {
                        System.err.println("Server Connection error : " + e.getMessage());
                    }
                    finally
                    {
                        try
                        {
                            if (socket != null)
                            {
                                socket.close();
                            }
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            };
            webServerThread.setDaemon(false);
            webServerThread.start();
        }
    }

    public static void kill()
    {
        WebModule.isRunning = false;
    }

    public static void main(String[] args)
    {
        startWebServer();
    }

    @Override
    public void run()
    {
        BufferedReader in = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;
        String request = null;

        if (verbose)
        {
            System.out.println("Connection opened from " + connection.getInetAddress() + " (" + new Date() + ")");
        }

        try
        {
            InputStream stream = ByteStreams.limit(connection.getInputStream(), 1024 * 256);//Limit the input to 256KB of data
            in = new BufferedReader(new InputStreamReader(stream));
            out = new PrintWriter(connection.getOutputStream());
            dataOut = new BufferedOutputStream(connection.getOutputStream());
            

            String input = in.readLine();
            StringTokenizer parse = new StringTokenizer(input);
            String method = parse.nextToken().toUpperCase();
            request = parse.nextToken().toLowerCase();

            for (RequestHandler handler : REQUESTS)
            {
                if (request.equalsIgnoreCase(handler.getRequest()))
                {
                    handler.handleRequest(out, dataOut);
                    return;
                }
            }

            if (!method.equals("GET") && !method.equals("HEAD"))
            {
                handle501(out, dataOut, request, method);
            }
            else
            {
                String o = "MDX Integrated Web Server";

                WebModule.buildGenericHeader(out, dataOut, o.length());
                WebModule.sendData(out, dataOut, o.getBytes(), o.length());
            }
            
            /**
            else
            {
                if (request.endsWith("/"))
                {
                    request += DEFAULT_FILE;
                }

                if (method.equals("GET"))
                {
                    File file = new File(WEB_ROOT, request);
                    int fileLength = (int) file.length();
                    byte[] fileData = readFileData(file, fileLength);

                    buildGenericHeader(out, dataOut, fileLength);
                    sendData(out, dataOut, fileData, fileLength);
                }

                if (verbose)
                {
                    String content = getContentType(request);
                    System.out.println("File " + request + " of type " + content + " returned");
                }
            }
            **/
        }
        catch (FileNotFoundException fnfe)
        {
            try
            {
                handle404(out, dataOut, request);
            }
            catch (IOException ioe)
            {
                System.err.println("Error handling 404: " + ioe.getMessage());
            }
        }
        catch (IOException ioe)
        {
            System.err.println("Server error: " + ioe);
        }
        finally
        {
            try
            {
                in.close();
                out.close();
                dataOut.close();
                connection.close();
            }
            catch (Exception e)
            {
                System.err.println("Error closing stream: " + e.getMessage());
            }

            if (verbose)
            {
                System.out.println("Connection closed.\n");
            }
        }
    }

    private byte[] readFileData(File file, int fileLength) throws IOException
    {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try
        {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        }
        finally
        {
            if (fileIn != null)
            {
                fileIn.close();
            }
        }

        return fileData;
    }

    private String getContentType(String fileRequested)
    {
        if (fileRequested.endsWith(".htm") || fileRequested.endsWith(".html"))
            return "text/html";
        else
            return "text/plain";
    }

    private void handle404(PrintWriter out, OutputStream dataOut, String request) throws IOException
    {
        String text = "404/NOT FOUND";
        int length = text.length();
        byte[] data = text.getBytes();

        out.println("HTTP/1.1 404 File Not Found");
        out.println("Server: MDXLib HTTP Server 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + "text/html");
        out.println("Content-length: " + length);
        out.println();
        out.flush();

        sendData(out, dataOut, data, length);
        System.out.println("404 " + request + " not found");
    }

    private void handle501(PrintWriter out, BufferedOutputStream dataOut, String request, String method) throws IOException
    {
        if (verbose)
        {
            System.out.println("501/NOT IMPLEMENTED: " + method + " method.");
        }

        String text = "501/NOT_IMPLEMENTED: " + method;
        int fileLength = (int) text.length();
        String contentMimeType = "text/html";
        byte[] fileData = text.getBytes();

        out.println("HTTP/1.1 501 Not Implemented");
        out.println("Server: MDXLib HTTP Server 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + contentMimeType);
        out.println("Content-length: " + fileLength);
        out.println();
        out.flush();
        sendData(out, dataOut, fileData, fileLength);
    }

    public static void buildGenericHeader(PrintWriter out, OutputStream dataOut, int dataLength) throws IOException
    {
        out.println("HTTP/1.1 200 OK");
        out.println("Server: MDXLib HTTP Server 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + "text/html");
        out.println("Content-length: " + dataLength);
        out.println();
        out.flush();
    }

    public static void sendData(PrintWriter out, OutputStream dataOut, byte[] dataBytes, int dataLength) throws IOException
    {
        dataOut.write(dataBytes, 0, dataLength);
        dataOut.flush();
    }

    public static boolean isVerbose()
    {
        return verbose;
    }
}