/**
 * Copyright (C) 2013
 *   Michael Mosmann <michael@mosmann.de>
 *
 * with contributions from
 * 	${lic.developers}
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.flapdoodle.logparser.collections;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;


public class Collections {
	
	private Collections() {
		// no instance
	}
	
	public static <K,V> Map<K, V> join(Collection<? extends Map<K, V>> maps) {
		Map<K, V> ret = Maps.newHashMap();
		for (Map<K, V> map : maps) {
			ret = join(ret, map);
		}
		return ret;
	}

	public static  <K,V> Map<K, V> join(Map<K, V>... maps) {
		Map<K, V> ret = Maps.newHashMap();
		for (Map<K, V> map : maps) {
			ret = join(ret, map);
		}
		return ret;
	}

	public static <K, V> Map<K, V> join(Map<K, V> a, Map<K, V> b) {
		Map<K, V> ret = Maps.newHashMap(a);
		ret.putAll(b);
		if (ret.size() != a.size() + b.size()) {
			throw new IllegalArgumentException("Map contains same keys: " + Sets.intersection(a.keySet(), b.keySet()));
		}
		return ret;
	}

}
