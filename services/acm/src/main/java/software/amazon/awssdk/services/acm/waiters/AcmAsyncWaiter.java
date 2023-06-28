/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with
 * the License. A copy of the License is located at
 * 
 * http://aws.amazon.com/apache2.0
 * 
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package software.amazon.awssdk.services.acm.waiters;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import software.amazon.awssdk.annotations.Generated;
import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.core.waiters.WaiterOverrideConfiguration;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.acm.AcmAsyncClient;
import software.amazon.awssdk.services.acm.model.DescribeCertificateRequest;
import software.amazon.awssdk.services.acm.model.DescribeCertificateResponse;
import software.amazon.awssdk.utils.SdkAutoCloseable;

/**
 * Waiter utility class that polls a resource until a desired state is reached or until it is determined that the
 * resource will never enter into the desired state. This can be created using the static {@link #builder()} method
 */
@Generated("software.amazon.awssdk:codegen")
@SdkPublicApi
public interface AcmAsyncWaiter extends SdkAutoCloseable {
    /**
     * Polls {@link AcmAsyncClient#describeCertificate} API until the desired condition {@code CertificateValidated} is
     * met, or until it is determined that the resource will never enter into the desired state
     *
     * @param describeCertificateRequest
     *        the request to be used for polling
     * @return CompletableFuture containing the WaiterResponse. It completes successfully when the resource enters into
     *         a desired state or exceptionally when it is determined that the resource will never enter into the
     *         desired state.
     */
    default CompletableFuture<WaiterResponse<DescribeCertificateResponse>> waitUntilCertificateValidated(
            DescribeCertificateRequest describeCertificateRequest) {
        throw new UnsupportedOperationException();
    }

    /**
     * Polls {@link AcmAsyncClient#describeCertificate} API until the desired condition {@code CertificateValidated} is
     * met, or until it is determined that the resource will never enter into the desired state.
     * <p>
     * This is a convenience method to create an instance of the request builder without the need to create one manually
     * using {@link DescribeCertificateRequest#builder()}
     *
     * @param describeCertificateRequest
     *        The consumer that will configure the request to be used for polling
     * @return CompletableFuture of the WaiterResponse containing either a response or an exception that has matched
     *         with the waiter success condition
     */
    default CompletableFuture<WaiterResponse<DescribeCertificateResponse>> waitUntilCertificateValidated(
            Consumer<DescribeCertificateRequest.Builder> describeCertificateRequest) {
        return waitUntilCertificateValidated(DescribeCertificateRequest.builder().applyMutation(describeCertificateRequest)
                .build());
    }

    /**
     * Polls {@link AcmAsyncClient#describeCertificate} API until the desired condition {@code CertificateValidated} is
     * met, or until it is determined that the resource will never enter into the desired state
     *
     * @param describeCertificateRequest
     *        The request to be used for polling
     * @param overrideConfig
     *        Per request override configuration for waiters
     * @return WaiterResponse containing either a response or an exception that has matched with the waiter success
     *         condition
     */
    default CompletableFuture<WaiterResponse<DescribeCertificateResponse>> waitUntilCertificateValidated(
            DescribeCertificateRequest describeCertificateRequest, WaiterOverrideConfiguration overrideConfig) {
        throw new UnsupportedOperationException();
    }

    /**
     * Polls {@link AcmAsyncClient#describeCertificate} API until the desired condition {@code CertificateValidated} is
     * met, or until it is determined that the resource will never enter into the desired state.
     * <p>
     * This is a convenience method to create an instance of the request builder and instance of the override config
     * builder
     *
     * @param describeCertificateRequest
     *        The consumer that will configure the request to be used for polling
     * @param overrideConfig
     *        The consumer that will configure the per request override configuration for waiters
     * @return WaiterResponse containing either a response or an exception that has matched with the waiter success
     *         condition
     */
    default CompletableFuture<WaiterResponse<DescribeCertificateResponse>> waitUntilCertificateValidated(
            Consumer<DescribeCertificateRequest.Builder> describeCertificateRequest,
            Consumer<WaiterOverrideConfiguration.Builder> overrideConfig) {
        return waitUntilCertificateValidated(DescribeCertificateRequest.builder().applyMutation(describeCertificateRequest)
                .build(), WaiterOverrideConfiguration.builder().applyMutation(overrideConfig).build());
    }

    /**
     * Create a builder that can be used to configure and create a {@link AcmAsyncWaiter}.
     *
     * @return a builder
     */
    static Builder builder() {
        return DefaultAcmAsyncWaiter.builder();
    }

    /**
     * Create an instance of {@link AcmAsyncWaiter} with the default configuration.
     * <p>
     * <b>A default {@link AcmAsyncClient} will be created to poll resources. It is recommended to share a single
     * instance of the waiter created via this method. If it is not desirable to share a waiter instance, invoke
     * {@link #close()} to release the resources once the waiter is not needed.</b>
     *
     * @return an instance of {@link AcmAsyncWaiter}
     */
    static AcmAsyncWaiter create() {
        return DefaultAcmAsyncWaiter.builder().build();
    }

    interface Builder {
        /**
         * Sets a custom {@link ScheduledExecutorService} that will be used to schedule async polling attempts
         * <p>
         * This executorService must be closed by the caller when it is ready to be disposed. The SDK will not close the
         * executorService when the waiter is closed
         *
         * @param executorService
         *        the executorService to set
         * @return a reference to this object so that method calls can be chained together.
         */
        Builder scheduledExecutorService(ScheduledExecutorService executorService);

        /**
         * Defines overrides to the default SDK waiter configuration that should be used for waiters created from this
         * builder
         *
         * @param overrideConfiguration
         *        the override configuration to set
         * @return a reference to this object so that method calls can be chained together.
         */
        Builder overrideConfiguration(WaiterOverrideConfiguration overrideConfiguration);

        /**
         * This is a convenient method to pass the override configuration without the need to create an instance
         * manually via {@link WaiterOverrideConfiguration#builder()}
         *
         * @param overrideConfiguration
         *        The consumer that will configure the overrideConfiguration
         * @return a reference to this object so that method calls can be chained together.
         * @see #overrideConfiguration(WaiterOverrideConfiguration)
         */
        default Builder overrideConfiguration(Consumer<WaiterOverrideConfiguration.Builder> overrideConfiguration) {
            WaiterOverrideConfiguration.Builder builder = WaiterOverrideConfiguration.builder();
            overrideConfiguration.accept(builder);
            return overrideConfiguration(builder.build());
        }

        /**
         * Sets a custom {@link AcmAsyncClient} that will be used to poll the resource
         * <p>
         * This SDK client must be closed by the caller when it is ready to be disposed. The SDK will not close the
         * client when the waiter is closed
         *
         * @param client
         *        the client to send the request
         * @return a reference to this object so that method calls can be chained together.
         */
        Builder client(AcmAsyncClient client);

        /**
         * Builds an instance of {@link AcmAsyncWaiter} based on the configurations supplied to this builder
         *
         * @return An initialized {@link AcmAsyncWaiter}
         */
        AcmAsyncWaiter build();
    }
}
