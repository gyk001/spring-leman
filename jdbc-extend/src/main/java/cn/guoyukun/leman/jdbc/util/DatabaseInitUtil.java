package cn.guoyukun.leman.jdbc.util;


import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guoyukun on 2016/6/13.
 */
public class DatabaseInitUtil {

    private static final Resource[] TYPE_ARRAY_RESOURCE = new Resource[]{};

    public static void execSQL(DataSource dataSource, Map<String, ?> context, String... sqls) {

        List<Resource> resourceList = new ArrayList<Resource>();
        for (String sql : sqls) {
            PlaceholderClassPathResource r = new PlaceholderClassPathResource(sql, context);
            resourceList.add(r);
        }
        execSQL(dataSource, resourceList.toArray(TYPE_ARRAY_RESOURCE));
    }

    public static void execSQL(DataSource dataSource, Resource... sqls) {
        ResourceDatabasePopulator rdp = new ResourceDatabasePopulator();
        for (Resource sql : sqls) {
            rdp.addScript(sql);
        }
        DatabasePopulatorUtils.execute(rdp, dataSource);
    }

    public static void execSQL(DataSource dataSource, String... sqls) {
        Map<String, ?> context = new HashMap<String, Object>();
        execSQL(dataSource, context, sqls);
    }
}
