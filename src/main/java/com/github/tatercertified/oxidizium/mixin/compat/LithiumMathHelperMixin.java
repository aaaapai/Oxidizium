package com.github.tatercertified.oxidizium.mixin.compat;

import com.github.tatercertified.rust.lib_h;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@IfModLoaded("lithium")
@Mixin(MathHelper.class)
public class LithiumMathHelperMixin {
    /**
     * @author QPCrummer
     * @reason Implement in Rust with Lithium compat
     */
    @Overwrite
    public static float sin(float value) {
        return lib_h.lithium_sin_float(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust with Lithium compat
     */
    @Overwrite
    public static float cos(float value) {
        return lib_h.lithium_cos_float(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust with Lithium compat
     */
    @Overwrite
    public static float easeInOutSine(float value) {
        return lib_h.lithium_ease_in_out_sine(value);
    }
}
