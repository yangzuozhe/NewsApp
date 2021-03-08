package example.com.newsapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import example.com.newsapp.jinritoutiao.toutiao_NewsItem;
import example.com.newsapp.search_news.Search_New_Item;
import example.com.newsapp.sport_news.Sprot_News_Item;
import example.com.newsapp.yulexinwen.Yule_News_Item;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.TlsVersion;

/**
 * Created by yzz on 2020/7/8.
 */

public class ViewPage1_shouYe_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView wenzhi;
    private EditText searchWord;
    private Button searchYes;

    private long exitTime = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpage);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        wenzhi = (TextView) findViewById(R.id.wenzhi);
        searchWord = (EditText) findViewById(R.id.search_word);
        searchYes = (Button) findViewById(R.id.search_yes);
        initToolBar();
        searchYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchWord.getText() != null) {
                    String s = searchWord.getText().toString();
                    search_Word(s);

                } else if (searchWord.getText().toString().equals("")) {
                    Toast.makeText(v.getContext(), "请填写内容", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void initToolBar() {

        List<Fragment> list_fragment = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        list_fragment.add(new toutiao_NewsItem());
        titleList.add("头条");
        list_fragment.add(new Yule_News_Item());
        titleList.add("娱乐");
        list_fragment.add(new Sprot_News_Item());
        titleList.add("体育");

        //viewpage的适配器，需要传入碎片的manager，传入碎片集合，传入标题集合titleList是为了和tablayout进行配合，才要传入的参数
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), list_fragment, titleList);
        viewPager.setAdapter(viewPagerAdapter);                             //给主viewpage设置适配器
        tabLayout.setupWithViewPager(viewPager);                           //将tablayout和viewpage进行绑定，这里注意上面的参数titleList一i的那个要有不然就会闪退
        tabLayout.setTabMode(TabLayout.MODE_FIXED);                         //将tablayout上的文字按比例充满
        viewPager.setOffscreenPageLimit(2);                                  //预加载左右两侧的页面，这样子画面就不会白屏


    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {                //viewpage的适配器类
        private List<Fragment> fragmentList;
        private List<String> titleList;

        public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {      //构造方法传入参数
            super(fm);                                                                      //super（）调用父类构造方法FragmentPagerAdapter（）
            this.fragmentList = fragmentList;
            this.titleList = titleList;
        }


        @Override
        public Fragment getItem(int position) {             //返回指定碎片，viewpage必须写

            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();                     //获取碎片数量，viewpage必须写
        }

        @Override
        public CharSequence getPageTitle(int position) {      //为了rangtablayout有标题名称要写
            return titleList.get(position);
        }
    }


    public void search_Word(String word) {                                                             //查询网络接口
        HttpUtil.sendOkHttpRequest("http://api.avatardata.cn/ActNews/Query?key=8528c731e80e4592aa05a86a829997c3&keyword=" + word, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {                                         //网络连接失败的时候
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ViewPage1_shouYe_Activity.this, "网络出问题了", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {                //网络连接成功
                String info = response.body().string();                                                      //获得网路中数据
                Intent intent = new Intent(ViewPage1_shouYe_Activity.this, Search_New_Item.class);      //跳转到搜索列表
                intent.putExtra("info", info);                                                           //传递网络信息
                startActivity(intent);                                                                     //跳转

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        searchWord.setText("");                                                          //跳转后，搜索框清空
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() - exitTime > 2000) {                                                   //如果间隔大于两秒
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
            Log.d("Demo", exitTime + "");
        } else {
            super.onBackPressed();
        }

    }


}
