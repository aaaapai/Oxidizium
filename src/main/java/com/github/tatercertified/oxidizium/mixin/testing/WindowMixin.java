package com.github.tatercertified.oxidizium.mixin.testing;

import imgui.app.Window;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Window.class, remap = false)
public class WindowMixin {
    @Inject(method = "decideGlGlslVersions", at = @At(value = "FIELD", target = "Limgui/app/Window;glslVersion:Ljava/lang/String;", ordinal = 1), cancellable = true)
    private void setCorrectGLVersions(CallbackInfo ci) {
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        ci.cancel();
    }
}
