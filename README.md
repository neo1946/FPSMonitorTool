一个用于检测android帧率以及卡顿的小工具，具体用法如下：

用法：
添加依赖：compile 'com.neo1946:fpsmonitor:1.4.1'
在需要的地方开启：KayzingFPSMonitor.start(getApplication());
如果需要自定义打印的某个帧率和需要记录的超时时间：KayzingFPSMonitor.start(getApplication(),new MonitorConfig(50,32));
50是低于50帧输出日志，日志记录的是超过32ms的任务。



原理查看：[这里](https://neo1946.github.io/android/2018/10/17/Android%E6%98%BE%E7%A4%BA%E6%B5%81%E7%A8%8B/) 



使用描述：启动之后会启动一个浮窗，在android高版本上会弹到授权页面，请授予浮窗权限，然后在返回的activity中点击启动。

![1](https://raw.githubusercontent.com/neo1946/FPSMonitorTool/master/image/1.jpg)







浮窗可以拖动：

![2](https://raw.githubusercontent.com/neo1946/FPSMonitorTool/master/image/2.jpg)





开始卡顿后可以从浮窗中实时显示：

![3](https://raw.githubusercontent.com/neo1946/FPSMonitorTool/master/image/3.jpg)





单击浮窗后可以显示日志：

![4](https://raw.githubusercontent.com/neo1946/FPSMonitorTool/master/image/4.jpg)





点击日志后可以看到详细情况，包命用蓝色标注，耗时用红色标注：

![5](https://raw.githubusercontent.com/neo1946/FPSMonitorTool/master/image/5.jpg)

![6](https://raw.githubusercontent.com/neo1946/FPSMonitorTool/master/image/6.jpg)

![7](https://raw.githubusercontent.com/neo1946/FPSMonitorTool/master/image/7.jpg)





