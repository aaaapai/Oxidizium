package com.github.tatercertified.oxidizium;

import com.github.tatercertified.oxidizium.test.NativeMathTest;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

// TODO Check if other mods Mixin into the code I change
// If a mod does Mixin into the code, disable my Mixin instead of clashing
public class OxidiziumMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String s) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (Oxidizium.DEBUG_MODE) {
            Oxidizium.TEST_LOGGER.info("Testing Native Math Compatibility");
            Oxidizium.TEST_LOGGER.warn("Oxidizium Mixins will NOT be applied");
            NativeMathTest.testNativeMath();
            boolean mathTest = NativeMathTest.testsFailed;
            if (mathTest) {
                Oxidizium.TEST_LOGGER.info("Native Math Test Failed");
            } else {
                Oxidizium.TEST_LOGGER.info("Native Math Test Passed");
            }
        }
        return !Oxidizium.DEBUG_MODE;
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {
    }

    @Override
    public List<String> getMixins() {
        return Oxidizium.DEBUG_MODE ? List.of() : null;
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
    }
}
