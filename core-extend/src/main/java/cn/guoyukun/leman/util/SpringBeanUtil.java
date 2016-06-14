package cn.guoyukun.leman.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import java.util.*;

public class SpringBeanUtil {
    private static final Logger LOG = LoggerFactory.getLogger(SpringBeanUtil.class);

    public static <T> T getBeansNested(ApplicationContext applicationContext, String beanId) {
        ApplicationContext ac = applicationContext;
        while (ac != null) {
            try {
                T bean = (T) ac.getBean(beanId);
                if (bean != null) {
                    return bean;
                }
            } catch (NoSuchBeanDefinitionException ingore) {
            }
            ac = ac.getParent();
        }
        throw new NoSuchBeanDefinitionException(beanId);
    }

    public static <T> T getBeanOfTypeNested(ApplicationContext applicationContext, Class<T> type) {
        Map<String, T> brothers = getBeansOfTypeNested(applicationContext, type);
        if (brothers.isEmpty()) {
            return null;
        }
        return brothers.values().iterator().next();
    }

    public static <T> Map<String, T> getBeansOfTypeNested(ApplicationContext applicationContext, Class<T> type) {
        ApplicationContext ac = applicationContext;
        Map<String, T> brothers = new HashMap<String, T>();
        while (ac != null) {
            Map<String, T> current = ac.getBeansOfType(type, true, true);
            if (current != null && !current.isEmpty()) {
                brothers.putAll(current);
            }
            ac = ac.getParent();
        }
        return brothers;
    }

    public static List<String> getBeanNamesForTypeNested(ApplicationContext applicationContext, Class<?> type) {
        ApplicationContext ac = applicationContext;
        List<String> brothers = new ArrayList<String>();
        while (ac != null) {
            String[] current = ac.getBeanNamesForType(type, true, true);
            if (current != null && current.length > 0) {
                brothers.addAll(Arrays.asList(current));
            }
            ac = ac.getParent();
        }
        return brothers;
    }

    public static List<String> getAliasesNested(ApplicationContext applicationContext, String beanId) {
        ApplicationContext ac = applicationContext;
        List<String> brothers = new ArrayList<String>();
        while (ac != null) {
            String[] current = ac.getAliases(beanId);
            if (current != null && current.length > 0) {
                brothers.addAll(Arrays.asList(current));
            }
            ac = ac.getParent();
        }
        return brothers;
    }

//	
//    public static <E> List<Class<E>> getSubClasses(final Class<E> parentClass,  
//              final String packagePath) {  
//         final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(  
//                   false);  
//         provider.addIncludeFilter(new AssignableTypeFilter(parentClass));  
//         final Set<BeanDefinition> components = provider  
//                   .findCandidateComponents(packagePath);  
//         final List<Class<E>> subClasses = new ArrayList<Class<E>>();  
//         for (final BeanDefinition component : components) {  
//              @SuppressWarnings("unchecked")
//			Class<E> cls;
//			try {
//				cls = (Class<E>) Class.forName(component.getBeanClassName());
//			      if (Modifier.isAbstract(cls.getModifiers())) {  
//	                   continue;  
//	              }  
//	              subClasses.add(cls); 
//			} catch (ClassNotFoundException e) {
//				LOG.error("异常",e);
//			}  
//         
//         }  
//         return subClasses;  
//    }  
}
