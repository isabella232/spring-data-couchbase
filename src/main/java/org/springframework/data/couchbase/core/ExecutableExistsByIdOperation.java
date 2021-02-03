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
package org.springframework.data.couchbase.core;

import java.util.Collection;
import java.util.Map;

import org.springframework.data.couchbase.core.support.OneAndAllExists;
import org.springframework.data.couchbase.core.support.WithCollection;
import org.springframework.data.couchbase.core.support.WithGetOptions;
import org.springframework.data.couchbase.core.support.WithScope;

import com.couchbase.client.java.kv.GetOptions;

public interface ExecutableExistsByIdOperation {

	/**
	 * Checks if the document exists in the bucket.
	 */
	ExecutableExistsById existsById();

	interface TerminatingExistsById extends OneAndAllExists {

		/**
		 * Performs the operation on the ID given.
		 *
		 * @param id the ID to perform the operation on.
		 * @return true if the document exists, false otherwise.
		 */
		boolean one(String id);

		/**
		 * Performs the operation on the collection of ids.
		 *
		 * @param ids the ids to check.
		 * @return a map consisting of the document IDs as the keys and if they exist as the value.
		 */
		Map<String, Boolean> all(Collection<String> ids);

	}

	interface ExistsByIdWithOptions<T> extends TerminatingExistsById, WithGetOptions<T> {
		TerminatingExistsById withOptions(GetOptions options);
	}

	interface ExistsByIdWithCollection<T> extends ExistsByIdWithOptions<T>, WithCollection<T> {
		ExistsByIdWithOptions<T> inCollection(String collection);
	}

	interface ExistsByIdWithScope<T> extends ExistsByIdWithCollection<T>, WithScope<T> {
		ExistsByIdWithCollection<T> inScope(String scope);
	}

	interface ExecutableExistsById extends ExistsByIdWithScope {}

}
