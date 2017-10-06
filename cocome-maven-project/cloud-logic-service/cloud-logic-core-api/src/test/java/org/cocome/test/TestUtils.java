package org.cocome.test;

import de.kit.ipd.java.utils.framework.table.Table;
import de.kit.ipd.java.utils.parsing.csv.CSVParser;
import org.cocome.tradingsystem.util.Configuration;
import org.mockito.Mockito;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

/**
 * Utilities for unit tests that use JPA together with an embedded database
 *
 * @author Rudolf Biczok
 */
public class TestUtils {

    private static final Configuration CONFIG = injectFakeCDIObject(Configuration.class, Collections.emptyMap());

    public static <T> T injectFakeCDIObject(Class<T> clazz, Map<Class<?>, Class<?>> mappings) {
        return injectFakeCDIObject(clazz, null , mappings);

    }

    private static <T> T injectFakeCDIObject(Class<T> clazz, final Field field, Map<Class<?>, Class<?>> mappings) {

        final T instance = injectInstance(clazz, field, mappings);

        for (final Field f : listAllFields(instance)) {

            for (final Annotation a : f.getAnnotations()) {
                if (a.annotationType().equals(EJB.class) || a.annotationType().equals(Inject.class)) {
                    setProperty(instance, f, injectFakeCDIObject(f.getType(), f, mappings));
                }
            }
        }

        for (final Method method : listAllMethods(instance)) {
            for (final Annotation a : method.getAnnotations()) {
                if (a.annotationType().equals(PostConstruct.class)) {
                    try {
                        method.invoke(instance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new IllegalArgumentException("Unable to call @PostConstruct method: " + method);
                    }
                }
            }
        }

        return instance;
    }

    private static <T> T injectInstance(Class<T> ejbClass, Field field, Map<Class<?>, Class<?>> mappings) {
        if (mappings.containsKey(ejbClass)) {
            return createInstance((Class<T>) mappings.get(ejbClass));
        }

        if (hasAnnotation(ejbClass, LocalBean.class) || hasAnnotation(ejbClass, Startup.class)) {
            return createInstance(ejbClass);
        }

        if(ejbClass == String.class) {
            InjectionPoint point = Mockito.mock(InjectionPoint.class);
            Mockito.when(point.getMember()).thenReturn(field);
            return (T) CONFIG.getString(point);
        }

        throw new IllegalArgumentException(String.format(
                "Class '%s' has no @LocalBean, @Startup annotation nor a interface / implementation mapping", ejbClass.getName())
                + ejbClass.getName());
    }

    private static <T> T createInstance(final Class<T> ejbClass) {
        try {
            return ejbClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String toCSV(final Table<String> table) {
        final CSVParser csvparser = new CSVParser();
        csvparser.setModel(table);
        return csvparser.toString();
    }

    public static Table<String> fromCSV(final String content) {
        final CSVParser parser = new CSVParser();
        Table<String> table;
        parser.parse(content);
        table = parser.getModel();
        return table;
    }

    private static List<Field> listAllFields(final Object obj) {
        return listAll(obj, Class::getDeclaredFields);
    }

    private static List<Method> listAllMethods(final Object obj) {
        return listAll(obj, Class::getDeclaredMethods);
    }

    private static <T> List<T> listAll(final Object obj, final Function<Class<?>, T[]> extractor) {
        final List<T> current = new ArrayList<>();
        Class tmpClass = obj.getClass();
        while (tmpClass.getSuperclass() != null) {
            current.addAll(Arrays.asList(extractor.apply(tmpClass)));
            tmpClass = tmpClass.getSuperclass();
        }
        return current;
    }

    private static boolean hasAnnotation(Class<?> ejbClass,
                                         Class<? extends Annotation> annotationClass) {
        return Arrays.stream(ejbClass.getDeclaredAnnotations())
                .filter(a -> a.annotationType().equals(annotationClass)).count() != 0;
    }

    private static void setProperty(final Object object, final Field field, final Object fieldValue) {
        try {
            field.setAccessible(true);
            field.set(object, fieldValue);
            field.setAccessible(false);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
