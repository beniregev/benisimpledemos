package com.beniregev.util;

import com.beniregev.exception.ReflectionException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.Advised;
import org.springframework.util.ReflectionUtils.FieldFilter;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

public final class ReflectionUtils {

    /**
     * Field filter instance which rejects any field which is static or final or
     * has the name (case insensitive) 'id' or has the name (case insensitive)
     * 'uuid'.
     */
    public static final FieldFilter idRejectingFieldFilter = new BasicCopyFieldFilter() {
        @Override
        public boolean matches(final Field field) {
            return super.matches(field)
                    && !(field.getName().equalsIgnoreCase("id") || field.getName().equalsIgnoreCase("uuid"));
        }
    };

    /**
     * Map of primitive types to default values.
     */
    private static final Map<Class<?>, Object> defaultPrimitives;

    static {
        // Map primitive types to default values.
        // Source http://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html
        Map<Class<?>, Object> primitives = new HashMap<Class<?>, Object>();
        primitives.put(Byte.class, 0);
        primitives.put(Short.class, 0);
        primitives.put(Integer.class, 0);
        primitives.put(Long.class, 0L);
        primitives.put(Float.class, 0.0f);
        primitives.put(Double.class, 0.0d);
        primitives.put(Character.class, '\u0000');
        primitives.put(Boolean.class, false);
        defaultPrimitives = Collections.unmodifiableMap(primitives);
    }

    private ReflectionUtils() {
        // static only
    }

    /**
     * Use reflection to set an otherwise inaccessible field's value for an object.
     * @param o the object
     * @param fieldName the name of the field
     * @param value the value to set
     */
    public static void setFieldValue(final Object o, final String fieldName, final Object value) {
        try {
            final Field field = findField(o, fieldName);
            field.setAccessible(true);
            field.set(o, value);
        }
        catch (final IllegalAccessException e) {
            org.springframework.util.ReflectionUtils.handleReflectionException(e);
            throw new IllegalStateException(
                    "Unexpected reflection exception - " + e.getClass().getName()
                            + ": " + e.getMessage());
        }
    }

    /**
     * Find a field in the class hierarchy.
     * @param o an instance, for instance fields, or a class for static fields.
     * @param fieldName the name of the field
     * @return the field, or <code>null</code> if not found
     */
    /*package*/	static Field findField(final Object o, final String fieldName) {

        Class<?> c = o instanceof Class ? (Class<?>)o : o.getClass();
        while (c != Object.class) {
            try {
                return c.getDeclaredField(fieldName);
            }
            catch (final NoSuchFieldException e) {
                c = c.getSuperclass();
            }
        }

        // will cause a NPE
        return null;
    }

    /**
     * Get a field's value.
     * @param <T>  the value type
     * @param o the object or the class if a static field
     * @param fieldName the field name
     * @return the field value
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(final Object o, final String fieldName) {
        try {
            final Field field = findField(o, fieldName);
            field.setAccessible(true);
            return (T)(o instanceof Class ? field.get(null) : field.get(o));
        }
        catch (final IllegalArgumentException e) {
            org.springframework.util.ReflectionUtils.handleReflectionException(e);
            throw new IllegalStateException(
                    "Unexpected reflection exception - " + e.getClass().getName()
                            + ": " + e.getMessage());
        }
        catch (final IllegalAccessException e) {
            org.springframework.util.ReflectionUtils.handleReflectionException(e);
            throw new IllegalStateException(
                    "Unexpected reflection exception - " + e.getClass().getName()
                            + ": " + e.getMessage());
        }
    }

    /**
     * Create an instance using the specified constructor and optional parameters,
     * converting exceptions as appropriate.
     * @param <T>  the type to create
     * @param constructor  the constructor
     * @param parameters  optional parameters
     * @return  the instance
     */
    public static <T> T newInstance(final Constructor<T> constructor, final Object... parameters) {
        try {
            return constructor.newInstance(parameters);
        }
        catch (final InstantiationException e) {
            org.springframework.util.ReflectionUtils.handleReflectionException(e);
            throw new IllegalStateException(
                    "Unexpected reflection exception - " + e.getClass().getName()
                            + ": " + e.getMessage());
        }
        catch (final InvocationTargetException e) {
            org.springframework.util.ReflectionUtils.handleReflectionException(e);
            throw new IllegalStateException(
                    "Unexpected reflection exception - " + e.getClass().getName()
                            + ": " + e.getMessage());
        }
        catch (final IllegalAccessException e) {
            org.springframework.util.ReflectionUtils.handleReflectionException(e);
            throw new IllegalStateException(
                    "Unexpected reflection exception - " + e.getClass().getName()
                            + ": " + e.getMessage());
        }
    }

    /**
     * Create a new instance of the specified class.
     * @param <T>  the class type
     * @param className  the class name
     * @return  the instance
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(final String className) {
        try {
            return (T)forName(className).newInstance();
        }
        catch (final InstantiationException e) {
            org.springframework.util.ReflectionUtils.handleReflectionException(e);
            throw new IllegalStateException(
                    "Unexpected reflection exception - " + e.getClass().getName()
                            + ": " + e.getMessage());
        }
        catch (final IllegalAccessException e) {
            org.springframework.util.ReflectionUtils.handleReflectionException(e);
            throw new IllegalStateException(
                    "Unexpected reflection exception - " + e.getClass().getName()
                            + ": " + e.getMessage());
        }
    }

    /**
     * Create a new instance of the class.
     * @param <T>  the type
     * @param clazz  the class
     * @return  the instance
     */
    public static <T> T newInstance(final Class<T> clazz) {
        try {
            // try public default constructor first
            return clazz.newInstance();
        }
        catch (final InstantiationException e) {
            org.springframework.util.ReflectionUtils.handleReflectionException(e);
            throw new IllegalStateException(
                    "Unexpected reflection exception - " + e.getClass().getName()
                            + ": " + e.getMessage());
        }
        catch (final IllegalAccessException e) {
            try {
                // make a non-public default constructor accessible
                final Constructor<T> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                return newInstance(constructor);
            }
            catch (final NoSuchMethodException nsme) {
                // no default constructor
                org.springframework.util.ReflectionUtils.handleReflectionException(nsme);
                throw new IllegalStateException(
                        "Unexpected reflection exception - " + nsme.getClass().getName()
                                + ": " + nsme.getMessage());
            }
        }
    }

    /**
     * Return the class for a given class name.
     * @param <T>  the class type
     * @param className name of class
     * @return the class
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> forName(final String className) {
        try {
            return (Class<T>)Class.forName(className);
        }
        catch (final ClassNotFoundException e) {
            org.springframework.util.ReflectionUtils.handleReflectionException(e);
            throw new IllegalStateException(
                    "Unexpected reflection exception - " + e.getClass().getName()
                            + ": " + e.getMessage());
        }
    }

    /**
     * Get the constructor with the specified parameter classes.
     * @param <T>  the type of the class
     * @param clazz  the class
     * @param parameterClasses  the optional parameter classes
     * @return  the constructor
     */
    public static <T> Constructor<T> getConstructor(
            final Class<T> clazz,
            final Class<?>... parameterClasses) {

        try {
            final Constructor<T> constructor = clazz.getDeclaredConstructor(parameterClasses);
            constructor.setAccessible(true);
            return constructor;
        }
        catch (final NoSuchMethodException e) {
            org.springframework.util.ReflectionUtils.handleReflectionException(e);
            throw new IllegalStateException(
                    "Unexpected reflection exception - " + e.getClass().getName()
                            + ": " + e.getMessage());
        }
    }

    /**
     * Get the proxied target object.
     * @param <T>  the type of the target
     * @param proxy  the proxy
     * @return  the target
     */
    @SuppressWarnings("unchecked")
    public static <T> T getProxyTarget(final Object proxy) {
        try {
            return (T)((Advised)proxy).getTargetSource().getTarget();
        }
        // CSOFF: IllegalCatch
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
        // CSON: IllegalCatch
    }

    /**
     * Given a class, find the property descriptor for the specified property path. This is similar
     * to functionality offered by Spring's property accessors and Apache Commons BeanUtils, but does not
     * require an instantiated object.
     * @param clazz The class.
     * @param propertyPath A dot-separated property path or simple property name. The final part of
     *   the path may include [] or () characters to specify an index of a collection, but the index
     *   is ignored, since this method operates on structures rather than objects.
     * @return The property descriptor.
     */
    public static PropertyDescriptor findPropertyDescriptor(final Class<?> clazz, final String propertyPath) {
        if (clazz == null || propertyPath == null) {
            throw new IllegalArgumentException("Object cannot be null.");
        }

        final String[] parts = propertyPath.split("\\.", 2);
        final String propName = parts[0].split("[\\[\\(]")[0];	// strip off any collection index at the end
        if (propName.length() == 0) {
            throw new IllegalArgumentException("Missing property name in path " + propertyPath + " for " + clazz);
        }
        // Search for matching property.
        final List<PropertyDescriptor> descriptors = Arrays.asList(PropertyUtils.getPropertyDescriptors(clazz));
        for (final PropertyDescriptor descriptor : descriptors) {
            if (descriptor.getName().equals(propName)) {
                // Found it.
                if (parts.length == 1) {
                    // This was the last property in the path.
                    return descriptor;
                }
                // There are more properties in the path. Keep drilling down.
                assert parts.length == 2;
                return findPropertyDescriptor(descriptor.getPropertyType(), parts[1]);
            }
        }

        // If we drop down here, we couldn't find the property.
        throw new ReflectionException("Cannot find property '" + propName + "' in " + clazz);
    }

    /**
     * Given the source object and the destination, which must be the same class
     * or a subclass, copy all fields, including inherited fields. Designed to
     * work on objects with public no-arg constructors.<br>
     * This is pretty much a copy of the Spring mehtod except that here we allow
     * the definition of the field filter to be used to prevent copying certain
     * fields, such as id or uuid.
     * @param src
     *            The source to copy from.
     * @param dest
     *            The destination to copy to.
     * @param filter
     *            A field filter which will determine which fields are copied.
     *            This filter should ACCEPT fields that will be copied.
     * @throws IllegalArgumentException
     *             if the arguments are incompatible
     */
    public static void shallowCopyFieldState(final Object src, final Object dest, final FieldFilter filter)
            throws IllegalArgumentException {
        if (src == null) {
            throw new IllegalArgumentException("Source for field copy cannot be null");
        }
        if (dest == null) {
            throw new IllegalArgumentException("Destination for field copy cannot be null");
        }
        if (!src.getClass().isAssignableFrom(dest.getClass())) {
            throw new IllegalArgumentException("Destination class [" + dest.getClass().getName()
                    + "] must be same or subclass as source class [" + src.getClass().getName() + "]");
        }
        org.springframework.util.ReflectionUtils.doWithFields(src.getClass(),
                new org.springframework.util.ReflectionUtils.FieldCallback() {
                    public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
                        org.springframework.util.ReflectionUtils.makeAccessible(field);
                        final Object srcValue = field.get(src);
                        field.set(dest, srcValue);
                    }
                }, filter);
    }

    /**
     * Basic filter to accept all fields which are not static and are not final.
     * @author Eric.Lundin
     */
    public static class BasicCopyFieldFilter implements FieldFilter {
        /**
         * {@inheritDoc}
         */
        public boolean matches(final Field field) {
            return !(Modifier.isStatic(field.getModifiers())
                    || Modifier.isFinal(field.getModifiers()));
        }
    }

    /**
     * Return the default value for primitives, null for anything else.
     * @param clazz The type for which we want a default value.
     * @return The default value.
     */
    public static Object defaultValue(final Class<?> clazz) {
        if (defaultPrimitives.containsKey(clazz)) {
            return defaultPrimitives.get(clazz);
        }
        else {
            return null;
        }
    }

    /**
     * Basic filter to reject all fields which are static or final as well as
     * reject the fields whose name matches (case insensitive) the given names.
     * Rejected fields are only defined in constructor to allow static
     * declaration without fear of modification by using class.
     * @author Eric.Lundin
     */
    public static class RejectNamedFieldFilter extends BasicCopyFieldFilter {
        private final List<String> rejectFields = new ArrayList<String>();

        /**
         * Create a new instance, adding the given fields to the list of fields
         * to reject.
         * @param rejectFields
         *            The names of the fields to reject.
         */
        public RejectNamedFieldFilter(final String... rejectFields) {
            if (rejectFields != null) {
                this.rejectFields.addAll(Arrays.asList(rejectFields));
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean matches(final Field field) {
            boolean matches = super.matches(field);
            if (matches) {
                for (final String current : rejectFields) {
                    matches = !StringUtils.equalsIgnoreCase(current, field.getName());
                    if (!matches) {
                        break;
                    }
                }
            }
            return matches;
        }
    }
}
