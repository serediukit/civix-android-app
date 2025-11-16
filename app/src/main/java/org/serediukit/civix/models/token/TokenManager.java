package org.serediukit.civix.models.token;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import org.serediukit.civix.models.entities.JWTTokens;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class TokenManager {
    private static TokenManager instance;

    private static final String PREF_FILE_NAME = "secure_auth_prefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";

    private SharedPreferences tokensPreferences;

    private TokenManager(Context context) {
       try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            tokensPreferences = EncryptedSharedPreferences.create(
                    context,
                    PREF_FILE_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

        } catch (GeneralSecurityException | IOException e) {
            Log.e("TOKEN MANAGER", "unable to get shared preferences", e);
        }
    }

    public static TokenManager getInstance(Context context) {
        if (instance == null) {
            instance = new TokenManager(context);
        }
        return instance;
    }

    /**
     * Retrieves the JWT tokens.
     * @return The JWT tokens, or null if not found.
     */
    public JWTTokens getTokens() {
        String access = tokensPreferences.getString(KEY_ACCESS_TOKEN, null);
        String refresh = tokensPreferences.getString(KEY_REFRESH_TOKEN, null);

        Log.d("TOKEN MANAGER", "access: " + access);
        Log.d("TOKEN MANAGER", "refresh: " + refresh);

        return new JWTTokens(access, refresh);
    }

    /**
     * Stores a new JWT tokens securely.
     * @param tokens The new JWT tokens.
     */
    public void saveTokens(JWTTokens tokens) {
        tokensPreferences.edit().putString(KEY_ACCESS_TOKEN, tokens.getAccessToken()).apply();
        tokensPreferences.edit().putString(KEY_REFRESH_TOKEN, tokens.getRefreshToken()).apply();
    }

    /**
     * Clears the stored tokens (e.g., on logout).
     */
    public void clearTokens() {
        tokensPreferences.edit().remove(KEY_ACCESS_TOKEN).apply();
        tokensPreferences.edit().remove(KEY_REFRESH_TOKEN).apply();
    }
}