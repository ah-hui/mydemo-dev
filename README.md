# mydemo-dev

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
2.log4j输出执行sql