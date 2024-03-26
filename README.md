## 配置文件
```java
    public Protocol protocol = Protocol.HTTP;

    public Serial serial =  new JdkSerial();

    public static ProxyMode proxyMode = ProxyMode.CGLIB;

    public int servicePort = 8080;

    public String serviceHost = "127.0.0.1";
```
## 配置详解
### 协议
- HTTP
- ZZQ
### 代理模式
- JDK proxy
- CGLIB proxy
