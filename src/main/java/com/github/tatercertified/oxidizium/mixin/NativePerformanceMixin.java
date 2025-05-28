package com.github.tatercertified.oxidizium.mixin;

import com.github.tatercertified.rust.lib_h;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;

@Mixin(lib_h.class)
public class NativePerformanceMixin {
    @Redirect(method = "*", at = @At(value = "INVOKE", target = "Ljava/lang/foreign/Linker;downcallHandle(Ljava/lang/foreign/MemorySegment;Ljava/lang/foreign/FunctionDescriptor;)Ljava/lang/invoke/MethodHandle;"))
    private static MethodHandle changeToCritical(Linker instance, MemorySegment memorySegment, FunctionDescriptor functionDescriptor) {
        return instance.downcallHandle(memorySegment, functionDescriptor, Linker.Option.critical(true));
    }
}
