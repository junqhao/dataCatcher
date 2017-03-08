#dataCatcher
##
A crawler using WebCollector getting data from dl.acm.org  

> 数据库连接配置

在 db.properties 中进行配置  
> 结构：Catcher+Dao

Catcher：爬虫具体逻辑层  
Dao：数据持久层

在Catcher.java的main方法中配置爬虫参数，包括：
#####线程数量
setThreads(int);
#####深度设置
start(int);
#####断点爬取
setResumable(true); 断点爬取在正式爬虫时应设置为true，防止宕机等原因导致程序终止，重新运行后原有的数据不丢失。默认为false。
#### 请先clone dev分支以进行使用！
> 持续更新
