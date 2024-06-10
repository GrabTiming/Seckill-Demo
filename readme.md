# 秒杀系统的demo

## 数据库设计
这里只针对秒杀系统建表。主要有三个表。 结构如下</br>

1. **商品表goods**
```java

```
2. **用户表user**

3. **订单表order**


## 秒杀商品的规则
每个用户对一件商品最多买一件，即不可重复下单
## 秒杀的流程图
![](images/process.png)

## 涉及的技术
### Mysql
主要是对数据库的增删改查

### Redis
用于 秒杀商品的缓存预热处理、库存的增减(需要保证原子性，一致性)

### MQ
MQ主要用于订单的延时支付处理。


## 在写这个demo时遇到的问题
