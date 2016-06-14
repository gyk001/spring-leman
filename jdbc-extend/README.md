Spring Leman Jdbc extend
====
Spring jdbc 扩展库

## DatabaseInitUtil

数据源初始化工具类,一般用于测试,比如每个test之前执行sql脚本初始化数据。

脚本可以是纯sql文本也可以使用jetbrick模板语法写构建复杂脚本,支持动态传递参数。


```java
@ContextConfiguration(locations = { "classpath:/spring-jdbc-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringJdbcTest {

    @Autowired
    private DataSource myDataSource;

    @Autowired
    private MyDao MyDao;

    /**
     * 执行所有测试前先drop并重新建库
     * @throws SQLException
     */
    @Before
    public void setUp() throws SQLException {
        Assert.assertNotNull("数据源为空", dataSource);
        DatabaseInitUtil.execSQL(myDataSource, "/sql/recreate-table.sql");
    }

    /**
     * 有数据时
     * @throws SQLException
     */
    @Test
    public void test2() throws SQLException {
        final String name = "zhangsan";
        final int count = 66;

        // 动态插入count条数据
        final Map<String,Object> context = Maps.newHashMap();
        context.put("name", name);
        context.put("count", count);
        
        DatabaseInitUtil.execSQL(dataSource, context, "sql/insert-data-loop.sql");

        List<MyModel> models =  myDao.findByName(name);
        Assert.assertNotNull("结果为空", models);
        Assert.assertEquals("结果集数量不相等", count, models.size());
        for(MyModel model: models){
            Assert.assertEquals("pin不一样", name, model.getName());
        }
    }
}
```
