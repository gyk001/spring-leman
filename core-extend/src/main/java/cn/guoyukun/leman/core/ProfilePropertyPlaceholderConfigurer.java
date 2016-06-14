package cn.guoyukun.leman.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author 郭玉昆 （<a href="guoyukun@jd.com">guoyukun@jd.com</a>）
 * @since 2015年11月30日
 */
public class ProfilePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    private static final Logger LOG = LoggerFactory.getLogger(ProfilePropertyPlaceholderConfigurer.class.getName());
    private static final String DEFAULT_PROFILE_PLACEHOLDER = "profile";
    private static final String DEFAULT_PROFILE_CONFIG_PATH = "profile.properties";
    // 配置键名
    private String profilePlaceholder = DEFAULT_PROFILE_PLACEHOLDER;
    // 主控文件路径
    private String profileConfigPath = DEFAULT_PROFILE_CONFIG_PATH;
    // 托底配置,配置文件里没有配的话会使用这个
    private String fallbackProfile;
    // 生效的配置
    private String profile;

    private void loadConfig() {
        // 已经直接配置profile，不读取配置文件了
        if (profile != null) {
            return;
        }

        try {
            Properties p = PropertiesLoaderUtils.loadAllProperties(profileConfigPath);
            profile = p.getProperty(profilePlaceholder);
            // 没有配置，使用托底配置
            if (profile == null || profile.trim().isEmpty()) {
                profile = fallbackProfile;
            }
        } catch (Exception e) {
            throw new RuntimeException("加载[" + profileConfigPath + "]失败", e);
        }
        // 没有读取到有效的配置
        if (profile == null || profile.trim().isEmpty()) {
            throw new RuntimeException("请配置profile或fallbackProfile");
        }
        profile = profile.trim();
    }

    @Override
    public void setLocations(Resource[] locations) {
        loadConfig();

        String baseProfile = profile.split("\\.")[0];

        LOG.warn("加载[" + baseProfile + "]+[" + profile + "]下的配置文件");
        if (locations != null) {
            List<Resource> resources = new ArrayList<Resource>();

            for (int i = 0; i < locations.length; i++) {
                Resource r = locations[i];
                if (r == null) {
                    continue;
                }
                if (r instanceof ClassPathResource) {
                    String path = ((ClassPathResource) r).getPath();
                    if (path.contains("${" + profilePlaceholder + "}")) {
                        // 带占位符，尝试解析成两个路径
                        String basePath = path.replace("${" + profilePlaceholder + "}", baseProfile);
                        ClassPathResource baseRes = checkClassPathResource(basePath);
                        if (baseRes != null) {
                            LOG.debug("加载基础属性文件: {} <- {}", basePath, path);
                            resources.add(baseRes);
                        } else {
                            LOG.debug("基础属性文件不存在,忽略: {} <- {}", basePath, path);
                        }
                        String specialPath = path.replace("${" + profilePlaceholder + "}", profile);
                        ClassPathResource specialRes = checkClassPathResource(specialPath);
                        if (specialRes != null) {
                            LOG.debug("加载组内属性文件: {} <- {}", specialPath, path);
                            resources.add(specialRes);
                        } else {
                            LOG.debug("组内属性文件不存在,忽略: {} <- {}", specialPath, path);
                        }
                    } else {
                        //newRes[i]=r;
                        LOG.debug("加载属性文件: {}", path);
                        resources.add(r);
                    }
                } else {
                    //newRes[i]=r;
                    LOG.debug("加载属性文件: {}", r.getFilename());
                    resources.add(r);
                }
            }
            Resource[] clacLocations = resources.toArray(new Resource[resources.size()]);
            super.setLocations(clacLocations);
        }
    }

    private ClassPathResource checkClassPathResource(String path) {
        ClassPathResource classPathResource = new ClassPathResource(path);
        if (classPathResource.exists() && classPathResource.isReadable()) {
            return classPathResource;
        }
        return null;
    }


    @Override
    protected Properties mergeProperties() throws IOException {
        Properties p = super.mergeProperties();
        p.setProperty(profilePlaceholder, profile);
        return p;
    }

    public String getFallbackProfile() {
        return fallbackProfile;
    }

    public void setFallbackProfile(String fallbackProfile) {
        this.fallbackProfile = fallbackProfile;
    }

    public String getProfilePlaceholder() {
        return profilePlaceholder;
    }

    public void setProfilePlaceholder(String profilePlaceholder) {
        this.profilePlaceholder = profilePlaceholder;
    }

    public String getProfileConfigPath() {
        return profileConfigPath;
    }

    public void setProfileConfigPath(String profileConfigPath) {
        this.profileConfigPath = profileConfigPath;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
