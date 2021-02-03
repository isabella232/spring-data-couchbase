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

import com.couchbase.client.java.kv.RemoveOptions;
import com.couchbase.client.java.kv.UpsertOptions;
import org.springframework.data.couchbase.core.support.WithRemoveOptions;
import org.springframework.data.couchbase.core.support.WithScope;
import org.springframework.data.couchbase.core.support.WithUpsertOptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collection;

import org.springframework.data.couchbase.core.support.OneAndAllEntityReactive;
import org.springframework.data.couchbase.core.support.WithCollection;

import com.couchbase.client.core.msg.kv.DurabilityLevel;
import com.couchbase.client.java.kv.PersistTo;
import com.couchbase.client.java.kv.ReplicateTo;

public interface ReactiveUpsertByIdOperation {

	<T> ReactiveUpsertById<T> upsertById(Class<T> domainType);

	interface TerminatingUpsertById<T> extends OneAndAllEntityReactive<T> {

		Mono<T> one(T object);

		Flux<? extends T> all(Collection<? extends T> objects);

	}

	interface UpsertByIdWithOptions<T> extends TerminatingUpsertById<T>, WithUpsertOptions<T> {
		TerminatingUpsertById<T> withOptions(UpsertOptions options);
	}

	interface UpsertByIdWithCollection<T> extends UpsertByIdWithOptions<T>, WithCollection<Object> {
		UpsertByIdWithOptions<T> inCollection(String collection);
	}

	interface UpsertByIdWithScope<T> extends UpsertByIdWithCollection<T>, WithScope<Object> {
		UpsertByIdWithCollection<T> inScope(String scope);
	}

	interface UpsertByIdWithDurability<T> extends  UpsertByIdWithScope<T>, WithDurability<T> {

		UpsertByIdWithCollection<T> withDurability(DurabilityLevel durabilityLevel);

		UpsertByIdWithCollection<T> withDurability(PersistTo persistTo, ReplicateTo replicateTo);

	}

	interface UpsertByIdWithExpiry<T> extends UpsertByIdWithDurability<T>, WithExpiry<T> {

		UpsertByIdWithDurability<T> withExpiry(Duration expiry);
	}

	interface ReactiveUpsertById<T> extends UpsertByIdWithExpiry<T> {}

}
