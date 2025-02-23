package com.github.tatercertified.oxidizium;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import com.github.tatercertified.rust.lib_h;

public class LoadRustDll implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        try (InputStream inputStream = LoadRustDll.class.getResourceAsStream("/oxidizium.dll")) {
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: /oxidizium.dll");
            }
            Path workingDir = getWorkingDir();
            Path destinationPath = workingDir.resolve("oxidizium.dll");
            if (Files.notExists(destinationPath)) {
                Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        testRust();
    }

    private Path getWorkingDir() {
        return Paths.get("").toAbsolutePath();
    }

    private void testRust() {
        try {
            float x = lib_h.sin_float(3.14F);
            System.out.println(STR."Expected: ~0; Result: \{x}");
        } catch (Exception e) {
            System.err.println(STR."""
FAILED TO LOAD RUST LIBRARY:
\{e}""");
        }
    }
}
