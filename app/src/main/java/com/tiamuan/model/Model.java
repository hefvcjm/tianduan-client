package com.tiamuan.model;

import com.tiamuan.annotation.Column;
import com.tiamuan.annotation.ToStringIgnore;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Model implements Serializable {

    @Column
    protected Long id;
    @Column
    protected String objectId;

    public Model() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @Override
    public String toString() {
        Map<String, Object> str = new HashMap<>();
        Class<? extends Model> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(Column.class) != null && field.getAnnotation(ToStringIgnore.class) == null) {
                String fieldName = field.getName();
                String funName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                try {
                    Method method = clazz.getMethod(funName);
                    Object get = method.invoke(this);
                    if (get == null) {
                        get = "null";
                    }
                    str.put(fieldName, get);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return str.toString();
    }

}
