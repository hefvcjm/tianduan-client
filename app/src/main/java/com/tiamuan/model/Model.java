package com.tiamuan.model;

import com.tiamuan.annotation.Column;
import com.tiamuan.annotation.ToStringIgnore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Model implements Serializable {

    @Column
    protected Long id;
    @Column
    protected String objectId;

    public Model() {

    }

    public Model(String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        Iterator<String> keys = object.keys();
        Set<String> keySet = new HashSet<>();
        while (keys.hasNext()) {
            keySet.add(keys.next());
        }
        Class<? extends Model> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            Class<?> fieldType = field.getType();
            if (keySet.contains(fieldName)) {
                try {
                    String funName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method method = clazz.getMethod(funName, fieldType);
                    if (fieldType.equals(String.class)) {
                        method.invoke(this, object.getString(fieldName));
                    } else if (fieldType.equals(Set.class)) {
                        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                        Type type = parameterizedType.getActualTypeArguments()[0];
                        if (Class.forName(type.toString()).isAssignableFrom(Model.class)){
                            method.invoke(this,(Model)Class.forName(type.toString()).newInstance(object.getString(fieldName)));
                        }
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
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
