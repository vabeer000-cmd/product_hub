package com.producthub.product.util;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

import com.producthub.common.exception.InvalidCursorException;
import com.producthub.product.dto.ProductCursor;

public final class CursorUtils {

	private CursorUtils() {
	}

	public static String encode(
			LocalDateTime createdAt, Long id) {

		String value = createdAt.toString() + "|" + id;

		return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
	}

	public static ProductCursor decode(String cursor) {

		try {
			String decoded = new String(Base64.getUrlDecoder().decode(cursor), StandardCharsets.UTF_8);

			String[] parts = decoded.split("\\|");

			LocalDateTime createdAt = LocalDateTime.parse(parts[0]);

			Long id = Long.parseLong(parts[1]);

			return new ProductCursor(createdAt, id);

		} catch (Exception e) {
			throw new InvalidCursorException("Invalid cursor");
		}
	}
}