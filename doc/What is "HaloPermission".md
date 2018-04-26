## 关于权限，你所需要了解的
对于权限的常规介绍以及使用这种教科书式的内容，已经烂大街了，所以需要补充这方面认识的可以查看官网，可以google、百度，而我想聊的是一些权限"不正常的东西"
。

对于最复杂的权限请求莫过于：先判断某个权限是否被授权，如果未被授权，根据业务规则决定是否要在请求权限之前向用户解释请求权限的原因，之后发起权限请求，如果请求之后权限结果还是未被允许，则根据业务逻辑规则决定是否需要引导用户进入系统权限设置界面，对于进入系统权限设置界面之后返回，可能还想知道用户的设置结果。
对于其它流程应该都是比这个流程更简单的，对于SDK提供的API本身也够用了，也足够应付这些需求了。但在设备提供商／系统提供商如此多样化的国内，所有的请求流程可能并没有想象的那么顺畅。

在国内，你可能遇到如下问题：

1. 权限校验结果与实际不相符
明明没有权限，但是通过API请求得到的权限结果是授权了的。
2. `shouldShowRequestPermissionRationale`总是返回false或true
是否需要向用户解释请求权限的原因通常是通过这个方法的返回值来进一步确定的。如果这一步不准确，可能永远都无法向用户解释原因。
3. 无法打开想要的系统权限设置界面
国内很多设备系统由于被高度定制，各大厂商搞各自特性，导致打开权限系统设置界面并没有统一方法。
4. 用户关闭系统权限设置界面之后，没有自动刷新权限请求结果
app无法得到权限，引导用户打开了权限设置界面，用户关闭权限设置界面之后，app无法得到用户操作权限的结果从而进一步处理业务逻辑。
5. 无法支持6.0之前的权限处理
android运行时权限是在6.0之后才提供的，对于以往版本的权限处理，默认返回的是true，但是用户可能通过设置界面或者手机管理软件禁止app的权限，而app在运行过程中是无法检测是否拥有权限的，导致app后续流程异常，甚至crash。
6. ...您来补充

对于上述问题，应该算是权限处理过程中最为难啃的骨头，这也是众多权限框架库想要致力于解决的问题，其中比较有名的应该是AndPermission，permission4m等,当然还有HaloPermission。

其实HaloPermission正是站在这些巨人的肩膀上产生的。但是很遗憾，对于上述的某些问题，就目前来说没有一个框架能够很好的解决，即使是目前比较有名的那些框架，HaloPermission深刻意识到了这个问题，因此HaloPermission在设计之初秉持一个理念"只有您自己才是最可靠的"。
也就是HaloPermission在设计上希望更合理，更容易配置，扩展，让每一个组件尽可能被重用，让代码更简单更可读，也就是说当遇到不可处理的一些问题时，您可以起到决定性作用。


* 如何处理权限检测结果不准确的问题

    如果通过权限检测得到的结果不准确（即通过 `*.checkSelfPermission`方法检测结果），那么我们可以通过对权限的实际使用来进一步确定权限是否可用，比如`Manifest.permission.WRITE_EXTERNAL_STORAGE`权限，假如我门在外置存储中创建一个文件成功，那说明app是具有`Manifest.permission.WRITE_EXTERNAL_STORAGE`权限的。
* 支持6.0以前的版本

    由前一个问题的答案可以得出，我们可以通过对权限的实际使用来进一步确定权限授权状态，如果没有被授权则可以打开权限设置界面让用户允许权限。

## HaloPermission的设计
HaloPermission在设计上将一个权限请求操作视作为一个流程(Processor)，
一个流程由用户的权限请求信息(Request)、权限检测者（PermissionChecker）、权限请求者（PermissionCaller）和一个权限设置界面请求者（SettingCaller）组成。
权限请求信息(Request)是最基本的部分，其它部分都可以任意搭配来应对不同情况的需求。

* Request
    包含所有基本信息：当前上下文，权限信息，回调，RationaleRender，SettingRender的配置。

* PermissionChecker
    用于判断权限是否被授权，目前提供了StandardChecker、StandardChecker23、StrictChecker；
    * StandardChecker
    标准权限校验，在23以前默认返回true，在23以后，如果targetVersion小于23，则使用android.support.v4.content.PermissionChecker提供的方法进行校验,否则使用android.support.v4.content.ContextCompat提供的方法校验
    * StandardChecker23
    标准权限校验，与StandardChecker不同的地方是增加了AppOpsManagerCompat的判断部分。
    * StrictChecker
    严格权限校验，通过对对应权限的具体使用确定权限授权状态，目前支持一些常规权限的实现。
* PermissionCaller
    用于向系统发起权限请求申请，目前只有`ActivityPermissionCaller`一个具体实现。
    `ActivityPermissionCaller`在请求权限的时候，启动一个透明的Activity作为请求主体发起权限请求并回调结果。
    当然可以非常容易的实现一个利用Fragment的请求方法，最初HaloPermission本身也提供了Fragment的请求方式，但是最终去掉了这部分的实现，
    因为对于Fragment的使用机制，如果使用不当，可能会出现一些奇怪的问题，我想这是你我都不愿看到的。
* SettingCaller
    用于请求打开系统权限设置界面，这一部分能够被改动的可能性很小，因此目前并没有让整个流程可以配置`SettingCaller`而是使用了一个默认的`SettingPermissionCaller`实现，
    用于充当与系统权限设置界面的中介，并未后续流程提供服务。
* Processor
    目前流程都是基于`BaseProcessor`流程创建，提供了`CommonProcessor`,`StrictProcessor`两个具体的实现。

## HaloPermission的具体使用
[查看具体使用](https://github.com/SupLuo/HaloPermission/blob/master/doc/README_USAGE.md)

## HaloPermission靠谱么
至于这个问题，我想是需要您综合判断的，对于我而言，我觉得您才是最可靠的。
正如前面所说
>其实HaloPermission正是站在这些巨人的肩膀上产生的。但是很遗憾，对于上述的某些问题，就目前来说没有一个框架能够很好的解决，即使是目前比较有名的那些框架，HaloPermission深刻意识到了这个问题，因此HaloPermission在设计之初秉持一个理念"只有您自己才是最可靠的"。
 也就是HaloPermission在设计上希望更合理，更容易配置，扩展，让每一个组件尽可能被重用，让代码更简单更可读，也就是说当遇到不可处理的一些问题时，您可以起到决定性作用。

HaloPermission目前使用的人不多，星星也少，但这并不意味着它不靠谱，毕竟Android权限这个问题出了这么久了，解决方案也有不少，程序员大多比较懒的，对于这种东西的抱有一个怀疑态度是很正常的，毕竟更换一个库还是需要从多方面考虑的，
其次这个库确实也才建立不久，还没有沉淀下来，**但是我的目的并不是为了推广这个库，而是我恰好也需要它，我也相信有很多人需要它，又恰好这样问题凭一己之力很难完成的很美好，所以我希望有其它人能共同建设**。
