package com.example.admin.ystablayout;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.ystablayout.view.YSTabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private YSTabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setTabLayout();
        setViewPager();

    }

    public void initView(){
        tabLayout= (YSTabLayout) findViewById(R.id.tablayout);
        viewPager= (ViewPager) findViewById(R.id.viewpage);
    }

    public void setTabLayout(){
        tabLayout.setTabView(getTabView()).setViewPage(viewPager);
    }

    public void setViewPager(){
        ViewPageAdapter v=new ViewPageAdapter(getView());
        viewPager.setAdapter(v);
    }

    /**
     * 获取view
     * @return
     */
    public List<View> getView(){
        List<View> views=new ArrayList<>();
        LayoutInflater lt=LayoutInflater.from(MainActivity.this);
        View view1=lt.inflate(R.layout.view1,null);
        View view2=lt.inflate(R.layout.view2,null);
        View view3=lt.inflate(R.layout.view3,null);
        View view4=lt.inflate(R.layout.view4,null);
        View view5=lt.inflate(R.layout.view5,null);
        View view6=lt.inflate(R.layout.view6,null);
        View view7=lt.inflate(R.layout.view7,null);
        View view8=lt.inflate(R.layout.view8,null);
        View view9=lt.inflate(R.layout.view9,null);
        View view10=lt.inflate(R.layout.view10,null);
        View view11=lt.inflate(R.layout.view11,null);
        View view12=lt.inflate(R.layout.view12,null);
        View view13=lt.inflate(R.layout.view13,null);
        View view14=lt.inflate(R.layout.view14,null);
        View view15=lt.inflate(R.layout.view15,null);

        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        views.add(view5);
        views.add(view6);
        views.add(view7);
        views.add(view8);
        views.add(view9);
        views.add(view10);
        views.add(view11);
        views.add(view12);
        views.add(view13);
        views.add(view14);
        views.add(view15);
        return views;
    }

    public List<String> getTabView(){
        List<String> list=new ArrayList<>();
        list.add("新闻");
        list.add("实时新闻");
        list.add("最新新闻");
        list.add("图片");
        list.add("段子");
        list.add("最新的");
        list.add("搞笑吧");
        list.add("军事大新闻");
        list.add("媒体");
        list.add("多媒体");
        list.add("今天最新的新闻");
        list.add("搞笑图片");
        list.add("昨日新闻");
        list.add("回顾");
        list.add("最新回顾");
        return list;
    }


    /**
     * ViewPage适配器
     */
    class ViewPageAdapter extends PagerAdapter {
        private List<View> views;
        public ViewPageAdapter(List<View> views){
            this.views=views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }
}
