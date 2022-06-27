package com.example.apjnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity {

    private TextView titleTextView, subDescTextView, contentTextView;
    private ImageView newsImageView;
    private Button readNewsButton;

    String title,desc,content,imageURL,url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        title=getIntent().getStringExtra("title");
        desc=getIntent().getStringExtra("desc");
        content=getIntent().getStringExtra("content");
        imageURL=getIntent().getStringExtra("image");
        url=getIntent().getStringExtra("url");

        titleTextView =findViewById(R.id.idTVTitle);
        subDescTextView =findViewById(R.id.idTVSubDesc);
        contentTextView =findViewById(R.id.idTVContent);
        newsImageView =findViewById(R.id.idIVNews);
        readNewsButton=findViewById(R.id.idBtnReadNews);

        titleTextView.setText(title);
        subDescTextView.setText(desc);
        contentTextView.setText(content);
        Picasso.get().load(imageURL).into(newsImageView);
        readNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
}