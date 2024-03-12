package me.springstudy.jpastudy.common;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
@Builder
public class PageDTO {

	private Integer currentPage;
	private Integer size;
	private String sortBy;

	public Pageable toPageable() {
		if (Objects.isNull(sortBy)) {
			return PageRequest.of(currentPage - 1, size);
		} else {
			return PageRequest.of(currentPage - 1, size, Sort.by(sortBy).descending());
		}
	}
}
