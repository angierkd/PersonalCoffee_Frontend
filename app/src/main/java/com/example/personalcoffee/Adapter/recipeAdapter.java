package com.example.personalcoffee.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.personalcoffee.AddRecipeActivity;
import com.example.personalcoffee.MainActivity;
import com.example.personalcoffee.Model.Recipe;
import com.example.personalcoffee.Net;
import com.example.personalcoffee.R;
import com.example.personalcoffee.SignUpActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class recipeAdapter extends RecyclerView.Adapter<recipeAdapter.ViewHolder> {

    private ArrayList<Recipe>mData = null ;
    Context context;
    String nickname;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView title;
        ImageView imageView;
        ImageView emptyHeart;
        ImageView fullHeart;
        TextView recipeId;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            textView1 = itemView.findViewById(R.id.itemMyrecipeNum1);
            textView2 = itemView.findViewById(R.id.itemMyrecipeNum2);
            textView3 = itemView.findViewById(R.id.itemMyrecipeNum3);
            title = itemView.findViewById(R.id.textView18);
            imageView = itemView.findViewById(R.id.menu);
            emptyHeart = itemView.findViewById(R.id.empty_heart);
            fullHeart = itemView.findViewById(R.id.full_heart);
            recipeId = itemView.findViewById(R.id.hidden_recipe_id);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public recipeAdapter(ArrayList<Recipe> list, Context context, String nickname) {
        mData = list ;
        this.context = context;
        this.nickname = nickname;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public recipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_myrecipe, parent, false) ;
        recipeAdapter.ViewHolder vh = new recipeAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(recipeAdapter.ViewHolder holder, int position) {
        Recipe recipe = mData.get(position);
        holder.textView1.setText(recipe.getEspresso());
        holder.textView2.setText(recipe.getWater());
        holder.textView3.setText(recipe.getSyrup());
        holder.title.setText(recipe.getName());
        holder.recipeId.setText(recipe.getRecipeId());

        if(recipe.getLove().equals("0")){
            holder.fullHeart.setVisibility(View.INVISIBLE);
            holder.emptyHeart.setVisibility(View.VISIBLE);
        }else{
            holder.fullHeart.setVisibility(View.VISIBLE);
            holder.emptyHeart.setVisibility(View.INVISIBLE);
        }

        if(nickname != null){
            holder.emptyHeart.setVisibility(View.INVISIBLE);
            holder.fullHeart.setVisibility(View.INVISIBLE);
        }

        // 팝업 메뉴를 생성합니다.
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nickname == null) {
                    PopupMenu popupMenu = new PopupMenu(context, holder.imageView);
                    popupMenu.inflate(R.menu.popup_menu);

                    // 팝업 메뉴의 아이템 클릭 이벤트를 처리합니다.
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.update:
                                    Intent intent = new Intent(context, AddRecipeActivity.class);
                                    Recipe updateRecipe = new Recipe(recipe.getRecipeId(), recipe.getEspresso(), recipe.getWater(), recipe.getSyrup(), recipe.getName(), "");
                                    intent.putExtra("updateRecipe", updateRecipe);
                                    context.startActivity(intent);
                                    return true;
                                case R.id.delete:
                                    deleteRecipe(recipe.getRecipeId(), position);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();
                }else{

                    PopupMenu popupMenu = new PopupMenu(context, holder.imageView);
                    popupMenu.inflate(R.menu.popup_menu_friend);

                    // 팝업 메뉴의 아이템 클릭 이벤트를 처리합니다.
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.addRecipe:
                                    addMyRecipe(recipe.getRecipeId());
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();
                }
            }
        });

        holder.emptyHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.fullHeart.setVisibility(View.VISIBLE);
                holder.emptyHeart.setVisibility(View.INVISIBLE);
                love(recipe.getRecipeId(), "1");
            }
        });

        holder.fullHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.fullHeart.setVisibility(View.INVISIBLE);
                holder.emptyHeart.setVisibility(View.VISIBLE);
                love(recipe.getRecipeId(), "0");
            }
        });
    }

    //친구 레시피를 내 레시피로 추가
    private void addMyRecipe(String recipeId) {
        Call<ResponseBody> call = Net.getInstance().getApiService().addMyRecipe(recipeId);

        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    System.out.println("성공");
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

    //좋아요 and 좋아요 취소
    private void love(String recipeId, String loveState) {
        Call<ResponseBody> call = Net.getInstance().getApiService().loveRecipe(recipeId, loveState);

        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    System.out.println("성공");
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

    //레시피 삭제
    private void deleteRecipe(String recipeId, int position) {
        Call<ResponseBody> call = Net.getInstance().getApiService().deleteRecipe(recipeId);

        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    System.out.println("성공");
                    mData.remove(position);
                    notifyDataSetChanged();
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

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
