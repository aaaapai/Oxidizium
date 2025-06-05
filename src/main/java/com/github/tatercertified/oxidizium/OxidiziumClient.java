package com.github.tatercertified.oxidizium;

import com.github.tatercertified.oxidizium.test.TestingGUI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

import static imgui.app.Application.launch;

public class OxidiziumClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (Config.getInstance().debug()) {
            ClientLifecycleEvents.CLIENT_STARTED.register(_ -> launch(new TestingGUI()));
        }
    }
}
