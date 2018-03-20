package biz.iws.core.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author Tomohiro Akinaga
 */
public class ReflectionUtils {

	/**
	 * @param field
	 * @param obj
	 * @param value
	 * @throws NumberFormatException
	 * @throws IllegalArgumentException
	 */
	public static void setField(Field field, Object obj,
			Object value) throws NumberFormatException, IllegalArgumentException, IllegalAccessException {

		if (field == null || obj == null || value == null) {
			return;
		}

		field.setAccessible(true);

		Class<?> type = field.getType();

		if (type.equals(int.class) || type.equals(Integer.class)) {
			if (NumberUtils.isParsable(value.toString())) {
				field.set(obj, Integer.parseInt(value.toString()));
			}
		} else if (type.equals(long.class) || type.equals(Long.class)) {
			if (NumberUtils.isParsable(value.toString())) {
				field.set(obj, Long.parseLong(value.toString()));
			}
		} else if (type.equals(float.class) || type.equals(Float.class)) {
			if (NumberUtils.isParsable(value.toString())) {
				field.set(obj, Float.parseFloat(value.toString()));
			}
		} else if (type.equals(double.class) || type.equals(Double.class)) {
			if (NumberUtils.isParsable(value.toString())) {
				field.set(obj, Double.parseDouble(value.toString()));
			}
		} else if (type.equals(byte.class) || type.equals(Byte.class)) {
			if (NumberUtils.isParsable(value.toString())) {
				field.set(obj, Byte.parseByte(value.toString()));
			}
		} else if (type.equals(String.class)) {
			field.set(obj, value.toString());
		} else {
			field.set(obj, value);
		}
	}

	/**
	 * @param c
	 * @param containsSuper If true, contains super class fields into return array.
	 * @return {@link Field[]}
	 */
	public static Field[] getDeclaredFields(Class<?> c, boolean containsSuper) {

		List<Field> fields = new ArrayList<>();

		if (containsSuper && c.getSuperclass() != null) {
			fields.addAll(Arrays.asList(getDeclaredFields(c.getSuperclass(), containsSuper)));
		}

		fields.addAll(Arrays.asList(c.getDeclaredFields()));

		return fields.stream().toArray(Field[]::new);
	}

}
