[![Release](https://img.shields.io/badge/release-1.0--rc-blue.svg)](https://github.com/SupLuo/HaloPermission/releases)
[![GitHub license](https://img.shields.io/github/license/SupLuo/HaloPermission.svg)](https://github.com/SupLuo/HaloPermission/blob/master/LICENSE.txt)

## HoloPermission
An Android permission library developed by Kotlin language with higher extensibility and compatibility.

Kotlin语言开发的Android权限库；提供更好的扩展性和兼容性支持。

## [HaloPermission解惑(Why choose "HaloPermission")](https://github.com/SupLuo/HaloPermission/blob/master/doc/What%20is%20%22HaloPermission%22.md)
您可能对HaloPermission有诸多疑问：已经有那么多成熟的权限处理框架，为什么要用这个？这个有什么值得使用的理由？为什么这个库的星星这么少？这库到底靠谱不靠谱？...
希望[HaloPermission解惑](https://github.com/SupLuo/HaloPermission/blob/master/doc/What%20is%20%22HaloPermission%22.md)能够消除您心中的疑虑。

### Features
* 支持RationaleRender
* 支持SettingRender（支持配置打开权限设置界面，并自动检测设置是否改变）
* 更好的系统权限设置界面兼容性
* 6.0以下权限支持
* 灵活简易的功能配置

### Setup

在使用HaloPermission的Module的build.gradle中添加以下依赖：
```
    dependencies {
        //gradle 3.+以后不是使用'compile'方法，而是使用'implementation' or 'api'等方式
        compile('halo.android:permission:1.0-rc@aar')
    }

```

额外配置说明:

1. 配置对V7依赖

    HaloPermission依赖`appcompat-v7`包（本身也依赖v4包，但v7依赖v4，所以引入v7即可），考虑您的support包版本一致问题，HaloPermission是以provided(gradle 3.x是compileOnly)的方式依赖v7包。

2. 配置Kotlin版本一致（不支持Kotlin开发的童鞋可以忽略此条）

    HaloPermission是基于Kotlin 1.2.10开发的，**如果您的工程也支持Kotlin开发，
并且与HaloPermission所使用的版本不一致**，你可以排除HaloPermission对Kotlin版本库的依赖。
```
    compile('halo.android:permission:1.0-rc@aar', {
        exclude group: 'org.jetbrains.kotlin', module: 'kotlin-stdlib'
    })
```

### Usage
  
**[查看详细使用](https://github.com/SupLuo/HoloPermission/blob/master/doc/README_USAGE.md)**

### 运行效果图
由于常规的运行截图比较基础，这里只给出RationaleRender和SettingRender的运行截图
* 包含SettingRender的效果

    ![image](https://github.com/SupLuo/HaloPermission/blob/master/doc/setting_render.gif?raw=true)
* 包含RationaleRender的效果

    ![image](https://github.com/SupLuo/HaloPermission/blob/master/doc/rationale_render.gif?raw=true)

### 缺陷
对于6.0以下的权限处理，某些权限校验并不准确，这一部分稍后进一步说明原因。
不仅HaloPermission如此，大多数有名的框架也不能解决这些问题。

### 联系方式
QQ：862638161

Email:super_luo@163.com

如需交流，欢迎讨论
