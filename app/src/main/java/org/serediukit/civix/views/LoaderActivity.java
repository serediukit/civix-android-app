package org.serediukit.civix.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.serediukit.civix.R;
import org.serediukit.civix.models.LoaderModel;
import org.serediukit.civix.models.token.TokenManager;
import org.serediukit.civix.viewmodels.LoaderViewModel;

public class LoaderActivity extends AppCompatActivity {
    private LoaderViewModel loaderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loader);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loader), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TokenManager tokenManager = TokenManager.getInstance(this.getApplicationContext());
        LoaderModel loaderModel = new LoaderModel();
        loaderViewModel = new LoaderViewModel(tokenManager, loaderModel);

        new Thread(() -> {
            Intent intent = resolveNextIntent();
            startActivity(intent);
            finish();
        }).start();
    }

    private Intent resolveNextIntent() {
        if (loaderViewModel.isUserAuthed()) {
            return new Intent(LoaderActivity.this, MainActivity.class);
        }
        return new Intent(LoaderActivity.this, LoginActivity.class);
    }
}