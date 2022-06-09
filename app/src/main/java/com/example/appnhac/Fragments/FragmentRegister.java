package com.example.appnhac.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appnhac.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class FragmentRegister extends Fragment {

    View view;
    TextInputEditText email,password1,password2,name;
    Button btRegister;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register,container,false);
        init();
        evenClick();
        return view;

    }

    private void evenClick() {
        password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!password1.getText().toString().equals(password2.getText().toString())){
                    password2.setError("Mật khẩu không khớp");
                } else {
                    password2.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void init(){
        email = view.findViewById(R.id.editTextUser);
        password1 = view.findViewById(R.id.editTextPassword);
        password2 = view.findViewById(R.id.editTextMK2);
        name = view.findViewById(R.id.editTextName);
        btRegister = view.findViewById(R.id.buttonRegisterProcess);

        String pass1 = password1.getText().toString();
        String pass2 = password2.getText().toString();


    }
}
