package com.itschool.buzuverov.forgdo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itschool.buzuverov.forgdo.Model.Theme.ThemeHelper;
import com.itschool.buzuverov.forgdo.R;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private static final int STATE_CODE_NOT_SENT = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int GOOGLE_SIGN_CODE = 505;

    private EditText number;
    private EditText code;
    private TextView info;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private String verificationId;
    private int state = STATE_CODE_NOT_SENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeHelper.getTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference().child("User");
        init();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                signInWithCredential(credential);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void init() {
        number = findViewById(R.id.login_activity_number);
        code = findViewById(R.id.login_activity_code);
        info = findViewById(R.id.login_activity_info);
        ImageButton back =  findViewById(R.id.login_activity_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        back.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_back_button));
        startAnim();
    }

    private void startAnim() {
        findViewById(R.id.login_activity_title).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_title_text));
        number.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_params_layout));
        code.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_params_layout));
        findViewById(R.id.constraintLayout6).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_tip_text));
        findViewById(R.id.login_activity_google_layout).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_create_button));
    }

    public void authByGoogle(View view) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        Intent signInIntent = GoogleSignIn.getClient(this, gso).getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_CODE);
    }

    public void doAuth(View view) {
        switch (state) {
            case STATE_CODE_NOT_SENT:
                String phoneNumber = number.getText().toString();
                if (phoneNumber.trim().length() > 0) {
                    if (phoneNumber.charAt(0) == '8') {
                        phoneNumber = "+7" + phoneNumber.substring(1);
                    }
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            signInWithCredential(phoneAuthCredential);
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            state = STATE_CODE_NOT_SENT;
                            info.setText(getString(R.string.get_code));
                        }

                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            verificationId = s;
                            state = STATE_CODE_SENT;
                        }
                    });
                    info.setText(R.string.check_code);
                }
                break;
            case STATE_CODE_SENT:
                String cod = code.getText().toString();
                if (cod.trim().length() > 0) {
                    signInWithCredential(PhoneAuthProvider.getCredential(verificationId, cod));
                }
                break;
        }
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final String currentUserId = auth.getCurrentUser().getUid();
                    database.child(currentUserId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists() && !dataSnapshot.hasChildren()) {
                                database.child(currentUserId).child("id").setValue(currentUserId);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    finish();
                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                state = STATE_CODE_NOT_SENT;
                info.setText(R.string.get_code);
            }
        });
    }

    private void signInWithCredential(AuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final String currentUserId = auth.getCurrentUser().getUid();
                    database.child(currentUserId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists() && !dataSnapshot.hasChildren()) {
                                database.child(currentUserId).child("id").setValue(currentUserId);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    finish();
                }
            }
        });
    }
}