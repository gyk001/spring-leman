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

## map标签

```xml
    <guo:map id="indenpentMap" map-class="java.util.TreeMap">
        <guo:for from="1" to="3" placeholder="index">
            <entry key="ka-{{index}}" value="va-{{index}}" ></entry>
            <entry key="kaz-{{index}}" value="vaz-{{index}}" ></entry>
        </guo:for>
        <guo:for from="1" to="3" desc="b">
            <entry key="kb-{{index}}" value="vb-{{index}}" ></entry>
        </guo:for>
    </guo:map>

```

等同于

```xml
<util:map id="indenpentMap" map-class="java.util.TreeMap">
	<entry key="ka-1" value="va-1"/>
	<entry key="kaz-1" value="vaz-1"/>
	<entry key="ka-2" value="va-2"/>
	<entry key="kaz-2" value="vaz-2"/>
	<entry key="kb-1" value="vb-1"/>
	<entry key="kb-2" value="vb-2"/>
</util:map>

```

