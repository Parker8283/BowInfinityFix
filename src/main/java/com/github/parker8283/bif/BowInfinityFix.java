package com.github.parker8283.bif;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.parker8283.bif.asm.RemappingHelper;
import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions({"com.github.parker8283.bif."})
public class BowInfinityFix extends DummyModContainer implements IFMLLoadingPlugin {

    public static final Logger log = LogManager.getLogger("BowInfinityFix");

    public BowInfinityFix() {
        super(MetadataCollection.from(MetadataCollection.class.getResourceAsStream("/bifmod.info"), "bowinfinityfix").getMetadataForId("bowinfinityfix", null));
        log.info("Initialized");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{Transformer.class.getName()};
    }

    @Override
    public String getModContainerClass() {
        return BowInfinityFix.class.getName();
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        RemappingHelper.obfuscated = (Boolean)data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        return true;
    }
}
