# Mirai-teamspeak3-event-listener

一个以 [Mirai-Console](https://github.com/mamoe/mirai) 为基础开发的Mirai插件，用于监听[Teamspeak3](https://www.teamspeak.com/en/)语音服务器中用户进入或离开等事件，并播报到群聊/私聊中

## 开始使用

插件需要以 [Mirai-Console](https://github.com/mamoe/mirai)

为基础，你可以下载 [MCL](https://github.com/iTXTech/mirai-console-loader/releases) 作为你的Mirai插件载入器

可选插件： Mirai 官方插件 [chat-command](https://github.com/project-mirai/chat-command)

## 使用方法



将插件放到MCL的plugin目录下，运行MCL,首次运行会在/config/cn.rapdog.mirai-teamspeak3-event-listener目录下生成配置文件
修改配置文件中teamspeak服务器地址，用户名，密码等字段，保存后重启MCL

配置文件中account需要填teamspeak server query的用户名，默认账号为serveradmin
password填密码，如果不知道密码请参考文末 ”serveradmin密码找回方法“



启动成功后即开始监听目标服务器，当用户进入/离开服务器时会根据配置在qq进行播报



## serveradmin密码找回方法

进入服务器TeamSpeak 3所在的文件夹

```shell
cd /opt/teamspeak3-server_linux_amd64
```

首先关闭TeamSpeak 3服务。如果已经把TeamSpeak 3作为服务添加到系统里，请停止服务：

```shell
systemctl stop teamspeak.service
```

如果没有作为服务运行，请执行以下来停止TeamSpeak 3：

```shell
./ts3server_startscript.sh stop
```

用以下命令启动TeamSpeak 3服务端：

```shell
./ts3server_minimal_runscript.sh serveradmin_password=yOuR_nEwP@ssw0rd
```

# 这一步操作会重置你的`serveradmin`密码，请自行替换`yOuR_nEwP@ssw0rd`为你想要的密码。

在此之后关闭./ts3server_minimal_runscript.sh，使用你正常的方式启动TeamSpeak 3服务端即可。