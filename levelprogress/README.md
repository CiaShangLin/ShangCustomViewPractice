# Android 客製化view練習(LevelProgress)

[Github](https://github.com/CiaShangLin/LevelProgress/tree/master)<br>
[參考來源:ClockSlider](https://github.com/a1573595/ClockSlider)

練習使用view去客製化UI
難點:
* 半圓的繪製
* 角度和弧度的轉換
* 使用三角函數找出X,Y點位

結論:
    要精準的算出X,Y的點位真的是有點麻煩，而且畫弧形的進度可能會跟算出來的點位有一點落差，應該是不夠精準的原因，未來要是萬一有類似的需求可以用上，不需要全部都用圖片處理，而且要是進度是0~100的話那可能就要放到101張圖片顯然不太可能。

#### Demo圖片
| ![](https://github.com/CiaShangLin/LevelProgress/blob/master/image/LV0.png) | ![](https://github.com/CiaShangLin/LevelProgress/blob/master/image/LV1.png) | 
| -------- | -------- |
| ![](https://github.com/CiaShangLin/LevelProgress/blob/master/image/LV3.png)    | ![](https://github.com/CiaShangLin/LevelProgress/blob/master/image/LV5.png)    |
| ![](https://github.com/CiaShangLin/LevelProgress/blob/master/image/LV7.png)| ![](https://github.com/CiaShangLin/LevelProgress/blob/master/image/LV9.png)|
| ![](https://github.com/CiaShangLin/LevelProgress/blob/master/image/LV10.png) | |