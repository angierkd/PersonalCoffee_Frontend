package com.example.personalcoffee.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalcoffee.Adapter.FriendListAdapter;
import com.example.personalcoffee.Adapter.recipeAdapter;
import com.example.personalcoffee.AddRecipeActivity;
import com.example.personalcoffee.FindFriendActivity;
import com.example.personalcoffee.MainActivity;
import com.example.personalcoffee.Net;
import com.example.personalcoffee.RequestListActivity;
import com.example.personalcoffee.databinding.FragmentFriendListBinding;
import com.example.personalcoffee.databinding.FragmentMyrecipeBinding;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendListFragment  extends Fragment {

    private FragmentFriendListBinding binding;
    private ArrayList<String> list = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFriendListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        SharedPreferences pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        binding.friendlistNickname.setText(pref.getString("nickname", "") + " 님");

        getFriendList();

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = binding.friendlistRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        FriendListAdapter adapter = new FriendListAdapter(list) ;
        binding.friendlistRecyclerview.setAdapter(adapter);

        binding.imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FindFriendActivity.class);
                startActivity(intent);
            }
        });

        binding.imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RequestListActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }

    private void getFriendList() {

        Call<ResponseBody> call = Net.getInstance().getApiService().friendList();

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

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject2 = dataArray.getJSONObject(i);
                            String nickname = jsonObject2.getString("nickname");
                            list.add(nickname);
                        }

                        FriendListAdapter adapter = new FriendListAdapter(list) ;
                        binding.friendlistRecyclerview.setAdapter(adapter);

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
