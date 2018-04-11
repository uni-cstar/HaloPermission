>An Android permission library developed by Kotlin language with higher extensibility and compatibility.

>Kotlin语言开发的Android权限库，具有更高的扩展性和兼容性。

##### 写在前面的
Android运行时权限，想必对Android开发者来说并不陌生，Github上也有不少相应的库也足够应付现在的使用了，但是`HaloPermission`不是在无聊的造轮子，它的职责是让自己提供的支持更完美，更能够拥抱变化。
其实`Halo`是一个系列,里面的每一个库我都会用心，尽自己所能的去写好，我也希望大家能给予更多的支持，共同建设，让Android开发闪射自己的`Halo`.
在开发`HaloPermission`之前，我阅读了很多文章，也看过很多库的源码，所以感谢这些伟大的无私奉献者和开源库作者，其中包括`RxPermission`,`HiPermission`,`EasyPermission`,`AndPermission`等。
##### 为什么是HaloPermission
- 作者的出发点（一个对事情要求完美的处女座特点）
- 基于Kotlin（双刃剑，仁者见仁，智者见智）
- 更多的扩展性（后面会写文章专门介绍HoloPermission的设计）
- 更多的兼容性（尽量兼容）；
- 更灵活的功能配置


##### 使用介绍
###### 1. 常规使用

* 请求一个权限，然后接收结果回调


```
      HoloPermission.with(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .setListener(object: PermissionListener{
                override fun onPermissionDenied(permissions: List<String>) {
                    {your code for deny}
                }
                override fun onPermissionGrand(permissions: List<String>) {
                    {your code for grand}
                }
            }).run()
```

* 请求多个权限

```
        HoloPermission.with(this,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CALL_PHONE)
            .{省略代码}

        //or

        val permissions:Array<String> = arrayOf("","")
        HoloPermission.with(this,*permissions)
            .{省略代码}
```
* 只关心权限被允许(未被允许)的回调
```
        HoloPermission.with(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .setGrandAction(object:GrandAction{
                override fun onPermissionGrand(permissions: List<String>) {
                    {your code for grand}
                }

            }).run()
```




###### 2. RationaleRender使用
如果你想向用户解释请求权限的原因，你可以使用`setRationaleRender`方法

```
    HoloPermission.with(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .{省略回调设置代码}
                    .setRationaleRender("为了确保功能的正常使用，请允许接下来的权限请求申请。")
                    .run()
```

如果你想自定义RationaleRender的样式，比如：

```
   HoloPermission.with(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .{省略回调设置代码}
                .setRationaleRender(object:RationaleRender{
                    override fun show(ctx: Context, permission: List<String>, process: RationaleRender.Process) {
                        //自定义使用了一个`Toast`展示信息。
                        Toast.makeText(ctx,"为了确保功能的正常使用，请允许接下来的权限请求申请。",Toast.LENGTH_SHORT).show()

                        //**为了确保后续的流程继续执行，你需要在适当的时候调用process的`onNext`或`onCancel`方法**
                        process.onNext()

                        //onNext()表示继续后面的执行
                        //onCancel会取消流程的执行，并且会最终回调onPermissionDenied方法
                    }
                })
                .run()
```

###### 3. SettingRender使用

对于无法获取权限时，如果你想引导用户打开权限设置界面，你可以使用`setSettingRender`方法

```
    HoloPermission.with(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
              .{省略回调设置代码}
              .setSettingRender("无法使用外部存储，请设置权限以便使用。")
              .run()
```

如果你想自定义SettingRender的样式，比如：

```
   HoloPermission.with(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .{省略回调设置代码}
                .setSettingRender(object:SettingRender{
                    override fun show(ctx: Context, permission: List<String>, process: SettingRender.Process) {
                        //自定义使用了一个`Toast`展示信息。
                        Toast.makeText(ctx,"无法使用外部存储，请设置权限以便使用。",Toast.LENGTH_SHORT).show()

                        //**为了确保后续的流程继续执行，你需要在适当的时候调用process的`onNext`或`onCancel`方法**
                        process.onNext()

                        //onNext()表示继续后面的执行,HaloPermission将打开系统应用权限设置界面
                        //onCancel会取消流程的执行，不会打开系统应用权限设置界面，最终会回调onPermissionDenied方法
                    }
                })
                .run()
```

如果你觉得`HaloPermission`打开的权限设置界面不是您所满意的，你可以重写`SettingRender`的`getCustomSettingIntent`方法提供一个`Intent`,如果返回null则将使用HaloPermission的默认方式打开：

```
    HoloPermission.with(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .{省略回调设置代码}
                .setSettingRender(object:SettingRender{
                    override fun show(ctx: Context, permission: List<String>, process: SettingRender.Process) {
                       {省略的代码}
                    }

                    //自定义SettingIntent
                    override fun getCustomSettingIntent(ctx: Context): Intent? {
                                            return super.getCustomSettingIntent(ctx)
                    }
                })
                .run()
```

###### 4. 自定义权限校验规则

两步即可实现

```
    //1. 创建自定义PermissionChecker
   class CustomChecker:PermissionChecker{
        override fun isPermissionGranted(ctx: Context, permission: String): Boolean {
            {使用你的规则}
        }
    }

    //2. 使用自定义规则
    HoloPermission.with(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
       .{省略常规代码}
       .run(CustomChecker())

```

**除非你非常有把握，否则不建议使用自定义权限校验规则，因为HaloPermission会尽可能的去适配和兼容**

###### 5. 自定义请求方式

HaloPermission默认使用ShadowActivity的形式请求权限，当然只要你愿意，您可以使用Fragment的形式去实现，HaloPermission本身也提供了
Fragment的请求方式，但是最终去掉了这部分的实现，因为对于Fragment的使用机制，如果使用不当，可能会出现一些奇怪的问题，我想这是你我都不愿看到的。

同样的，两步即可实现自定义请求方式

```
    //1. 创建自定义PermissionCaller
   class CustomCaller: PermissionCaller{
          override fun requestPermission(ctx: Context, responder: PermissionResponder, vararg permision: String) {
              {可以仿造HaloPermission实现，最终要在适当的时候调用responder让流程正常进行}
          }
   }

    //2. 使用自定义规则
    HoloPermission.with(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
       .{省略常规代码}
       .run(CustomCaller())

```

##### 实际运行效果截图
熟话说无图无真相，由于常规请求的效果图比较单调，这里只贴了设置了RationaleRender和SettingRender的效果截图：
* 包含SettingRender的效果

   ![](https://upload-images.jianshu.io/upload_images/2149929-c41e9b6940bcec79.gif?imageMogr2/auto-orient/strip)

* 包含RationaleRender的效果

![](https://upload-images.jianshu.io/upload_images/2149929-e7ea8e3ccb880755.gif?imageMogr2/auto-orient/strip)




#更多请见 [Github](https://github.com/SupLuo/HaloPermission)


