package com.github.parker8283.bif;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.relauncher.FMLInjectionData;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.TransformerExclusions({"com.github.parker8283.bif."})
public class BIFLoader implements IFMLLoadingPlugin {

    public static final Logger log = LogManager.getLogger("BowInfinityFix");
    public static Boolean DEV_ENV = null;

    public BIFLoader() {
        String ver = (String)FMLInjectionData.data()[4];
        if(!ver.startsWith("1.9") && !ver.startsWith("1.10")) {
            throw new RuntimeException("Wrong Minecraft Version for BowInfinityFix; Must be 1.9, 1.9.4, 1.10, or 1.10.2");
        }
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"com.github.parker8283.bif.Transformer"};
    }

    @Override
    public String getModContainerClass() {
        return "com.github.parker8283.bif.BIFContainer";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        DEV_ENV = (Boolean)data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
