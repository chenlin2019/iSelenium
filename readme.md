**Selenium自动化测试项目**

1. 代码：java
2. 项目框架： maven
3. 自动化驱动框架：junit
4. 自动化测试驱动框架 Selenium（WebDriver）
5. 支持浏览器类型：chrome, firefox, headless (no-gui)
6. 配置文件：src/resources/iselenium.properties ， 需要放置到到系统的 ${user.home} 下
7. 配置文件中的对应浏览器webdriver驱动程序的路径要根据运行环境实际预先设定

**目录结构**
1. IDE：Intellij Idea
2. WebBase：基础⽅法类，读⼊配置，设定测试⽤例的初始化操作步骤
3. WebUITasks：⾃动化⽅法实现类
4. TestBaiDu： ⾃动化测试执⾏类
5. log4j.properties ：log4j⽇志配置⽂件
6. iselenium.properties：运⾏环境本地参数配置⽂件， 需要实现复制/粘贴到 ${user.home} 并且保证⾥⾯的参数配置与运
    ⾏环境的实际对应参数⼀致
7. pom.xml：Maven框架项⽬配置
8. readme.md：说明⽂件

