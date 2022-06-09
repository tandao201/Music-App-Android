package com.example.appnhac.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appnhac.Activities.DashboardActivity;
import com.example.appnhac.Activities.MainActivity;
import com.example.appnhac.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;

public class FragmentLogin extends Fragment {

    View view;
    TextInputEditText email,password;
    Button btLogin;
    TextView textViewQuenMk;
    LinearLayout navBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login,container,false);
        init();
        eventClick();
        return view;
    }

    private void eventClick() {
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (email.getText().toString().isEmpty()){
                    email.setError("Không để trống");
                } else {
                    email.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (password.getText().toString().isEmpty()){
                    password.setError("Không để trống");
                } else {
                    password.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = email.getText().toString();
                String strPass = password.getText().toString();
                if (!strEmail.isEmpty() && !strPass.isEmpty()){
                    MainActivity.firebaseAuth.signInWithEmailAndPassword(strEmail,strPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                                startActivity(intent);
                                Toast.makeText(getActivity(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
//                                FragmentDardboard fragmentDardboard = new FragmentDardboard();
//                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                                transaction.addToBackStack(null);
//                                transaction.replace(R.id.fragment_dang_nhap,fragmentDardboard);
//                                transaction.commit();

                            } else {
                                Toast.makeText(getActivity(), "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                                String error = String.valueOf(task.getException());
                                Log.e("Auth Error",error);
                            }
                        }
                    });
                } else if (strEmail.isEmpty()) {
                    email.requestFocus();
                    email.setError("Không để trống");
                } else if (strPass.isEmpty()){
                    password.requestFocus();
                    password.setError("Không để trống");
                }

            }
        });

        textViewQuenMk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Click Quen mk", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init(){
        email = view.findViewById(R.id.editTextUserLogin);
        password = view.findViewById(R.id.editTextPasswordLogin);
        btLogin = view.findViewById(R.id.buttonLoginProcess);
        textViewQuenMk = view.findViewById(R.id.textViewQuenMk);
        navBar = view.findViewById(R.id.navBarLinear);
    }
}
