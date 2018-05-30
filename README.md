dataCatcher
---
A crawler using WebCollector getting data from dl.acm.org  

> 数据库连接配置

在 db.properties 中进行配置  

> 结构：Catcher+Dao  

Catcher：爬虫具体逻辑层  
Dao：数据持久层   

> 在Catcher.java的main方法中配置爬虫参数，包括：

### 线程数量
setThreads(int);  

### 深度设置
start(int);   

### 断点爬取
setResumable(true); 断默认为false,每次启动爬虫都会重新爬取，true 从断点处继续爬取。  

### 搭建环境  
建议使用intellij idea，同时使用Maven，jdk 1.7+   

### 请先clone dev分支以进行使用！  

> 持续更新
