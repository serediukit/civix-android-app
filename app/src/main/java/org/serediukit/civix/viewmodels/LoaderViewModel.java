package org.serediukit.civix.viewmodels;

import android.util.Log;

import org.serediukit.civix.models.LoaderModel;
import org.serediukit.civix.models.api.auth.response.RefreshResponse;
import org.serediukit.civix.models.entities.JWTTokens;
import org.serediukit.civix.models.token.TokenManager;

public class LoaderViewModel {
    private final LoaderModel loaderModel;
    private final TokenManager tokenManager;

    public LoaderViewModel(LoaderModel loaderModel, TokenManager tokenManager) {
        this.loaderModel = loaderModel;
        this.tokenManager = tokenManager;
    }

    public boolean isUserAuthed() {
        JWTTokens tokens = tokenManager.getTokens();
        Log.d("LoaderViewModel", tokens.getAccessToken());
        Log.d("LoaderViewModel", tokens.getRefreshToken());
        if (tokens.getRefreshToken() == null) {
            return false;
        }

        RefreshResponse resp  = loaderModel.refreshTokens(tokens.getRefreshToken());
        if (resp == null) {
            tokenManager.clearTokens();
            return false;
        }

        JWTTokens newTokens = new JWTTokens(resp.getAccessToken(), resp.getRefreshToken());
        tokenManager.saveTokens(newTokens);

        return true;
    }
}
