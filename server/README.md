# Mirai-Plugin-Center Server Side

**Mirai-Plugin-Center Server Side**（以下简称 MPC）是 Mirai Plugin Center 的服务端实现，所有服务端内容都会包括在其中。

* [`resources`](./resources) 包含所有运行时所需的资源文件
* [`src`](./src) 包含所有主要的代码
* [`test`](./test) 包含所有测试代码

## 代码结构

### MVC

MVC 部分包含：

* [`src/net/mamoe/.../model`](./src/net/mamoe/mirai/plugincenter/model) Model 部分
* 没有 View 部分，因为前后端分离
* [`src/controller`](./src/controller) Controller 部分

### ApiResp

[`ApiResp`](./src/dto/ApiResp.kt) 是所有接口的通用返回类型，没有特殊情况下，都应该使用这个类作为返回类型。

### Database

[`repo`](./src/repo) 包内包含了所有和数据库交互的，实现了 `IRepository` 接口的 `interface`，所有和数据库进行交互的行为都应该通过这些接口实现。

### Services

[`Service`](./src/services) 进行了对底层操作的包装，暴露高层接口。

底层操作包含：

* 数据库操作（[`Database`](#Database)）
* Entity 操作（[`Model`](#MVC)）
* 其他杂项的包装

### Serializer

[`serializer`](./src/serializer) 包内包含所有自定义的 `JsonSerializer` 的实现。

### Event

[`event`](./src/event) 包内定义了所有可以被记录到日志中的事件。

### Utility

[`utils`](./src/utils) 包内包含对各种内容的 _实用_ 函数扩展。

### Advice/Configure

[`advice`](./src/advice) 和 [`config`](./src/config) 包内包含所有对 Spring Boot 的配置项。