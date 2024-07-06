package com.example.personalcoffee.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalcoffee.AddNfcActivity;
import com.example.personalcoffee.AddRecipeActivity;
import com.example.personalcoffee.Model.Recipe;
import com.example.personalcoffee.Net;
import com.example.personalcoffee.R;
import com.example.personalcoffee.databinding.FragmentMyrecipeBinding;
import com.example.personalcoffee.Adapter.recipeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRecipeFragment extends Fragment {

    private FragmentMyrecipeBinding binding;
    ArrayList<Recipe> list = new ArrayList<>();
    String userId;
    String nickname = null;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMyrecipeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();

        //System.out.println(getArguments());
        if (getArguments() != null)
        {
            binding.textView8.setText(pref.getString("nickname", "") + " 님");
            //친구 커피 레시피
            nickname = getArguments().getString("nickname"); // 프래그먼트1에서 받아온 값 넣기
            System.out.println("nickname:"+nickname);
            getFriendRecipe(nickname);

            binding.nfcBtn.setVisibility(View.INVISIBLE);
            binding.imageView.setVisibility(View.INVISIBLE);
            binding.textView9.setVisibility(View.INVISIBLE);
            binding.textView10.setText(nickname + " 님의 커피 레시피");
        }else{
            //내 레시피
            getRecipe();
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        binding.myrecipeRecyclerview.setLayoutManager(gridLayoutManager) ;
        recipeAdapter adapter = new recipeAdapter(list, getContext(),nickname) ;
        binding.myrecipeRecyclerview.setAdapter(adapter);

        //레시피 추가
        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddRecipeActivity.class);
                startActivity(intent);
            }
        });

        //nfc 등록 버튼
        binding.nfcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddNfcActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        binding.textView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), binding.textView8);
                popupMenu.inflate(R.menu.delete_menu);

                // 팝업 메뉴의 아이템 클릭 이벤트를 처리합니다.
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.logout:
                                System.out.println("logout");
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

        return view;
    }

    private void getRecipe() {
        Call<ResponseBody> call = Net.getInstance().getApiService().RecipeList();

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

                        String my_nickname = jsonObject.get("nickname").toString();
                        binding.textView8.setText(my_nickname + " 님");
                        editor.putString("nickname", my_nickname);
                        editor.apply();

                        userId = jsonObject.get("userId").toString();

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject obj = dataArray.getJSONObject(i);
                            list.add(new Recipe(obj.getString("recipe_id"), obj.getString("espresso"), obj.getString("water"),
                                    obj.getString("syrup"), obj.getString("name"), obj.getString("love")));
                        }

                        recipeAdapter adapter = new recipeAdapter(list, getContext(), nickname) ;
                        binding.myrecipeRecyclerview.setAdapter(adapter);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        System.out.println("실패");
                        String errorResponse = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorResponse);
                        System.out.println(jsonObject);
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

    private void getFriendRecipe(String nickname) {
        Call<ResponseBody> call = Net.getInstance().getApiService().friendRecipe(nickname);

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

                        //binding.textView8.setText(jsonObject.get("nickname").toString() + " 님");
                        //userId = jsonObject.get("userId").toString();
                        System.out.println(dataArray);

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject obj = dataArray.getJSONObject(i);
                            list.add(new Recipe(obj.getString("recipe_id"), obj.getString("espresso"), obj.getString("water"),
                                    obj.getString("syrup"), obj.getString("name"), obj.getString("love")));
                        }

                        recipeAdapter adapter = new recipeAdapter(list, getContext(), nickname) ;
                        binding.myrecipeRecyclerview.setAdapter(adapter);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        System.out.println("실패");
                        String errorResponse = response.errorBody().string();
                      //  JSONObject jsonObject = new JSONObject(errorResponse);
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

