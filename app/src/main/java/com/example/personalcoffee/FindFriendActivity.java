package com.example.personalcoffee;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.personalcoffee.Adapter.FindFriendListAdapter;
import com.example.personalcoffee.Adapter.FriendListAdapter;
import com.example.personalcoffee.Adapter.recipeAdapter;
import com.example.personalcoffee.Model.Recipe;
import com.example.personalcoffee.databinding.ActivityFindFriendBinding;
import com.example.personalcoffee.databinding.ActivityLoginBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindFriendActivity extends AppCompatActivity {

    private ActivityFindFriendBinding binding;
    ArrayList<HashMap<String, String>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFindFriendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFriend();
            }
        });

        /* initiate adapter */
        binding.findFriendRecyclerview.setAdapter(new FindFriendListAdapter(list));
        binding.findFriendRecyclerview.setLayoutManager(new LinearLayoutManager(this));

    }

    private void searchFriend() {
        Call<ResponseBody> call = Net.getInstance().getApiService().searchFriend(binding.editTextTextPersonName7.getText().toString());
        list = new ArrayList<>();

        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    System.out.println("성공");
                    try {
                        JSONArray dataArray = new JSONArray(response.body().string());
                        System.out.println(dataArray);
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject obj = dataArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String,String>();
                            map.put("nickname",obj.getString("nickname"));
                            map.put("status",obj.getString("status"));
                            list.add(map);
                        }

                        binding.findFriendRecyclerview.setAdapter(new FindFriendListAdapter(list));
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("실패");
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
