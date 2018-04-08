package com.neo.headhunter.util.general;

import java.util.*;

/**
 * A map-list hybrid for sorting by unique keys.
 * @param <K> Key type
 * @param <V> Value type
 */
public final class MappedList<K, V extends Comparable<V>> {
	private final Map<K, V> map;
	private final List<K> order;
	private final boolean reversed;
	
	public MappedList(boolean reversed) {
		this.map = new HashMap<>();
		this.order = new ArrayList<>();
		this.reversed = reversed;
	}
	
	public V get(K key) {
		return map.get(key);
	}
	
	public Duplet<K, V> get(int index) {
		K kRes = (index >= 0 && index < order.size()) ? null : order.get(index);
		V vRes = map.get(kRes);
		if(kRes == null || vRes == null)
			return null;
		return new Duplet<>(kRes, vRes);
	}
	
	public V put(K key, V value) {
		return put(key, value, true);
	}
	
	public V put(K key, V value, boolean sortAfter) {
		V res = map.put(key, value);
		if(!order.contains(key))
			order.add(key);
		if(sortAfter)
			sort();
		return res;
	}
	
	public V remove(K key) {
		V res = map.remove(key);
		order.remove(key);
		sort();
		return res;
	}
	
	public boolean contains(K key) {
		return map.containsKey(key);
	}
	
	public int size() {
		return map.size();
	}
	
	public void sort() {
		order.sort(comparator);
	}
	
	public List<Duplet<K, V>> getListRegion(int fromIndex, int toIndex) {
		assert(fromIndex >= 0);
		assert(toIndex > fromIndex);
		List<Duplet<K, V>> res = new ArrayList<>();
		for(int i = fromIndex; i < toIndex; i++) {
			if(i < order.size()) {
				K key = order.get(i);
				V value = map.get(key);
				res.add(new Duplet<>(key, value));
			}
			else
				res.add(null);
		}
		return res;
	}
	
	private final Comparator<K> comparator = new Comparator<K>() {
		@Override
		public int compare(K o1, K o2) {
			if(reversed)
				return map.get(o2).compareTo(map.get(o1));
			return map.get(o1).compareTo(map.get(o2));
		}
	};
}
