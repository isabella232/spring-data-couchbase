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

import org.springframework.data.couchbase.core.ReactiveExistsByIdOperationSupport.ReactiveExistsByIdSupport;

import com.couchbase.client.java.kv.GetOptions;

public class ExecutableExistsByIdOperationSupport implements ExecutableExistsByIdOperation {

	private final CouchbaseTemplate template;

	ExecutableExistsByIdOperationSupport(CouchbaseTemplate template) {
		this.template = template;
	}

	@Override
	public ExecutableExistsById existsById() {
		return new ExecutableExistsByIdSupport(template, null, null, null);
	}

	static class ExecutableExistsByIdSupport implements ExecutableExistsById {

		private final CouchbaseTemplate template;
		private final String scope;
		private final String collection;
		private final GetOptions options;

		private final ReactiveExistsByIdSupport reactiveSupport;

		ExecutableExistsByIdSupport(final CouchbaseTemplate template, final String scope, final String collection,
				final GetOptions options) {
			this.template = template;
			this.scope = scope;
			this.collection = collection;
			this.options = options;
			this.reactiveSupport = new ReactiveExistsByIdSupport(template.reactive(), scope, collection, options);
		}

		@Override
		public boolean one(final String id) {
			return reactiveSupport.one(id).block();
		}

		@Override
		public Map<String, Boolean> all(final Collection<String> ids) {
			return reactiveSupport.all(ids).block();
		}

		@Override
		public ExistsByIdWithOptions inCollection(final String collection) {
			return new ExecutableExistsByIdSupport(template, scope, collection, options);
		}

		@Override
		public TerminatingExistsById withOptions(GetOptions options) {
			return new ExecutableExistsByIdSupport(template, scope, collection, options);
		}

		@Override
		public ExistsByIdWithCollection inScope(String scope) {
			return new ExecutableExistsByIdSupport(template, scope, collection, options);
		}
	}

}
