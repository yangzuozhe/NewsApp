package example.com.newsapp.jinritoutiao;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import example.com.newsapp.R;

public class NewsContent extends AppCompatActivity {

    private TextView titleNews;
    private TextView contentNews;
    private ImageView pictureNews;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        titleNews = (TextView) findViewById(R.id.title_news);
        contentNews = (TextView) findViewById(R.id.content_news);
        pictureNews = (ImageView) findViewById(R.id.picture_news);
        String content=getIntent().getStringExtra("content");
        String title=getIntent().getStringExtra("title");
        String picture=getIntent().getStringExtra("picture");
        contentNews.setText(content);
        titleNews.setText(title);
        Glide.with(this).load(picture).into(pictureNews);



    }

    public static void actionStart(Context context, String content, String title, String picture){
        Intent intent=new Intent(context,NewsContent.class);
        intent.putExtra("content",content);
        intent.putExtra("title",title);
        intent.putExtra("picture",picture);
        context.startActivity(intent);
    }

}
