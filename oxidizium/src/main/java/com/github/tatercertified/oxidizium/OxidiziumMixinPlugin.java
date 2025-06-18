package com.github.tatercertified.oxidizium;

// import com.github.tatercertified.mixin_config.MCMixinConfigPlugin;
// import com.github.tatercertified.mixin_config.MixinConfig;
// import com.github.tatercertified.mixin_config.validation.Validator;
import com.github.tatercertified.oxidizium.utils.asm.StripProcessor;
import com.moulberry.mixinconstraints.MixinConstraints;
import com.moulberry.mixinconstraints.mixin.MixinConstraintsBootstrap;
// import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class OxidiziumMixinPlugin implements IMixinConfigPlugin {
    private String mixinPackage;

    @Override
    public void onLoad(String s) {
        Config.init();
        /*
        MixinConfig.init(
                FabricLoader.getInstance().getConfigDir().resolve("oxidizium-mixins.txt"),
                0,
                Oxidizium.class
        );

         */
        this.mixinPackage = s;
        MixinConstraintsBootstrap.init(mixinPackage);
        // Validator.preprocess();
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (this.mixinPackage != null && !mixinClassName.startsWith(this.mixinPackage)) {
            return true;
        }
        return MixinConstraints.shouldApplyMixin(mixinClassName); // && MCMixinConfigPlugin.processMixinConfig(mixinClassName);
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {
    }

    @Override
    public List<String> getMixins() {
        return Config.getInstance().test() ? List.of() : null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode classNode, String mixinClassName, IMixinInfo iMixinInfo) {
        StripProcessor.processStrips(targetClassName, classNode);
    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
    }
}
