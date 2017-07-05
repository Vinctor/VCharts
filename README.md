# VCharts

## <span id="preview">Preview</span>([demo](apk/app-debug.apk))( [使用](#user-content-usagedemo))

![bar](screenshot/diagram.png)
![bar](screenshot/sigle_has.png) ![bar](screenshot/single_no.png) ![bar](screenshot/multi_has.png) ![bar](screenshot/multi_no.png)  ![line](screenshot/line.gif)![bar](screenshot/lin3.png) ![bar](screenshot/compare.gif)![line](screenshot/ring_has.png) ![line](screenshot/ring_no.png)
![radar](screenshot/radar.png)![line](screenshot/dount.png)
![bar](screenshot/sector.png)![bar](screenshot/progress.gif)

## 适配AUTO([AutoLayout](https://github.com/hongyangAndroid/AndroidAutoLayout))
![line](screenshot/auto.gif)

>view.setAuto(boolean);//默认开启

## usage([demo](apk/app-debug.apk))

>compile 'com.vinctor:vcharts:x.y.z'

lastest version：x.y.z-> ![Download](https://api.bintray.com/packages/xcht1209/maven/vcharts/images/download.svg)

### 雷达图

添加至```xml```中

     <com.vinctor.vchartviews.radar.RadarChart
            android:id="@+id/radarview"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
            
```java```代码中

           radarView.setCount(6)//多边形几条边
                .setDensity(6)//雷达图蜘蛛网密度
                .setMinAndMax(0, 100)//最小与最大值
                .setAlpha(150)//雷达图数据遮盖透明度
                .clearData()//清楚数据
                .setList(list)//设置数据
                .addData(data)//添加数据
                .setTitleTextSize(30)//雷达边角标题文字大小(px)默认30
                .setTagTextSize(30)//雷达刻度文字大小
                .setTitles(new String[]{"语文", "数学", "英语", "物理", "化学", "生物"})//边角文字
                .commit();//以上设置需要此方法才能生效

### 单条柱状图

添加至```xml```中

    <com.vinctor.vchartviews.bar.BarCharSingle
        android:id="@+id/bar"
        android:layout_width="match_parent"
        android:layout_height="400dp"
        app:barTextSize="28px"
        app:barTitleMargin="28px"
        app:barWidth="58px"
        app:density="5"
        app:gradutaionColor="@color/$d2d2d2"
        app:gradutaionLineWidth="2px"
        app:gradutionTextSize="26px"
        app:max="100"
        app:min="0"
        app:titleColor="@color/$999999"
        app:titleTextSize="32px"/>
        
```java```代码中

      singleBar = (BarCharSingle) findViewById(R.id.bar_single);
        singleBar.setShowGraduation(true)
                .setMinAndMax(50, 100)
                .setDensity(4)//数值方向的刻度密度
                .setBarWidth(30)//柱状图宽度.默认为宽度的1/10
                .setGraduationTextSize(30)//左侧刻度的文字大小
                .setTitleTextSize(30)//底部文字大小
                .setBarTextSize(30)//柱状图上方数字大小
                .commit();
        singleBar.setData(new BarDataSingle("语文", 0, Color.BLUE))
                .addData(new BarDataSingle("数学", 80, Color.RED))
                .addData(new BarDataSingle("英语", 120, Color.MAGENTA))
                .addData(new BarDataSingle("物理", 60, Color.GREEN))
                .commit();
                
                
### 多条柱状图

添加至```xml```中

    <com.vinctor.vchartviews.bar.BarCharMulti
                    android:id="@+id/bar"
                    android:layout_width="match_parent"
                    android:layout_height="500px"
                    android:layout_marginTop="34px"
                    app:barTextSize="28px"
                    app:barTitleMargin="28px"
                    app:barWidth="58px"
                    app:density="5"
                    app:gradutaionColor="@color/$d2d2d2"
                    app:gradutaionLineWidth="2px"
                    app:gradutionTextSize="26px"
                    app:max="100"
                    app:min="0"
                    app:titleColor="@color/$999999"
                    app:titleTextSize="32px" />
        
```java```代码中

       multiBar = (BarCharMulti) findViewById(R.id.bar_multi);
        multiBar.setShowGraduation(true)
                .setMinAndMax(0, 100)
                .setShowGraduation(false)
                .setDensity(4)//数值方向的刻度密度
                .setBarWidth(30)//柱状图宽度.默认为宽度的1/10
                .setGraduationTextSize(30)//左侧刻度的文字大小
                .setTitleTextSize(30)//底部文字大小
                .setBarTextSize(30)//柱状图上方数字大小
                .commit();
        List<SingleData> singles = new ArrayList<>();
        singles.add(new SingleData(90, Color.BLUE));
        singles.add(new SingleData(80, Color.RED));
        singles.add(new SingleData(40, Color.DKGRAY));
        
        List<SingleData> singles2 = new ArrayList<>();
        singles2.add(new SingleData(120, Color.MAGENTA));
        singles2.add(new SingleData(60, Color.GREEN));
        singles2.add(new SingleData(30, Color.CYAN));
        
         multiBar.setBarGroupCount(3)//设置每组中柱状图的个数
                    .addData(new BarDataMulti(singles, "语文"))
                    .addData(new BarDataMulti(singles2, "数学"))
                    .commit();
                  
### 折线图

添加至```xml```中

      <com.vinctor.vchartviews.line.LineChart
                    android:id="@+id/line"
                    android:layout_width="match_parent"
                    android:layout_height="500px"
                    android:layout_marginTop="34px"
                    app:lineGradutaionColor="@color/$d2d2d2"
                    app:lineGradutaionLineWidth="2px"
                    app:lineGradutionTextSize="26px"
                    app:circleClickOffset="12px"//点击区域偏移
                    app:lineMax="100"
                    app:lineMin="0"
                    app:lineTitleColor="@color/$9b9b9b"
                    app:lineTitleTextSize="32px"
                    app:linedensity="5" />
        
```java```中

      line.setDensity(5)
                 .setHorizontalOpen(true)//---是否左右开放,无坐标轴
                .setShowHorGraduation(false)//---在setHorizontalOpen(false)的前提下,设置是否按照setDensity(int)显示刻度线
                .setSpecialLineNum(60.3f)//---在setHorizontalOpen(false)的前提下,设置特殊刻度(比如合格线)
                .setShowTagBack(false)//---设置是否显示数字标签的背景,默认true
                .setShowAllTag(true)//----设置是否显示全部的数字标签,默认为false
                .setCoordinateRectLineWidth(10)//---设置刻度矩形的线宽
                .setSpecialLineWidth(10)//--设置setSpecialLineNum(float)中特殊线的线宽

                .setShowAnimation(false)//设置绘制时是否显示动画
                .setDensity(5)//设置刻度密度
                .setAllowClickShowTag(true)//设置是否允许点击节点显示当前线的tag
                .setLineSmoothness(0f)//--设置曲线的平滑系数(0.0f~0.5f),默认0.4
                .setCoordinateTextSize(30)//设置刻度文字的大小
                .setTagTextSize(40)//设置数字标签的字体大小(px)
                  .setTitleTextSize(30)//底部标题大小
                .setLineSmoothness(0.3f)//折线平滑度
                .setCoordinateTextSize(30)//刻度文字大小
                .setShowTag(false)//点击圆圈是否显示tag
                .setCoorinateColor(0xff888888)//刻度文字颜色
                .setLineStrokeWidth(8)//网格线宽度
                .setTitles(new String[]{"语文", "数学", "英语", "物理", "化学"})//底部标题,需与折线数据长度一致
                .setTitleTextSize(30)//底部标题文字大小
                .setMinAndMax(-100, 100)
                .addData(new LineData(new int[]{20, 50, 20, 70, 0}, 0xff61B6E7))//需与title长度一致
                .addData(new LineData(new int[]{30, 80, 50, 60, 100}, 0xffF8AC58))
                .addData(new LineData(new int[]{-10, 30, 60, 80, 1500}, 0xffF593A0))
                .commit();
       //底部title点击事件
        line.setOnTitleClickListener(new LineChart.OnTitleClickListener() {
            @Override
            public void onClick(LineChart linechart, String title, int index) {
                ToastUtil.show(title + "index--" + index);
            }
        });
                
### 环形图(三角标签)

```xml```中

     <com.vinctor.vchartviews.ring.RingChart
        android:id="@+id/pie"
        android:layout_width="match_parent"
        android:layout_height="400dp" />
        
 ```java```:
 
     chart = (RingChart) findViewById(R.id.pie);

        List<Data> datas = new ArrayList<>();
        datas.add(new Data(1, "1人掌握"));
        datas.add(new Data(17, "17人掌握"));
        datas.add(new Data(17, "17人掌握"));
        datas.add(new Data(2, "2人掌握"));
        datas.add(new Data(3, "3人掌握"));
        datas.add(new Data(16, "16人掌握"));
        datas.add(new Data(17, "17人掌握"));
        datas.add(new Data(1, "1人掌握"));

        chart.setMaxRingWidth(100)
                .setMinRingWidth(70)
                .setData(new RingData(datas,
                        new int[]{0xff5EB9EE, 0xffC9E9FE, 0xff3B8DBD, 0xff31769F, Color.GREEN, Color.CYAN, 0xff3176eF, 0xff3f769F,}));
  
  
### 环形图(折线标注)
```xml```中

     <com.vinctor.vchartviews.dount.DountView
        android:id="@+id/dount"
        android:layout_width="match_parent"
        app:dountWidth="80px"
        app:spaceAngle="5"
        app:tagLineColor="#4E9AC1"
        app:tagLineWidth="3px"
        app:tagTextColor="#E47C6D"
        app:tagTextSize="30px"
        android:layout_height="400dp" />
             
```java```中

        dountView.clearList()///清楚数据
                .addData(new DountData(5))
                .addData(new DountData(200, 0x3468A4))//自定义圆环块颜色
                .addData(new DountData(570))
                .addData(new DountData(4))
                .addData(new DountData(5))
                .addData(new DountData(6))
                .addData(new DountData(5))
                .setDountWidth(80)//圆环的宽度
                .setSpaceAngle(5)//白色间隔的角度
                .setTagLineWidth(3)//标签线的线宽
                .setTagLineColor(0x4E9AC1)//标签线的颜色
                .setTagTextColor(0xE47C6D)//标签文字颜色
                .setTextSize(30)//标签文字大小
                .setOnShowTagCallBack(new DountView.onShowTagCallBack() {//显示文字回调
                    @Override
                    public String onShowTag(int num, int position, @DountView.DountNumType int tag) {
                        //数字类型
                        // NUM_TYPE_DEFAULT 正常
                        // NUM_TYPE_MAX  最大
                        // NUM_TYPE_MIN  最小
                        if (num == 200) {
                            return num + ",自定义颜色";
                        }
                        if (tag == DountView.NUM_TYPE_MAX) {
                            return num + ",这是最大值!";
                        }
                        if (tag == DountView.NUM_TYPE_MIN) {
                            return num + ",这是最小值!";
                        }
                        return num + "个人";
                    }
                })
                .commit();//提交进行绘制
  
### 图例

#### 单图例

      <com.vinctor.vchartviews.diagram.DiagramView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:content="图例"
        app:contentColor="#9b9b9b"
        app:contentMargin="14px"
        app:contentSize="24px"
        app:viewColor="#9b9b9b"
        app:viewSize="20px" />
        
 #### 多图例

```xml```中

      <com.vinctor.vchartviews.diagram.DiagramFlowLayout
        android:id="@+id/flowlayout"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginBottom="18px"
        app:contentColor="#9b9b9b"
        app:contentMargin="14px"
        app:contentSize="24px"
        app:viewSize="20px" />
        
```java```中

      DiagramFlowLayout flowLayout = (DiagramFlowLayout) findViewById(R.id.flowlayout);
        List<DiagramData> list = new ArrayList<>();
        list.add(new DiagramData(0xff3B8DBD, "0~25个知识点"));
        list.add(new DiagramData(0xff30769F, "25~50个知识点"));
        list.add(new DiagramData(0xffC8E9FE, "50~75个知识点"));
        list.add(new DiagramData(0xff5EB9EE, "75~100个知识点"));
        flowLayout.setList(list);

### 对比图
```xml```中

     <com.vinctor.vchartviews.compare.CompareView
             android:id="@+id/compare"
             android:layout_width="match_parent"
             android:layout_height="400px"
             android:background="#ff4A90E2"
             app:borderColor="#ff2f629d"     //描边颜色
             app:circleStrokeWidth="8px"     //圆形描边宽度
             app:imgRes="@mipmap/ic_launcher"//中间图片
             app:lineColor="#ffffffff"       //底部线条颜色
             app:lineStrokeWidth="8px"       //底部线条宽度
             app:maxNum="100"                //最大
             app:minNum="0"                  //最小
             app:moreDataColor="#ffff7e76"   //两者中较大的数据条填充颜色
             app:progressHeight="30px"       //数据条高度
             app:progressStrokeWidth="2px"   //数据条描边宽度
             app:radius="70px" />            //中间圆形半径
             
### 扇形图 
```xml```中

      <com.vinctor.vchartviews.sector.SectorView
             android:id="@+id/view"
             android:layout_width="match_parent"
             android:layout_height="400dp" />
             
```java```中

       view = (SectorView) findViewById(R.id.view);
             view.setOnShowDescriptionLinstener(new SectorView.OnShowDescriptionLinstener() {
                 @Override
                 public String onShowDes(int num) {
                     return num + "个知识点";
                 }
             });
             view.setDescriptionTextSize(30)
                     .setBorderWidth(8)
                     .setData(new SectorData(120, 0xff2CB072, 0xff186D45, Color.WHITE))
                     .addData(new SectorData(60, 0xffDDF4E9, 0xff2CB072, 0xff186D45))
                     .commit();

### 进度条

```xml```中

           <com.vinctor.vchartviews.progress.ProgressBarView
                  android:id="@+id/progress"
                  android:layout_width="match_parent"
                  android:layout_height="100dp" />
                  
```java```中

           progressBarView
                .setProgressBarHeight(30)
                .setIndicatorCircleRadius(50)
                .setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pic))
                .commit();

### 未完待续
