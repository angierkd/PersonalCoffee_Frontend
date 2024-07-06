package com.example.personalcoffee.Fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalcoffee.Adapter.FriendListAdapter;
import com.example.personalcoffee.Adapter.RecommendAdapter;
import com.example.personalcoffee.Adapter.recipeAdapter;
import com.example.personalcoffee.Model.Recipe;
import com.example.personalcoffee.Net;
import com.example.personalcoffee.databinding.FragmentRecommendBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendFragment extends Fragment {

    private FragmentRecommendBinding binding;
    ArrayList<Recipe> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRecommendBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        SharedPreferences pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        binding.recommendNickname.setText(pref.getString("nickname", "") + " 님");

        getRecommendRecipe();

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        binding.recommendRecyclerview.setLayoutManager(gridLayoutManager) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        recipeAdapter adapter = new recipeAdapter(list,getContext(),"admin") ;
        binding.recommendRecyclerview.setAdapter(adapter);

        return view;
    }

    private void getRecommendRecipe() {
        Call<ResponseBody> call = Net.getInstance().getApiService().getRecommendRecipe();

        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    try {
                        System.out.println("성공");
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        System.out.println(jsonObject);
                        JSONArray dataArray = jsonObject.getJSONArray("data");

                        System.out.println(dataArray);

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject obj = dataArray.getJSONObject(i);
                            list.add(new Recipe(obj.getString("recipe_id"), obj.getString("espresso"), obj.getString("water"),
                                    obj.getString("syrup"), obj.getString("name"), obj.getString("love")));
                        }

                        recipeAdapter adapter = new recipeAdapter(list,getContext(),"admin") ;
                        binding.recommendRecyclerview.setAdapter(adapter);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        System.out.println("실패");
                        String errorResponse = response.errorBody().string();
                        System.out.println(errorResponse);
                    } catch (IOException e) {
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
