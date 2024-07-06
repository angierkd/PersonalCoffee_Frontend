package com.example.personalcoffee;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalcoffee.Adapter.FriendListAdapter;
import com.example.personalcoffee.Adapter.FriendRequestListAdapter;
import com.example.personalcoffee.Fragment.FriendListFragment;
import com.example.personalcoffee.Fragment.MyRecipeFragment;
import com.example.personalcoffee.Fragment.RecommendFragment;
import com.example.personalcoffee.databinding.ActivityMainBinding;
import com.example.personalcoffee.databinding.ActivityRequestListBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestListActivity extends AppCompatActivity {

    private ActivityRequestListBinding binding;
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRequestListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.requestToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false); // 기본 타이틀 표시 안 함
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = binding.requestRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        getRequestFriend();

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        FriendRequestListAdapter adapter = new FriendRequestListAdapter(list) ;
        binding.requestRecyclerview.setAdapter(adapter);
    }

    private void getRequestFriend() {
        Call<ResponseBody> call = Net.getInstance().getApiService().requestFriendList();

        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    System.out.println("성공");
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        System.out.println(dataArray);
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject2 = dataArray.getJSONObject(i);
                            String nickname = jsonObject2.getString("nickname");
                            list.add(nickname);
                        }

                        FriendRequestListAdapter adapter = new FriendRequestListAdapter(list) ;
                        binding.requestRecyclerview.setAdapter(adapter);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

