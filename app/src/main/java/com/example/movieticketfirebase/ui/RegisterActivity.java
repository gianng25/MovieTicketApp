package com.example.movieticketfirebase.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movieticketfirebase.databinding.ActivityRegisterBinding;
import com.example.movieticketfirebase.model.UserProfile;
import com.example.movieticketfirebase.util.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth auth;
    private FirebaseRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        repository = new FirebaseRepository();

        binding.btnRegister.setOnClickListener(v -> register());
    }

    private void register() {
        String name = binding.edtName.getText().toString().trim();
        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            toast("Vui lòng nhập đầy đủ thông tin");
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    if (firebaseUser == null) {
                        toast("Không lấy được thông tin user");
                        return;
                    }

                    UserProfile profile = new UserProfile(firebaseUser.getUid(), name, email, "");
                    repository.saveUserProfile(profile,
                            unused -> {
                                toast("Đăng ký thành công");
                                startActivity(new Intent(this, MovieListActivity.class));
                                finishAffinity();
                            },
                            e -> toast("Lưu profile thất bại: " + e.getMessage()));
                })
                .addOnFailureListener(e -> toast("Đăng ký thất bại: " + e.getMessage()));
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
