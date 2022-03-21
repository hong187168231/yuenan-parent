package com.indo.common.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.*;

public class BeanConvertUtils {

	private static final Logger logger = LoggerFactory.getLogger(BeanConvertUtils.class);

	private BeanConvertUtils() {
	}

	public static <T> T srcToTarget(Object source, Class<T> target) {
		if (Objects.isNull(source)) {
			return null;
		}
		T targetObject = null;
		try {
			targetObject = target.newInstance();
			BeanUtils.copyProperties(source, targetObject);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return targetObject;
	}

	public static <T> List<T> srcToTarget(Collection<?> sourceList, Class<T> target) {
		if (sourceList == null) {
			return Collections.emptyList();
		}

		List<T> targetList = new ArrayList<>(sourceList.size());
		try {
			for (Object source : sourceList) {
				T targetObject = target.newInstance();
				BeanUtils.copyProperties(source, targetObject);
				targetList.add(targetObject);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return targetList;
	}
}