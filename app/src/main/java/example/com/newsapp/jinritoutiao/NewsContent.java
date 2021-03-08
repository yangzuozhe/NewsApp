package example.com.newsapp.jinritoutiao;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import example.com.newsapp.R;
import example.com.newsapp.SQL.MySQLhelper;

public class NewsContent extends AppCompatActivity {

    private TextView titleNews;
    private TextView contentNews;
    private ImageView pictureNews;
    String content;
    String title;
    String picture;

    private ImageButton shoucangButton;
    MySQLhelper mySQLhelper;                            //数据库helper类
    boolean shuocang_zhuangtai=false;                   //收藏默认是不确认的


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        titleNews = (TextView) findViewById(R.id.title_news);
        contentNews = (TextView) findViewById(R.id.content_news);
        pictureNews = (ImageView) findViewById(R.id.picture_news);
        shoucangButton = (ImageButton) findViewById(R.id.shoucang_button);
        //页面详情布局数据带入
          content=getIntent().getStringExtra("content");
          title=getIntent().getStringExtra("title");
         picture=getIntent().getStringExtra("picture");


        mySQLhelper=new MySQLhelper(NewsContent.this,"ShouCang.db",null,1);                         //创建数据库

        SQLiteDatabase db=mySQLhelper.getWritableDatabase();                                            //打开数据库

        Cursor cursor= db.query("shoucang",null,"titleNews=?",new String[]{title},null,null,null);  //根据数据库里的数据得到当前收藏的状态
        if(cursor!=null) {                                                                               //如果收藏数据库里有该标题，则不为空，不为空的话，进行查询
           if(cursor.moveToFirst()){
               do{
                   int number=cursor.getInt(cursor.getColumnIndex("shuocang_zhuangtai"));
                   shuocang_zhuangtai = SQL_toshuocang(number);                                         ////如果为1，转为true，如果为0转为false
               }while (cursor.moveToNext());
           }
            cursor.close();
        }

        contentNews.setText(content);
        titleNews.setText(title);
        Glide.with(this).load(picture).into(pictureNews);
       if(shuocang_zhuangtai){                                                                          //判断状态
           Glide.with(NewsContent.this).load(R.drawable.shoucang_yes).into(shoucangButton);         //收藏按钮的确认状态
       }else {
           Glide.with(NewsContent.this).load(R.drawable.shoucang_cancle).into(shoucangButton);         //收藏按钮的取消状态
       }




        //收藏按钮的点击事件和数据带入数据库
        shoucangButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuocang_zhuangtai();

                if(shuocang_zhuangtai){                                 //如果收藏状态为true，则将数据存入数据库，如果收藏状态为false，则将数据从数据库中删除
                    SQLiteDatabase db= mySQLhelper.getWritableDatabase();
                    ContentValues values=new ContentValues();
                    values.put("titleNews",title);
                    values.put("contentNews",content);
                    values.put("pictureNews",picture);
                    values.put("shuocang_zhuangtai",shuocang_toSQL(shuocang_zhuangtai));
                    db.insert("shoucang",null,values);
                    values.clear();
                    Glide.with(NewsContent.this).load(R.drawable.shoucang_yes).into(shoucangButton);
                }else {
                    SQLiteDatabase db=mySQLhelper.getWritableDatabase();
                    db.delete("shoucang","titleNews=?",new String[]{title});
                    Glide.with(NewsContent.this).load(R.drawable.shoucang_cancle).into(shoucangButton);
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    boolean shuocang_zhuangtai(){
        if(shuocang_zhuangtai==true){
            return shuocang_zhuangtai=false;
        }else {
            return shuocang_zhuangtai=true;
        }

    }
     int shuocang_toSQL(boolean shuocang_zhuangtai){
        if(shuocang_zhuangtai){
            return 1;
        }else {
            return 0;
        }
     }

     boolean SQL_toshuocang(int SQLnumber){
         if(SQLnumber==1){
             return true;
         }else {
             return false;
         }
     }

    public static void actionStart(Context context, String content, String title, String picture){
        Intent intent=new Intent(context,NewsContent.class);
        intent.putExtra("content",content);
        intent.putExtra("title",title);
        intent.putExtra("picture",picture);
        context.startActivity(intent);
    }

}
