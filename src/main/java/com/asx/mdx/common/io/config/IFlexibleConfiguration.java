package com.asx.mdx.common.io.config;

import java.util.ArrayList;

public interface IFlexibleConfiguration
{
    public ArrayList<ConfigSetting> allSettings();
    
    public void saveSettings();
}
