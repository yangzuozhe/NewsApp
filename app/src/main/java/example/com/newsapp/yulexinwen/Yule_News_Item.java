package example.com.newsapp.yulexinwen;

import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import example.com.newsapp.jinritoutiao.NewsContent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by yzz on 2020/7/8.
 */

public class Yule_News_Item extends Fragment{
    @Nullable
    private TextView yuleNewstitle;
    private TextView yuleFabuzhe;
    private TextView yuleFabutime;
    private ImageView yulePicture;

    List<Yule_News> yule_newsList=new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         final View view=inflater.inflate(R.layout.yule_news_recyclerview,container,false);
        yuleNewstitle = (TextView) view.findViewById(R.id.yule_newstitle);
        yuleFabuzhe = (TextView) view.findViewById(R.id.yule_fabuzhe);
        yuleFabutime = (TextView) view.findViewById(R.id.yule_fabutime);
        yulePicture = (ImageView) view.findViewById(R.id.yule_picture);

        //这里联网的方法
        HttpUtil.sendOkHttpRequest("http://c.3g.163.com/nc/article/list/T1348648517839/0-20.html", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {                                   //联网失败
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"网络出问题了", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {           //联网成功
                String info=response.body().string();
                parseJSONWithJSONobject(info);
               getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.yule_news_recyclerview);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);
                        yule_RecycleViewAdapter adapter=new yule_RecycleViewAdapter(yule_newsList);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });

        return view;
    }

    public void parseJSONWithJSONobject(String jsonadata){
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(jsonadata);

            JSONArray jsonArray=jsonObject.getJSONArray("T1348648517839");
            for(int i=0;i<jsonArray.length();i++){
                String newsContent=jsonArray.getJSONObject(i).toString();
                Yule_News news=new Gson().fromJson(newsContent,Yule_News.class);
                Log.d("Demo",news.title);
                yule_newsList.add(news);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }






    //底下是娱乐Rcycleview适配器
    class yule_RecycleViewAdapter extends RecyclerView.Adapter<yule_RecycleViewAdapter.ViewHolder>{

        List<Yule_News> mnewsList;

        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView newstitle;
            private TextView fabuzhe;
            private TextView fabutime;
            private ImageView picture;
            public ViewHolder(View itemView) {
                super(itemView);
                newstitle = (TextView) itemView.findViewById(R.id.newstitle);
                fabuzhe = (TextView) itemView.findViewById(R.id.fabuzhe);
                fabutime = (TextView) itemView.findViewById(R.id.fabutime);
                picture = (ImageView) itemView.findViewById(R.id.picture);

            }
        }
        public yule_RecycleViewAdapter(List<Yule_News> newsList){
            mnewsList=newsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.newstitle,parent,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Yule_News news=mnewsList.get(position);
            holder.fabuzhe.setText(news.source);
            holder.fabutime.setText(news.mtime.substring(0,10));
            holder.newstitle.setText(news.title);


            Glide.with(Yule_News_Item.this)
                    .load(news.imgsrc)
                    .override(300, 300)
                    .fitCenter()
                    .into(holder.picture);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=holder.getAdapterPosition();
                    Yule_News news1=mnewsList.get(position);
                    NewsContent.actionStart(v.getContext(),news1.digest,news1.title,news1.imgsrc);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mnewsList.size();
        }



    }




}
