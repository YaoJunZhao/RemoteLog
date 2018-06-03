# remoteLog

## 介绍
* 在开发过程中发现，有些电视设备，没有入口打开usb调试，但是又有很多奇怪bug出现，不能调试也不能开log，所以就有了这个remoteLog
* 项目包括后端服务器和客户端接入代码
* 后端服务器部分使用golang写得，前端给出了android版本的接入代码

## 支持
* 拿到后端代码自己配置服务器，需要更改html中几个ip值为自己的服务器ip
* android版本接入代码中可以指定上报的服务器ip

## 现有功能
* 支持多进程上报
* 实时上报

## 接入
* android接入，由于是用content provider做的多进程，需要在manifest文件中添加provider

```
<provider
	android:name=".RemoteLog.RemoteLogProvider"
	android:authorities="org.Randy.RemoteLogProvider" />
```

* 初始化
``` 后端服务器端口固定8752
RemoteLog.init(MainActivity.this, "127.0.0.1", 8752, "helloworld");
```

* 打log
```
RemoteLog.i(context, TAG, "test remotelog");
```

* 查看log，网页查看配置服务器ip:8080，例如：配置在本地，那么查看网址为：127.0.0.1:8080


## 未完待续
* 添加进程名&线程id信息
* log导入到文件，文件上传到服务器
* 添加log自动清理功能
* 整理后端代码，导出一份配置，读取配置，不修改源代码配置



