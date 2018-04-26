[![Release](https://img.shields.io/badge/release-1.0--rc-blue.svg)](https://github.com/SupLuo/HaloPermission/releases)
[![GitHub license](https://img.shields.io/github/license/SupLuo/HaloPermission.svg)](https://github.com/SupLuo/HaloPermission/blob/master/LICENSE.txt)

## HoloPermission
An Android permission library developed by Kotlin language with higher extensibility and compatibility.

Kotlin语言开发的Android权限库，具有更高的扩展性和兼容性。


### Setup

在使用HaloPermission的Module的build.gradle中添加以下依赖：
```
    dependencies {
        //gradle 3.+以后不是使用'compile'方法，而是使用'implementation' or 'api'等方式
        compile('halo.android:permission:1.0-rc@aar')
    }

```

额外配置说明:

1. 配置V7依赖

    HaloPermission依赖`appcompat-v7`包（本身也依赖v4包，但v7依赖v4，所以引入v7即可），
因此请**在你的工程中配置对`appcompat-v7`的依赖**

2. 配置Kotlin版本一致（不支持Kotlin开发的童鞋可以忽略此条）

    HaloPermission是基于Kotlin 1.2.10开发的，**如果您的工程也支持Kotlin开发，
并且与HaloPermission所使用的版本不一致**，你可以排除HaloPermission对Kotlin版本库的依赖
    ```
    compile('halo.android:permission:1.0-rc@aar', {
        exclude group: 'org.jetbrains.kotlin', module: 'kotlin-stdlib'
    })
    ```


### Features
* 支持RationaleRender
* 支持SettingRender（支持打开权限设置界面，并自动检测设置是否改变）
* 尽可能兼容的打开系统权限设置界面


### Usage
  
**[查看详细使用](https://github.com/SupLuo/HoloPermission/blob/master/doc/README_USAGE.md)**

### 运行效果图
由于常规的运行截图比较基础，这里只给出RationaleRender和SettingRender的运行截图
* 包含SettingRender的效果

    ![image](https://github.com/SupLuo/HaloPermission/blob/master/doc/setting_render.gif?raw=true)
* 包含RationaleRender的效果

    ![image](https://github.com/SupLuo/HaloPermission/blob/master/doc/rationale_render.gif?raw=true)


### 联系方式
QQ：862638161
Email:super_luo@163.com
如需交流，欢迎讨论
