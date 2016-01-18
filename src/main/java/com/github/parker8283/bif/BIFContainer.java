package com.github.parker8283.bif;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.MetadataCollection;

import com.google.common.eventbus.EventBus;

public class BIFContainer extends DummyModContainer {

    public BIFContainer() {
        super(MetadataCollection.from(MetadataCollection.class.getResourceAsStream("/bifmod.info"), "bowinfinityfix").getMetadataForId("bowinfinityfix", null));
        BIFLoader.log.info("Initialized");
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        return true;
    }
}
