package org.serediukit.civix.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.serediukit.civix.R;
import org.serediukit.civix.models.RegisterModel;
import org.serediukit.civix.models.entities.city.Location;
import org.serediukit.civix.util.uicodes.UICode;
import org.serediukit.civix.viewmodels.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {
    private RegisterViewModel registerViewModel;

    private EditText emailInput, passwordInput, nameInput, surnameInput, phoneInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
    }

    private void init() {
        RegisterModel registerModel = new RegisterModel(this.getApplicationContext());
        registerViewModel = new RegisterViewModel(registerModel);

        emailInput = findViewById(R.id.register_email_edit_text);
        passwordInput = findViewById(R.id.register_password_edit_text);
        nameInput = findViewById(R.id.register_name_edit_text);
        surnameInput = findViewById(R.id.register_surname_edit_text);
        phoneInput = findViewById(R.id.register_phone_edit_text);
    }

    public void signUp(View view) {
        final String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();
        final String name = nameInput.getText().toString();
        final String surname = surnameInput.getText().toString();
        final String phoneNumber = phoneInput.getText().toString();

        boolean isSomeEmpty = false;
        if (email.isEmpty()) {
            isSomeEmpty = true;
            emailInput.setError("Email is required");
        }
        if (password.isEmpty()) {
            isSomeEmpty = true;
            passwordInput.setError("Password is required");
        }
        if (name.isEmpty()) {
            isSomeEmpty = true;
            nameInput.setError("Name is required");
        }
        if (phoneNumber.isEmpty()) {
            isSomeEmpty = true;
            phoneInput.setError("Phone number is required");
        }

        if (isSomeEmpty) {
            return;
        }

        // Create a default location (0, 0) - can be updated with actual location later
        Location location = new Location(0.0, 0.0);

        new Thread(() -> {
            UICode responseCode = registerViewModel.register(email, password, name, surname, phoneNumber, location);

            runOnUiThread(() -> {
                if (responseCode == UICode.Ok) {
                    Toast.makeText(RegisterActivity.this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();
                    openLoginActivity();
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void openLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void openLogIn(View view) {
        openLoginActivity();
    }
}