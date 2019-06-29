package dev.blank.oauth2github;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import dev.blank.oauth2githublibrary.LoginGithub;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    LoginGithub btnGithub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
        btnGithub = findViewById(R.id.btnGithub);
        btnGithub.setLoginListener(new LoginGithub.LoginListener() {
            @Override
            public void onError(String error) {

            }

            @Override
            public void onFinish(String kode) {
                textView.setText(kode);
            }
        });
    }

}
