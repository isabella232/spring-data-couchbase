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

import static com.couchbase.client.java.kv.GetAnyReplicaOptions.getAnyReplicaOptions;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

import org.springframework.data.couchbase.core.support.PseudoArgs;

import com.couchbase.client.java.codec.RawJsonTranscoder;
import com.couchbase.client.java.kv.GetAnyReplicaOptions;
import com.couchbase.client.java.kv.GetOptions;

public class ReactiveFindFromReplicasByIdOperationSupport implements ReactiveFindFromReplicasByIdOperation {

	private final ReactiveCouchbaseTemplate template;

	ReactiveFindFromReplicasByIdOperationSupport(ReactiveCouchbaseTemplate template) {
		this.template = template;
	}

	@Override
	public <T> ReactiveFindFromReplicasById<T> findFromReplicasById(Class<T> domainType) {
		return new ReactiveFindFromReplicasByIdSupport<>(template, domainType, domainType, null, null, null);
	}

	static class ReactiveFindFromReplicasByIdSupport<T> implements ReactiveFindFromReplicasById<T> {

		private final ReactiveCouchbaseTemplate template;
		private final Class<?> domainType;
		private final Class<T> returnType;
		private final String scope;
		private final String collection;
		private final GetOptions options;

		ReactiveFindFromReplicasByIdSupport(ReactiveCouchbaseTemplate template, Class<?> domainType, Class<T> returnType,
				String scope, String collection, GetOptions options) {
			this.template = template;
			this.domainType = domainType;
			this.returnType = returnType;
			this.scope = scope;
			this.collection = collection;
			this.options = options;
		}

		@Override
		public Mono<T> any(final String id) {
			return Mono.just(id).flatMap(docId -> {
				GetAnyReplicaOptions options = getAnyReplicaOptions().transcoder(RawJsonTranscoder.INSTANCE);
				PseudoArgs<GetAnyReplicaOptions> pArgs = new PseudoArgs<>(options, null, collection);
				return template.getCollection(pArgs.getCollection()).reactive().getAnyReplica(docId, pArgs.getOptions());
			}).map(result -> template.support().decodeEntity(id, result.contentAs(String.class), result.cas(), returnType))
					.onErrorMap(throwable -> {
						if (throwable instanceof RuntimeException) {
							return template.potentiallyConvertRuntimeException((RuntimeException) throwable);
						} else {
							return throwable;
						}
					});
		}

		@Override
		public Flux<? extends T> any(Collection<String> ids) {
			return Flux.fromIterable(ids).flatMap(this::any);
		}

		@Override
		public TerminatingFindFromReplicasById<T> withOptions(GetOptions options) {
			return new ReactiveFindFromReplicasByIdSupport<>(template, domainType, returnType, scope, collection, options);
		}

		@Override
		public FindFromReplicasByIdWithOptions<T> inCollection(final String collection) {
			return new ReactiveFindFromReplicasByIdSupport<>(template, domainType, returnType, scope, collection, options);
		}

		@Override
		public FindFromReplicasByIdWithCollection<T> inScope(String scope) {
			return new ReactiveFindFromReplicasByIdSupport<>(template, domainType, returnType, scope, collection, options);
		}

	}

}
