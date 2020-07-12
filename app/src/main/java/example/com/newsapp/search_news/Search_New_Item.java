package example.com.newsapp.search_news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import example.com.newsapp.R;
import example.com.newsapp.jinritoutiao.NewsContent;

/**
 * Created by yzz on 2020/7/8.
 */

public class Search_New_Item extends AppCompatActivity {
    List<Search_News> search_newsList=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_news_recyclerview);
        String info=getIntent().getStringExtra("info");
        JSONObjectData(info);
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.search_recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(Search_New_Item.this);
        Search_RecyclerView adpter=new Search_RecyclerView(search_newsList);
        recyclerView.setAdapter(adpter);
        recyclerView.setLayoutManager(layoutManager);

    }

    class Search_RecyclerView extends RecyclerView.Adapter<Search_RecyclerView.ViewHold>{

        List<Search_News> msearch_newsList;


        class ViewHold extends RecyclerView.ViewHolder{
            private TextView searchNewstitle;
            private TextView searchFabuzhe;
            private TextView searchFabutime;
            private ImageView searchPicture;

            public ViewHold(View itemView) {
                super(itemView);
                searchNewstitle = (TextView)itemView. findViewById(R.id.search_newstitle);
                searchFabuzhe = (TextView) itemView.findViewById(R.id.search_fabuzhe);
                searchFabutime = (TextView) itemView.findViewById(R.id.search_fabutime);
                searchPicture = (ImageView) itemView.findViewById(R.id.search_picture);

            }
        }
        public Search_RecyclerView(List<Search_News> search_newsList){
            msearch_newsList=search_newsList;
        }

        @Override
        public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(Search_New_Item.this).inflate(R.layout.search_news_item,parent,false);
            ViewHold viewHold=new ViewHold(view);
            return viewHold;
        }

        @Override
        public void onBindViewHolder(final ViewHold holder, int position) {
            Search_News search_news=msearch_newsList.get(position);
            holder.searchFabutime.setText(search_news.time);
            holder.searchFabuzhe.setText(search_news.src);
            holder.searchNewstitle.setText(search_news.title);
            Glide.with(Search_New_Item.this)
                    .load(search_news.img)
                    .override(300, 300)
                    .fitCenter()
                    .into(holder.searchPicture);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=holder.getAdapterPosition();
                    Search_News search_news1=msearch_newsList.get(position);
                    NewsContent.actionStart(Search_New_Item.this,search_news1.content,search_news1.title,search_news1.img);
                }
            });

        }

        @Override
        public int getItemCount() {
            return msearch_newsList.size();
        }

    }
    public void JSONObjectData(String data){
        try {
            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonArray=jsonObject.getJSONArray("result");
            for(int i=0;i<jsonArray.length();i++){
                String newsContent=jsonArray.getJSONObject(i).toString();
                Search_News search_news=new Gson().fromJson(newsContent,Search_News.class);
                Log.d("Demo",search_news.title);
                search_newsList.add(search_news);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
