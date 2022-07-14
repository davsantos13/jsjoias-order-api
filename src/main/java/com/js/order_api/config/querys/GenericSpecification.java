package com.js.order_api.config.querys;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.js.order_api.util.DateConverter;

@SuppressWarnings("unchecked")
public class GenericSpecification<T> implements Specification<T> {

	private static final long serialVersionUID = 1L;

	private Set<SearchCriteria> list = new HashSet<>();

	public Set<SearchCriteria> getList() {
		return list;
	}

	public void setList(Set<SearchCriteria> list) {
		this.list = list;
	}

	/**
	 * @param list
	 */
	public GenericSpecification(Set<SearchCriteria> list) {
		super();
		this.list = list;
	}

	public GenericSpecification() {
		super();
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

		// create a new predicate list
		List<Predicate> predicates = new ArrayList<>();

		// add add criteria to predicates
		for (SearchCriteria criteria : list) {
			if (criteria.getKey().contains(".")) {
				Join<Object, Object> join = null;
				String[] split = criteria.getKey().split("\\.");
				String key = split[split.length - 1];

				for (int i = 0; i < split.length - 1; i++) {
					String field = split[i];

					if (join != null)
						join = join.join(field);
					else
						join = root.join(field);

				}
				criteria.setKey(key);
				addPredicate(join, builder, predicates, criteria);

			} else
				addPredicate(root, builder, predicates, criteria);
		}

		return builder.and(predicates.toArray(new Predicate[0]));
	}

	private void addPredicate(Join<Object, Object> join, CriteriaBuilder builder, List<Predicate> predicates,
			SearchCriteria criteria) {
		Class<? extends Object> javaType = join.get(criteria.getKey()).getJavaType();
		this.getEnumValue(javaType, criteria);

		switch (criteria.getOperation()) {
		case GREATER_THAN:
			predicates.add(builder.greaterThan(join.get(criteria.getKey()), criteria.getValue().toString()));
			break;
		case LESS_THAN:
			predicates.add(builder.lessThan(join.get(criteria.getKey()), criteria.getValue().toString()));
			break;
		case GREATER_THAN_EQUAL:
			predicates.add(builder.greaterThanOrEqualTo(join.get(criteria.getKey()), criteria.getValue().toString()));
			break;
		case LESS_THAN_EQUAL:
			predicates.add(builder.lessThanOrEqualTo(join.get(criteria.getKey()), criteria.getValue().toString()));
			break;
		case NOT_EQUAL:
			predicates.add(builder.notEqual(join.get(criteria.getKey()), criteria.getValue()));
			break;
		case EQUAL:
			predicates.add(builder.equal(join.get(criteria.getKey()), criteria.getValue()));
			break;
		case MATCH:
			predicates.add(builder.like(builder.lower(join.get(criteria.getKey())),
					"%" + criteria.getValue().toString().toLowerCase() + "%"));
			break;
		case MATCH_START:
			predicates.add(builder.like(builder.lower(join.get(criteria.getKey())),
					criteria.getValue().toString().toLowerCase() + "%"));
			break;
		case MATCH_END:
			predicates.add(builder.like(builder.lower(join.get(criteria.getKey())),
					"%" + criteria.getValue().toString().toLowerCase()));
			break;
		case BETWEEN_DATE:
			if (javaType.equals(LocalDateTime.class)) {
				Path<LocalDateTime> entityDate = join.get(criteria.getKey());
				predicates.add(
						builder.between(entityDate, getComparingDateTime((ArrayList<String>) criteria.getValue(), 0),
								getComparingDateTime((ArrayList<String>) criteria.getValue(), 1)));
			} else if (javaType.equals(LocalDate.class)) {

				Path<LocalDate> entityDate = join.get(criteria.getKey());
				predicates.add(builder.between(entityDate, getComparingDate((ArrayList<String>) criteria.getValue(), 0),
						getComparingDate((ArrayList<String>) criteria.getValue(), 1)));
			}

			break;
		case IN:
			Path<Object> expression = join.get(criteria.getKey());
			Predicate predicate = expression.in(criteria.getValue());
			predicates.add(predicate);
			break;
		}
	}

	private void addPredicate(Root<?> root, CriteriaBuilder builder, List<Predicate> predicates,
			SearchCriteria criteria) {
		try {
			Class<? extends Object> javaType = root.get(criteria.getKey()).getJavaType();
			this.getEnumValue(javaType, criteria);

			switch (criteria.getOperation()) {
			case GREATER_THAN:
				predicates.add(builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString()));
				break;
			case LESS_THAN:
				predicates.add(builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString()));
				break;
			case GREATER_THAN_EQUAL:
				predicates
						.add(builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
				break;
			case LESS_THAN_EQUAL:
				predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
				break;
			case NOT_EQUAL:
				predicates.add(builder.notEqual(root.get(criteria.getKey()), criteria.getValue()));
				break;
			case EQUAL:
				predicates.add(builder.equal(root.get(criteria.getKey()), criteria.getValue()));
				break;
			case MATCH:
				predicates.add(builder.like(builder.lower(root.get(criteria.getKey())),
						"%" + criteria.getValue().toString().toLowerCase() + "%"));
				break;
			case MATCH_START:
				predicates.add(builder.like(builder.lower(root.get(criteria.getKey())),
						criteria.getValue().toString().toLowerCase() + "%"));
				break;
			case MATCH_END:
				predicates.add(builder.like(builder.lower(root.get(criteria.getKey())),
						"%" + criteria.getValue().toString().toLowerCase()));
				break;
			case BETWEEN_DATE:
				if (javaType.equals(LocalDateTime.class)) {
					Path<LocalDateTime> entityDate = root.get(criteria.getKey());
					predicates.add(builder.between(entityDate,
							getComparingDateTime((ArrayList<String>) criteria.getValue(), 0),
							getComparingDateTime((ArrayList<String>) criteria.getValue(), 1)));
				} else if (javaType.equals(LocalDate.class)) {

					Path<LocalDate> entityDate = root.get(criteria.getKey());
					predicates.add(
							builder.between(entityDate, getComparingDate((ArrayList<String>) criteria.getValue(), 0),
									getComparingDate((ArrayList<String>) criteria.getValue(), 1)));
				}

				break;
			case IN:
				Path<Object> expression = root.get(criteria.getKey());
				Predicate predicate = expression.in(criteria.getValue());
				predicates.add(predicate);
				break;
			}
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
	}

	private LocalDateTime getComparingDateTime(ArrayList<String> dates, int order) {
		LocalDateTime result = null;
		if (order == 0) {
			LocalDateTime from = DateConverter.stringTimeToLocalDateTime(dates.get(0));

			// LocalDateTime.parse(dates.get(0).toString(), localeIta);
			result = from;
		} else {
			LocalDateTime to = DateConverter.stringTimeToLocalDateTime(dates.get(1));

			// LocalDateTime.parse(dates.get(1).toString(), localeIta);
			result = to;
		}
		return result;
	}

	private LocalDate getComparingDate(ArrayList<String> dates, int order) {
		LocalDate result = null;
		if (order == 0) {
			LocalDate from = DateConverter.stringToLocalDate(dates.get(0), "yyyy-MM-dd");

			// LocalDateTime.parse(dates.get(0).toString(), localeIta);
			result = from;
		} else {
			LocalDate to = DateConverter.stringToLocalDate(dates.get(1), "yyyy-MM-dd");

			// LocalDateTime.parse(dates.get(1).toString(), localeIta);
			result = to;
		}
		return result;
	}

	private void getEnumValue(Class<? extends Object> javaType, SearchCriteria criteria) {
		if (javaType.isEnum()) {

			if (criteria.getValue() instanceof List) {
				List<Object> list = new ArrayList<>();
				for (Object v : javaType.getEnumConstants()) {
					if (((List<String>) criteria.getValue()).contains(v.toString())) {
						list.add(v);
					}
				}
				criteria.setValue(list);
				criteria.setOperation(SearchOperation.IN);

			} else {
				for (Object v : javaType.getEnumConstants()) {
					if (v.toString().equals(criteria.getValue())) {
						criteria.setValue(v);
						criteria.setOperation(SearchOperation.EQUAL);
						break;
					}
				}
			}
		}
	}

}
