package com.arisux.mdxlib;

public class AccessTransformer extends cpw.mods.fml.common.asm.transformers.AccessTransformer
{
    public AccessTransformer() throws Exception
    {
        super("amdxlib_at.cfg");
        MDX.log().info("Loading access transformer: %s", this.getClass().getName());
    }
}
