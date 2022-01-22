package com.example.watchnext;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.watchnext.activities.guests.GuestsActivity;
import com.example.watchnext.activities.users.UsersActivity;
import com.example.watchnext.models.Model;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Model.instance.executor.execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (Model.instance.isSignedIn()) {
                startUsersActivity();
                Model.instance.logout();
                startGuestsActivity();
            } else {
                startGuestsActivity();
            }
        });

    }

    private void startGuestsActivity() {
        startActivityFromIntent(GuestsActivity.class);
    }

    private void startUsersActivity() {
        startActivityFromIntent(UsersActivity.class);
    }

    private void startActivityFromIntent(Class<? extends AppCompatActivity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
        finish();
    }

}