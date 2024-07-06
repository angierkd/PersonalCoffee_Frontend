package com.example.personalcoffee.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.personalcoffee.R;

import java.util.ArrayList;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {

    private ArrayList<String> mData = null ;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1 ;
        ImageView emptyHeart;
        ImageView fullHeart;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            textView1 = itemView.findViewById(R.id.itemRecommendTitle) ;
            emptyHeart = itemView.findViewById(R.id.empty_heart);
            fullHeart = itemView.findViewById(R.id.full_heart);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public RecommendAdapter(ArrayList<String> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RecommendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_recommend, parent, false) ;
        RecommendAdapter.ViewHolder vh = new RecommendAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RecommendAdapter.ViewHolder holder, int position) {
        String text = mData.get(position) ;
        holder.textView1.setText(text) ;

        holder.emptyHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.fullHeart.setVisibility(View.VISIBLE);
                holder.emptyHeart.setVisibility(View.INVISIBLE);
            }
        });

        holder.fullHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.fullHeart.setVisibility(View.INVISIBLE);
                holder.emptyHeart.setVisibility(View.VISIBLE);
            }
        });
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}

