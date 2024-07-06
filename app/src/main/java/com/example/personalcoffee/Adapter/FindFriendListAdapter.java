package com.example.personalcoffee.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.personalcoffee.Net;
import com.example.personalcoffee.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindFriendListAdapter extends RecyclerView.Adapter<FindFriendListAdapter.ViewHolder> {

    private ArrayList<HashMap<String, String>> mData = new ArrayList<>();
    //private Context context = null;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView requestFriend;

        ViewHolder(View itemView) {
            super(itemView) ;
            // 뷰 객체에 대한 참조. (hold strong reference)
            textView1 = itemView.findViewById(R.id.search_textView15);
            requestFriend = itemView.findViewById(R.id.request_friend);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public FindFriendListAdapter(ArrayList<HashMap<String, String>> list){
        //this.context = context;
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public FindFriendListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_friend_search_list, parent, false) ;
        FindFriendListAdapter.ViewHolder vh = new FindFriendListAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(FindFriendListAdapter.ViewHolder holder, int position) {
        Map text = mData.get(position) ;
        System.out.println(text);
        holder.textView1.setText(text.get("nickname").toString()) ;

        if (!text.get("status").equals("1")&&!text.get("status").equals("0")) {
            holder.requestFriend.setVisibility(View.VISIBLE);
        }

        holder.requestFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestFriend(text.get("nickname").toString());
            }

            private void requestFriend(String text) {
                Call<ResponseBody> call = Net.getInstance().getApiService().requestFriend(text);

                call.enqueue(new Callback<ResponseBody>()
                {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                    {
                        if (response.isSuccessful() && response.body() != null)
                        {
                            System.out.println("성공");
                            holder.requestFriend.setVisibility(View.INVISIBLE);
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
        });
    }



    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}

