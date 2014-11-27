package org.leebli.parser.bytecode.model;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class AnnotationData implements Map<String, Object> {
	private String desc;
	private boolean visible;
	private Map<String, Object> parameters = new Hashtable<String, Object>();

	public AnnotationData(String desc, boolean visible) {
		this.desc = desc;
		this.visible = visible;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	@Override
	public Object put(String name, Object value) {
		return parameters.put(name, value);
	}

	@Override
	public void clear() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean containsKey(Object key) {
		return this.parameters.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return this.parameters.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return this.parameters.entrySet();
	}

	@Override
	public Object get(Object key) {
		return this.parameters.get(key);
	}

	@Override
	public boolean isEmpty() {
		return this.parameters.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return this.parameters.keySet();
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Object remove(Object key) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public int size() {
		return this.parameters.size();
	}

	@Override
	public Collection<Object> values() {
		return this.parameters.values();
	}

}
