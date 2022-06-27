package com.example.apjnews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private ArrayList<Articles> articlesArrayList;
    private Context context;

    //constructor
    public NewsAdapter(ArrayList<Articles> articlesArrayList, Context context) {
        this.articlesArrayList = articlesArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
        return new NewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        final Articles articles=articlesArrayList.get(position); //MADE FINAL********************
        holder.newsItemSubHeadingTextView.setText(articles.getDescription());
        holder.newsItemHeadingTextView.setText(articles.getTitle());
        Picasso.get().load(articles.getUrlToImage()).into(holder.newsItemImageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, NewsDetailActivity.class);
                i.putExtra("title", articles.getTitle());
                i.putExtra("content", articles.getContent());
                i.putExtra("desc", articles.getDescription());
                i.putExtra("image", articles.getUrlToImage());
                i.putExtra("url", articles.getUrl());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articlesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView newsItemHeadingTextView,newsItemSubHeadingTextView;
        private ImageView newsItemImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsItemHeadingTextView=itemView.findViewById(R.id.newsItemHeadingTextView);
            newsItemSubHeadingTextView=itemView.findViewById(R.id.newsItemSubHeadingTextView);
            newsItemImageView=itemView.findViewById(R.id.newsItemImageView);
        }
    }

}
