package com.example.fleet.LoginAndSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fleet.MainActivity;
import com.example.fleet.MainPage.mainpage;
import com.example.fleet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login;
    private TextView signup;
    private EditText et_username, et_password;
    private ImageButton faceboook_login, google_login, twitter_login;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        check_authentication();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login_button);
        login.setOnClickListener(this);

        signup = findViewById(R.id.signuptxt);
        signup.setOnClickListener(this);

        et_username = findViewById(R.id.username);
        et_password = findViewById(R.id.password);
        faceboook_login = findViewById(R.id.facebook_login);
        google_login = findViewById(R.id.google_login);
        twitter_login = findViewById(R.id.twitter_login);

        mAuth  = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        check_authentication();
    }

    private void check_authentication() {
        mAuth  = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent b = new Intent(LoginActivity.this, mainpage.class);
            startActivity(b);
        }else{

        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.login_button: {
                login_user();
                break;
            }
            case R.id.signuptxt: {
                Intent j = new Intent(LoginActivity.this, Signup_Page.class);
                startActivity(j);
                break;
            }
        }
    }

    private void login_user() {
        String username, password;
        username = et_username.getText().toString().trim();
        password = et_password.getText().toString().trim();

        if(username.isEmpty()){
            et_username.setError("Username is required");
            et_username.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            et_username.setError("Enter valid Email-ID");
            et_username.requestFocus();
            return;
        }
        if(password.isEmpty()){
            et_password.setError("Password is required");
            et_password.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        Intent i = new Intent(LoginActivity.this, mainpage.class);
                        startActivity(i);
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Check your email to verify your acount", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(LoginActivity.this, "Failed to Login! Please check your Email and Password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}