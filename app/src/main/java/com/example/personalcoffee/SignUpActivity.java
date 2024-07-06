package com.example.personalcoffee;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personalcoffee.Model.Register;
import com.example.personalcoffee.databinding.ActivityLoginBinding;
import com.example.personalcoffee.databinding.ActivitySignUpBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nickname = binding.editTextTextPersonName2.getText().toString();
                String id = binding.editTextTextPersonName3.getText().toString();
                String password = binding.editTextTextPersonName4.getText().toString();
                String password2 = binding.editTextTextPersonName5.getText().toString();

                binding.signupIdText.setVisibility(View.GONE);
                binding.signupNicknameText.setVisibility(View.GONE);
                binding.signupPwdText.setVisibility(View.GONE);
                binding.signupPwd2Text.setVisibility(View.GONE);

                singup(new Register(nickname, id, password, password2));
            }
        });
    }

    private void singup(Register register) {
        Call<ResponseBody> call = Net.getInstance().getApiService().userRegister(register);

        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    System.out.println("성공");
                    System.out.println(response.body());
                    Toast.makeText(getApplication(), "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),  LoginActivity.class);
                    startActivity(intent);
                }else{
                    System.out.println("실패");
                    try {
                        String errorResponse = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorResponse);
                        JSONObject obj = (JSONObject) jsonObject.get("errors");
                        System.out.println(obj);
 //                        System.out.println(obj.get("nickname"));
                        if(obj.has("nickname")){
                            binding.signupNicknameText.setVisibility(View.VISIBLE);
                            binding.signupNicknameText.setText(obj.get("nickname").toString());
                        }
                        if(obj.has("id")){
                            binding.signupIdText.setVisibility(View.VISIBLE);
                            binding.signupIdText.setText(obj.get("id").toString());
                        }
                        if(obj.has("password")){
                            binding.signupPwdText.setVisibility(View.VISIBLE);
                            binding.signupPwdText.setText(obj.get("password").toString());
                        }

                        if(obj.has("passwordMatch")){
                            binding.signupPwd2Text.setVisibility(View.VISIBLE);
                            binding.signupPwd2Text.setText(obj.get("passwordMatch").toString());
                        }



                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                t.printStackTrace();
                System.out.println("에러");
            }
        });
    }
}
