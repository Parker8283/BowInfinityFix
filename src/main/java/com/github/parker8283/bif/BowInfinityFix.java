package com.github.parker8283.bif;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.parker8283.bif.asm.RemappingHelper;
import com.google.common.eventbus.EventBus;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.MetadataCollection;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.8")
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
