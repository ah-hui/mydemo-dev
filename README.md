# mydemo-dev

#注意
自动化测试时，请关闭spring-security的关于权限的拦截

#T01-1
初步引入spring-security,通过配置文件分配的权限
直接运行项目访问项目主页,或访问任意连接,将跳转到自动生成的登录页面
登录页面由http的 auto-config=”true”的Spring Security配置 自动为我们生成
采用低版本的3.1.7是因为高版本报错,应该是某个东东被弃用了

#T01-2
http 元素下的 form-login 元素是用来定义表单登录信息的。
当我们什么属性都不指定的时候 Spring Security 会为我们生成一个默认的登录页面
不巧,我想自己指定
之后,还需要指定登录后的页面

#T02
1.引入spring-security ---专业地
2.log4j输出执行sql（mybatis）
3.整改整个user模块并加入密码加密
4.修改认证失败处理器

#T03
1.spring-security权限搞定，强无敌

#T04
1.引入activiti做流程，先只做了一个test用例
2.activiti多个例子
3.引入serviceTask

#T05
1.引入jstl标签库（简化页面编写）和spring-security标签库（这个是做权限的）
2.receiveTask用做手动触发事件，暂时未引入因为不知道什么用
3.加入定时任务的流程test（包含多种自动执行的测试）

#T06
1.修改redis配置
2.加入对dubbo的支持，升级成为服务提供方（但是如果不先启动zookeeper会无法启动项目，所以只能先关闭dubbo的配置，如果要测试dubbo，rename配置文件即可）
3.SpringAOP实现日志管理
4.引入ElasticSearch+Jest做全文搜索

#T??
1.计划引入plivo发送SMS短信
做成可配置的，使用一个配置文件记录短信开关、token获取url、发送url、secret等
GLOBAL.getConfig ---> HttpUtils.doPost ---> SmsTempleteUtil ---> sendMessage


