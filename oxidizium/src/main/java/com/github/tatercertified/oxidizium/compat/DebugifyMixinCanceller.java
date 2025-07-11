package com.github.tatercertified.oxidizium.compat;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import com.github.tatercertified.oxidizium.Oxidizium;

import java.util.List;

public class DebugifyMixinCanceller implements MixinCanceller {
    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        if ("dev.isxander.debugify.mixins.basic.mc199467.MthMixin".equals(mixinClassName)) {
            Oxidizium.LOGGER.warn("Disabling Debugify's mc199467 patch; Rust fixes it");
            return true;
        }
        return false;
    }
}
