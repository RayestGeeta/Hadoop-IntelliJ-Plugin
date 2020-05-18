## Hadoop-IntelliJ-Plugin
在原作者的基础上进行了更新，目前支持在IntelliJ IDEA 2020.1 上编译，修复了测试连接时一直失败的bug

### 更新体验
1. 因为本人刚开始学习hadoop，用的是IDEA的2020.1版本，
所以当找到[fangyuzhong2016](https://github.com/fangyuzhong2016/HadoopIntellijPlugin)大佬的插件时，
发现要重新下载低版本的IDEA才能编译时，懒惰的想法瞬间灌满了我的脑袋，
我直接下载项目导入了我的IDEA，开始了侥(自)幸(虐)的测试修改之路，想着能否稍作修改适应我的版本。😅

2. 面对大佬的代码不明觉厉，但此时脑中只有四个字（干就完了），所以在官网搜索各种资料，找到了下列很有用的资料  
* https://upsource.jetbrains.com/idea-ce/structure/idea-ce-40e5005d02df57f58ac2d498867446c43d61101f/platform/
* https://www.jetbrains.org/intellij/sdk/docs/basics/plugin_structure/plugin_actions.html
* https://kana112233.github.io/intellij-sdk-docs-cn/basics.html

3. 结合这些信息分析源码，更新API，经过各种摸爬滚打，插件终于能正常使用了，修复了测试功能的bug，
但是丢失了显示“关于”窗口的功能(应该是哪不小心被我给改了，还没找到原因，不过不影响使用)

###### 使用方面
原作者讲得很清楚，编译安装过程是一样的
https://github.com/fangyuzhong2016/HadoopIntellijPlugin

###### 小细节
1. 下载文件时会连带下载crc验证文件
2. 右键“查看文件”时，在项目根目录下会自动创建data目录，文件会自动下载到data目录中，第一次查看是无法自动跳转的，
要在data目录右键“Reload from Disk”，再次右键查看hdfs的文件时就能自动跳转了
3. 目前还无法上传目录
