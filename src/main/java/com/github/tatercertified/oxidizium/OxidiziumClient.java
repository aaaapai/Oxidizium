package com.github.tatercertified.oxidizium;

import com.github.tatercertified.oxidizium.test.TestingGUI;
import net.fabricmc.api.ClientModInitializer;

import static imgui.app.Application.launch;

public class OxidiziumClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (Config.getInstance().test()) {
            launch(new TestingGUI());
        }
    }
}
