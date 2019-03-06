package com.asx.mdx.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.lwjgl.opengl.GL11;

import com.asx.mdx.MDX;
import com.asx.mdx.lib.client.gui.GuiCustomButton;
import com.asx.mdx.lib.client.gui.GuiCustomScreen;
import com.asx.mdx.lib.client.gui.IAction;
import com.asx.mdx.lib.client.gui.IGuiElement;
import com.asx.mdx.lib.client.gui.windows.Window;
import com.asx.mdx.lib.client.model.loaders.TabulaModelLoader;
import com.asx.mdx.lib.client.model.loaders.tabula.container.TabulaCubeContainer;
import com.asx.mdx.lib.client.model.loaders.tabula.container.TabulaModelContainer;
import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.client.util.OpenGL;
import com.asx.mdx.lib.client.util.ScaledResolution;
import com.asx.mdx.lib.client.util.Screen;
import com.asx.mdx.lib.util.Chat;
import com.asx.mdx.lib.util.Game;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CommandKeyframeDifferenceCalculator extends CommandBase
{
    @Override
    public String getName()
    {
        return "keyframecalculator";
    }

    @Override
    public String getUsage(ICommandSender commandSender)
    {
        return "Opens an interface for calculating keyframe animation differences between models.";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            try
            {
                GuiCustomScreen screen = new GuiCustomScreen() {

                    private File                 fileModelA        = null;
                    private File                 fileModelB        = null;
                    private File                 fileModelO        = null;

                    private TabulaModelContainer modelA            = null;
                    private TabulaModelContainer modelB            = null;
                    private TabulaModelContainer modelO            = null;

                    private boolean              verifiedStructure = false;

                    private int                  colorModelA       = 0x22FFFFFF;
                    private int                  colorModelB       = 0x22FFFFFF;

                    protected String             error             = "";

                    private GuiCustomButton      browseModelA      = (GuiCustomButton) new GuiCustomButton(0, 0, 0, 0, 0, "Browse").setAction(new IAction() {
                                                                       @Override
                                                                       public void perform(IGuiElement element)
                                                                       {
                                                                           final JFileChooser fc = new JFileChooser();

                                                                           int returnVal = fc.showOpenDialog(null);

                                                                           if (returnVal == JFileChooser.APPROVE_OPTION)
                                                                           {
                                                                               fileModelA = fc.getSelectedFile();
                                                                               reset();
                                                                           }
                                                                       }
                                                                   });
                    private GuiCustomButton      browseModelB      = (GuiCustomButton) new GuiCustomButton(1, 0, 0, 0, 0, "Browse").setAction(new IAction() {
                                                                       @Override
                                                                       public void perform(IGuiElement element)
                                                                       {
                                                                           final JFileChooser fc = new JFileChooser();

                                                                           int returnVal = fc.showOpenDialog(null);

                                                                           if (returnVal == JFileChooser.APPROVE_OPTION)
                                                                           {
                                                                               fileModelB = fc.getSelectedFile();
                                                                               reset();
                                                                           }
                                                                       }
                                                                   });
                    {
                        browseModelA.baseColor = 0x22FFFFFF;
                        browseModelA.overlayColorHover = 0x22FFFFFF;
                        browseModelA.width = 50;
                        browseModelA.height = 19;
                        browseModelA.x = 5;
                        browseModelA.y = 4;

                        browseModelB.baseColor = 0x22FFFFFF;
                        browseModelB.overlayColorHover = 0x22FFFFFF;
                        browseModelB.height = 19;
                        browseModelB.width = 50;
                        browseModelB.x = 3;
                        browseModelB.y = 4;
                    }

                    public void reset()
                    {
                        verifiedStructure = false;
                        modelA = null;
                        modelB = null;
                        error = "";
                    }

                    @Override
                    public void drawScreen(int mouseX, int mouseY, float partialTicks)
                    {
                        ScaledResolution res = Screen.scaledDisplayResolution();
                        OpenGL.enableBlend();
                        Draw.drawRect(0, 0, res.getScaledWidth(), res.getScaledHeight(), 0xEE000000);
                        super.drawScreen(mouseX, mouseY, partialTicks);

                        Draw.drawRect(5, 4, res.getScaledWidth() / 2 - 10, 62, 0x22FFFFFF);

                        int modelADataX = 5;
                        int modelADataY = 67;
                        int modelADataW = res.getScaledWidth() / 2 - 10;
                        int modelADataH = res.getScaledHeight();
                        Draw.drawRect(modelADataX, modelADataY, modelADataW, modelADataH, colorModelA);

                        Draw.drawString((fileModelA != null ? fileModelA.getAbsolutePath() : "No File Selected"), 60, 10, 0xFFFFFFFF, false);
                        browseModelA.drawButton(mc, mouseX, mouseY, partialTicks);

                        if (modelA != null)
                        {
                            Draw.drawString(String.format("%s modeled by %s", modelA.getName(), modelA.getAuthor()), 10, 25, 0x88FFFFFF, false);
                            Draw.drawString(String.format("ProjectVersion: %s", modelA.getProjectVersion()), 10, 35, 0x88FFFFFF, false);
                            Draw.drawString(String.format("%s Cubes", modelA.getAllCubes().size()), 10, 45, 0x88FFFFFF, false);
                            Draw.drawString(String.format("TextureMap(%s, %s)", modelA.getTextureWidth(), modelA.getTextureHeight()), 10, 55, 0x88FFFFFF, false);

                            float s = 0.5F;

                            OpenGL.pushMatrix();
                            OpenGL.scale(s, s, s);
                            drawCubeTree(10 * 2, 65 * 2, modelA, null, 0, 0, (modelADataW - 5) * 2, (modelADataH - 5) * 2);
                            OpenGL.popMatrix();
                        }
                        else
                        {
                            Draw.drawString("No model data has been loaded.", 10, 75, 0xFFFFFFFF, false);
                        }

                        Draw.drawRect(res.getScaledWidth() / 2 - 4, 4, res.getScaledWidth() / 2, 62, 0x22FFFFFF);

                        int modelBDataX = res.getScaledWidth() / 2 - 4;
                        int modelBDataY = 67;
                        int modelBDataW = res.getScaledWidth() / 2;
                        int modelBDataH = res.getScaledHeight();
                        Draw.drawRect(modelBDataX, modelBDataY, modelBDataW, modelBDataH, colorModelB);

                        Draw.drawString((fileModelB != null ? fileModelB.getAbsolutePath() : "No File Selected"), res.getScaledWidth() / 2 + 50, 10, 0xFFFFFFFF, false);
                        browseModelB.drawButton(mc, mouseX, mouseY, partialTicks);
                        browseModelB.x = res.getScaledWidth() / 2 - 4;

                        if (modelB != null)
                        {
                            Draw.drawString(String.format("%s modeled by %s", modelB.getName(), modelB.getAuthor()), res.getScaledWidth() / 2, 25, 0x88FFFFFF, false);
                            Draw.drawString(String.format("ProjectVersion: %s", modelB.getProjectVersion()), res.getScaledWidth() / 2, 35, 0x88FFFFFF, false);
                            Draw.drawString(String.format("%s Cubes", modelB.getAllCubes().size()), res.getScaledWidth() / 2, 45, 0x88FFFFFF, false);
                            Draw.drawString(String.format("TextureMap(%s, %s)", modelB.getTextureWidth(), modelB.getTextureHeight()), res.getScaledWidth() / 2, 55, 0x88FFFFFF, false);

                            float s = 0.5F;

                            OpenGL.pushMatrix();
                            OpenGL.scale(s, s, s);
                            drawCubeTree((res.getScaledWidth() / 2) * 2, 65 * 2, modelB, null, 0, 0, (modelBDataW - 5) * 2, (modelBDataH - 5) * 2);
                            OpenGL.popMatrix();
                        }
                        else
                        {
                            Draw.drawString("No model data has been loaded.", res.getScaledWidth() / 2, 75, 0xFFFFFFFF, false);
                        }
                    }

                    public boolean isStructureSimilarTo(TabulaModelContainer modelA, TabulaModelContainer modelB)
                    {
                        int sizeA = modelA.getAllCubes().size();
                        int sizeB = modelB.getAllCubes().size();

                        if (sizeA != sizeB)
                        {
                            error = String.format("Cube count does not match. %s vs %s", sizeA, sizeB);
                            return false;
                        }

                        /** Compare this model's structure to the second model's **/
                        for (TabulaCubeContainer c : modelA.getCubes())
                        {
                            if (modelB.getCubeByName(c.getName()) == null)
                            {
                                error = String.format("Model mismatch. Model B does not contain cube.\n\nName: \"" + Chat.Chars.SECTION_SIGN + "7" + "%s" + Chat.Chars.SECTION_SIGN + "f" + "\"\nId: \"" + Chat.Chars.SECTION_SIGN + "7" + "%s" + Chat.Chars.SECTION_SIGN + "F" + "\"", c.getName(), c.getIdentifier());
                                return false;
                            }
                        }

                        /** Compare the second model's structure to this model's **/
                        for (TabulaCubeContainer c : modelB.getCubes())
                        {
                            if (modelA.getCubeByName(c.getName()) == null)
                            {
                                error = String.format("Model mismatch. Model A does not contain cube.\n\nName: \"" + Chat.Chars.SECTION_SIGN + "7" + "%s" + Chat.Chars.SECTION_SIGN + "F" + "\"\nId: \"" + Chat.Chars.SECTION_SIGN + "7" + "%s" + Chat.Chars.SECTION_SIGN + "F" + "\"", c.getName(), c.getIdentifier());
                                return false;
                            }
                        }

                        return true;
                    }

                    public int drawCubeTree(int x, int y, TabulaModelContainer model, TabulaCubeContainer parent, int idx, int level)
                    {
                        return drawCubeTree(x, y, model, parent, idx, level, 0, 0);
                    }

                    public int drawCubeTree(int x, int y, TabulaModelContainer model, TabulaCubeContainer parent, int idx, int level, int width, int height)
                    {
                        for (TabulaCubeContainer p : (parent == null ? model.getCubes() : parent.getChildren()))
                        {
                            idx += 1;

                            if (parent != null)
                            {
                                level++;
                            }

                            String prefix = "";

                            for (int i = 0; i < level; i++)
                            {
                                prefix = "-" + prefix;
                            }

                            int stringY = y + (idx * 5) * 2;

                            if (stringY > y && stringY < y + height || height == 0)
                            {
                                String line = (level < 10 ? "0" : "") + level + " " + (prefix) + p.getName();
                                line = line + String.format(" :  " + Chat.Chars.SECTION_SIGN + "3" + "Position(%s, %s, %s)", p.getPosition()[0], p.getPosition()[1], p.getPosition()[2]);
                                line = line + String.format(Chat.Chars.SECTION_SIGN + "B" + " Offset(%s, %s, %s)", p.getOffset()[0], p.getOffset()[1], p.getOffset()[2]);
                                line = line + String.format(Chat.Chars.SECTION_SIGN + "9" + " Dimensions(%s, %s, %s)", p.getDimensions()[0], p.getDimensions()[1], p.getDimensions()[2]);
                                line = line + String.format(Chat.Chars.SECTION_SIGN + "D" + " Rotation(%s, %s, %s)", p.getRotation()[0], p.getRotation()[1], p.getRotation()[2]);

                                if (width > 0)
                                {
                                    int w = Draw.sizeStringToWidth(line, width, false);
                                    line = line.substring(0, w);
                                }

                                Draw.drawString(line, x, stringY, 0x88FFFFFF, false);
                            }

                            if (p.getChildren().size() > 0)
                            {
                                idx = drawCubeTree(x, y, model, p, idx, level, width, height);
                            }
                        }

                        return idx;
                    }

                    @Override
                    public void updateScreen()
                    {
                        super.updateScreen();

                        try
                        {
                            if (fileModelA != null && modelA == null)
                            {
                                modelA = TabulaModelLoader.INSTANCE.loadTabulaModel(fileModelA);
                                modelO = TabulaModelLoader.INSTANCE.loadTabulaModel(fileModelA);
                                browseModelA.baseColor = modelA != null ? 0xAA00AA00 : 0xAAAA0000;
                            }

                            if (fileModelB != null && modelB == null)
                            {
                                modelB = TabulaModelLoader.INSTANCE.loadTabulaModel(fileModelB);
                                browseModelB.baseColor = modelB != null ? 0xAA00AA00 : 0xAAAA0000;
                            }

                            if (modelA != null && modelB != null)
                            {
                                if (!verifiedStructure)
                                {
                                    if (this.isStructureSimilarTo(modelA, modelB))
                                    {
                                        colorModelA = 0x22FFFFFF;
                                        colorModelB = 0x22FFFFFF;

                                        TabulaModelContainer result = calculateOffsets(modelA, modelB, modelO);

                                        MDX.windows().showWindowManager();
                                        MDX.windows().getWindows().clear();
                                        MDX.windows().getWindowManager().setParentScreen(this);

                                        if (error.isEmpty())
                                        {
                                            Window window = new Window("kfcalc_results", MDX.windows().getWindowManager(), "Results Preview", 100, 100, 300, 200) {

                                                @Override
                                                public void onButtonPress(GuiButton paramGuiButton)
                                                {
                                                    ;
                                                }

                                                @Override
                                                public void keyTyped(char paramChar, int paramInt)
                                                {
                                                    ;
                                                }

                                                @Override
                                                public void drawWindowContents()
                                                {
                                                    int border = 1;
                                                    int x = this.getX() + border;
                                                    int y = this.getY() + border;
                                                    int w = this.getWidth() - border * 2;
                                                    int h = this.getHeight() - border * 2;
                                                    Draw.drawRect(x, y, w, h, 0xEE000000);

                                                    float s = 0.5F;
                                                    OpenGL.pushMatrix();
                                                    OpenGL.scale(s, s, s);
                                                    OpenGL.disable(GL11.GL_SCISSOR_TEST);
                                                    GL11.glScissor(x, y, w, h);
                                                    drawCubeTree((this.getX() + 5) * 2, (this.getY()) * 2, result, null, 0, 0, (this.getWidth() - 5) * 2, (this.getHeight() - 5) * 2);
                                                    OpenGL.popMatrix();
                                                }
                                            };

                                            GuiCustomButton buttonExport = new GuiCustomButton(0, 0, 0, 0, 0, "Export");
                                            GuiCustomButton buttonExportBrowse = new GuiCustomButton(0, 0, 0, 0, 0, "Browse");
                                            Window exportWindow = new Window("kfcalc_result_export", MDX.windows().getWindowManager(), "Differential Result Exporter", 150, 150, 200, 90) {

                                                @Override
                                                public void onButtonPress(GuiButton paramGuiButton)
                                                {
                                                    ;
                                                }

                                                @Override
                                                public void keyTyped(char paramChar, int paramInt)
                                                {
                                                    ;
                                                }

                                                @Override
                                                public void drawWindowContents()
                                                {
                                                    buttonExport.baseColor = 0x11000000;
                                                    buttonExport.overlayColorHover = 0x33000000;
                                                    buttonExport.fontShadow = false;
                                                    buttonExport.width = this.getWidth();
                                                    buttonExport.height = 20;
                                                    buttonExport.x = this.getX();
                                                    buttonExport.y = this.getY() + this.getHeight() - buttonExport.height;
                                                    buttonExport.drawButton();
                                                    buttonExport.setAction(new IAction() {
                                                        @Override
                                                        public void perform(IGuiElement element)
                                                        {
                                                            DifferentialExporter exporter = new DifferentialExporter(modelO, fileModelO);
                                                            exporter.export();
                                                        }
                                                    });

                                                    buttonExportBrowse.baseColor = 0x11000000;
                                                    buttonExportBrowse.overlayColorHover = 0x33000000;
                                                    buttonExportBrowse.fontShadow = false;
                                                    buttonExportBrowse.width = 50;
                                                    buttonExportBrowse.height = 20;
                                                    buttonExportBrowse.x = this.getX();
                                                    buttonExportBrowse.y = this.getY() + this.getHeight() - buttonExport.height * 2 - 1;
                                                    buttonExportBrowse.drawButton();
                                                    buttonExportBrowse.setAction(new IAction() {
                                                        @Override
                                                        public void perform(IGuiElement element)
                                                        {
                                                            final JFileChooser fc = new JFileChooser();

                                                            int returnVal = fc.showOpenDialog(null);

                                                            if (returnVal == JFileChooser.APPROVE_OPTION)
                                                            {
                                                                fileModelO = fc.getSelectedFile();
                                                            }
                                                        }
                                                    });

                                                    Draw.drawRect(buttonExportBrowse.x() + buttonExportBrowse.width(), buttonExportBrowse.y(), this.getWidth() - buttonExportBrowse.width(), buttonExportBrowse.height(), 0x22000000);

                                                    OpenGL.pushMatrix();
                                                    {
                                                        float s = 0.5F;
                                                        OpenGL.scale(s, s, s);

                                                        String info = "This tool exports the differential offsets calculated between two models, to Java source code, for use with MDX's integrated keyframe animator. Default frame delays will be set. Additional tweaking will be needed post-export. Export format will be an incomplete Java source code snippet to be used in the animate() function of the Model class.\n\nFor additional help using this tool, please reference a general keyframe animation tutorial, and MDX's source code, which is available on GitHub as an Open Source project. Link: https://github.com/Ri5ux/MDXLib";
                                                        Game.minecraft().fontRenderer.drawSplitString(info, (this.getX() + 3) * 2, (this.getY() + 5) * 2, (this.getWidth() - 3) * 2, 0xAA000000);
                                                        Draw.drawString(fileModelO == null ? "No File Path Provided" : fileModelO.getAbsolutePath(), (buttonExportBrowse.x() + buttonExportBrowse.width() + 5) * 2, (buttonExportBrowse.y() + (buttonExportBrowse.height() / 2 - 7) + 5) * 2, 0x88000000, false);
                                                    }
                                                    OpenGL.popMatrix();
                                                }
                                            };
                                        }
                                    }
                                    else
                                    {
                                        colorModelA = 0x55FF0000;
                                        colorModelB = 0x55FF0000;

                                        Window errorWindow = new Window("kfcalc_error", MDX.windows().getWindowManager(), "ERROR: Model Structure Mismatch", 150, 150, 200, 90) {

                                            @Override
                                            public void onButtonPress(GuiButton paramGuiButton)
                                            {
                                                ;
                                            }

                                            @Override
                                            public void keyTyped(char paramChar, int paramInt)
                                            {
                                                ;
                                            }

                                            @Override
                                            public void drawWindowContents()
                                            {
                                                Game.minecraft().fontRenderer.drawSplitString(error, (this.getX() + 5) * 1, (this.getY() + 5) * 1, this.getWidth() * 1, 0xFFFFFFFF);
                                            }
                                        };

                                        MDX.windows().showWindowManager();
                                        MDX.windows().getWindowManager().setParentScreen(this);
                                    }

                                    verifiedStructure = true;
                                }
                            }
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void keyTyped(char typedChar, int keyCode) throws IOException
                    {
                        super.keyTyped(typedChar, keyCode);
                    }

                    @Override
                    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
                    {
                        super.mouseClicked(mouseX, mouseY, mouseButton);
                    }
                };

                FMLCommonHandler.instance().showGuiScreen(screen);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    public static TabulaModelContainer calculateOffsets(TabulaModelContainer a, TabulaModelContainer b, TabulaModelContainer o)
    {
        for (TabulaCubeContainer aCube : a.getAllCubes())
        {
            TabulaCubeContainer bCube = b.getCubeByName(aCube.getName());
            TabulaCubeContainer oCube = o.getCubeByName(aCube.getName());

            double[] offsetsA = aCube.getOffset();
            double[] offsetsB = bCube.getOffset();
            double[] offsetsO = new double[offsetsA.length];

            for (int i = offsetsA.length - 1; i >= 0; i--)
            {
                double da = offsetsA[i];
                double db = offsetsB[i];
                offsetsO[i] = db - da;
            }

            oCube.setOffset(offsetsO);

            double[] rotationA = aCube.getRotation();
            double[] rotationB = bCube.getRotation();
            double[] rotationO = new double[rotationA.length];

            for (int i = rotationA.length - 1; i >= 0; i--)
            {
                double da = rotationA[i];
                double db = rotationB[i];
                rotationO[i] = db - da;
            }

            oCube.setRotation(rotationO);

            double[] positionA = aCube.getPosition();
            double[] positionB = bCube.getPosition();
            double[] positionO = new double[positionA.length];

            for (int i = positionA.length - 1; i >= 0; i--)
            {
                double da = positionA[i];
                double db = positionB[i];
                positionO[i] = db - da;
                System.out.println(oCube.getName() + " " + da + " " + db);
            }

            oCube.setPosition(positionO);
        }

        return o;
    }

    public static class DifferentialExporter
    {
        private File                 exportTo;
        private TabulaModelContainer result;

        public DifferentialExporter(TabulaModelContainer result, File exportTo)
        {
            this.exportTo = exportTo;
            this.result = result;
        }

        public String generateRotationSyntax(TabulaCubeContainer cube, double x, double y, double z)
        {
            return String.format("animator.rotateTo(%s, %sF, %sF, %sF);", cube.getName(), x, y, z);
        }

        public String generateTranslateSyntax(TabulaCubeContainer cube, double x, double y, double z)
        {
            return String.format("animator.moveTo(%s, %sF, %sF, %sF);", cube.getName(), x, y, z);
        }

        protected ArrayList<String> generateKeyframeInstructions()
        {
            ArrayList<String> instructions = new ArrayList<String>();

            for (TabulaCubeContainer cube : result.getAllCubes())
            {
                double[] offsets = cube.getOffset();
                double[] rotations = cube.getRotation();

                if (Math.abs(offsets[0]) > 0 || Math.abs(offsets[1]) > 0D || Math.abs(offsets[2]) > 0D)
                {
                    String i = generateTranslateSyntax(cube, offsets[0], offsets[1], offsets[2]);
                    instructions.add(i);
                }

                if (Math.abs(rotations[0]) > 0D || Math.abs(rotations[1]) > 0D || Math.abs(rotations[2]) > 0D)
                {
                    String i = generateRotationSyntax(cube, rotations[0], rotations[1], rotations[2]);
                    instructions.add(i);
                }
            }
            return instructions;
        }

        public void export()
        {
            try
            {
                if (!exportTo.exists())
                {
                    exportTo.createNewFile();
                }

                if (exportTo.exists())
                {
                    FileOutputStream stream = new FileOutputStream(exportTo);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));

                    for (String line : generateKeyframeInstructions())
                    {
                        writer.write(line);
                        writer.newLine();
                    }

                    writer.close();

                    Window exportComplete = new Window("kfcalc_export_complete", MDX.windows().getWindowManager(), "Export Complete!", 150, 150, 200, 90) {

                        @Override
                        public void onButtonPress(GuiButton paramGuiButton)
                        {
                            ;
                        }

                        @Override
                        public void keyTyped(char paramChar, int paramInt)
                        {
                            ;
                        }

                        @Override
                        public void drawWindowContents()
                        {
                            Draw.drawStringAlignCenter("Export completed successfully!", this.getX() + (this.getWidth() / 2), this.getY() + (this.getHeight() / 2) - 10, 0xFFFFFFFF);
                        }
                    };
                }
            }
            catch (Exception e)
            {
                MDX.log().warn("An error was encountered trying to export the resulting differential model offsets.");
                e.printStackTrace();
            }
        }
    }
}
