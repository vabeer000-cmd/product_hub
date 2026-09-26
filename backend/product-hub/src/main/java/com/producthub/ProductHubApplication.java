package com.producthub;

import java.time.LocalDateTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;

import com.producthub.product.util.CursorUtils;

@EnableCaching
@EnableJpaAuditing
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
@SpringBootApplication
public class ProductHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductHubApplication.class, args);
		System.err.println(
			    CursorUtils.encode(
			        LocalDateTime.parse("2026-09-12T21:14:19"),
			        100016L
			    )
			);
	}

}
