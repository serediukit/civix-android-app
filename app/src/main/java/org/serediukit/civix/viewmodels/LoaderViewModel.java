package org.serediukit.civix.viewmodels;

import org.serediukit.civix.models.LoaderModel;
import org.serediukit.civix.models.entities.JWTTokens;
import org.serediukit.civix.models.token.TokenManager;

public class LoaderViewModel {
    private TokenManager tokenManager;
    private LoaderModel loaderModel;

    public LoaderViewModel(TokenManager tokenManager, LoaderModel loaderModel) {
        this.tokenManager = tokenManager;
        this.loaderModel = loaderModel;
    }

    public boolean isUserAuthed() {
        JWTTokens tokens = tokenManager.getTokens();

        if (tokens.getRefreshToken() == null) {
            return false;
        }
    }
}
