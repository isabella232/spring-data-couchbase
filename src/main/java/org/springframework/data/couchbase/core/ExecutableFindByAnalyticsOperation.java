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

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.couchbase.core.query.AnalyticsQuery;
import org.springframework.data.couchbase.core.support.OneAndAll;
import org.springframework.data.couchbase.core.support.WithAnalyticsConsistency;
import org.springframework.data.couchbase.core.support.WithAnalyticsOptions;
import org.springframework.data.couchbase.core.support.WithAnalyticsQuery;
import org.springframework.data.couchbase.core.support.WithCollection;
import org.springframework.data.couchbase.core.support.WithScope;
import org.springframework.lang.Nullable;

import com.couchbase.client.java.analytics.AnalyticsOptions;
import com.couchbase.client.java.analytics.AnalyticsScanConsistency;

public interface ExecutableFindByAnalyticsOperation {

	/**
	 * Queries the analytics service.
	 *
	 * @param domainType the entity type to use for the results.
	 */
	<T> ExecutableFindByAnalytics<T> findByAnalytics(Class<T> domainType);

	interface TerminatingFindByAnalytics<T> extends OneAndAll<T> {

		/**
		 * Get exactly zero or one result.
		 *
		 * @return {@link Optional#empty()} if no match found.
		 * @throws IncorrectResultSizeDataAccessException if more than one match found.
		 */
		default Optional<T> one() {
			return Optional.ofNullable(oneValue());
		}

		/**
		 * Get exactly zero or one result.
		 *
		 * @return {@literal null} if no match found.
		 * @throws IncorrectResultSizeDataAccessException if more than one match found.
		 */
		@Nullable
		T oneValue();

		/**
		 * Get the first or no result.
		 *
		 * @return {@link Optional#empty()} if no match found.
		 */
		default Optional<T> first() {
			return Optional.ofNullable(firstValue());
		}

		/**
		 * Get the first or no result.
		 *
		 * @return {@literal null} if no match found.
		 */
		@Nullable
		T firstValue();

		/**
		 * Get all matching elements.
		 *
		 * @return never {@literal null}.
		 */
		List<T> all();

		/**
		 * Stream all matching elements.
		 *
		 * @return a {@link Stream} of results. Never {@literal null}.
		 */
		Stream<T> stream();

		/**
		 * Get the number of matching elements.
		 *
		 * @return total number of matching elements.
		 */
		long count();

		/**
		 * Check for the presence of matching elements.
		 *
		 * @return {@literal true} if at least one matching element exists.
		 */
		boolean exists();

	}

	interface FindByAnalyticsWithQuery<T> extends TerminatingFindByAnalytics<T>, WithAnalyticsQuery<T> {

		/**
		 * Set the filter for the analytics query to be used.
		 *
		 * @param query must not be {@literal null}.
		 * @throws IllegalArgumentException if query is {@literal null}.
		 */
		TerminatingFindByAnalytics<T> matching(AnalyticsQuery query);

	}

	interface FindByAnalyticsWithOptions<T> extends FindByAnalyticsWithQuery<T>, WithAnalyticsOptions<T> {
		FindByAnalyticsWithQuery<T> withOptions(AnalyticsOptions options);
	}

	interface FindByAnalyticsInCollection<T> extends FindByAnalyticsWithOptions<T>, WithCollection<T> {
		FindByAnalyticsWithOptions<T> inCollection(String collection);
	}

	interface FindByAnalyticsWithScope<T> extends FindByAnalyticsInCollection<T>, WithScope<T> {
		FindByAnalyticsInCollection<T> inScope(String scope);
	}

	@Deprecated
	interface FindByAnalyticsConsistentWith<T> extends FindByAnalyticsWithScope<T> {

		/**
		 * Allows to override the default scan consistency.
		 *
		 * @param scanConsistency the custom scan consistency to use for this analytics query.
		 */
		@Deprecated
		FindByAnalyticsWithQuery<T> consistentWith(AnalyticsScanConsistency scanConsistency);

	}

	interface FindByAnalyticsWithConsistency<T> extends FindByAnalyticsConsistentWith<T>, WithAnalyticsConsistency<T> {

		/**
		 * Allows to override the default scan consistency.
		 *
		 * @param scanConsistency the custom scan consistency to use for this analytics query.
		 */
		FindByAnalyticsConsistentWith<T> withConsistency(AnalyticsScanConsistency scanConsistency);

	}

	interface ExecutableFindByAnalytics<T> extends FindByAnalyticsWithConsistency<T> {}

}
