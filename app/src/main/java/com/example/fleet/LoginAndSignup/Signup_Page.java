package com.example.fleet.LoginAndSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fleet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Signup_Page extends AppCompatActivity implements View.OnClickListener {

    TextView login;
    private EditText et_first_name, et_last_name, et_email, et_password, et_password_confirm;
    private FirebaseAuth mAuth;
    private Button signup_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__page);

        mAuth = FirebaseAuth.getInstance();

        signup_btn = (Button) findViewById(R.id.signup_btn);
        signup_btn.setOnClickListener(this);

        et_email = findViewById(R.id.email);
        et_first_name = findViewById(R.id.first_name);
        et_last_name = findViewById(R.id.last_name);
        et_password = findViewById(R.id.password1);
        et_password_confirm = findViewById(R.id.password2);


        login = findViewById(R.id.logintxt);
        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.signup_btn:
                register_user();
                break;
            case R.id.logintxt:
            {Intent i = new Intent(Signup_Page.this, LoginActivity.class);
                startActivity(i);}
                break;
        }
    }

    private void register_user() {
        final String email = et_email.getText().toString().trim();
        final String fname = et_first_name.getText().toString().trim();
        final String lname = et_last_name.getText().toString().trim();
        final String password1 = et_password.getText().toString().trim();
        final String password2 = et_password_confirm.getText().toString().trim();

        if(fname.isEmpty()){
            et_first_name.setError("First Name is required");
            et_first_name.requestFocus();
            return;
        }
        if(lname.isEmpty()){
            et_last_name.setError("Last Name is required");
            et_last_name.requestFocus();
            return;
        }
        if(email.isEmpty()){
            et_email.setError("Email ID is required");
            et_email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("Please enter valid Email-ID");
            et_email.requestFocus();
            return;
        }
        if(password1.isEmpty()){
            et_password.setError("Password is required");
            et_password.requestFocus();
            return;
        }
        if(password1.length()<8){
            et_password.setError("Password should have atleast 8 characters");
            et_password.requestFocus();
            return;
        }
        if(password2.isEmpty()){
            et_password_confirm.setError("Password Confirm is required");
            et_password_confirm.requestFocus();
            return;
        }
        if(!password1.equals(password2)){
            et_password_confirm.setError("Passwords does not match");
            et_password_confirm.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    user usr = new user(fname, lname, email ,password1, password2);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(usr).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(Signup_Page.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(Signup_Page.this, "Failed to registered User", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(Signup_Page.this, "Failed to registered User", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}