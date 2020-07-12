package example.com.newsapp;

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

import java.util.ArrayList;
import java.util.List;


import example.com.newsapp.jinritoutiao.toutiao_NewsItem;
import example.com.newsapp.yulexinwen.Yule_News_Item;

/**
 * Created by yzz on 2020/7/8.
 */

public class ViewPage1_shouye extends Fragment{

    private Toolbar toolbar;
    private TabLayout tabLayout;



    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.viewpage,container,false);


        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);



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



        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(),list_fragment,titleList);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

}
