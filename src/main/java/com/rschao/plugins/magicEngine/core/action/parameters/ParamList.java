package com.rschao.plugins.magicEngine.core.action.parameters;

import java.util.*;

/**
 * Lista de parámetros para una Action. Cada parámetro tiene nombre y valor.
 * Diseñada para ser fácilmente ampliable (tipos, metadatos) y para ofrecer
 * compatibilidad con código que espera solo valores en forma de String[].
 */
public class ParamList {

	private final List<Param> params = new ArrayList<>();

	public ParamList() {
	}

	public static ParamList empty() {
		return new ParamList();
	}

	public static ParamList of(Param... ps) {
		ParamList list = new ParamList();
		for (Param p : ps) list.add(p);
		return list;
	}

	public ParamList add(Param p) {
		params.add(p);
		return this;
	}

	public ParamList put(String name, Object value) {
		params.add(new Param(name, value));
		return this;
	}

	public Optional<Param> get(String name) {
		return params.stream().filter(p -> Objects.equals(p.getName(), name)).findFirst();
	}

	public List<Param> getAll() {
		return Collections.unmodifiableList(params);
	}

	/** Devuelve los valores como String[] (compatibilidad). */
	public String[] toStringValues() {
		return params.stream().map(p -> Objects.toString(p.getValue(), "null")).toArray(String[]::new);
	}

	/** Devuelve un mapa nombre->valor (mutable copia). */
	public Map<String, Object> asMap() {
		Map<String, Object> map = new LinkedHashMap<>();
		for (Param p : params) map.put(p.getName(), p.getValue());
		return map;
	}

	@Override
	public String toString() {
		return params.toString();
	}
}
