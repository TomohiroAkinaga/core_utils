package biz.iws.http.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import biz.iws.reflection.ReflectionUtils;

/**
 * @author Tomohiro Akinaga
 */
public class UrlParameterUtils {

	private static final Logger log = LoggerFactory.getLogger(UrlParameterUtils.class);

	private static final String QMARK = "?";
	private static final String EQUAL = "=";
	private static final String AMP = "&";

	/**
	 * private constructor
	 */
	private UrlParameterUtils() {}

	/**
	 * @param baseUrl
	 * @param getParams
	 * @return {@link String}
	 */
	public static String buildGetUrl(String baseUrl, Map<String, String> getParams, final boolean encodeParamValue) {

		if (getParams == null || getParams.isEmpty()) {
			return baseUrl;
		}

		String queryParams = getParams.entrySet().stream()
				.map(entry -> {
					try {
						String value = encodeParamValue ? URLEncoder.encode(entry.getValue(), "UTF-8") : entry
								.getValue();
						return String.join(EQUAL, entry.getKey(), value);
					} catch (UnsupportedEncodingException e) {
						log.warn("Can not url-encode. [parameter : {}]", entry.getValue());
						return "";
					}
				}).collect(Collectors.joining(AMP));

		if (baseUrl.indexOf(QMARK) < 0) {
			return baseUrl + QMARK + queryParams;
		} else if (baseUrl.endsWith(QMARK)) {
			return baseUrl + queryParams;
		} else {
			return baseUrl + AMP + queryParams;
		}

	}

	/**
	 * @param baseUrl
	 * @param obj
	 * @return {@link String}
	 */
	@SuppressWarnings("unchecked")
	public static String buildGetUrl(String baseUrl, Object obj, final boolean encodeParamValue) {

		if (obj == null) {
			return baseUrl;
		}

		if (obj instanceof Map<?, ?>) {
			return buildGetUrl(baseUrl, (Map<String, String>) obj, encodeParamValue);
		} else {

			Map<String, String> getParams = new HashMap<>();
			Field[] fields = ReflectionUtils.getDeclaredFields(obj.getClass(), true);
			for (Field field : fields) {
				try {
					if (Modifier.isTransient(field.getModifiers())) {
						continue;
					}
					field.setAccessible(true);
					Object value = field.get(obj);
					String key = field.getName();
					Name name = field.getAnnotation(Name.class);
					if (name != null && StringUtils.isNotEmpty(name.value())) {
						if (!name.appendIfNull() && value == null) {
							continue;
						}
						key = name.value();
					}
					getParams.put(key, value != null ? value.toString() : "");
				} catch (IllegalArgumentException | IllegalAccessException e) {
					log.error("Can not access field value. [class : {}, field : {}", obj.getClass().getName(),
							field.getName());
					log.error("", e);
				}
			}
			return buildGetUrl(baseUrl, getParams, encodeParamValue);
		}
	}

}
