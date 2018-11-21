Esb-core-server is a high-performance lightweight data service bus built with Akka. It supports dynamic registration and monitoring of clusters and data access. Currently, single-node support is called 1 billion times a day.

![image](https://github.com/caoyingde/esb-core-server/blob/master/esb-example/doc/run.png)

   1 Introduction

  1.1 Background

   The construction of an enterprise information bus (ESB) helps to optimize the overall IT architecture. Its primary task should be to build an EASB (Enterprise Application Service Bus) and an EDSB (Enterprise Data Service Bus). EASB realizes the sharing of application services by registering and distributing application services of different systems, shielding the differences in communication and data delivery methods of different systems; EDSB provides high-speed channels for data transmission between different systems, and realizes data conversion based on certain standards. Store and provide direct data services to the outside world. The EDSB can realize the loose coupling of data relationships between different systems, and realize the information resource sharing of enterprises in a more direct way.
We use Akka and Spring to build a highly scalable, highly concurrent distributed architecture data service bus, and maintain a certain degree of programming simplicity, management ease of operation.

1.2 Akka

Akka is a library written in Scala to simplify the writing of fault-tolerant, highly scalable Java and Scala Actor model applications. It has been successfully used in the telecommunications industry. The system will hardly crash (high availability 99.9999999 % only 31 ms down every year). Actors enable you to manage service failure management (regulators), load management (mitigation strategies, timeouts and isolation), horizontal and vertical scalability (increasing cpu cores and/or adding more machines). Akka 2.3 provides Cluster Sharing and Persistence capabilities to easily write a large distributed cluster architecture.
Akka will be the core of the entire system, responsible for the delivery of all data messages, cluster implementation, RPC and more.

1.3 Spring

The Spring Framework provides an easy way to develop a large number of property files and helper classes that can cause the underlying code to become confusing. With Spring's IOC (Reverse Control) and DI (Dependency Injection) to make the entire code structure simpler, and Spring has a wide range of usability, it is easier to publish existing Spring Beans directly as a service.
In the upper application of the entire ESB, it is built with Spring Bean. Spring shields Akka's learning curve, and Spring's high adhesion makes it easy to integrate with other frameworks.

2 Service Bus Architecture

-->init -->HeatBeat -->Sync Call
![image](https://github.com/caoyingde/esb-core-server/blob/master/esb-example/doc/akka.png)

2.1 Node Role Description

Provider: Service Provider
Consumer: Service Consumer
Core: Service Center, responsible for finding services, data distribution, state persistence, etc.
Monitor: the monitoring service call center and call time monitoring center
In the entire architecture, each node is a seed-node in the cluster, an ActorSystem. The Provider and Consumer of the business service are written by the developers themselves, they may be in the same project, the same ActorSystem. Because a service may consume other services, then provide a new service. But we need to avoid this as much as possible.

2.2 Call relationship description

The service center Core needs to be activated first, exposing the addresses of a group of Core nodes.
When the Provider starts, it will rely on the existing Core node to join the cluster. And register your own services with Core.
When the Consumer starts, it depends on the existing Core node to join the cluster. I want to register to subscribe to the services I need.
The service center returns the service provider address to the consumer, and if there is a change, each node will be notified via Akka's distributed message publishing/subscription.
The Consumer groups all providers into one Router, which can distribute messages based on load balancing. If the call fails, it will select another address to call.
The service consumer and provider, the cumulative number of calls and the call time in memory, the statistics are sent to the monitoring center every minute. The monitoring center sends heartbeats to each node to obtain the load status of each node. Any node leaving the cluster will be notified to the monitoring center, and the monitoring center can notify the developer in other ways.

2.3 Architecture Features

2.3.1 High performance

The core code of the entire architecture is based on Akka build, Akka's Actor programming model provides a good guarantee for the performance of the entire architecture. The characteristics of Akka are as follows:

Everything in the system can act as an actor
Actors are completely independent
All actions taken by the Actor when the message is received are parallel, and the actions in one method are not in an explicit order.
Actor is identified by the identifier and current behavior
Actors may be classified into primitive and non primitive categories
Non-original Actors
An identifier represented by an email address
The current behavior consists of a set of knowledge (acquaintances) (instance variables or local state) and defines the actions that the Actor will take when it receives the message.
Message passing is non-blocking and asynchronous, the mechanism is mail queue (mail-queue)
All messages are sent in parallel
The state of each Actor can be persisted and automatically restored after the entire architecture is restarted.

2.3.2 Connectivity

The service center Core is only responsible for the registration and search of the service address, equivalent to the directory service. The service provider and the consumer only interact with the registration center at startup, and the registration center does not forward the request, and the pressure is small.
The monitoring center is responsible for counting the number of service calls, calling time, etc. The statistics are sent to the monitoring center server once every minute after the memory is summarized, and displayed in a report.
The service provider registers its service with the registration center and reports the call time to the monitoring center. This time does not include network overhead.
The service consumer obtains the service provider address list from the registration center, and directly invokes the provider according to the load algorithm, and reports the call time to the monitoring center, which includes the network overhead.
The service center, service provider, service consumer, and monitoring center are all nodes in the akka cluster. They are all equal relationships. They spread events through a distributed message subscription mechanism.
The departure of any node can be perceived by the service center and then propagated to each node in the cluster.
Service center and monitoring center are all down, do not affect the providers and consumers that have been running, consumers cache the list of providers locally

2.3.3 Health

Akka's own high fault tolerance, the system that can be written with Akka is almost no downtime (high availability 99.9999999 % only 31 ms a year down)
Each part of the entire architecture is a peer-to-peer cluster. The address of each call does not care which server the Actor being called is on.
All the monitoring centers will not affect the use, but only some of the sampled data will be lost.
After the service center is down, the service providers and service consumers can still communicate through the local cache.
The service provider is stateless, and any one of them will not affect the use.
After the service provider is completely smashed, the service consumer application will be unavailable, wait for the service provider to recover, and rejoin the cluster.
When any new node is added to the cluster, it will be broadcast to each node, and each node will store information about all other nodes. Unless you manually remove it through the Monitoring Center, any node can be added to an existing cluster when it is restored.


2.3.4 Flexibility

All nodes are peer-to-peer clusters, which can dynamically add machine deployment instances, and other nodes can get notifications of node joins.
The Akka Actor's supervisor tree structure dynamically increases or decreases the number of Actors that actually do things in a single node.


2.4 Implementation plan

2.4.1 Akka

2.4.1.1 Actor Model

The Actor model is a relatively common model in concurrent programming. Many development languages ​​provide native Actor model support, such as erlang, scala, and more.
Features of the Actor Model:
A calculation of particles (granularity), each actor can be seen as a separate entity containing processing (behavior), storage (state), communication (message)
Three principles - When an Actor receives a message, it can:
Create some other Actors
Send a message to a known Actor
Specify behavior when receiving the next message
The state and behavior of a single Actor is only driven by the received message
A single actor processes the received message serially
A single Actor is always thread safe
A large number of Actors are active at the same time, and their behavior is parallel.
Parallelism is the behavior of multiple Actors

2.4.1.2 Akka's Actor Implementation

Actor is a very lightweight computing unit
50 million / sec message forwarding capability (single, single core, local)
2.5 million Actors / GB of memory (about 400 bytes per empty Actor)
The Actor is transparent in position and has the ability to distribute itself (each service in the architecture corresponds to at least one Actor, then each service has a unique address in the entire architecture)
Create and find by address - local or remote node
Accessing local or remote nodes only depends on the address (Path)
Can be migrated across nodes
Actors are supervised by hierarchy.
Actors are hierarchically organized into a tree
The parent Actor monitors the state of the child Actor and can be stopped, restarted, and restored when the condition is out.

========================================================================================================================

esb-core-server是一个用Akka构建的高性能轻量级的数据服务总线，支持集群、数据接入的动态注册、监控等功能，目前单节点支持日10亿次调用。

   1.简介

  1.1 背景

   企业级信息总线(ESB)的建设有助于优化IT总体架构，其首要任务应是建设EASB（Enterprise Application Service Bus，企业应用服务总线）和EDSB（Enterprise　Data Service Bus，企业数据服务总线）。EASB通过注册和发布不同系统的应用服务、屏蔽不同系统在通信和数据提供方式上的差异，实现应用服务的共享；EDSB提供不同系统间数据传输的高速通道，同时实现数据基于一定标准的转换和存储，并对外提供直接数据服务。通过EDSB可实现不同系统之间数据关系的松耦合，以更加直接的方式实现企业的信息资源共享。
我们通过Akka和Spring来构建一个高扩展性、高并发的分布式构架的数据服务总线，并保持一定编程上的简单性，管理的易操作性。

1.2 Akka

Akka 是一个用 Scala 编写的库，用于简化编写容错的、高可伸缩性的 Java 和 Scala 的 Actor 模型应用。它已经成功运用在电信行业。系统几乎不会宕机（高可用性 99.9999999 % 一年只有 31 ms 宕机）。Actor使你能够进行服务失败管理（监管者），负载管理（缓和策略、超时和隔离），水平和垂直方向上的可扩展性（增加cpu核数和/或增加更多的机器）管理。Akka 2.3中提供了 Cluster Sharing(分片集群)和Persistence功能可以很简单的写出一个大型的分布式集群的架构。
Akka将作为整个系统的核心，来负责所有数据消息的传递、集群的实现、RPC等等。

1.3 Spring

Spring Framework 提供了一个简易的开发方式，这种开发方式，将避免那些可能致使底层代码变得繁杂混乱的大量的属性文件和帮助类。借助Spring的IOC(反转控制)以及DI(依赖注入)来让整个代码结构更加简单，而且Spring有着广泛的使用性，可以更加方便的将现有的Spring Bean直接作为服务发布。
在整个ESB的上层应用中，都是以Spring Bean来构建。通过Spring来屏蔽akka的学习曲线，Spring的高粘合性可以很方便整合其他框架。

2 服务总线架构

-->init -->HeatBeat -->Sync Call
![image](https://github.com/caoyingde/esb-core-server/blob/master/esb-example/doc/akka.png)
2.1 节点角色说明

Provider：服务提供方
Consumer：服务消费方
Core：服务中心，负责查找服务、数据分发、状态持久化等
Monitor：统计服务的调用次调和调用时间的监控中心
整个架构中，每一个节点都是集群中的一个seed-node，是一个ActorSystem。业务服务的Provider和Consumer由开发者自己编写，他们可能在同一个工程中，同一个ActorSystem。因为一个服务可能消费其他服务，然后提供一个新的服务。但是我们需要尽可能的去避免这样的情况发生。

2.2 调用关系说明

服务中心Core需要优先启动，暴露一组Core节点的地址。
Provider启动时，将依赖现有的Core节点来加入到集群中。并向Core注册自己提供的服务。
Consumer启动时，依赖现有的Core节点加入到集群中。想注册中订阅自己所需的服务。
服务中心返回服务提供者地址给消费者，如果有变更，将通过Akka的分布式消息发布/订阅来通知各个节点。
Consumer将所有提供者组成一个Router，可基于负载均衡来分发消息，如果调用失败将选另外一个地址调用
服务消费者和提供者，在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心。监控中心定时会向各个节点发送心跳，获取各个节点的负荷状态。任何节点的离开集群都会通知给监控中心，监控中心可以采用其他方式通知开发人员。

2.3 架构特性

2.3.1 高性能

整个架构的核心代码都是基于Akka构建，Akka的Actor编程模型为整个架构的性能提供了很好的保证。Akka的特点如下：

系统中的所有事物都可以扮演一个Actor
Actor之间完全独立
在收到消息时Actor所采取的所有动作都是并行的，在一个方法中的动作没有明确的顺序
Actor由标识和当前行为描述
Actor可能被分成原始（primitive）和非原始（non primitive）类别
非原始Actor有
由一个邮件地址表示的标识
当前行为由一组知识（acquaintances）（实例变量或本地状态）和定义Actor在收到消息时将采取的动作组成
消息传递是非阻塞和异步的，其机制是邮件队列（mail-queue）
所有消息发送都是并行的
每一个Actor的状态都可以被持久化，在整个架构重新启动后可以自动恢复。

2.3.2 连通性

服务中心Core只负责服务地址的注册与查找、相当于目录服务，服务提供者和消费者只在启动时与注册中心交互，注册中心不转发请求，压力较小
监控中心负责统计各服务调用次数，调用时间等，统计先在内存汇总后每分钟一次发送到监控中心服务器，并以报表展示
服务提供者向注册中心注册其提供的服务，并汇报调用时间到监控中心，此时间不包含网络开销
服务消费者向注册中心获取服务提供者地址列表，并根据负载算法直接调用提供者，同时汇报调用时间到监控中心，此时间包含网络开销
服务中心，服务提供者，服务消费者、监控中心均为akka集群中的一个节点，它们之间都是平等的关系，它们之间通过分布式消息订阅机制来传播事件
任何一个节点的离开都可以被服务中心感知，然后传播给集群中的各个节点
服务中心和监控中心全部宕机，不影响已运行的提供者和消费者，消费者在本地缓存了提供者列表

2.3.3 健状性

Akka本身的高容错性，用Akka可以编写的系统几乎不会宕机（高可用性 99.9999999 % 一年只有 31 ms 宕机）
整个架构中的每个部分都是对等集群的，每个调用的地址其实都不关心被调用的Actor具体在哪个服务器上面。
监控中心全部宕掉不影响使用，只是丢失部分采样数据
服务中心全部宕机以后，服务提供者和服务消费者仍能通过本地缓存通讯
服务提供者无状态，任意一台宕掉后，不影响使用
服务提供者全部宕掉后，服务消费者应用将无法使用，等待服务提供者恢复，重新加入集群
当任何一个新的节点加入到集群中的时候，将会广播到各个节点，各个节点会存储其他所有节点的信息。除非通过监控中心手动移除它，这样任何一个节点恢复时，就可以加入到现有的集群中。


2.3.4 伸缩性

所有节点都是对等集群，可动态增加机器部署实例，其他节点都可以获得该节点加入的通知
Akka Actor的监管者树形结构，可以动态增减单个节点中，真正做事的Actor的数量。


2.4 实现方案

2.4.1 Akka

2.4.1.1 Actor模型

Actor模型在并发编程中是一直比较常见的模型。很多开发语言都提供了原生的Actor模型支持，如erlang、scala等。
Actor模型的特点：
一种计算颗粒（粒度），每个Actor都可以看做一个独立的包含处理(行为)、存贮(状态)、通讯(消息)的实体
三定原则—当Actor接收到一条消息的时候，它可以：
创建另外一些Actor
向已知的Actor发送消息
指定接收下一条消息时的行为
单个Actor的状态和行为只由接收到的消息驱动
单个Actor串行地处理接收到的消息
单个Actor总是线程安全的
大量 Actors 同时处在活跃状态，其行为是并行的
并行是多个 Actors 的行为

2.4.1.2 Akka的Actor实现

Actor 是非常轻量的计算单元
5000 万 / 秒消息转发能力（单机、单核、本地）
250 万 Actors / GB 内存（每个空 Actor 约 400 多字节）
Actor 位置透明，本身即具分布能力（架构中的每一个服务对应至少一个Actor，那么每一个服务在整个构架中拥有一个唯一的地址）
按地址创建和查找  -  本地或远程节点
访问本地或远程节点仅在于地址 (Path) 不同
可以跨节点迁移
Actor 是按层级实现督导 (supervision) 的
Actor 按树状组织成层级
父 Actor 监控子 Actor 的状态，可以在出状况时停止、重启、恢复
