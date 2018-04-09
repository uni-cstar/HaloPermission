## HoloPermission (0.9-beta)
Android Runtime Permission
### Setup
    最近会发布jcenter，敬请期待
### Usage

1. 简单使用

   ```
    //'this'可以是`Activity` 或 `Fragment`，甚至是｀Context｀
    HoloPermission.with(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
        // 权限请求结果回调
        .setListener(object : PermissionListener {
            override fun onPermissionDenied(permissions: List<String>) {
                //至少有一个权限未被授权
                {your code}
            }

            override fun onPermissionGrand(permissions: List<String>) {
                 //所有权限都被授权了
                {your code}
            }
        }
        .run()
   ```
2. Rationale使用

   当需要向用户解释为什么请求权限时，你可以像这样去使用：
   ```
       HoloPermission.with(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
          .setListener(object : PermissionListener {
              override fun onPermissionDenied(permissions: List<String>) {
                  {your code}
              }
              override fun onPermissionGrand(permissions: List<String>) {
                  {your code} 
              }
          }
         //默认会使用一个对话框展示提示信息，如果你需要自定义样式，可以调用另外一个同名的重载函数
         .setRationaleRender("{Explain to user why you request the permission}")
         .run()
   ```
3. Setting使用
   当您无法获取一些权限时，你可能希望引导用户进入权限设置界面进行权限设置，你可以像这样去使用：
   ```
       HoloPermission.with(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .setListener(object : PermissionListener {
                override fun onPermissionDenied(permissions: List<String>) {
                    {your code}
                }
                override fun onPermissionGrand(permissions: List<String>) {
                    {your code} 
                }
            }
           //默认会使用一个对话框展示提示信息引导用户去设置，如果你需要自定义样式，可以调用另外一个同名的重载函数
           .setSettingRender("{Ask user to setting the permission for your app}")
           .run()
   ```
    
查看**Wiki**了解更多
    
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
