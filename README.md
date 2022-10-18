# 服务接口依赖关系

在微服务规模比较大的项目中, 我们遇到一些必要的修改或升级接口时, 需要知道有哪服务调用了此接口,
以便通知相应的开发负责人进行同步变更. 以免服务发布后出现问题.  

本项目可以直观的观察服务间的接口依赖关系. 通过agent方式对服务实现零侵入.

## 安装

### 下载程序

https://github.com/lizhiwei88/api-dependencies/releases/tag/0.0.1

### 启动控制台
```shell
java -jar console-x.x.x.jar
```

### 启动服务
```shell
java -jar xxx.jar -javaagent:agent-openfeign-x.x.x.jar=<控制台IP>:<控制台PORT>
```

### 例子
使用启动服务命令启动项目中example下的三个服务

## 演示

<img src="https://note.youdao.com/yws/public/resource/109bf24b18dc8990d36147c776324bc0/xmlnote/WEBRESOURCE5da800046766a79fe5aa9d1869dd355f/7693" />
