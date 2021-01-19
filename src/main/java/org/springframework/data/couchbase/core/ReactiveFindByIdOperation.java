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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

import org.springframework.data.couchbase.core.support.OneAndAllIdReactive;
import org.springframework.data.couchbase.core.support.WithCollection;
import org.springframework.data.couchbase.core.support.WithProjectionId;

public interface ReactiveFindByIdOperation {

	/**
	 * Loads a document from a bucket.
	 *
	 * @param domainType the entity type to use for the results.
	 */
	<T,I> ReactiveFindById<T,I> findById(Class<T> domainType);

	interface TerminatingFindById<T,I> extends OneAndAllIdReactive<T,I> {

		/**
		 * Finds one document based on the given ID.
		 *
		 * @param id the document ID.
		 * @return the entity if found.
		 */
		Mono<T> one(I id);

		/**
		 * Finds a list of documents based on the given IDs.
		 *
		 * @param ids the document ID ids.
		 * @return the list of found entities.
		 */
		Flux<? extends T> all(Collection<I> ids);

	}

	interface FindByIdWithCollection<T,I> extends TerminatingFindById<T,I>, WithCollection<T> {

		/**
		 * Allows to specify a different collection than the default one configured.
		 *
		 * @param collection the collection to use in this scope.
		 */
		TerminatingFindById<T,I> inCollection(String collection);
	}

	interface FindByIdWithProjection<T,I> extends FindByIdWithCollection<T,I>, WithProjectionId<T,I> {

		/**
		 * Load only certain fields for the document.
		 *
		 * @param fields the projected fields to load.
		 */
		FindByIdWithCollection<T,I> project(String... fields);

	}

	interface ReactiveFindById<T,I> extends FindByIdWithProjection<T,I> {}

}
