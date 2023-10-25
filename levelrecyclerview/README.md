# Android 客製化view練習(LevelRecyclerView)

[參考網址](https://juejin.cn/post/7291474028744278016 "參考網址")

客製化RecyclerView，初始化自動置中，滾動時放大縮小透明度變化，點擊自動置中，客製化分隔線
比較特別的點
- 初始化自動置中：這個透過Decoration去幫他預設增加距離就可。
- 滾動時放大縮小透明度變化：這個需要透過view的位置距離中心點的距離去算出比例比較麻煩，這邊參考原作者的做法因為如果有類似需求可能算法又會不同。
- 點擊自動置中：這個要客製化CustomSmoothScroller也是第一次用到，因為置中用到PageSpanHelper所以不能用ScrollBy處理滾動到置中的問題。

[![GIF](https://github.com/CiaShangLin/ShangCustomViewPractice/blob/main/levelrecyclerview/image/levelRecyclerView.gif "GIF")](http://https://github.com/CiaShangLin/ShangCustomViewPractice/blob/main/levelrecyclerview/image/levelRecyclerView.gif "GIF")

