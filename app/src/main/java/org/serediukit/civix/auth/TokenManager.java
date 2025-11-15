package org.serediukit.civix.auth;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class TokenManager {

    private static final String PREF_FILE_NAME = "secure_auth_prefs";
    private static final String KEY_JWT_TOKEN = "jwt_token";

    private final SharedPreferences sharedPreferences;

    public TokenManager(Context context) {
        try {
            // 1. Create a Master Key
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            // 2. Initialize EncryptedSharedPreferences
            sharedPreferences = EncryptedSharedPreferences.create(
                PREF_FILE_NAME,
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            // In a real app, handle this exception (e.g., log and fallback to standard storage or crash)
            throw new RuntimeException("Could not initialize secure storage", e);
        }
    }

    /**
     * Retrieves the JWT token.
     * @return The JWT token string, or null if not found.
     */
    public String getToken() {
        return sharedPreferences.getString(KEY_JWT_TOKEN, null);
    }

    /**
     * Stores a new JWT token securely.
     * @param token The new JWT token.
     */
    public void saveToken(String token) {
        sharedPreferences.edit().putString(KEY_JWT_TOKEN, token).apply();
    }

    /**
     * Clears the stored token (e.g., on logout).
     */
    public void clearToken() {
        sharedPreferences.edit().remove(KEY_JWT_TOKEN).apply();
    }
}