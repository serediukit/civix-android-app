package org.serediukit.civix.viewmodels;

import android.util.Log;

import org.serediukit.civix.models.LoginModel;
import org.serediukit.civix.models.api.auth.response.LoginResponse;
import org.serediukit.civix.models.entities.JWTTokens;
import org.serediukit.civix.models.token.TokenManager;
import org.serediukit.civix.util.uicodes.UICode;

public class LoginViewModel {
    private final LoginModel loginModel;
    private final TokenManager tokenManager;

    public LoginViewModel(LoginModel loginModel, TokenManager tokenManager) {
        this.loginModel = loginModel;
        this.tokenManager = tokenManager;
    }

    public UICode login(String email, String password) {
        LoginResponse resp = loginModel.login(email, password);
        if (resp == null) {
            return UICode.AuthError;
        }

        JWTTokens newTokens = new JWTTokens(resp.getAccessToken(), resp.getRefreshToken());

        tokenManager.saveTokens(newTokens);
        JWTTokens tokens = tokenManager.getTokens();
        if (tokens != null && tokens.getAccessToken() != null && tokens.getRefreshToken() != null) {
            Log.d("LOGIN VIEWMODEL | LOGIN", "Access Token: " + tokens.getAccessToken());
            Log.d("LOGIN VIEWMODEL | LOGIN", "Refresh Token: " + tokens.getRefreshToken());
        }

        return UICode.Ok;
    }
}
