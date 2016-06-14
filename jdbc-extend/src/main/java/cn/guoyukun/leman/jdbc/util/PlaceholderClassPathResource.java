package cn.guoyukun.leman.jdbc.util;


import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guoyukun on 2016/6/13.
 */
public class PlaceholderClassPathResource extends ClassPathResource {
    private static final JetEngine engine = JetEngine.create();

    private Map<String, Object> placeholders;

    public PlaceholderClassPathResource(String path) {
        super(path);
        this.placeholders = new HashMap<String, Object>();
    }

    public PlaceholderClassPathResource(String path, Map placeholders) {
        super(path);
        this.placeholders = placeholders;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is = super.getInputStream();
        if (placeholders == null || placeholders.isEmpty()) {
            return is;
        }
        String content = IOUtils.toString(is, "UTF-8");
        JetTemplate template = engine.createTemplate(content);
        StringWriter sw = new StringWriter();
        template.render(placeholders, sw);
        return IOUtils.toInputStream(sw.toString(), "UTF-8");
    }
}

