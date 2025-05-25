package com.github.tatercertified.oxidizium;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

public class LoadRustBinary implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        String osName = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        String arch = System.getProperty("os.arch").toLowerCase(Locale.ENGLISH);

        String binaryName;

        if (SystemUtils.IS_OS_WINDOWS) {
            binaryName = switch (arch) {
                case "amd64", "x86_64" -> "oxidizium_windows_x64.dll";
                case "arm", "aarch64" -> "oxidizium_windows_arm64.dll";
                default -> throw new IllegalStateException("Unsupported architecture: " + arch);
            };
        } else if (SystemUtils.IS_OS_LINUX) {
            binaryName = switch (arch) {
                case "amd64", "x86_64" -> "liboxidizium_linux_x64.so";
                case "arm64", "aarch64" -> "liboxidizium_linux_arm64.so";
                default -> throw new IllegalStateException("Unsupported architecture: " + arch);
            };
        } else if (SystemUtils.IS_OS_MAC) {
            binaryName = switch (arch) {
                case "x86_64" -> "liboxidizium_mac_x64.dylib";
                case "arm64", "aarch64" -> "liboxidizium_mac_arm64.dylib";
                default -> throw new IllegalStateException("Unsupported architecture: " + arch);
            };
        } else {
            throw new IllegalStateException("Unsupported OS: " + osName);
        }

        try (InputStream inputStream = LoadRustBinary.class.getResourceAsStream("/" + binaryName)) {
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: /" + binaryName);
            }
            Path workingDir = getWorkingDir();
            int lastDotIndex = binaryName.lastIndexOf('.');
            String extension = binaryName.substring(lastDotIndex + 1);
            Path destinationPath = workingDir.resolve("oxidizium." + extension);
            if (Files.notExists(destinationPath)) {
                Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path getWorkingDir() {
        return Paths.get("").toAbsolutePath();
    }
}
