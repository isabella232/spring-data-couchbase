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

public interface ExecutableExistsByIdOperation {

	/**
	 * Checks if the document exists in the bucket.
	 */
	<T,I> ExecutableExistsById<T,I> existsById();

	interface TerminatingExistsById<T,I> extends OneAndAllExists<T,I> {

		/**
		 * Performs the operation on the ID given.
		 *
		 * @param id the ID to perform the operation on.
		 * @return true if the document exists, false otherwise.
		 */
		boolean one(I id);

		/**
		 * Performs the operation on the collection of ids.
		 *
		 * @param ids the ids to check.
		 * @return a map consisting of the document IDs as the keys and if they exist as the value.
		 */
		Map<I, Boolean> all(Collection<I> ids);

	}

	interface ExistsByIdWithCollection<T,I> extends TerminatingExistsById<T,I>, WithCollection<T> {

		/**
		 * Allows to specify a different collection than the default one configured.
		 *
		 * @param collection the collection to use in this scope.
		 */
		TerminatingExistsById<T,I> inCollection(String collection);
	}

	interface ExecutableExistsById<T,I> extends ExistsByIdWithCollection<T,I> {}

}
