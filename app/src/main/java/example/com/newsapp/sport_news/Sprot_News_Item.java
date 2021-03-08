package example.com.newsapp.sport_news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import example.com.newsapp.HttpUtil;
import example.com.newsapp.R;
import example.com.newsapp.jinritoutiao.News;
import example.com.newsapp.jinritoutiao.NewsContent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by yzz on 2020/7/8.
 */

public class Sprot_News_Item extends Fragment {


    List<Sport_News> sport_newsList=new ArrayList<>();                                          //所有的19条数据
    List<Sport_News> resport_data=new ArrayList<>();                                            //要显示的数据
    int renumber=8;
    private SwipeRefreshLayout sportSwipRefresh;
    Sport_RecyclerViewAdapter sport_recyclerViewAdapter;
    RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.sport_news_rectclerview,container,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.sport_recyclerView);
        HttpUtil.sendOkHttpRequest("http://c.3g.163.com/nc/article/list/T1348649079062/0-20.html", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"网络出问题了", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            String info=response.body().string();
            JSONwithGson(info);
                initdata();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sport_recyclerViewAdapter=new Sport_RecyclerViewAdapter(resport_data);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(sport_recyclerViewAdapter);

                    }
                });
            }

        });



        sportSwipRefresh = (SwipeRefreshLayout) view.findViewById(R.id.sport_swip_refresh);
        sportSwipRefresh.setColorSchemeResources(R.color.colorAccent);
        sportSwipRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(renumber!=24){
                    redata(sport_newsList);
                    sport_recyclerViewAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getContext(),"没有新的新闻了",Toast.LENGTH_SHORT).show();
                }
                sportSwipRefresh.setRefreshing(false);
            }
        });




        return view;
    }

    public void redata(List<Sport_News> newsList){
        int k=8;
        if(renumber==16){
            k=3;
        }

        for(int i=renumber;i<k+renumber;i++){
            resport_data.add(0,sport_newsList.get(i));
        }
        renumber=renumber+8;
    }


    public void initdata(){
        for(int i=0;i<8;i++){
            resport_data.add(sport_newsList.get(i));
        }
    }


    public void JSONwithGson(String data){
        try {
            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonArray=jsonObject.getJSONArray("T1348649079062");
            for(int i=0;i<jsonArray.length();i++){
                String newsConten=jsonArray.getJSONObject(i).toString();
                Sport_News sport_news=new Gson().fromJson(newsConten,Sport_News.class);
                sport_newsList.add(sport_news);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class Sport_RecyclerViewAdapter extends RecyclerView.Adapter<Sport_RecyclerViewAdapter.ViewHolder>{
       List<Sport_News> msport_newsList;
        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView sprotNewstitle;
            private TextView sportFabuzhe;
            private TextView sportFabutime;
            private ImageView sportPicture;
            public ViewHolder(View view) {
                super(view);
                sprotNewstitle = (TextView) view.findViewById(R.id.sprot_newstitle);
                sportFabuzhe = (TextView) view.findViewById(R.id.sport_fabuzhe);
                sportFabutime = (TextView) view.findViewById(R.id.sport_fabutime);
                sportPicture = (ImageView) view.findViewById(R.id.sport_picture);

            }
        }
        public  Sport_RecyclerViewAdapter(List<Sport_News> sport_newsList){
            msport_newsList=sport_newsList;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(getContext()).inflate(R.layout.sport_news_title,parent,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            Sport_News sport_news=msport_newsList.get(position);
            holder.sprotNewstitle.setText(sport_news.title);
            holder.sportFabutime.setText(sport_news.mtime.substring(0,16));
            holder.sportFabuzhe.setText(sport_news.source);
            Glide.with(getContext())
                    .load(sport_news.imgsrc)
                    .override(300, 300)
                    .fitCenter()
                    .into(holder.sportPicture);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position1=holder.getAdapterPosition();
                    Sport_News sport_news1=msport_newsList.get(position);
                    NewsContent.actionStart(getContext(),sport_news1.digest,sport_news1.title,sport_news1.imgsrc);


                }
            });
        }

        @Override
        public int getItemCount() {
            return msport_newsList.size();
        }


     }



}
