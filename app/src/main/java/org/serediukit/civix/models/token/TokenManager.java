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
            // If encrypted preferences fail, delete the corrupted file and retry
            Log.w("TOKEN MANAGER", "Attempting to recover by deleting corrupted preferences");
            try {
                context.deleteSharedPreferences(PREF_FILE_NAME);

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
                Log.i("TOKEN MANAGER", "Successfully recreated encrypted preferences");
            } catch (GeneralSecurityException | IOException retryException) {
                Log.e("TOKEN MANAGER", "Failed to recover encrypted preferences", retryException);
                // Fall back to regular SharedPreferences as last resort
                Log.w("TOKEN MANAGER", "Falling back to regular SharedPreferences (NOT SECURE)");
                tokensPreferences = context.getSharedPreferences(PREF_FILE_NAME + "_fallback", Context.MODE_PRIVATE);
            }
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
        if (tokensPreferences == null) {
            Log.e("TOKEN MANAGER", "tokensPreferences is null, cannot retrieve tokens");
            return null;
        }

        String access = tokensPreferences.getString(KEY_ACCESS_TOKEN, null);
        String refresh = tokensPreferences.getString(KEY_REFRESH_TOKEN, null);

        Log.d("TOKEN MANAGER", "access: " + access);
        Log.d("TOKEN MANAGER", "refresh: " + refresh);

        if (access == null) {
            return null;
        }

        return new JWTTokens(access, refresh);
    }

    /**
     * Stores a new JWT tokens securely.
     * @param tokens The new JWT tokens.
     */
    public void saveTokens(JWTTokens tokens) {
        if (tokensPreferences == null) {
            Log.e("TOKEN MANAGER", "tokensPreferences is null, cannot save tokens");
            return;
        }
        tokensPreferences.edit().putString(KEY_ACCESS_TOKEN, tokens.getAccessToken()).apply();
        tokensPreferences.edit().putString(KEY_REFRESH_TOKEN, tokens.getRefreshToken()).apply();
    }

    /**
     * Clears the stored tokens (e.g., on logout).
     */
    public void clearTokens() {
        if (tokensPreferences == null) {
            Log.e("TOKEN MANAGER", "tokensPreferences is null, cannot clear tokens");
            return;
        }
        tokensPreferences.edit().remove(KEY_ACCESS_TOKEN).apply();
        tokensPreferences.edit().remove(KEY_REFRESH_TOKEN).apply();
    }

    public String getAccessToken() {
        if (tokensPreferences == null) {
            Log.e("TOKEN MANAGER", "tokensPreferences is null, cannot get access token");
            return null;
        }
        return tokensPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public String getRefreshToken() {
        if (tokensPreferences == null) {
            Log.e("TOKEN MANAGER", "tokensPreferences is null, cannot get refresh token");
            return null;
        }
        return tokensPreferences.getString(KEY_REFRESH_TOKEN, null);
    }
}