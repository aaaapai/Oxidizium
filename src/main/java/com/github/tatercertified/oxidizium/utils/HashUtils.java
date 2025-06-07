package com.github.tatercertified.oxidizium.utils;

import com.github.tatercertified.oxidizium.Oxidizium;

import java.io.*;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashUtils {

    public static boolean checkHash(Path libPath) {
        File libOxidizium = new File(libPath.toUri());
        String correctHash = getLibHash();
        String presentHash = getFileHash(libOxidizium);
        if (!correctHash.equals(presentHash)) {
            Oxidizium.LOGGER.info("Outdated Oxidizium Binary Found. Updating...");
            return false;
        }
        return true;
    }

    private static String getLibHash() {
        try (InputStream inputStream = HashUtils.class.getResourceAsStream("/oxidizium_rust.hash")) {
            if (inputStream == null) {
                throw new IOException("Hash file not found in resources.");
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                return reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read oxidizium_rust.hash", e);
        }
    }

    private static String getFileHash(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             DigestInputStream dis = new DigestInputStream(fis, MessageDigest.getInstance("SHA-256"))) {

            byte[] buffer = new byte[8192];
            while (dis.read(buffer) != -1) {}

            byte[] hashBytes = dis.getMessageDigest().digest();
            return bytesToHex(hashBytes);

        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to compute hash for file: " + file, e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }
}
