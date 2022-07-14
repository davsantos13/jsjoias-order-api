package com.js.order_api.config.querys;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.js.order_api.util.DateConverter;

public class Request {

	private Integer pageSize;

	private Integer pageNumber;

	private String sortOrder;

	private String sortField;

	private Map<String, Object> filter = new HashMap<>();

	private Set<SearchCriteria> list = new HashSet<>();

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNumber() {
		return pageNumber - 1;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getSortOrder() {
		return sortOrder == null ? "" : sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getSortField() {
		return sortField == null ? "" : sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public Map<String, Object> getFilter() {
		return filter;
	}

	public void setFilter(Map<String, Object> filter) {
		this.filter = filter;
	}

	public Set<SearchCriteria> getList() {
		list = new HashSet<>();
		if (this.filter != null && !this.filter.isEmpty()) {
			for (String key : filter.keySet()) {
				Object value = filter.get(key);
				if (value != null && !value.toString().isEmpty()) {
					SearchOperation operation = SearchOperation.EQUAL;
					if (value instanceof String)
						operation = SearchOperation.MATCH;

					if (value.toString().startsWith("Enum.")) {
						value = value.toString().replace("Enum.", "");
						operation = SearchOperation.EQUAL;
					}

					if (value instanceof List) {
						@SuppressWarnings("unchecked")
						List<Object> lista = (List<Object>) value;

						if (lista.size() == 2 && DateConverter.isDate(lista.get(0))
								&& DateConverter.isDate(lista.get(1)))
							operation = SearchOperation.BETWEEN_DATE;
						else
							operation = SearchOperation.IN;
					}

					list.add(new SearchCriteria(key, value, operation));
				}
			}
		}
		return list;
	}

	public void setList(Set<SearchCriteria> list) {
		this.list = list;
	}

	private Sort getSort() {
		if ((sortField != null && !sortField.isEmpty()) && (sortOrder != null && !sortOrder.isEmpty()))
			return Sort.by(Sort.Direction.valueOf(sortOrder.toUpperCase()), sortField);
		else if ((sortField != null && !sortField.isEmpty()) && (sortOrder == null || sortOrder.isEmpty()))
			return Sort.by(sortField);
		return null;
	}

	public Pageable getPageable() {
		int pageNumber = this.pageNumber != null ? this.pageNumber : 0;
		int pageSize = this.pageSize != null ? this.pageSize : 100;

		Sort sort = this.getSort();

		if (sort == null)
			return PageRequest.of(pageNumber - 1, pageSize);
		else
			return PageRequest.of(pageNumber - 1, pageSize, sort);
	}

}
