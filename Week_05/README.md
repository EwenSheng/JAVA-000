## 作业题目

###（周四）

1.（必做）写代码实现 Spring Bean 的装配，方式越多越好（XML、Annotation 都可以）, 提交到 Github。=> 已完成

2.（选做）使 Java 里的动态代理，实现一个简单的 AOP。 => 已完成

3.（选做）实现一个 Spring XML 自定义配置，配置一组 Bean，例如：Student/Klass/School。 => 已完成

4.（选做，会添加到高手附加题）
4.1 （挑战）讲网关的 frontend/backend/filter/router 线程池都改造成 Spring 配置方式；
4.2 （挑战）基于 AOP 改造 Netty 网关，filter 和 router 使用 AOP 方式实现；
4.3 （中级挑战）基于前述改造，将网关请求前后端分离，中级使用 JMS 传递消息；
4.4 （中级挑战）尝试使用 ByteBuddy 实现一个简单的基于类的 AOP；
4.5 （超级挑战）尝试使用 ByteBuddy 与 Instrument 实现一个简单 JavaAgent 实现无侵入下的 AOP。

###（周六）：

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


## 开发环境
```
个人终端：
处理器：AMD Ryzen 9 4900HS Radeon Graphics 3.00GHZ
RAM：40.0 GB(可用 39.4GB)
类型：x64处理器
```

```
JDK版本：
java version "1.8.0_131"
Java(TM) SE Runtime Environment (build 1.8.0_131-b11)
Java HotSpot(TM) 64-Bit Server VM (build 25.131-b11, mixed mode)
```

```
涉及工具：
IntelliJ IDEA 2020.2
Windows Terminal 1.3.2651.9
SuperBenchmarker 4.5.1
gateway-server-0.0.1-SNAPSHOT.jar -- 测试服务 端口8088
Google Chrome
```

## 解题(必做题)

### 1.通过3种方式实现Bean装配

-- Xml方式
作业地址：
~~~
org.home.work.GotBeanFormApplicationContextXml
通过applicationContext的 <bean>标签显式装配bean
~~~

-- Java Bean方式
作业地址：
~~~
org.home.work.GotBeanFormScanPackageBeanConfig
GotBeanFromAnnotationTest 
自动装配bean , 通过ComponentScan配置自动扫包 使用@Service添加到Spring容器中，通过@Autowired获取Bean
~~~

-- 自动装配Bean方式
作业地址：
~~~
org.home.work.GotBeanFormBeanConfig
GotBeanFromBeanAnnotationTest
使用 Java Config注解 @bean
~~~

### 2. Aop实现


Aop实现有2种方法(xml , 通过Annotation方式)
作业地址：
~~~
1. 本次作业通过Annotation方式 实现了 AopConfig
    先定义切点
    然后定义切入点 , Before() -- 方法执行之前 / After() -- 方法执行之后 / Around() -- 环绕
2. Xml 借鉴老师的写法 通过Aop标签实现

注解方式：
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
~~~

通过实现发现切面顺序如下：

~~~
1. around
2. begin
3. around
4. after
~~~

### 3. 通过Spring XML 自定义配置，配置一组 Bean，例如：Student/Klass/School

作业地址：
~~~
1. 定义了BeanConfig

<bean id="student1" class="org.home.work.entity.Student">
        <property name="id" value="1"/>
        <property name="name" value="cgy"/>
    </bean>

    <bean id="student2" class="org.home.work.entity.Student">
        <property name="id" value="2"/>
        <property name="name" value="zj"/>
    </bean>

    <bean id="student3" class="org.home.work.entity.Student">
        <property name="id" value="3"/>
        <property name="name" value="ewen"/>
    </bean>

    <bean id="klass" class="org.home.work.entity.Klass">
        <property name="students">
            <list>
                <ref bean="student1"/>
                <ref bean="student2"/>
                <ref bean="student3"/>
            </list>
        </property>
    </bean>

    <bean id="student4" class="org.home.work.entity.Student">
        <property name="id" value="3"/>
        <property name="name" value="squall"/>
    </bean>

    <bean id="school" class="org.home.work.entity.School">
        <property name="klass" ref="klass"/>
        <property name="student" ref="student4"/>

2. 通过 ClassPathXmlApplicationContext 装载Bean

    ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:BeanConfig.xml");
    classPathXmlApplicationContext.refresh();
    School school = (School) classPathXmlApplicationContext.getBean("school");
    System.out.println(school.toString());
~~~

### 4. 给前面课程提供的 Student/Klass/School 实现自动配置和 Starter。

作业地址：
~~~
1. 构建Starter项目：fifth-week-home-work-spring-boot-starter
了解了下Starter会使用到一些注解

@Bean 实例化一个 bean;
@ConditionalOnMissingBean 是条件判断的注解，表示如果不存在对应的bean条件才成立;这里就表示如果已经有SchoolService的bean了，那么就不再进行该bean的生成;
                          这个注解十分重要，涉及到默认配置和用户自定义配置的原理。也就是说用户可以自定义一个bean;这样的话，spring容器就不需要再初始化这个默认的bean了。
@ConditionalOnProperty 是条件判断的注解，表示如果配置文件中的响应配置项数值为true,才会对该bean进行初始化
@ConditionalOnClass  条件判断的注解，表示对应的类在classpath目录下存在时，才会去解析对应的配置文件
@EnableConfigurationProperties 注解给出了该配置类所需要的配置信息类

了解了下使用到的组件

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>
        </dependency>

        <!-- 这个依赖是为了读取配置文件 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure-processor</artifactId>
            <optional>true</optional>
        </dependency>

2. 构建Web项目：fifth-week-home-work-spring-boot

@RestController
@RequestMapping(value = "/demo")
public class DemoController {

    @Autowired
    private SchoolService schoolService;

    @GetMapping(value = "/print")
    public String print() {

        schoolService.doIt();

        return "success";
    }

}

使用start被装载的Bean,会打印出结果
~~~

### 5. 研究一下 JDBC 接口和数据库连接池，掌握它们的设计和用法
作业地址：

~~~
1. DriverManager：驱动管理对象
			* 功能：
				1. 注册驱动：告诉程序该使用哪一个数据库驱动jar
					static void registerDriver(Driver driver) :注册与给定的驱动程序 DriverManager 。 
					写代码使用：  Class.forName("com.mysql.jdbc.Driver");
					通过查看源码发现：在com.mysql.jdbc.Driver类中存在静态代码块
					 static {
					        try {
					            java.sql.DriverManager.registerDriver(new Driver());
					        } catch (SQLException E) {
					            throw new RuntimeException("Can't register driver!");
					        }
    					}
					Mysql5之后的驱动jar包可以省略注册驱动的步骤。
				2. 获取数据库连接：
					* 方法：static Connection getConnection(String url, String user, String password) 
					* 参数：
						* url：指定连接的路径
							* 语法：jdbc:mysql://ip地址(域名):端口号/数据库名称
							* 例子：jdbc:mysql://localhost:3306/db3
							* 细节：如果连接的是本机mysql服务器，并且mysql服务默认端口是3306，则url可以简写为：jdbc:mysql:///数据库名称
						* user：用户名
						* password：密码 
2. Connection：数据库连接对象
			1. 功能：
				1. 获取执行sql 的对象
					* Statement createStatement()
					* PreparedStatement prepareStatement(String sql)  
				2. 管理事务：
					* 开启事务：setAutoCommit(boolean autoCommit) ：调用该方法设置参数为false，即开启事务
					* 提交事务：commit() 
					* 回滚事务：rollback() 
3. Statement：执行sql的对象
			1. 执行sql
				1. boolean execute(String sql) ：可以执行任意的sql 了解 
				2. int executeUpdate(String sql) ： 执行DML（insert、update、delete）语句、DDL(create，alter、drop)语句
					                                返回值：影响的行数，可以通过这个影响的行数判断DML语句是否执行成功 返回值>0的则执行成功，反之，则失败。
				3. ResultSet executeQuery(String sql)  ：执行DQL（select)语句
4. PreparedStatement：执行Sql对象，区别在于它是预编译SQL
~~~
#### 5.1 使用 JDBC 原生接口，实现数据库的增删改查操作
1. 装配Mysql驱动包
~~~
1. 加入Mysql驱动包
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
OriginalJdbcConfiguration
~~~
2. 装载数据库驱动程序，使用之前的配置注解加载Bean方式
~~~
    @Bean
    public Connection connection() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");

        Connection connection = DriverManager.getConnection("jdbc:mysql://47.102.152.243:3306/syw_jdbc_demo", "root", "Dv8,zia13vvKqdly");

        return connection;
    }
~~~
3. 编写包级结构
~~~
config -- 项目自定配置类
controller -- Contoller层
dao -- 数据处理层
service -- 业务处理层
entity -- 实体类包
~~~
4. 编写dao，获取到与数据库连接，获取可以执行SQL语句的对象且执行SQL
~~~

//获取执行sql语句的statement对象
  statement = connection.createStatement();

//执行sql语句,拿到结果集
  resultSet = statement.executeQuery("SELECT * FROM account WHERE id = " + id);
~~~
5. 逐层传递对象返回结果
~~~
AccountDO(accountName=2016中秋节同步, password=57fff0ea55146baa6ac1a9999dfcd1ad, idType=, idNo=, mobile=18367002206, email=, remark=null, lastLoginDt=null, lastLoginIp=null, sha=null, sso_token=null)
~~~
6. 添加update方式
~~~
    public boolean updateAccountNameById(Integer id, String name) throws SQLException {

        try {
            statement = connection.createStatement();

            // 执行DML（insert、update、delete）语句、DDL(create，alter、drop)语句 返回值：影响的行数，可以通过这个影响的行数判断DML语句是否执行成功 返回值>0的则执行成功，反之，则失败
            return statement.executeUpdate("UPDATE account SET account_name = '" + name + "' WHERE id = " + id) > 0;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }

            if (resultSet != null) {
                resultSet.close();
            }
        }
        return true;
    }
~~~

#### 5.2 使用事务，PrepareStatement 方式，批处理方式， 

~~~
    public boolean updateBatchAccountByIds(List<Integer> ids) throws SQLException {

        connection.setAutoCommit(false); // 开启事务

        try {

            String sql = String.format(
                    "UPDATE account SET version = version + 1,update_by = 'local_dev',update_dt = NOW() WHERE id in (%s)",
                    ids.stream().map(String::valueOf).collect(Collectors.joining(","))
            );

            preparedStatement = connection.prepareStatement(sql);

            connection.commit(); // 提交事务

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException throwables) {
            connection.rollback(); // 发生异常，事务回滚
            throwables.printStackTrace();
        } finally {
            if (statement != null) {
                preparedStatement.close();
            }

            if (resultSet != null) {
                resultSet.close();
            }
        }
        return false;
    }
~~~

#### 5.3 配置 Hikari 连接池，改进上述操作

1. 引用Hikari的jar
~~~
        <dependency>
            <groupId>com.zaxxer</groupId>
            <artifactId>HikariCP</artifactId>
            <version>${hikaricp.version}</version>
        </dependency>
~~~
2. 注入Hikari的DataSourceConfig
~~~
    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource dataSource() {
        return new HikariDataSource();
    }
~~~
3. 同步修改获取连接的方式
~~~
    @Bean
    public Connection connection() throws SQLException {

        /*HikariDataSource dataSource = new HikariDataSource();*/

        Connection connection = dataSource.getConnection();

        return connection;
    }
~~~
4. 启动应用,监控到Hikari日志打印,测试ok
~~~
2020-11-17 21:16:50.893  INFO 24612 --- [           main] com.zaxxer.hikari.HikariDataSource       : SYW_HikariCP - Starting...
2020-11-17 21:16:51.345  INFO 24612 --- [           main] com.zaxxer.hikari.HikariDataSource       : SYW_HikariCP - Start completed.
~~~