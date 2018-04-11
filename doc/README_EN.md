## HoloPermission (0.9-rc)
An Android permission library developed by Kotlin language with higher extensibility and compatibility

### Setup
    Soon，please wait.
### Usage

1. Simple Use

   base use you can like this:
   ```
    //'this' is can be an 'Activity' or a 'Fragment',and ‘even a Context' 
    HoloPermission.with(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
        // the call back for the permission request result
        .setListener(object : PermissionListener {
            override fun onPermissionDenied(permissions: List<String>) {
                //at least a permissoin denied
                {your code} // example :toast("...")
            }

            override fun onPermissionGrand(permissions: List<String>) {
                 //all permissions grand
                {your code} // example: toast("...")
            }
        }
        .run()
   ```
2. Rationale Use

   When you should show UI with rationale for requesting a permission,you can use like this:
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
         //set the rationale render;the default UI style is show a alert dialog to show the message for requesting a permission.
         //And you can call another overload method to show your custom style 
         .setRationaleRender("{Explain to user why you request the permission}")
         .run()
   ```
3. Setting Use
  
   When you should show the permission setting activity of the system for setting some denied permission,you can use like this:
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
           //set the setting render;the default UI style is show a alert dialog to show the message for request setting.
           //And you can call another overload method to show your custom style 
           .setSettingRender("{Ask user to setting the permission for your app}")
           .run()
   ```
    
**[For More](https://github.com/SupLuo/HoloPermission/blob/master/doc/README_USAGE.md)**
    
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
