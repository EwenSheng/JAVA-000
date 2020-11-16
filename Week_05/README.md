Week05 作业题目（周四）：

1.（必做）写代码实现 Spring Bean 的装配，方式越多越好（XML、Annotation 都可以）, 提交到 Github。=> 已完成

~~~
-- Xml方式
org.home.work.GotBeanFormApplicationContextXml
通过applicationContext的 <bean>标签显式装配bean

-- Java Bean方式
org.home.work.GotBeanFormScanPackageBeanConfig
GotBeanFromAnnotationTest 
自动装配bean , 通过ComponentScan配置自动扫包 使用@Service添加到Spring容器中，通过@Autowired获取Bean

-- 自动装配Bean方式
org.home.work.GotBeanFormBeanConfig
GotBeanFromBeanAnnotationTest
使用 Java Config注解 @bean

~~~

2.（选做）使 Java 里的动态代理，实现一个简单的 AOP。 => 已完成

~~~
Aop实现有2种方法
1. Xml 借鉴老师的写法 通过Aop标签实现
2. 通过Annotation方式 实现了 AopConfig

 * 1、标准写法
 * 权限修饰符 返回值类型 包名.包名.包名..包名.类名.方法名(参数)
 * 
 * 2、省略权限修饰符
 * 返回值类型 包名.包名.包名..包名.类名.方法名(参数)
 * 
 * 3、返回值可使用通配符代替，表示任意类型
 * * 包名.包名.包名..包名.类名.方法名(参数)
 * 
 * 4、包名可使用通配符表示任意包名
 * 1）* *.*.*.*.类名.方法名(参数)
 * 2）* *..*.类名.方法名(参数)
 * 
 * 5、参数列表
 * 基本类型直接写名称 int
 * 引用类型写包名.类名形式 java.lang.String
 * 使用通配符*表示有参数
 * 使用..表示有无参数均可
 * 
 * 6、全通配
 * * *..*.*.*(..)

先定义切点
然后定义切入点 , Before() -- 方法执行之前 / After() -- 方法执行之后 / Around() -- 环绕

切面顺序
1. around
2. begin
3. around
4. after

~~~

3.（选做）实现一个 Spring XML 自定义配置，配置一组 Bean，例如：Student/Klass/School。 => 已完成

org.home.work.GotBeanFormBeanConfig


4.（选做，会添加到高手附加题）
4.1 （挑战）讲网关的 frontend/backend/filter/router 线程池都改造成 Spring 配置方式；
4.2 （挑战）基于 AOP 改造 Netty 网关，filter 和 router 使用 AOP 方式实现；
4.3 （中级挑战）基于前述改造，将网关请求前后端分离，中级使用 JMS 传递消息；
4.4 （中级挑战）尝试使用 ByteBuddy 实现一个简单的基于类的 AOP；
4.5 （超级挑战）尝试使用 ByteBuddy 与 Instrument 实现一个简单 JavaAgent 实现无侵入下的 AOP。

Week05 作业题目（周六）：

1.（必做）给前面课程提供的 Student/Klass/School 实现自动配置和 Starter。

2.（必做）研究一下 JDBC 接口和数据库连接池，掌握它们的设计和用法：
1）使用 JDBC 原生接口，实现数据库的增删改查操作。
2）使用事务，PrepareStatement 方式，批处理方式，改进上述操作。
3）配置 Hikari 连接池，改进上述操作。提交代码到 Github。

3.（选做）总结一下，单例的各种写法，比较它们的优劣。
4.（选做）maven/spring 的 profile 机制，都有什么用法？
5.（选做）总结 Hibernate 与 MyBatis 的各方面异同点。
6.（选做）学习 MyBatis-generator 的用法和原理，学会自定义 TypeHandler 处理复杂类型。

附加题（可以后面上完数据库的课再考虑做）：
(挑战) 基于 AOP 和自定义注解，实现 @MyCache(60) 对于指定方法返回值缓存 60 秒。
(挑战) 自定义实现一个数据库连接池，并整合 Hibernate/Mybatis/Spring/SpringBoot。
(挑战) 基于 MyBatis 实现一个简单的分库分表 + 读写分离 + 分布式 ID 生成方案。
