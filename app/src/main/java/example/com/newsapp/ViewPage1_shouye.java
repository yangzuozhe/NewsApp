package example.com.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by yzz on 2020/7/8.
 */

public class ViewPage1_shouye extends Fragment{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private EditText searchWord;
    private Button searchYes;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.viewpage,container,false);


        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        searchWord = (EditText) view.findViewById(R.id.search_word);
        searchYes = (Button) view.findViewById(R.id.search_yes);
        initToolBar();
        searchYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(searchWord.getText()!=null){
                    String s=searchWord.getText().toString();
                    search_Word(s);

                }else if(searchWord.getText().toString().equals("")){
                    Toast.makeText(v.getContext(),"请填写内容",Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;
        private List<String> titleList;

        public ViewPagerAdapter(FragmentManager fm,List<Fragment> fragmentList, List<String> titleList) {
            super(fm);
            this.fragmentList=fragmentList;
            this.titleList=titleList;
        }


        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }

    private void initToolBar() {

        List<Fragment> list_fragment = new ArrayList<>();
        List<String>  titleList=new ArrayList<>();
        list_fragment.add(new toutiao_NewsItem());
        titleList.add("头条");
        list_fragment.add(new Yule_News_Item());
        titleList.add("娱乐");
        list_fragment.add(new Sprot_News_Item());
        titleList.add("体育");


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(),list_fragment,titleList);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

    }

    public void search_Word(String word){
        HttpUtil.sendOkHttpRequest("http://api.avatardata.cn/ActNews/Query?key=8528c731e80e4592aa05a86a829997c3&keyword=" + word, new Callback() {
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
                Intent intent=new Intent(getContext(), Search_New_Item.class);
                intent.putExtra("info",info);
                startActivity(intent);



                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        searchWord.setText("");
                    }
                });
            }
        });
    }


}
