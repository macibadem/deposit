package com.deposit.customerservice.security.service;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

  private static final int IV_LENGTH = 12;
  private static final int GCM_TAG_LENGTH = 16;

  public String decrypt(String encryptedText, String key) throws Exception {
    byte[] decoded = Base64.getDecoder().decode(encryptedText);

    if (decoded.length < IV_LENGTH) {
      throw new IllegalArgumentException("Ciphertext too short");
    }

    byte[] iv = new byte[IV_LENGTH];
    System.arraycopy(decoded, 0, iv, 0, iv.length);

    GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);

    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, createSecretKey(key), gcmSpec);

    byte[] cipherText = new byte[decoded.length - IV_LENGTH];
    System.arraycopy(decoded, IV_LENGTH, cipherText, 0, cipherText.length);

    byte[] decryptedText = cipher.doFinal(cipherText);

    return new String(decryptedText);
  }

  private SecretKey createSecretKey(final String key) {
    byte[] decodedKey = Base64.getDecoder().decode(key);
    return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
  }
}