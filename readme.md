# 介绍:
## 工具如下
读取手机硬件和系统信息的分类
主要有：
- 联网方式
- 联网方式名称
- IMEI
- 系统版本
- IMSI
- 手机号码
- 手机卡序列号
- 运营商
- 运营商名字
- 国家iso代码
- 网络类型
- 手机卡国家
- 网络运营商类型
- 网络类型名
- 固件版本
- Mac地址
- 无线路由器名
- 无线路由器地址
- 蓝牙地址
- 蓝牙名称
- android_id
- 串口序列号
- 手机品牌
- 系统版本(osVersion)
- 描述build的标签
- 设备名
- 主板引导程序
- Android系统版本
- 系统版本值
- 系统的API级别
- 固件开发版本代号
- 源码控制版本号
- CPU型号
- cpu指令集
- 主板
- 型号
- 产品名称
- 设备版本类型
- 设备用户名
- 显示屏幕参数
- 硬件名称
- 设备主机地址
- 制造商
- 手机类型
- 手机卡状态
- ID
- 内网ip
- Build时间
- 指纹
- 以及屏幕的相关信息等相关信息……



# 新增加：
- 高斯模糊，毛玻璃效果

# 使用方法
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
- 添加依赖
 dependencies {
	        implementation 'com.github.yhsh:GetPhoneMessage:1.0.0'
	}
  
