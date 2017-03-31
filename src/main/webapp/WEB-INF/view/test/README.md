# mydemo-dev

#test_child.html+test_parent.html
测试_blank打开的新窗口和原窗口互动调用彼此

在文件系统中直接测试报错:
Uncaught DOMException: Blocked a frame with origin "null" from accessing a cross-origin frame.
跨页面操作涉及域的概念（origin）。
错误的意思是：
未捕获的安全错误：阻止了一个域为null的frame页面访问另一个域为null的页面。
也许你是在本地直接用浏览器打开的、地址栏是file:///的页面吧。
可以试着在本地架设服务器来调试，

