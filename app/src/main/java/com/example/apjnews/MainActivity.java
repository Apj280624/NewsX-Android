package com.example.apjnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//youtube video: 41:00 Picasso

public class MainActivity extends AppCompatActivity implements CategoryAdapter.CategoryClickInterface {

    private RecyclerView newsRecyclerView, categoryRecyclerView;
    private ProgressBar loadingProgressBar;
    private ArrayList<Articles> articlesArrayList; //structure of data items in a news article
    private ArrayList<CategoryModal> categoryModalArrayList; //structure of data item in category pallete
    private CategoryAdapter categoryAdapter;
    private NewsAdapter newsAdapter;
    String country="in";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=findViewById(R.id.toolBar);
        toolbar.setOverflowIcon(getDrawable(R.drawable.menu_icon)); //for three white dots
        setSupportActionBar(toolbar);

        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        //initialize two ArrayLists
        articlesArrayList = new ArrayList<>();
        categoryModalArrayList = new ArrayList<>();

        //initialize two Adapters
        newsAdapter = new NewsAdapter(articlesArrayList, this);
        categoryAdapter = new CategoryAdapter(categoryModalArrayList, this, this); // it was: this::onCategoryClick ********************

        //set layout manager and adapters
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsRecyclerView.setAdapter(newsAdapter);
        categoryRecyclerView.setAdapter(categoryAdapter); //layout manager already set in xml file

        getCatogories();

        sharedPreferences = this.getSharedPreferences("com.example.sharedpreferencesdemopractice", Context.MODE_PRIVATE);
        //sharedPreferences.edit().putString("countryKey","in").apply();
        country = sharedPreferences.getString("countryKey","");
        //Toast.makeText(this,username,Toast.LENGTH_SHORT).show();

        getNews("All");
        newsAdapter.notifyDataSetChanged();
    }

    public void updateCountryAndReloadNews(String newCountryCode)
    {
        sharedPreferences.edit().putString("countryKey",newCountryCode).apply();
        country=newCountryCode;
        getNews("All");
    }

    private void getCatogories()
    {
        String allImageUrl="https://images.unsplash.com/photo-1483401757487-2ced3fa77952?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1952&q=80";
        String techImageUrl="https://images.unsplash.com/photo-1593508512255-86ab42a8e620?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=798&q=80";
        String scienceImageUrl="https://images.unsplash.com/photo-1548691905-57c36cc8d935?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MjE3fHxzY2llbmNlfGVufDB8fDB8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60";
        String sportsImageUrl="https://images.unsplash.com/photo-1444491741275-3747c53c99b4?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80";
        String generalImageUrl="https://images.unsplash.com/photo-1531297484001-80022131f5a1?ixid=MnwxMjA3fDB8MHxzZWFyY2h8NXx8dGVjaG5vbG9neXxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60";
        String businessImageUrl="https://images.unsplash.com/photo-1568992687947-868a62a9f521?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8YnVzaW5lc3MlMjBtZWV0fGVufDB8fDB8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60";
        String healthImageUrl="https://images.unsplash.com/photo-1437750769465-301382cdf094?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Njd8fGhlYWx0aHxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60";
        String entertainmentImageUrl="https://images.unsplash.com/photo-1561174356-638d86f24f04?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MjB8fGVudGVydGFpbm1lbnR8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60";
        categoryModalArrayList.add(new CategoryModal("All",allImageUrl));
        categoryModalArrayList.add(new CategoryModal("Technology",techImageUrl));
        categoryModalArrayList.add(new CategoryModal("Science",scienceImageUrl));
        categoryModalArrayList.add(new CategoryModal("Sports",sportsImageUrl));
        categoryModalArrayList.add(new CategoryModal("General",generalImageUrl));
        categoryModalArrayList.add(new CategoryModal("Business",businessImageUrl));
        categoryModalArrayList.add(new CategoryModal("Health",healthImageUrl));
        categoryModalArrayList.add(new CategoryModal("Entertainment",entertainmentImageUrl));
        categoryAdapter.notifyDataSetChanged();
    }

    private void getNews(String category) {
        loadingProgressBar.setVisibility(View.VISIBLE);
        articlesArrayList.clear();

        // Log.d("CCCCCCCCCCCCCCCC", country);

        String apiKey = "12ka442ka1";

        // String categoryURL = "https://newsapi.org/v2/top-headlines?country=in&category=" + category + "&apiKey=" + apiKey;
        // String url="https://newsapi.org/v2/top-headlines?country=us&apiKey=" + apiKey;

        String categoryURL = "https://newsapi.org/v2/top-headlines?country="+ country +"&category=" + category + "&apiKey=" + apiKey;
        String url="https://newsapi.org/v2/top-headlines?country="+ country +"&apiKey=" + apiKey;

        String baseURL="https://newsapi.org/";
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI=retrofit.create(RetrofitAPI.class);
        Call<NewsModal> call;
        if(category.equals("All"))
        {
            call=retrofitAPI.getAllNews(url);
        }
        else
        {
            call=retrofitAPI.getNewsByCategory(categoryURL);
        }

        call.enqueue(new Callback<NewsModal>() {
            @Override
            public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {
                NewsModal newsModal=response.body();
                loadingProgressBar.setVisibility(View.GONE);
                ArrayList<Articles> articles=newsModal.getArticles();
                for(int i=0;i<articles.size();i++)
                {
                    articlesArrayList.add(new Articles(
                                    articles.get(i).getTitle(),
                                    articles.get(i).getDescription(),
                                    articles.get(i).getUrlToImage(),
                                    articles.get(i).getUrl(),
                                    articles.get(i).getContent()
                            )
                    );
                }
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsModal> call, Throwable t) {
                Toast.makeText(MainActivity.this,"FAILED TO FETCH NEWS!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.countryItem) {
            //Toast.makeText(this, "Country selected!", Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.countryIndia) { // code = in
            String code="in";
            updateCountryAndReloadNews(code);
            Toast.makeText(this, "Country set to India.", Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.countryUnitedStatesofAmerica) { // code = us
            String code="us";
            updateCountryAndReloadNews(code);
            Toast.makeText(this, "Country set to United States of America.", Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.countryUnitedArabEmirates) { // code = ae
            String code="ae";
            updateCountryAndReloadNews(code);
            Toast.makeText(this, "Country set to United Arab Emirates.", Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.countryArgentina) { // code = ar
            String code="ar";
            updateCountryAndReloadNews(code);
            Toast.makeText(this, "Country set to Argentina.", Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.countryAustria) { // code = at
            String code="at";
            updateCountryAndReloadNews(code);
            Toast.makeText(this, "Country set to Austria.", Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.countryAustralia) { // code = au
            String code="au";
            updateCountryAndReloadNews(code);
            Toast.makeText(this, "Country set to Australia.", Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.countryBelgium) { // code = be
            String code="be";
            updateCountryAndReloadNews(code);
            Toast.makeText(this, "Country set to Belgium.", Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.countryBulgaria) { // code = bg
            String code="bg";
            updateCountryAndReloadNews(code);
            Toast.makeText(this, "Country set to Bulgaria.", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    public void onCategoryClick(int position) {
        String category=categoryModalArrayList.get(position).getCategory();
        getNews(category);
    }
}