package cn.zifangsky.quickmodules.user.plugins;

import cn.zifangsky.quickmodules.common.annotations.PropertySourcedMapping;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UriTemplate;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用于在{@link Controller}的{@link RequestMapping}中动态注入请求URL
 * @author zifangsky
 * @date 2017/11/7 11:00
 * @since 1.0.0
 */
public class PropertySourcedRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

  private final Map<String, HandlerMethod> handlerMethods = new LinkedHashMap<>();
  private final WebUserInfo webUserInfo;
  private final Object handler;

  public PropertySourcedRequestMappingHandlerMapping(
          WebUserInfo webUserInfo,
      Object handler) {
    this.webUserInfo = webUserInfo;
    this.handler = handler;
  }
  
  @Override
  protected void initHandlerMethods() {
    logger.debug("initialising the handler methods");
    setOrder(Ordered.HIGHEST_PRECEDENCE + 1000);
    Class<?> clazz = handler.getClass();
    if (isHandler(clazz)) {
      for (Method method : clazz.getMethods()) {
        //获取参数注解
        PropertySourcedMapping mapper = AnnotationUtils.getAnnotation(method, PropertySourcedMapping.class);
        if (mapper != null) {
          RequestMappingInfo mapping = getMappingForMethod(method, clazz);
          HandlerMethod handlerMethod = createHandlerMethod(handler, method);
          String mappingPath = mappingPath(mapper);

          if (mappingPath != null) {
            logger.info(String.format("Mapped URL path [%s] onto method [%s]", mappingPath, handlerMethod.toString()));
            //将urlPath和对应的处理HandlerMethod放到LinkedHashMap
            handlerMethods.put(mappingPath, handlerMethod);
          } else {
            for (String path : mapping.getPatternsCondition().getPatterns()) {
              logger.info(String.format("Mapped URL path [%s] onto method [%s]", path, handlerMethod.toString()));
              handlerMethods.put(path, handlerMethod);
            }
          }
        }
      }
    }
  }

  /**
   * 根据参数注解获取请求URL
   * @author zifangsky
   * @date 2017/11/7 10:50
   * @since 1.0.0
   * @param mapper 参数注解
   * @return java.lang.String
   */
  private String mappingPath(final PropertySourcedMapping mapper) {
    final String key = mapper.propertyKey();

    if(StringUtils.isNoneBlank(key)){
        try {
            return BeanUtils.getProperty(webUserInfo, key);
        } catch (Exception e) {
            logger.error(MessageFormat.format("获取参数[{0}]对应的值失败：", key), e);
        }
    }

    return null;
  }

  @Override
  protected boolean isHandler(Class<?> beanType) {
    return ((AnnotationUtils.findAnnotation(beanType, Controller.class) != null) ||
                (AnnotationUtils.findAnnotation(beanType, RequestMapping.class) != null));
  }

  /**
   * 通过请求的urlPath查找处理的{@link HandlerMethod}
   * @author zifangsky
   * @date 2017/11/7 10:35
   * @since 1.0.0
   * @param urlPath 请求URL
   * @return org.springframework.web.method.HandlerMethod
   */
  @Override
  protected HandlerMethod lookupHandlerMethod(String urlPath, HttpServletRequest request) {
    logger.debug("looking up handler for path: " + urlPath);
    HandlerMethod handlerMethod = handlerMethods.get(urlPath);
    if (handlerMethod != null) {
      return handlerMethod;
    }
    for (String path : handlerMethods.keySet()) {
      UriTemplate template = new UriTemplate(path);
      if (template.matches(urlPath)) {
        request.setAttribute(
            HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE,
            template.match(urlPath));
        return handlerMethods.get(path);
      }
    }
    return null;
  }

}
