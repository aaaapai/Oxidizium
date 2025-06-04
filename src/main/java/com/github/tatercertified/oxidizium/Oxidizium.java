package com.github.tatercertified.oxidizium;

import com.github.tatercertified.oxidizium.test.NativeTest;
import com.github.tatercertified.oxidizium.utils.Cleanup;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Oxidizium implements ModInitializer {
    public static final Logger TEST_LOGGER = LoggerFactory.getLogger("Oxidizium Native Test");
    public static final boolean DEBUG_MODE = true;
    @Override
    public void onInitialize() {
        NativeTest.invokeTests();
        Cleanup.cleanupClasses();
    }
}
