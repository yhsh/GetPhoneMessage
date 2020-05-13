#介绍:
##工具如下
读取手机硬件和系统信息的分类
主要有：
-联网方式</br>
-联网方式名称</br>
-IMEI</br>
-系统版本</br>
-IMSI</br>
-手机号码</br>
-手机卡序列号</br>
-运营商</br>
-运营商名字</br>
-国家iso代码</br>
-网络类型</br>
-手机卡国家</br>
-网络运营商类型</br>
-网络类型名</br>
-固件版本</br>
-Mac地址</br>
-无线路由器名</br>
-无线路由器地址</br>
-蓝牙地址</br>
-蓝牙名称</br>
-android_id</br>
-串口序列号</br>
-手机品牌</br>
-系统版本(osVersion)</br>
-描述build的标签</br>
-设备名</br>
-主板引导程序</br>
-Android系统版本</br>
-系统版本值</br>
-系统的API级别</br>
-固件开发版本代号</br>
-源码控制版本号</br>
-CPU型号</br>
-cpu指令集</br>
-主板</br>
-型号</br>
-产品名称</br>
-设备版本类型</br>
-设备用户名</br>
-显示屏幕参数</br>
-硬件名称</br>
-设备主机地址</br>
-制造商</br>
-手机类型</br>
-手机卡状态</br>
-ID</br>
-内网ip</br>
-Build时间</br>
-指纹</br>
-以及屏幕的相关信息等相关信息……



#新增加：
-高斯模糊，毛玻璃效果

#使用方法
-allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  -dependencies {
	        implementation 'com.github.yhsh:GetPhoneMessage:1.0.0'
	}
  
