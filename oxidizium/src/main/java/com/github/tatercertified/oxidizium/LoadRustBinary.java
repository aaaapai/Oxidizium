package com.github.tatercertified.oxidizium;

import com.github.tatercertified.oxidizium.utils.HashUtils;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Locale;
import java.util.Set;

public class LoadRustBinary implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        String osName = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        String arch = System.getProperty("os.arch").toLowerCase(Locale.ENGLISH);

        String binaryName;
        String outputName;

        if (SystemUtils.IS_OS_WINDOWS) {
            binaryName = switch (arch) {
                case "amd64", "x86_64" -> "oxidizium_windows_x86.dll";
                case "arm", "aarch64" -> "oxidizium_windows_arm64.dll";
                default -> throw new IllegalStateException("Unsupported architecture: " + arch);
            };
            outputName = "oxidizium";
        } else if (SystemUtils.IS_OS_LINUX || osName.contains("Android")) {
            binaryName = switch (arch) {
                case "amd64", "x86_64" -> "liboxidizium_linux_x86.so";
                case "arm64", "aarch64" -> "liboxidizium_linux_arm64.so";
                default -> throw new IllegalStateException("Unsupported architecture: " + arch);
            };
            outputName = "liboxidizium";
        } else if (SystemUtils.IS_OS_MAC) {
            binaryName = switch (arch) {
                case "x86_64" -> "liboxidizium_mac_x86.dylib";
                case "arm64", "aarch64" -> "liboxidizium_mac_arm64.dylib";
                default -> throw new IllegalStateException("Unsupported architecture: " + arch);
            };
            outputName = "liboxidizium";
        } else {
            throw new IllegalStateException("Unsupported OS: " + osName);
        }

        String binaryNameNoExtension = FilenameUtils.removeExtension(binaryName);
        copyNativeLib(binaryName, outputName, binaryNameNoExtension);
    }

    private static void copyNativeLib(String binaryName, String outputName, String binaryNameNoExt) {
        try (InputStream inputStream = LoadRustBinary.class.getResourceAsStream("/" + binaryName)) {
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: /" + binaryName);
            }
            Path workingDir = Paths.get(System.getProperty("java.io.tmpdir"));
            String extension = binaryName.substring(binaryName.lastIndexOf('.') + 1);
            Path destinationPath = workingDir.resolve(outputName + "." + extension);

            Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);

            if (!SystemUtils.IS_OS_WINDOWS) {
                try {
                    Set<PosixFilePermission> permissions = Set.of(
                        PosixFilePermission.OWNER_READ,
                        PosixFilePermission.OWNER_WRITE,
                        PosixFilePermission.OWNER_EXECUTE
                    );
                    Files.setPosixFilePermissions(destinationPath, permissions);
                } catch (UnsupportedOperationException e) {
                    Runtime.getRuntime().exec(new String[]{"/system/bin/chmod", "777", destinationPath.toString()});
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy native library: " + binaryName, e);
        }
    }
}
