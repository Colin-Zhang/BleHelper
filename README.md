目录

1\. 如何集成<br>
2\. 搜索蓝牙设备<br>
3\. 连接设备<br>
4\. 收取设备信息<br>
5\. 写入设备数据<br>
6\. 构建命令 <br>

------

**1\.如何集成**

###### maven

> <dependency> 
>
> <groupId>colin.zhang</groupId>
>
>  <artifactId>blehelperlibrary</artifactId>
>
>  <version>1.0.3</version> <type>pom</type> </dependency>

###### Gradle

> compile 'colin.zhang:blehelperlibrary:1.0.3'

###### lvy

> <dependency org='colin.zhang' name='blehelperlibrary' rev='1.0.3'> 
>
>    <artifact name='blehelperlibrary' ext='pom' ></artifact>
> </dependency>


**2\.搜索蓝牙设备**

###### 传入参数

> | 参数                   | 必选 | 类型      | 说明             |
> | :--------------------- | :--- | :-------- | ---------------- |
> | OnSearchDeviceListener | true | interface | 蓝牙搜索设备回调 |

###### onNewDeviceFound

> | 返回结果 | 字段类型        | 说明       |
> | :------- | :-------------- | :--------- |
> | device   | BluetoothDevice | 设备实体类 |

###### 方法调用示例

> 示例：<br>

```
    BleHelper.instance.searchBleDevices(this)
```

**3\.连接设备**

###### 传入参数

> |       参数        | 必选 | 类型      | 说明            |
> | :---------------: | :--- | :-------- | --------------- |
> |        mac        | true | String    | 蓝牙设备Mac地址 |
> | onConnectListener | true | interface | 蓝牙连接回调    |
###### 返回结果

> | 返回结果             | 字段类型 | 说明             |
> | :------------------- | :------- | :--------------- |
> | onConnectSuccess回调 | 无       | 手机连接设备成功 |

> | 返回结果                      | 字段类型 |       说明       |
> | :---------------------------- | :------- | :--------------: |
> | onNotificationOpenSuccess回调 | 无       | 设备开启通知成功 |
###### 方法调用示例

> 示例：<br>

```
  BleHelper.instance.connectDevice(mac, this)
```

**4\.收取设备信息**

###### 传入参数

> |           参数           | 必选 | 类型      | 说明                     |
> | :----------------------: | :--- | :-------- | ------------------------ |
> | OnReceiveMessageListener | true | interface | 设备发送到手机的数据回调 |
###### 返回结果

> | 返回结果 | 字段类型  | 说明                   |
> | :------- | :-------- | :--------------------- |
> | s        | ByteArray | 设备发送的数据byte数组 |

###### 方法调用示例

> 示例：<br>

```
           BleHelper.instance.setReceivedMessageListener(this)
```

**5\.写入设备数据**

###### 传入参数

> |           参数           | 必选 | 类型      | 说明                     |
> | :----------------------: | :--- | :-------- | ------------------------ |
> | OnReceiveMessageListener | true | interface | 设备发送到手机的数据回调 |
###### 返回结果

> | 返回结果 | 字段类型  | 说明                   |
> | :------- | :-------- | :--------------------- |
> | s        | ByteArray | 设备发送的数据byte数组 |

###### 方法调用示例

> 示例：<br>

```
         ResultBean result = SefonLockHelper.getInstance().analysisCommand(bean.getMessage());
```

**6\.构建命令**

###### 传入参数

> |           参数           | 必选 | 类型      | 说明                     |
> | :----------------------: | :--- | :-------- | ------------------------ |
> | OnReceiveMessageListener | true | interface | 设备发送到手机的数据回调 |

###### 返回结果

> | 返回结果 | 字段类型  | 说明                   |
> | :------- | :-------- | :--------------------- |
> | s        | ByteArray | 设备发送的数据byte数组 |

###### 方法调用示例

> 示例：<br>

```
 byte[] byteArray = SefonLockHelper.getInstance().buildCommand(100, message)
```

> 使用范围: <br>

> > 查询命令使用 <br>

```
 byte[] byteArray = SefonLockHelper.getInstance().buildCommand(100, message)
```

> > 修改命令使用 <br>

```
 byte[] byteArray = SefonLockHelper.getInstance().buildCommand(107, message,value)
```