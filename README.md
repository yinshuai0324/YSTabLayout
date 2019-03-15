# **自适应TabLayout**
**作者:yinshuai**

**邮箱:1317972280@qq.com**

**个人博客:www.blog.ooimi.com**

## 效果图
![TabLayout](https://github.com/yinshuaiblog/YSTabLayout/blob/master/YSTablayout.gif)

## 使用说明
基本使用和官方的TabLayout的使用方法相同,示例代码如下:
```
public class MainActivity extends AppCompatActivity {
    private YSTabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();//初始化View
        setTabLayout();//设置TabLyout Item数据
        setViewPager();//设置适配器

    }

    public void initView(){
        tabLayout= (YSTabLayout) findViewById(R.id.tablayout);
        viewPager= (ViewPager) findViewById(R.id.viewpage);
    }

    public void setTabLayout(){
        tabLayout.setTabView(getTabView()).setViewPage(viewPager);//设置TabLayout数据源和ViewPage
    }

    public void setViewPager(){
        ViewPageAdapter v=new ViewPageAdapter(getView());
        viewPager.setAdapter(v);
    }
  }
```

## 属性和方法

**方法使用说明**

| 方法名     | 用途   |
| ------- | ----|
| setTabView()  |设置Tablayou数据源|
| setViewPage()  |设置ViewPage|

**属性使用说明**

| 属性名     | 用途   |
| ------- | ----|
| ys_margin|设置外边距(只对左右有效)|
| ys_lineColor  |设置线的颜色|
| ys_lineHeight|设置线的高度|
| ys_selectColor|设置选中时文字的颜色|
| ys_noSelectColor|设置未选中时文字的颜色|
| ys_textSize|设置文字大小|
| ys_backgroundColor|设置背景颜色|
| ys_overstriking|设置字体是否加粗|

