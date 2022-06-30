package com.example.appnhac.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.appnhac.Adapters.LoginRegisterAdapter;
import com.example.appnhac.Models.User;
import com.example.appnhac.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class DangNhapActivity extends AppCompatActivity {

    public static ViewPager2 viewPager2;
    Button btLogin,btRegister;
    View colorLogin,colorRegister;
    Toolbar toolbar;
    ImageButton btFb,btGg;
    CallbackManager callbackManager;
    String TAG="DnActivity";
    public static FirebaseAuth mAuth;

    private GoogleSignInClient googleSignInClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        init();
        eventClick();
    }

    private void eventClick() {
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager2.setCurrentItem(0,true);
                colorLogin.setVisibility(View.VISIBLE);
                colorRegister.animate().translationX(0);
                colorRegister.setVisibility(View.GONE);
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager2.setCurrentItem(1,true);
                colorRegister.setVisibility(View.VISIBLE);
                colorLogin.animate().translationX(0);
                colorLogin.setVisibility(View.GONE);
            }
        });

        btFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(DangNhapActivity.this,
                        Arrays.asList("email","public_profile"));// gui yeu cau cho phep truy cap email, public profile: name,avatar
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() { // thuc hien login
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken()); //xu ly dang nhap
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mAuth.getCurrentUser()!=null){
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    User user1 = new User(user.getEmail(),user.getDisplayName(),user.getPhotoUrl().toString());
                                    Log.e("user",user.getEmail());
                                    Intent intent = new Intent(DangNhapActivity.this,DashboardActivity.class);
                                    if (user != null){
                                        intent.putExtra("user",user1);
                                    }
                                    startActivity(intent);
                                    handler.removeCallbacks(this);
                                } else {
                                    handler.postDelayed(this,500);
                                }
                            }
                        },500);
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
                    }
                });
            }
        });
        btGg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"begin click Google signin");
                Intent intent = googleSignInClient.getSignInIntent();
                resultLauncher.launch(intent);
            }
        });

    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent intent = result.getData();
                Log.d(TAG,"onActivityResult: Google Signin intent");
                Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(intent);
                if (accountTask.isSuccessful()){
                    try {
                        GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                        if (account!=null){
                            firebaseAuthWithGoogleAccount(account);
                        }
                    } catch (Exception e) {
                        Log.d(TAG,"onActivityResult: "+e.getMessage());
                    }
                }

            }
        }
    });

    @SuppressLint("WrongViewCast")
    private void init(){
        viewPager2 = findViewById(R.id.viewPagerLogin);
        btLogin = findViewById(R.id.buttonLogin);
        btRegister = findViewById(R.id.buttonRegister);
        colorLogin = findViewById(R.id.viewLogin);
        colorRegister = findViewById(R.id.viewSignup);
        toolbar = findViewById(R.id.toolbarDangNhap);
        btFb = findViewById(R.id.buttonLoginFb);
        btGg = findViewById(R.id.buttonLoginGg);
        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        MainActivity.firebaseAuth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        LoginRegisterAdapter loginRegisterAdapter = new LoginRegisterAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(loginRegisterAdapter);

        // configure the Google Signin
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("774107411755-poe82j3vj9mmcf0rfbf49ju03dgq3jab.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
        googleSignInClient.revokeAccess();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager!=null)
            callbackManager.onActivityResult(requestCode,resultCode,data);
        else {

        }
    }

    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
        Log.d(TAG,"firebaseAuthWithGoogleAccount: begin firebase login with google");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d(TAG, "onSuccess: Logged in");
                FirebaseUser user = mAuth.getCurrentUser();
                String uid = user.getUid();
                String email = user.getEmail();
                Log.d(TAG, "onSuccess: Email: "+email);
                Log.d(TAG, "onSuccess: UID: "+uid);
                User user1 = new User(user.getEmail(),user.getDisplayName(),user.getPhotoUrl().toString());
                Log.e("user",user.getEmail());
                Intent intent = new Intent(DangNhapActivity.this,DashboardActivity.class);
                if (user != null){
                    intent.putExtra("user",user1);
                }
                startActivity(intent);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Login failed "+e.getMessage());
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");
//                    FirebaseUser user = mAuth.getCurrentUser();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    Toast.makeText(DangNhapActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}