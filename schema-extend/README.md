Spring Leman Schema extend
====
Spring xml配置扩展标签库

## for循环标签

for标签内部允许嵌套任意bean标签，可循环生成Bean，且提供循环变量占位替换功能。

如常用的多数据源可配置如下：

```xml
    <guo:for from="0" to="4">
        <bean id="demoDatasource{{index}}" parent="parentDataSource">
            <property name="url" value="${db{{index}}.jdbc.url}" />
            <property name="username" value="${db{{index}}.jdbc.username}" />
            <property name="password" value="${db{{index}}.jdbc.password}" />
        </bean>
    </guo:for>
```

等同于代码：

```xml
    
    <bean id="demoDatasource0" parent="parentDataSource">
        <property name="url" value="${db0.jdbc.url}" />
        <property name="username" value="${db0.jdbc.username}" />
        <property name="password" value="${db0.jdbc.password}" />
    </bean>
    <bean id="demoDatasource1" parent="parentDataSource">
        <property name="url" value="${db1.jdbc.url}" />
        <property name="username" value="${db1.jdbc.username}" />
        <property name="password" value="${db1.jdbc.password}" />
    </bean>
    <bean id="demoDatasource2" parent="parentDataSource">
        <property name="url" value="${db2.jdbc.url}" />
        <property name="username" value="${db2.jdbc.username}" />
        <property name="password" value="${db2.jdbc.password}" />
    </bean>    
    <bean id="demoDatasource3" parent="parentDataSource">
        <property name="url" value="${db3.jdbc.url}" />
        <property name="username" value="${db3.jdbc.username}" />
        <property name="password" value="${db3.jdbc.password}" />
    </bean>      
```

