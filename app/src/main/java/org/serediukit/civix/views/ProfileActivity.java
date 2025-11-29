package org.serediukit.civix.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.serediukit.civix.R;
import org.serediukit.civix.models.api.users.UsersClient;
import org.serediukit.civix.models.api.users.response.GetUserResponse;
import org.serediukit.civix.models.entities.user.User;
import org.serediukit.civix.models.token.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView emailValue;
    private TextView nameValue;
    private TextView surnameValue;
    private TextView phoneValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailValue = findViewById(R.id.emailValue);
        nameValue = findViewById(R.id.nameValue);
        surnameValue = findViewById(R.id.surnameValue);
        phoneValue = findViewById(R.id.phoneValue);

        loadUserProfile();
    }

    private void loadUserProfile() {
        UsersClient usersClient = UsersClient.getInstance(this);
        Call<GetUserResponse> call = usersClient.getCurrentUser();

        call.enqueue(new Callback<GetUserResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetUserResponse> call, @NonNull Response<GetUserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetUserResponse userResponse = response.body();
                    if (userResponse.isSuccess() && userResponse.getData() != null) {
                        User user = userResponse.getData().getUser();
                        if (user != null) {
                            emailValue.setText(user.getEmail() != null ? user.getEmail() : "");
                            nameValue.setText(user.getName() != null ? user.getName() : "");
                            surnameValue.setText(user.getSurname() != null ? user.getSurname() : "");
                            phoneValue.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : "");
                        }
                    }
                } else {
                    Log.e("PROFILE", "Failed to load user profile: " + response.code());
                    Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetUserResponse> call, Throwable t) {
                Log.e("PROFILE", "Error loading user profile", t);
                Toast.makeText(ProfileActivity.this, "Error loading profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openMain(View view) {
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void logOut(View view) {
        TokenManager tokenManager = TokenManager.getInstance(this);
        tokenManager.clearTokens();

        Intent intent = new Intent(ProfileActivity.this, LoaderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}