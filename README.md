"# remoteLog"

远程log

android接入
由于是用content provider做的多进程
需要在manifest文件中添加
<provider
            android:name=".RemoteLog.RemoteLogProvider"
            android:authorities="org.Randy.RemoteLogProvider" />



