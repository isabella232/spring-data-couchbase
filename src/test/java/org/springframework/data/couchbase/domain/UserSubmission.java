/*
 * Copyright 2012-2020 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.data.couchbase.domain;

import lombok.Data;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.couchbase.core.index.CompositeQueryIndex;
import org.springframework.data.couchbase.core.mapping.Document;

import java.lang.reflect.Field;
import java.util.List;

/**
 * UserSubmission entity for tests
 *
 * @author Michael Reiche
 */
@Data
@Document
@TypeAlias("user")
@CompositeQueryIndex(fields = { "id", "username", "email" })
public class UserSubmission {
	private String id;
	private String username;
	private String email;
	private String password;
	private List<String> roles;
	private Address address;
	private int credits;
	private List<Submission> submissions;
	private List<Course> courses;

	public void setSubmissions(List<Submission> submissions) {
		this.submissions = submissions;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	@Override
	public boolean equals(Object that) throws RuntimeException {
		if (this == that)
			return true;
		if (that == null || getClass() != that.getClass())
			return false;
		for (Field f : getClass().getFields()) {
			if (!same(f, this, that))
				return false;
		}
		for (Field f : getClass().getDeclaredFields()) {
			if (!same(f, this, that))
				return false;
		}
		return true;
	}

	private static boolean same(Field f, Object a, Object b) {
		Object thisField = null;
		Object thatField = null;

		try {
			f.get(a);
			f.get(b);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		if (thisField == null && thatField == null) {
			return true;
		}
		if (thisField == null && thatField != null) {
			return false;
		}
		if (!thisField.equals(thatField)) {
			return false;
		}
		return true;
	}
}
