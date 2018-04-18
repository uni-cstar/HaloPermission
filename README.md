## HoloPermission (0.9-rc)
An Android permission library developed by Kotlin language with higher extensibility and compatibility.

Kotlin语言开发的Android权限库，具有更高的扩展性和兼容性。


### Setup

1.在工程(Project)的build.gradle中添加以下语句：（正在申请添加到JCenter，通过之后便可以不用执行此步骤）

```
    allprojects {
        repositories {
            jcenter()
            maven {
                url 'https://dl.bintray.com/supluo/maven'
            }
        }
```
2.在使用HaloPermission的Module的build.gradle中添加以下依赖：
```
    dependencies {
        compile('halo.android:permission:0.9-rc')
    }

```

### Features
* 支持RationaleRender
* 支持SettingRender（支持打开权限设置界面，并自动检测设置是否改变）


### Usage
  
**[查看详细使用](https://github.com/SupLuo/HoloPermission/blob/master/doc/README_USAGE.md)**

### 运行效果图
由于常规的运行截图比较基础，这里只给出RationaleRender和SettingRender的运行截图
* 包含SettingRender的效果

    ![image](https://github.com/SupLuo/HaloPermission/blob/master/doc/setting_render.gif?raw=true)
* 包含RationaleRender的效果

    ![image](https://github.com/SupLuo/HaloPermission/blob/master/doc/rationale_render.gif?raw=true)
### Todo
* 兼容6.0以下权限处理
* 补全更多机型的权限设置界面
* 安全软件中的权限管理

### License

    ```
      Copyright (C) 2018 Lucio

      Licensed under the Apache License, Version 2.0 (the "License");
      you may not use this file except in compliance with the License.
      You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      See the License for the specific language governing permissions and
      limitations under the License.

    ```
