package org.serediukit.civix.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.serediukit.civix.R;
import org.serediukit.civix.models.LoginModel;
import org.serediukit.civix.models.token.TokenManager;
import org.serediukit.civix.util.uicodes.UICode;
import org.serediukit.civix.viewmodels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;

    private EditText emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }

    private void init() {
        TokenManager tokenManager = TokenManager.getInstance(this.getApplicationContext());
        LoginModel loginModel = new LoginModel(this.getApplicationContext());
        loginViewModel = new LoginViewModel(loginModel, tokenManager);

        emailInput = findViewById(R.id.login_email_edit_text);
        passwordInput = findViewById(R.id.login_password_edit_text);
    }

    public void login(View view) {
        final String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();

        boolean isSomeEmpty = false;
        if (email.isEmpty()) {
            isSomeEmpty = true;
            emailInput.setBackgroundColor(R.color.ic_launcher_background);
        }
        if (password.isEmpty()) {
            isSomeEmpty = true;
            passwordInput.setBackgroundColor(R.color.ic_launcher_background);
        }

        if (isSomeEmpty) {
            return;
        }

        Log.d("TESTTEST", "3");

        new Thread(() -> {
            UICode responseCode = loginViewModel.login(email, password);

            runOnUiThread(() -> {
                if (responseCode == UICode.Ok) {
                    openMainActivity(view);
                } else {
                    Toast.makeText(LoginActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void openMainActivity(View view) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}