# Mirai-teamspeak3-event-listener

一个以 [Mirai-Console](https://github.com/mamoe/mirai) 为基础开发的Mirai插件，用于监听[Teamspeak3](https://www.teamspeak.com/en/)语音服务器中用户进入或离开等事件，并播报到群聊/私聊中

## 开始使用

插件需要以 [Mirai-Console](https://github.com/mamoe/mirai)

为基础，你可以下载 [MCL](https://github.com/iTXTech/mirai-console-loader/releases) 作为你的Mirai插件载入器

可选插件： Mirai 官方插件 [chat-command](https://github.com/project-mirai/chat-command)

## 使用方法



将插件放到MCL的plugin目录下，运行MCL,首次运行会在/config/cn.rapdog.mirai-teamspeak3-event-listener目录下生成配置文件
修改配置文件中teamspeak服务器地址，用户名，密码等字段，保存后重启MCL

启动成功后即开始监听目标服务器，当用户进入/离开服务器时会根据配置在qq进行播报

