package com.emily.infrastructure.sensitive;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @Description :  对实体类进行脱敏，返回原来的实体类对象
 * @Author :  Emily
 * @CreateDate :  Created in 2023/4/21 1:50 PM
 */
public class DeSensitiveUtils {


    /**
     * @param entity 实体类|普通对象
     * @return 对实体类进行脱敏，返回原来的实体类对象
     */
    public static <T> T acquire(final T entity) throws IllegalAccessException {
        if (JavaBeanUtils.isFinal(entity)) {
            return entity;
        }
        if (entity instanceof Collection) {
            for (Iterator it = ((Collection) entity).iterator(); it.hasNext(); ) {
                acquire(it.next());
            }
        } else if (entity instanceof Map) {
            for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) entity).entrySet()) {
                acquire(entry.getValue());
            }
        } else if (entity.getClass().isArray()) {
            if (!entity.getClass().getComponentType().isPrimitive()) {
                for (Object v : (Object[]) entity) {
                    acquire(v);
                }
            }
        } else if (entity.getClass().isAnnotationPresent(JsonSensitive.class)) {
            doSetField(entity);
        }
        return entity;
    }

    /**
     * @param entity 实体类对象
     * @throws IllegalAccessException 非法访问异常
     * @Description 对实体类entity的属性及父类的属性遍历并对符合条件的属性进行多语言翻译
     */
    protected static <T> void doSetField(final T entity) throws IllegalAccessException {
        Field[] fields = FieldUtils.getAllFields(entity.getClass());
        for (Field field : fields) {
            if (JavaBeanUtils.isModifierFinal(field)) {
                continue;
            }
            field.setAccessible(true);
            Object value = field.get(entity);
            if (checkNullValue(field, value)) {
                doGetEntityNull(field, entity, value);
                continue;
            }
            if (value instanceof String) {
                doGetEntityStr(field, entity, value);
            } else if (value instanceof Collection) {
                doGetEntityColl(field, entity, value);
            } else if (value instanceof Map) {
                doGetEntityMap(field, entity, value);
            } else if (value.getClass().isArray()) {
                doGetEntityArray(field, entity, value);
            } else {
                acquire(value);
            }
        }
        doGetEntityFlex(entity);
    }

    /**
     * 判定Field字段值是否置为null
     * -------------------------------------------
     * 1.value为null,则返回true
     * 2.field字段类型为原始数据类型，如int、boolean、double等，则返回false
     * 3.field被JsonNullField注解标注，则返回true
     * 4.其它场景都返回false
     * -------------------------------------------
     *
     * @param field 字段对象
     * @param value 字段值
     * @return true-置为null, false-按原值展示
     */
    protected static boolean checkNullValue(Field field, Object value) {
        if (value == null) {
            return true;
        } else if (field.getType().isPrimitive()) {
            return false;
        } else if (field.isAnnotationPresent(JsonNullField.class)) {
            return true;
        }
        return false;
    }

    /**
     * 将字段值置为null
     *
     * @param field  实体类属性对象
     * @param entity 实体类对象
     * @param value  属性值对象
     * @throws IllegalAccessException 抛出非法访问异常
     */
    protected static <T> void doGetEntityNull(final Field field, final T entity, final Object value) throws IllegalAccessException {
        field.set(entity, null);
    }

    /**
     * @param field  实体类属性对象
     * @param entity 实体类对象
     * @param value  属性值对象
     * @throws IllegalAccessException 抛出非法访问异常
     * @Description 对字符串进行多语言支持
     */
    protected static <T> void doGetEntityStr(final Field field, final T entity, final Object value) throws IllegalAccessException {
        if (field.isAnnotationPresent(JsonSimField.class)) {
            field.set(entity, DataMaskUtils.doGetProperty((String) value, field.getAnnotation(JsonSimField.class).value()));
        } else {
            acquire(value);
        }
    }

    /**
     * @param field  实体类属性对象
     * @param entity 实体类对象
     * @param value  属性值对象
     * @throws IllegalAccessException 抛出非法访问异常
     * @Description 对Collection集合中存储是字符串、实体对象进行多语言支持
     */
    protected static <T> void doGetEntityColl(final Field field, final T entity, final Object value) throws IllegalAccessException {
        Collection<Object> list = null;
        Collection collection = ((Collection) value);
        for (Iterator it = collection.iterator(); it.hasNext(); ) {
            Object v = it.next();
            if (Objects.isNull(v)) {
                continue;
            }
            if ((v instanceof String) && field.isAnnotationPresent(JsonSimField.class)) {
                list = (list == null) ? Lists.newArrayList() : list;
                list.add(DataMaskUtils.doGetProperty((String) v, field.getAnnotation(JsonSimField.class).value()));
            } else {
                acquire(v);
            }
        }
        if (Objects.nonNull(list)) {
            field.set(entity, list);
        }
    }

    /**
     * @param field  实体类属性对象
     * @param entity 实体类对象
     * @param value  属性值对象
     * @throws IllegalAccessException 抛出非法访问异常
     * @Description 对Map集合中存储是字符串、实体对象进行多语言支持
     */
    protected static <T> void doGetEntityMap(final Field field, final T entity, final Object value) throws IllegalAccessException {
        Map<Object, Object> dMap = ((Map<Object, Object>) value);
        for (Map.Entry<Object, Object> entry : dMap.entrySet()) {
            Object key = entry.getKey();
            Object v = entry.getValue();
            if (Objects.isNull(v)) {
                continue;
            }
            if ((v instanceof String) && field.isAnnotationPresent(JsonSimField.class)) {
                dMap.put(key, DataMaskUtils.doGetProperty((String) v, field.getAnnotation(JsonSimField.class).value()));
            } else {
                acquire(value);
            }
        }
    }

    /**
     * @param field  实体类属性对象
     * @param entity 实体类对象
     * @param value  属性值对象
     * @throws IllegalAccessException 抛出非法访问异常
     * @Description 对数组中存储是字符串、实体对象进行多语言支持
     */
    protected static <T> void doGetEntityArray(final Field field, final T entity, final Object value) throws IllegalAccessException {
        if (value.getClass().getComponentType().isPrimitive()) {
            return;
        }
        Object[] arrays = ((Object[]) value);
        for (int i = 0; i < arrays.length; i++) {
            Object v = arrays[i];
            if (Objects.isNull(v)) {
                continue;
            }
            if ((v instanceof String) && field.isAnnotationPresent(JsonSimField.class)) {
                arrays[i] = DataMaskUtils.doGetProperty((String) v, field.getAnnotation(JsonSimField.class).value());
            } else {
                acquire(value);
            }
        }
    }

    /**
     * @param entity 实体类对象
     * @throws IllegalAccessException 抛出非法访问异常
     */
    protected static <T> void doGetEntityFlex(final T entity) throws IllegalAccessException {
        Field[] fields = FieldUtils.getFieldsWithAnnotation(entity.getClass(), JsonFlexField.class);
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(entity);
            if (Objects.isNull(value)) {
                continue;
            }
            JsonFlexField jsonFlexField = field.getAnnotation(JsonFlexField.class);
            if (Objects.isNull(jsonFlexField.fieldValue())) {
                return;
            }
            Field flexField = FieldUtils.getField(entity.getClass(), jsonFlexField.fieldValue(), true);
            if (Objects.isNull(flexField)) {
                return;
            }
            Object flexValue = flexField.get(entity);
            if (Objects.isNull(flexValue) || !(flexValue instanceof String)) {
                return;
            }
            int index = Arrays.asList(jsonFlexField.fieldKeys()).indexOf((String) value);
            if (index < 0) {
                return;
            }
            SensitiveType type;
            if (index >= jsonFlexField.types().length) {
                type = SensitiveType.DEFAULT;
            } else {
                type = jsonFlexField.types()[index];
            }
            flexField.set(entity, DataMaskUtils.doGetProperty((String) flexValue, type));
        }
    }
}
