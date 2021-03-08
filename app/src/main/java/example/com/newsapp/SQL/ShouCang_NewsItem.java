package example.com.newsapp.SQL;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import example.com.newsapp.R;
import example.com.newsapp.jinritoutiao.News;
import example.com.newsapp.jinritoutiao.NewsContent;

public class ShouCang_NewsItem extends AppCompatActivity {                              //这个类是收藏的列表
    MySQLhelper mySQLhelper;
    List<SC_News> sc_newsList=new ArrayList<>();                                        //收藏的列表的数据
    RecyclerView recyclerView;
    ShouCang_Adapter shouCang_adapter;
    int scrollPotion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shou_cang__news_item);                     //这个是新的，收藏列表的活动

    }

    @Override
    protected void onStart() {
        super.onStart();
        SC_SQL();                       //从收藏数据库中提取数据
         recyclerView=(RecyclerView)findViewById(R.id.shoucang_recyclerview);
         shouCang_adapter=new ShouCang_Adapter(sc_newsList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(shouCang_adapter);
        recyclerView.scrollToPosition(scrollPotion);




    }

    @Override
    protected void onStop() {
        super.onStop();
        sc_newsList.clear();


    }

    public static void actionStart(Context context){
       Intent intent=new Intent(context,ShouCang_NewsItem.class);
       context.startActivity(intent);
    }


    void SC_SQL(){                                                  //打开数据库，将数据放进SC_News类中，将各个类放进集合里
        String titleNews;
        String contentNews;
        String pictureNews;
         mySQLhelper=new MySQLhelper(ShouCang_NewsItem.this,"ShouCang.db",null,1);
        SQLiteDatabase db=mySQLhelper.getWritableDatabase();
        Cursor cursor=db.query("shoucang",null,null,null,null,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
            do{
                 titleNews=cursor.getString(cursor.getColumnIndex("titleNews"));
                 contentNews=cursor.getString(cursor.getColumnIndex("contentNews"));
                 pictureNews=cursor.getString(cursor.getColumnIndex("pictureNews"));
                SC_News sc_news=new SC_News(titleNews,contentNews,pictureNews);
                sc_newsList.add(0,sc_news);
            }while(cursor.moveToNext());
                cursor.close();
            }


        }
    }








    class ShouCang_Adapter extends RecyclerView.Adapter<ShouCang_Adapter.ViewHolder>{
        List<SC_News> msc_newsList;

        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView searchNewstitle;
            private TextView searchFabuzhe;
            private TextView searchFabutime;
            private ImageView searchPicture;



            public ViewHolder(View view) {
                super(view);
                searchNewstitle = (TextView)view.findViewById(R.id.search_newstitle);           //这些都不是新创建的，是旧的那个搜索的xml文件
                searchFabuzhe = (TextView) view.findViewById(R.id.search_fabuzhe);
                searchFabutime = (TextView) view.findViewById(R.id.search_fabutime);
                searchPicture = (ImageView) view.findViewById(R.id.search_picture);

            }
        }

        public ShouCang_Adapter(List<SC_News> sc_newsList){
            msc_newsList=sc_newsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_news_item,parent,false);     //注意这里，这是复用搜索的那个xml文件
            ViewHolder viewHolder=new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            SC_News sc_news=msc_newsList.get(position);
            holder.searchNewstitle.setText(sc_news.getTitleNews());
            Glide.with(ShouCang_NewsItem.this).load(sc_news.getPictureNews())
                    .override(300, 300)
                    .fitCenter()
                    .into(holder.searchPicture);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=holder.getAdapterPosition();
                    scrollPotion=position;                                      //当回到收藏页面所滚动到的相应页面下，
                                                                                 // 主要是为了在onStart（）方法里，回到原来的新闻位置
                    SC_News sc_news=msc_newsList.get(position);                 //得到点击的相应的类
                    NewsContent.actionStart(v.getContext(),sc_news.getContentNews(),sc_news.getTitleNews(),sc_news.getPictureNews());//启动相应的新闻详细布局
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShouCang_NewsItem.this);
                    builder.setIcon(android.R.drawable.ic_dialog_info);
                    builder.setTitle("温馨提示");
                    builder.setMessage("确定删除吗");
                    builder.setCancelable(true);

                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            int position=holder.getAdapterPosition();
                            String title=msc_newsList.get(position).getTitleNews();

                            mySQLhelper=new MySQLhelper(ShouCang_NewsItem.this,"ShouCang.db",null,1);
                            SQLiteDatabase db=mySQLhelper.getWritableDatabase();
                            db.delete("shoucang","titleNews=?",new String[]{title});
                            msc_newsList.remove(position);
                            shouCang_adapter.notifyDataSetChanged();

                            Toast.makeText(getApplicationContext(), "你已删除",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });
                    builder.create().show();
                    return true;
                }
            });


        }

        @Override
        public int getItemCount() {
          return msc_newsList.size();
        }



    }

}
