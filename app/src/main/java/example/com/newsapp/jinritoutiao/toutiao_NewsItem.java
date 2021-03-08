package example.com.newsapp.jinritoutiao;

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
import android.widget.Button;
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
import example.com.newsapp.SQL.ShouCang_NewsItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by yzz on 2020/7/8.
 */

public class toutiao_NewsItem extends Fragment {
    List<News> newsList=new ArrayList<>();              //所有的40条新闻
    RecyclerView recyclerView;
    List<News> redataList=new ArrayList<>();            //显示出来的新闻
    int renumber=8;

    RecycleViewAdapter adapter;

    private SwipeRefreshLayout swipRefresh;

    Button sc_button;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.activity_main,container,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclernews);


        HttpUtil.sendOkHttpRequest("http://c.m.163.com/nc/article/headline/T1348647853363/0-40.html", new Callback() {      //进行联网
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
                parseJSONWithJSONobject(info);                                           //解析网络数据，放进News，这里面有40条数据
                initdata();                                                             //显示了初始的8条数据
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.recyclernews);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);
                         adapter=new RecycleViewAdapter(redataList);
                        recyclerView.setAdapter(adapter);


                    }
                });
                swipRefresh = (SwipeRefreshLayout)view.findViewById(R.id.toutiao_swip_refresh);      //刷新
                swipRefresh.setColorSchemeResources(R.color.colorAccent);                                //设置颜色
                swipRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {          //设置刷新事件
                    @Override
                    public void onRefresh() {
                              if(renumber!=40){                                                         //如果数量不为40
                                  redata(newsList);                                                     //将总数据，带入分布数据
                                  adapter.notifyDataSetChanged();                                       //通知适配器数据更新
                              }else {
                                  Toast.makeText(getContext(),"没有新的新闻了",Toast.LENGTH_SHORT).show();
                              }
                              swipRefresh.setRefreshing(false);                                         //刷新结束
                    }
                });

            }
        });

        sc_button=(Button)view.findViewById(R.id.sc_button);
        sc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             ShouCang_NewsItem.actionStart(v.getContext());
            }
        });



        return view;
    }


    public void redata(List<News> newsList){
        for(int i=renumber;i<8+renumber;i++){
            redataList.add(0,newsList.get(i));
        }
        renumber=renumber+8;
    }


    public void initdata(){
        for(int i=0;i<8;i++){
            redataList.add(newsList.get(i));
        }
    }
    


    public void parseJSONWithJSONobject(String jsonadata){
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(jsonadata);

            JSONArray jsonArray=jsonObject.getJSONArray("T1348647853363");                                   //首先选定首个对象，分成多个数组
            for(int i=0;i<jsonArray.length();i++){
                String newsContent=jsonArray.getJSONObject(i).toString();                                      //
                News news=new Gson().fromJson(newsContent,News.class);
                Log.d("Demo",news.imgsrc);
                newsList.add(news);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




    public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>{

        List<News> mnewsList;

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
        public RecycleViewAdapter(List<News> newsList){
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
            News news=mnewsList.get(position);
            holder.fabuzhe.setText(news.source);
            holder.fabutime.setText(news.mtime.substring(0,10));
            holder.newstitle.setText(news.title);

            Glide.with(getContext())
                    .load(news.imgsrc)
                    .override(300, 300)
                    .fitCenter()
                    .into(holder.picture);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int left=v.getLeft();
                    int right=v.getRight();
                    int bottom=v.getBottom();
                    int top=v.getTop();
                    Log.d("Demo","left"+left);
                    Log.d("Demo","right"+right);
                    Log.d("Demo","bottom"+bottom);
                    Log.d("Demo","top"+top);

                    int position=holder.getAdapterPosition();                   //获得当前点击的位置
                    News news1=mnewsList.get(position);
                    NewsContent.actionStart(v.getContext(),news1.digest,news1.title,news1.imgsrc);          //将数据放进方法中，并启动页面详细布局
                }
            });




        }

        @Override
        public int getItemCount() {
            return mnewsList.size();
        }



    }


}
