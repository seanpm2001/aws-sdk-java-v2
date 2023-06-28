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

package software.amazon.awssdk.services.acm;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.annotations.Generated;
import software.amazon.awssdk.annotations.SdkInternalApi;
import software.amazon.awssdk.awscore.AwsRequestOverrideConfiguration;
import software.amazon.awssdk.awscore.client.handler.AwsAsyncClientHandler;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ApiName;
import software.amazon.awssdk.core.RequestOverrideConfiguration;
import software.amazon.awssdk.core.client.config.SdkClientConfiguration;
import software.amazon.awssdk.core.client.config.SdkClientOption;
import software.amazon.awssdk.core.client.handler.AsyncClientHandler;
import software.amazon.awssdk.core.client.handler.ClientExecutionParams;
import software.amazon.awssdk.core.http.HttpResponseHandler;
import software.amazon.awssdk.core.metrics.CoreMetric;
import software.amazon.awssdk.core.util.VersionInfo;
import software.amazon.awssdk.metrics.MetricCollector;
import software.amazon.awssdk.metrics.MetricPublisher;
import software.amazon.awssdk.metrics.NoOpMetricCollector;
import software.amazon.awssdk.protocols.core.ExceptionMetadata;
import software.amazon.awssdk.protocols.json.AwsJsonProtocol;
import software.amazon.awssdk.protocols.json.AwsJsonProtocolFactory;
import software.amazon.awssdk.protocols.json.BaseAwsJsonProtocolFactory;
import software.amazon.awssdk.protocols.json.JsonOperationMetadata;
import software.amazon.awssdk.services.acm.model.AccessDeniedException;
import software.amazon.awssdk.services.acm.model.AcmException;
import software.amazon.awssdk.services.acm.model.AcmRequest;
import software.amazon.awssdk.services.acm.model.AddTagsToCertificateRequest;
import software.amazon.awssdk.services.acm.model.AddTagsToCertificateResponse;
import software.amazon.awssdk.services.acm.model.ConflictException;
import software.amazon.awssdk.services.acm.model.DeleteCertificateRequest;
import software.amazon.awssdk.services.acm.model.DeleteCertificateResponse;
import software.amazon.awssdk.services.acm.model.DescribeCertificateRequest;
import software.amazon.awssdk.services.acm.model.DescribeCertificateResponse;
import software.amazon.awssdk.services.acm.model.ExportCertificateRequest;
import software.amazon.awssdk.services.acm.model.ExportCertificateResponse;
import software.amazon.awssdk.services.acm.model.GetAccountConfigurationRequest;
import software.amazon.awssdk.services.acm.model.GetAccountConfigurationResponse;
import software.amazon.awssdk.services.acm.model.GetCertificateRequest;
import software.amazon.awssdk.services.acm.model.GetCertificateResponse;
import software.amazon.awssdk.services.acm.model.ImportCertificateRequest;
import software.amazon.awssdk.services.acm.model.ImportCertificateResponse;
import software.amazon.awssdk.services.acm.model.InvalidArgsException;
import software.amazon.awssdk.services.acm.model.InvalidArnException;
import software.amazon.awssdk.services.acm.model.InvalidDomainValidationOptionsException;
import software.amazon.awssdk.services.acm.model.InvalidParameterException;
import software.amazon.awssdk.services.acm.model.InvalidStateException;
import software.amazon.awssdk.services.acm.model.InvalidTagException;
import software.amazon.awssdk.services.acm.model.LimitExceededException;
import software.amazon.awssdk.services.acm.model.ListCertificatesRequest;
import software.amazon.awssdk.services.acm.model.ListCertificatesResponse;
import software.amazon.awssdk.services.acm.model.ListTagsForCertificateRequest;
import software.amazon.awssdk.services.acm.model.ListTagsForCertificateResponse;
import software.amazon.awssdk.services.acm.model.PutAccountConfigurationRequest;
import software.amazon.awssdk.services.acm.model.PutAccountConfigurationResponse;
import software.amazon.awssdk.services.acm.model.RemoveTagsFromCertificateRequest;
import software.amazon.awssdk.services.acm.model.RemoveTagsFromCertificateResponse;
import software.amazon.awssdk.services.acm.model.RenewCertificateRequest;
import software.amazon.awssdk.services.acm.model.RenewCertificateResponse;
import software.amazon.awssdk.services.acm.model.RequestCertificateRequest;
import software.amazon.awssdk.services.acm.model.RequestCertificateResponse;
import software.amazon.awssdk.services.acm.model.RequestInProgressException;
import software.amazon.awssdk.services.acm.model.ResendValidationEmailRequest;
import software.amazon.awssdk.services.acm.model.ResendValidationEmailResponse;
import software.amazon.awssdk.services.acm.model.ResourceInUseException;
import software.amazon.awssdk.services.acm.model.ResourceNotFoundException;
import software.amazon.awssdk.services.acm.model.TagPolicyException;
import software.amazon.awssdk.services.acm.model.ThrottlingException;
import software.amazon.awssdk.services.acm.model.TooManyTagsException;
import software.amazon.awssdk.services.acm.model.UpdateCertificateOptionsRequest;
import software.amazon.awssdk.services.acm.model.UpdateCertificateOptionsResponse;
import software.amazon.awssdk.services.acm.model.ValidationException;
import software.amazon.awssdk.services.acm.paginators.ListCertificatesPublisher;
import software.amazon.awssdk.services.acm.transform.AddTagsToCertificateRequestMarshaller;
import software.amazon.awssdk.services.acm.transform.DeleteCertificateRequestMarshaller;
import software.amazon.awssdk.services.acm.transform.DescribeCertificateRequestMarshaller;
import software.amazon.awssdk.services.acm.transform.ExportCertificateRequestMarshaller;
import software.amazon.awssdk.services.acm.transform.GetAccountConfigurationRequestMarshaller;
import software.amazon.awssdk.services.acm.transform.GetCertificateRequestMarshaller;
import software.amazon.awssdk.services.acm.transform.ImportCertificateRequestMarshaller;
import software.amazon.awssdk.services.acm.transform.ListCertificatesRequestMarshaller;
import software.amazon.awssdk.services.acm.transform.ListTagsForCertificateRequestMarshaller;
import software.amazon.awssdk.services.acm.transform.PutAccountConfigurationRequestMarshaller;
import software.amazon.awssdk.services.acm.transform.RemoveTagsFromCertificateRequestMarshaller;
import software.amazon.awssdk.services.acm.transform.RenewCertificateRequestMarshaller;
import software.amazon.awssdk.services.acm.transform.RequestCertificateRequestMarshaller;
import software.amazon.awssdk.services.acm.transform.ResendValidationEmailRequestMarshaller;
import software.amazon.awssdk.services.acm.transform.UpdateCertificateOptionsRequestMarshaller;
import software.amazon.awssdk.services.acm.waiters.AcmAsyncWaiter;
import software.amazon.awssdk.utils.CompletableFutureUtils;

/**
 * Internal implementation of {@link AcmAsyncClient}.
 *
 * @see AcmAsyncClient#builder()
 */
@Generated("software.amazon.awssdk:codegen")
@SdkInternalApi
final class DefaultAcmAsyncClient implements AcmAsyncClient {
    private static final Logger log = LoggerFactory.getLogger(DefaultAcmAsyncClient.class);

    private final AsyncClientHandler clientHandler;

    private final AwsJsonProtocolFactory protocolFactory;

    private final SdkClientConfiguration clientConfiguration;

    private final AcmServiceClientConfiguration serviceClientConfiguration;

    private final ScheduledExecutorService executorService;

    protected DefaultAcmAsyncClient(AcmServiceClientConfiguration serviceClientConfiguration,
            SdkClientConfiguration clientConfiguration) {
        this.clientHandler = new AwsAsyncClientHandler(clientConfiguration);
        this.clientConfiguration = clientConfiguration;
        this.serviceClientConfiguration = serviceClientConfiguration;
        this.protocolFactory = init(AwsJsonProtocolFactory.builder()).build();
        this.executorService = clientConfiguration.option(SdkClientOption.SCHEDULED_EXECUTOR_SERVICE);
    }

    /**
     * <p>
     * Adds one or more tags to an ACM certificate. Tags are labels that you can use to identify and organize your
     * Amazon Web Services resources. Each tag consists of a <code>key</code> and an optional <code>value</code>. You
     * specify the certificate on input by its Amazon Resource Name (ARN). You specify the tag by using a key-value
     * pair.
     * </p>
     * <p>
     * You can apply a tag to just one certificate if you want to identify a specific characteristic of that
     * certificate, or you can apply the same tag to multiple certificates if you want to filter for a common
     * relationship among those certificates. Similarly, you can apply the same tag to multiple resources if you want to
     * specify a relationship among those resources. For example, you can add the same tag to an ACM certificate and an
     * Elastic Load Balancing load balancer to indicate that they are both used by the same website. For more
     * information, see <a href="https://docs.aws.amazon.com/acm/latest/userguide/tags.html">Tagging ACM
     * certificates</a>.
     * </p>
     * <p>
     * To remove one or more tags, use the <a>RemoveTagsFromCertificate</a> action. To view all of the tags that have
     * been applied to the certificate, use the <a>ListTagsForCertificate</a> action.
     * </p>
     *
     * @param addTagsToCertificateRequest
     * @return A Java Future containing the result of the AddTagsToCertificate operation returned by the service.<br/>
     *         The CompletableFuture returned by this method can be completed exceptionally with the following
     *         exceptions.
     *         <ul>
     *         <li>ResourceNotFoundException The specified certificate cannot be found in the caller's account or the
     *         caller's account cannot be found.</li>
     *         <li>InvalidArnException The requested Amazon Resource Name (ARN) does not refer to an existing resource.</li>
     *         <li>InvalidTagException One or both of the values that make up the key-value pair is not valid. For
     *         example, you cannot specify a tag value that begins with <code>aws:</code>.</li>
     *         <li>TooManyTagsException The request contains too many tags. Try the request again with fewer tags.</li>
     *         <li>TagPolicyException A specified tag did not comply with an existing tag policy and was rejected.</li>
     *         <li>InvalidParameterException An input parameter was invalid.</li>
     *         <li>ThrottlingException The request was denied because it exceeded a quota.</li>
     *         <li>SdkException Base class for all exceptions that can be thrown by the SDK (both service and client).
     *         Can be used for catch all scenarios.</li>
     *         <li>SdkClientException If any client side error occurs such as an IO related failure, failure to get
     *         credentials, etc.</li>
     *         <li>AcmException Base class for all service exceptions. Unknown exceptions will be thrown as an instance
     *         of this type.</li>
     *         </ul>
     * @sample AcmAsyncClient.AddTagsToCertificate
     * @see <a href="https://docs.aws.amazon.com/goto/WebAPI/acm-2015-12-08/AddTagsToCertificate" target="_top">AWS API
     *      Documentation</a>
     */
    @Override
    public CompletableFuture<AddTagsToCertificateResponse> addTagsToCertificate(
            AddTagsToCertificateRequest addTagsToCertificateRequest) {
        List<MetricPublisher> metricPublishers = resolveMetricPublishers(clientConfiguration, addTagsToCertificateRequest
                .overrideConfiguration().orElse(null));
        MetricCollector apiCallMetricCollector = metricPublishers.isEmpty() ? NoOpMetricCollector.create() : MetricCollector
                .create("ApiCall");
        try {
            apiCallMetricCollector.reportMetric(CoreMetric.SERVICE_ID, "ACM");
            apiCallMetricCollector.reportMetric(CoreMetric.OPERATION_NAME, "AddTagsToCertificate");
            JsonOperationMetadata operationMetadata = JsonOperationMetadata.builder().hasStreamingSuccessResponse(false)
                    .isPayloadJson(true).build();

            HttpResponseHandler<AddTagsToCertificateResponse> responseHandler = protocolFactory.createResponseHandler(
                    operationMetadata, AddTagsToCertificateResponse::builder);

            HttpResponseHandler<AwsServiceException> errorResponseHandler = createErrorResponseHandler(protocolFactory,
                    operationMetadata);

            CompletableFuture<AddTagsToCertificateResponse> executeFuture = clientHandler
                    .execute(new ClientExecutionParams<AddTagsToCertificateRequest, AddTagsToCertificateResponse>()
                            .withOperationName("AddTagsToCertificate")
                            .withMarshaller(new AddTagsToCertificateRequestMarshaller(protocolFactory))
                            .withResponseHandler(responseHandler).withErrorResponseHandler(errorResponseHandler)
                            .withMetricCollector(apiCallMetricCollector).withInput(addTagsToCertificateRequest));
            CompletableFuture<AddTagsToCertificateResponse> whenCompleted = executeFuture.whenComplete((r, e) -> {
                metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            });
            executeFuture = CompletableFutureUtils.forwardExceptionTo(whenCompleted, executeFuture);
            return executeFuture;
        } catch (Throwable t) {
            metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    /**
     * <p>
     * Deletes a certificate and its associated private key. If this action succeeds, the certificate no longer appears
     * in the list that can be displayed by calling the <a>ListCertificates</a> action or be retrieved by calling the
     * <a>GetCertificate</a> action. The certificate will not be available for use by Amazon Web Services services
     * integrated with ACM.
     * </p>
     * <note>
     * <p>
     * You cannot delete an ACM certificate that is being used by another Amazon Web Services service. To delete a
     * certificate that is in use, the certificate association must first be removed.
     * </p>
     * </note>
     *
     * @param deleteCertificateRequest
     * @return A Java Future containing the result of the DeleteCertificate operation returned by the service.<br/>
     *         The CompletableFuture returned by this method can be completed exceptionally with the following
     *         exceptions.
     *         <ul>
     *         <li>ResourceNotFoundException The specified certificate cannot be found in the caller's account or the
     *         caller's account cannot be found.</li>
     *         <li>ResourceInUseException The certificate is in use by another Amazon Web Services service in the
     *         caller's account. Remove the association and try again.</li>
     *         <li>AccessDeniedException You do not have access required to perform this action.</li>
     *         <li>ThrottlingException The request was denied because it exceeded a quota.</li>
     *         <li>ConflictException You are trying to update a resource or configuration that is already being created
     *         or updated. Wait for the previous operation to finish and try again.</li>
     *         <li>InvalidArnException The requested Amazon Resource Name (ARN) does not refer to an existing resource.</li>
     *         <li>SdkException Base class for all exceptions that can be thrown by the SDK (both service and client).
     *         Can be used for catch all scenarios.</li>
     *         <li>SdkClientException If any client side error occurs such as an IO related failure, failure to get
     *         credentials, etc.</li>
     *         <li>AcmException Base class for all service exceptions. Unknown exceptions will be thrown as an instance
     *         of this type.</li>
     *         </ul>
     * @sample AcmAsyncClient.DeleteCertificate
     * @see <a href="https://docs.aws.amazon.com/goto/WebAPI/acm-2015-12-08/DeleteCertificate" target="_top">AWS API
     *      Documentation</a>
     */
    @Override
    public CompletableFuture<DeleteCertificateResponse> deleteCertificate(DeleteCertificateRequest deleteCertificateRequest) {
        List<MetricPublisher> metricPublishers = resolveMetricPublishers(clientConfiguration, deleteCertificateRequest
                .overrideConfiguration().orElse(null));
        MetricCollector apiCallMetricCollector = metricPublishers.isEmpty() ? NoOpMetricCollector.create() : MetricCollector
                .create("ApiCall");
        try {
            apiCallMetricCollector.reportMetric(CoreMetric.SERVICE_ID, "ACM");
            apiCallMetricCollector.reportMetric(CoreMetric.OPERATION_NAME, "DeleteCertificate");
            JsonOperationMetadata operationMetadata = JsonOperationMetadata.builder().hasStreamingSuccessResponse(false)
                    .isPayloadJson(true).build();

            HttpResponseHandler<DeleteCertificateResponse> responseHandler = protocolFactory.createResponseHandler(
                    operationMetadata, DeleteCertificateResponse::builder);

            HttpResponseHandler<AwsServiceException> errorResponseHandler = createErrorResponseHandler(protocolFactory,
                    operationMetadata);

            CompletableFuture<DeleteCertificateResponse> executeFuture = clientHandler
                    .execute(new ClientExecutionParams<DeleteCertificateRequest, DeleteCertificateResponse>()
                            .withOperationName("DeleteCertificate")
                            .withMarshaller(new DeleteCertificateRequestMarshaller(protocolFactory))
                            .withResponseHandler(responseHandler).withErrorResponseHandler(errorResponseHandler)
                            .withMetricCollector(apiCallMetricCollector).withInput(deleteCertificateRequest));
            CompletableFuture<DeleteCertificateResponse> whenCompleted = executeFuture.whenComplete((r, e) -> {
                metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            });
            executeFuture = CompletableFutureUtils.forwardExceptionTo(whenCompleted, executeFuture);
            return executeFuture;
        } catch (Throwable t) {
            metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    /**
     * <p>
     * Returns detailed metadata about the specified ACM certificate.
     * </p>
     * <p>
     * If you have just created a certificate using the <code>RequestCertificate</code> action, there is a delay of
     * several seconds before you can retrieve information about it.
     * </p>
     *
     * @param describeCertificateRequest
     * @return A Java Future containing the result of the DescribeCertificate operation returned by the service.<br/>
     *         The CompletableFuture returned by this method can be completed exceptionally with the following
     *         exceptions.
     *         <ul>
     *         <li>ResourceNotFoundException The specified certificate cannot be found in the caller's account or the
     *         caller's account cannot be found.</li>
     *         <li>InvalidArnException The requested Amazon Resource Name (ARN) does not refer to an existing resource.</li>
     *         <li>SdkException Base class for all exceptions that can be thrown by the SDK (both service and client).
     *         Can be used for catch all scenarios.</li>
     *         <li>SdkClientException If any client side error occurs such as an IO related failure, failure to get
     *         credentials, etc.</li>
     *         <li>AcmException Base class for all service exceptions. Unknown exceptions will be thrown as an instance
     *         of this type.</li>
     *         </ul>
     * @sample AcmAsyncClient.DescribeCertificate
     * @see <a href="https://docs.aws.amazon.com/goto/WebAPI/acm-2015-12-08/DescribeCertificate" target="_top">AWS API
     *      Documentation</a>
     */
    @Override
    public CompletableFuture<DescribeCertificateResponse> describeCertificate(
            DescribeCertificateRequest describeCertificateRequest) {
        List<MetricPublisher> metricPublishers = resolveMetricPublishers(clientConfiguration, describeCertificateRequest
                .overrideConfiguration().orElse(null));
        MetricCollector apiCallMetricCollector = metricPublishers.isEmpty() ? NoOpMetricCollector.create() : MetricCollector
                .create("ApiCall");
        try {
            apiCallMetricCollector.reportMetric(CoreMetric.SERVICE_ID, "ACM");
            apiCallMetricCollector.reportMetric(CoreMetric.OPERATION_NAME, "DescribeCertificate");
            JsonOperationMetadata operationMetadata = JsonOperationMetadata.builder().hasStreamingSuccessResponse(false)
                    .isPayloadJson(true).build();

            HttpResponseHandler<DescribeCertificateResponse> responseHandler = protocolFactory.createResponseHandler(
                    operationMetadata, DescribeCertificateResponse::builder);

            HttpResponseHandler<AwsServiceException> errorResponseHandler = createErrorResponseHandler(protocolFactory,
                    operationMetadata);

            CompletableFuture<DescribeCertificateResponse> executeFuture = clientHandler
                    .execute(new ClientExecutionParams<DescribeCertificateRequest, DescribeCertificateResponse>()
                            .withOperationName("DescribeCertificate")
                            .withMarshaller(new DescribeCertificateRequestMarshaller(protocolFactory))
                            .withResponseHandler(responseHandler).withErrorResponseHandler(errorResponseHandler)
                            .withMetricCollector(apiCallMetricCollector).withInput(describeCertificateRequest));
            CompletableFuture<DescribeCertificateResponse> whenCompleted = executeFuture.whenComplete((r, e) -> {
                metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            });
            executeFuture = CompletableFutureUtils.forwardExceptionTo(whenCompleted, executeFuture);
            return executeFuture;
        } catch (Throwable t) {
            metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    /**
     * <p>
     * Exports a private certificate issued by a private certificate authority (CA) for use anywhere. The exported file
     * contains the certificate, the certificate chain, and the encrypted private 2048-bit RSA key associated with the
     * public key that is embedded in the certificate. For security, you must assign a passphrase for the private key
     * when exporting it.
     * </p>
     * <p>
     * For information about exporting and formatting a certificate using the ACM console or CLI, see <a
     * href="https://docs.aws.amazon.com/acm/latest/userguide/gs-acm-export-private.html">Export a Private
     * Certificate</a>.
     * </p>
     *
     * @param exportCertificateRequest
     * @return A Java Future containing the result of the ExportCertificate operation returned by the service.<br/>
     *         The CompletableFuture returned by this method can be completed exceptionally with the following
     *         exceptions.
     *         <ul>
     *         <li>ResourceNotFoundException The specified certificate cannot be found in the caller's account or the
     *         caller's account cannot be found.</li>
     *         <li>RequestInProgressException The certificate request is in process and the certificate in your account
     *         has not yet been issued.</li>
     *         <li>InvalidArnException The requested Amazon Resource Name (ARN) does not refer to an existing resource.</li>
     *         <li>SdkException Base class for all exceptions that can be thrown by the SDK (both service and client).
     *         Can be used for catch all scenarios.</li>
     *         <li>SdkClientException If any client side error occurs such as an IO related failure, failure to get
     *         credentials, etc.</li>
     *         <li>AcmException Base class for all service exceptions. Unknown exceptions will be thrown as an instance
     *         of this type.</li>
     *         </ul>
     * @sample AcmAsyncClient.ExportCertificate
     * @see <a href="https://docs.aws.amazon.com/goto/WebAPI/acm-2015-12-08/ExportCertificate" target="_top">AWS API
     *      Documentation</a>
     */
    @Override
    public CompletableFuture<ExportCertificateResponse> exportCertificate(ExportCertificateRequest exportCertificateRequest) {
        List<MetricPublisher> metricPublishers = resolveMetricPublishers(clientConfiguration, exportCertificateRequest
                .overrideConfiguration().orElse(null));
        MetricCollector apiCallMetricCollector = metricPublishers.isEmpty() ? NoOpMetricCollector.create() : MetricCollector
                .create("ApiCall");
        try {
            apiCallMetricCollector.reportMetric(CoreMetric.SERVICE_ID, "ACM");
            apiCallMetricCollector.reportMetric(CoreMetric.OPERATION_NAME, "ExportCertificate");
            JsonOperationMetadata operationMetadata = JsonOperationMetadata.builder().hasStreamingSuccessResponse(false)
                    .isPayloadJson(true).build();

            HttpResponseHandler<ExportCertificateResponse> responseHandler = protocolFactory.createResponseHandler(
                    operationMetadata, ExportCertificateResponse::builder);

            HttpResponseHandler<AwsServiceException> errorResponseHandler = createErrorResponseHandler(protocolFactory,
                    operationMetadata);

            CompletableFuture<ExportCertificateResponse> executeFuture = clientHandler
                    .execute(new ClientExecutionParams<ExportCertificateRequest, ExportCertificateResponse>()
                            .withOperationName("ExportCertificate")
                            .withMarshaller(new ExportCertificateRequestMarshaller(protocolFactory))
                            .withResponseHandler(responseHandler).withErrorResponseHandler(errorResponseHandler)
                            .withMetricCollector(apiCallMetricCollector).withInput(exportCertificateRequest));
            CompletableFuture<ExportCertificateResponse> whenCompleted = executeFuture.whenComplete((r, e) -> {
                metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            });
            executeFuture = CompletableFutureUtils.forwardExceptionTo(whenCompleted, executeFuture);
            return executeFuture;
        } catch (Throwable t) {
            metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    /**
     * <p>
     * Returns the account configuration options associated with an Amazon Web Services account.
     * </p>
     *
     * @param getAccountConfigurationRequest
     * @return A Java Future containing the result of the GetAccountConfiguration operation returned by the service.<br/>
     *         The CompletableFuture returned by this method can be completed exceptionally with the following
     *         exceptions.
     *         <ul>
     *         <li>AccessDeniedException You do not have access required to perform this action.</li>
     *         <li>ThrottlingException The request was denied because it exceeded a quota.</li>
     *         <li>SdkException Base class for all exceptions that can be thrown by the SDK (both service and client).
     *         Can be used for catch all scenarios.</li>
     *         <li>SdkClientException If any client side error occurs such as an IO related failure, failure to get
     *         credentials, etc.</li>
     *         <li>AcmException Base class for all service exceptions. Unknown exceptions will be thrown as an instance
     *         of this type.</li>
     *         </ul>
     * @sample AcmAsyncClient.GetAccountConfiguration
     * @see <a href="https://docs.aws.amazon.com/goto/WebAPI/acm-2015-12-08/GetAccountConfiguration" target="_top">AWS
     *      API Documentation</a>
     */
    @Override
    public CompletableFuture<GetAccountConfigurationResponse> getAccountConfiguration(
            GetAccountConfigurationRequest getAccountConfigurationRequest) {
        List<MetricPublisher> metricPublishers = resolveMetricPublishers(clientConfiguration, getAccountConfigurationRequest
                .overrideConfiguration().orElse(null));
        MetricCollector apiCallMetricCollector = metricPublishers.isEmpty() ? NoOpMetricCollector.create() : MetricCollector
                .create("ApiCall");
        try {
            apiCallMetricCollector.reportMetric(CoreMetric.SERVICE_ID, "ACM");
            apiCallMetricCollector.reportMetric(CoreMetric.OPERATION_NAME, "GetAccountConfiguration");
            JsonOperationMetadata operationMetadata = JsonOperationMetadata.builder().hasStreamingSuccessResponse(false)
                    .isPayloadJson(true).build();

            HttpResponseHandler<GetAccountConfigurationResponse> responseHandler = protocolFactory.createResponseHandler(
                    operationMetadata, GetAccountConfigurationResponse::builder);

            HttpResponseHandler<AwsServiceException> errorResponseHandler = createErrorResponseHandler(protocolFactory,
                    operationMetadata);

            CompletableFuture<GetAccountConfigurationResponse> executeFuture = clientHandler
                    .execute(new ClientExecutionParams<GetAccountConfigurationRequest, GetAccountConfigurationResponse>()
                            .withOperationName("GetAccountConfiguration")
                            .withMarshaller(new GetAccountConfigurationRequestMarshaller(protocolFactory))
                            .withResponseHandler(responseHandler).withErrorResponseHandler(errorResponseHandler)
                            .withMetricCollector(apiCallMetricCollector).withInput(getAccountConfigurationRequest));
            CompletableFuture<GetAccountConfigurationResponse> whenCompleted = executeFuture.whenComplete((r, e) -> {
                metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            });
            executeFuture = CompletableFutureUtils.forwardExceptionTo(whenCompleted, executeFuture);
            return executeFuture;
        } catch (Throwable t) {
            metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    /**
     * <p>
     * Retrieves an Amazon-issued certificate and its certificate chain. The chain consists of the certificate of the
     * issuing CA and the intermediate certificates of any other subordinate CAs. All of the certificates are base64
     * encoded. You can use <a href="https://wiki.openssl.org/index.php/Command_Line_Utilities">OpenSSL</a> to decode
     * the certificates and inspect individual fields.
     * </p>
     *
     * @param getCertificateRequest
     * @return A Java Future containing the result of the GetCertificate operation returned by the service.<br/>
     *         The CompletableFuture returned by this method can be completed exceptionally with the following
     *         exceptions.
     *         <ul>
     *         <li>ResourceNotFoundException The specified certificate cannot be found in the caller's account or the
     *         caller's account cannot be found.</li>
     *         <li>RequestInProgressException The certificate request is in process and the certificate in your account
     *         has not yet been issued.</li>
     *         <li>InvalidArnException The requested Amazon Resource Name (ARN) does not refer to an existing resource.</li>
     *         <li>SdkException Base class for all exceptions that can be thrown by the SDK (both service and client).
     *         Can be used for catch all scenarios.</li>
     *         <li>SdkClientException If any client side error occurs such as an IO related failure, failure to get
     *         credentials, etc.</li>
     *         <li>AcmException Base class for all service exceptions. Unknown exceptions will be thrown as an instance
     *         of this type.</li>
     *         </ul>
     * @sample AcmAsyncClient.GetCertificate
     * @see <a href="https://docs.aws.amazon.com/goto/WebAPI/acm-2015-12-08/GetCertificate" target="_top">AWS API
     *      Documentation</a>
     */
    @Override
    public CompletableFuture<GetCertificateResponse> getCertificate(GetCertificateRequest getCertificateRequest) {
        List<MetricPublisher> metricPublishers = resolveMetricPublishers(clientConfiguration, getCertificateRequest
                .overrideConfiguration().orElse(null));
        MetricCollector apiCallMetricCollector = metricPublishers.isEmpty() ? NoOpMetricCollector.create() : MetricCollector
                .create("ApiCall");
        try {
            apiCallMetricCollector.reportMetric(CoreMetric.SERVICE_ID, "ACM");
            apiCallMetricCollector.reportMetric(CoreMetric.OPERATION_NAME, "GetCertificate");
            JsonOperationMetadata operationMetadata = JsonOperationMetadata.builder().hasStreamingSuccessResponse(false)
                    .isPayloadJson(true).build();

            HttpResponseHandler<GetCertificateResponse> responseHandler = protocolFactory.createResponseHandler(
                    operationMetadata, GetCertificateResponse::builder);

            HttpResponseHandler<AwsServiceException> errorResponseHandler = createErrorResponseHandler(protocolFactory,
                    operationMetadata);

            CompletableFuture<GetCertificateResponse> executeFuture = clientHandler
                    .execute(new ClientExecutionParams<GetCertificateRequest, GetCertificateResponse>()
                            .withOperationName("GetCertificate")
                            .withMarshaller(new GetCertificateRequestMarshaller(protocolFactory))
                            .withResponseHandler(responseHandler).withErrorResponseHandler(errorResponseHandler)
                            .withMetricCollector(apiCallMetricCollector).withInput(getCertificateRequest));
            CompletableFuture<GetCertificateResponse> whenCompleted = executeFuture.whenComplete((r, e) -> {
                metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            });
            executeFuture = CompletableFutureUtils.forwardExceptionTo(whenCompleted, executeFuture);
            return executeFuture;
        } catch (Throwable t) {
            metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    /**
     * <p>
     * Imports a certificate into Certificate Manager (ACM) to use with services that are integrated with ACM. Note that
     * <a href="https://docs.aws.amazon.com/acm/latest/userguide/acm-services.html">integrated services</a> allow only
     * certificate types and keys they support to be associated with their resources. Further, their support differs
     * depending on whether the certificate is imported into IAM or into ACM. For more information, see the
     * documentation for each service. For more information about importing certificates into ACM, see <a
     * href="https://docs.aws.amazon.com/acm/latest/userguide/import-certificate.html">Importing Certificates</a> in the
     * <i>Certificate Manager User Guide</i>.
     * </p>
     * <note>
     * <p>
     * ACM does not provide <a href="https://docs.aws.amazon.com/acm/latest/userguide/acm-renewal.html">managed
     * renewal</a> for certificates that you import.
     * </p>
     * </note>
     * <p>
     * Note the following guidelines when importing third party certificates:
     * </p>
     * <ul>
     * <li>
     * <p>
     * You must enter the private key that matches the certificate you are importing.
     * </p>
     * </li>
     * <li>
     * <p>
     * The private key must be unencrypted. You cannot import a private key that is protected by a password or a
     * passphrase.
     * </p>
     * </li>
     * <li>
     * <p>
     * The private key must be no larger than 5 KB (5,120 bytes).
     * </p>
     * </li>
     * <li>
     * <p>
     * If the certificate you are importing is not self-signed, you must enter its certificate chain.
     * </p>
     * </li>
     * <li>
     * <p>
     * If a certificate chain is included, the issuer must be the subject of one of the certificates in the chain.
     * </p>
     * </li>
     * <li>
     * <p>
     * The certificate, private key, and certificate chain must be PEM-encoded.
     * </p>
     * </li>
     * <li>
     * <p>
     * The current time must be between the <code>Not Before</code> and <code>Not After</code> certificate fields.
     * </p>
     * </li>
     * <li>
     * <p>
     * The <code>Issuer</code> field must not be empty.
     * </p>
     * </li>
     * <li>
     * <p>
     * The OCSP authority URL, if present, must not exceed 1000 characters.
     * </p>
     * </li>
     * <li>
     * <p>
     * To import a new certificate, omit the <code>CertificateArn</code> argument. Include this argument only when you
     * want to replace a previously imported certificate.
     * </p>
     * </li>
     * <li>
     * <p>
     * When you import a certificate by using the CLI, you must specify the certificate, the certificate chain, and the
     * private key by their file names preceded by <code>fileb://</code>. For example, you can specify a certificate
     * saved in the <code>C:\temp</code> folder as <code>fileb://C:\temp\certificate_to_import.pem</code>. If you are
     * making an HTTP or HTTPS Query request, include these arguments as BLOBs.
     * </p>
     * </li>
     * <li>
     * <p>
     * When you import a certificate by using an SDK, you must specify the certificate, the certificate chain, and the
     * private key files in the manner required by the programming language you're using.
     * </p>
     * </li>
     * <li>
     * <p>
     * The cryptographic algorithm of an imported certificate must match the algorithm of the signing CA. For example,
     * if the signing CA key type is RSA, then the certificate key type must also be RSA.
     * </p>
     * </li>
     * </ul>
     * <p>
     * This operation returns the <a
     * href="https://docs.aws.amazon.com/general/latest/gr/aws-arns-and-namespaces.html">Amazon Resource Name (ARN)</a>
     * of the imported certificate.
     * </p>
     *
     * @param importCertificateRequest
     * @return A Java Future containing the result of the ImportCertificate operation returned by the service.<br/>
     *         The CompletableFuture returned by this method can be completed exceptionally with the following
     *         exceptions.
     *         <ul>
     *         <li>ResourceNotFoundException The specified certificate cannot be found in the caller's account or the
     *         caller's account cannot be found.</li>
     *         <li>LimitExceededException An ACM quota has been exceeded.</li>
     *         <li>InvalidTagException One or both of the values that make up the key-value pair is not valid. For
     *         example, you cannot specify a tag value that begins with <code>aws:</code>.</li>
     *         <li>TooManyTagsException The request contains too many tags. Try the request again with fewer tags.</li>
     *         <li>TagPolicyException A specified tag did not comply with an existing tag policy and was rejected.</li>
     *         <li>InvalidParameterException An input parameter was invalid.</li>
     *         <li>InvalidArnException The requested Amazon Resource Name (ARN) does not refer to an existing resource.</li>
     *         <li>SdkException Base class for all exceptions that can be thrown by the SDK (both service and client).
     *         Can be used for catch all scenarios.</li>
     *         <li>SdkClientException If any client side error occurs such as an IO related failure, failure to get
     *         credentials, etc.</li>
     *         <li>AcmException Base class for all service exceptions. Unknown exceptions will be thrown as an instance
     *         of this type.</li>
     *         </ul>
     * @sample AcmAsyncClient.ImportCertificate
     * @see <a href="https://docs.aws.amazon.com/goto/WebAPI/acm-2015-12-08/ImportCertificate" target="_top">AWS API
     *      Documentation</a>
     */
    @Override
    public CompletableFuture<ImportCertificateResponse> importCertificate(ImportCertificateRequest importCertificateRequest) {
        List<MetricPublisher> metricPublishers = resolveMetricPublishers(clientConfiguration, importCertificateRequest
                .overrideConfiguration().orElse(null));
        MetricCollector apiCallMetricCollector = metricPublishers.isEmpty() ? NoOpMetricCollector.create() : MetricCollector
                .create("ApiCall");
        try {
            apiCallMetricCollector.reportMetric(CoreMetric.SERVICE_ID, "ACM");
            apiCallMetricCollector.reportMetric(CoreMetric.OPERATION_NAME, "ImportCertificate");
            JsonOperationMetadata operationMetadata = JsonOperationMetadata.builder().hasStreamingSuccessResponse(false)
                    .isPayloadJson(true).build();

            HttpResponseHandler<ImportCertificateResponse> responseHandler = protocolFactory.createResponseHandler(
                    operationMetadata, ImportCertificateResponse::builder);

            HttpResponseHandler<AwsServiceException> errorResponseHandler = createErrorResponseHandler(protocolFactory,
                    operationMetadata);

            CompletableFuture<ImportCertificateResponse> executeFuture = clientHandler
                    .execute(new ClientExecutionParams<ImportCertificateRequest, ImportCertificateResponse>()
                            .withOperationName("ImportCertificate")
                            .withMarshaller(new ImportCertificateRequestMarshaller(protocolFactory))
                            .withResponseHandler(responseHandler).withErrorResponseHandler(errorResponseHandler)
                            .withMetricCollector(apiCallMetricCollector).withInput(importCertificateRequest));
            CompletableFuture<ImportCertificateResponse> whenCompleted = executeFuture.whenComplete((r, e) -> {
                metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            });
            executeFuture = CompletableFutureUtils.forwardExceptionTo(whenCompleted, executeFuture);
            return executeFuture;
        } catch (Throwable t) {
            metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    /**
     * <p>
     * Retrieves a list of certificate ARNs and domain names. You can request that only certificates that match a
     * specific status be listed. You can also filter by specific attributes of the certificate. Default filtering
     * returns only <code>RSA_2048</code> certificates. For more information, see <a>Filters</a>.
     * </p>
     *
     * @param listCertificatesRequest
     * @return A Java Future containing the result of the ListCertificates operation returned by the service.<br/>
     *         The CompletableFuture returned by this method can be completed exceptionally with the following
     *         exceptions.
     *         <ul>
     *         <li>InvalidArgsException One or more of of request parameters specified is not valid.</li>
     *         <li>ValidationException The supplied input failed to satisfy constraints of an Amazon Web Services
     *         service.</li>
     *         <li>SdkException Base class for all exceptions that can be thrown by the SDK (both service and client).
     *         Can be used for catch all scenarios.</li>
     *         <li>SdkClientException If any client side error occurs such as an IO related failure, failure to get
     *         credentials, etc.</li>
     *         <li>AcmException Base class for all service exceptions. Unknown exceptions will be thrown as an instance
     *         of this type.</li>
     *         </ul>
     * @sample AcmAsyncClient.ListCertificates
     * @see <a href="https://docs.aws.amazon.com/goto/WebAPI/acm-2015-12-08/ListCertificates" target="_top">AWS API
     *      Documentation</a>
     */
    @Override
    public CompletableFuture<ListCertificatesResponse> listCertificates(ListCertificatesRequest listCertificatesRequest) {
        List<MetricPublisher> metricPublishers = resolveMetricPublishers(clientConfiguration, listCertificatesRequest
                .overrideConfiguration().orElse(null));
        MetricCollector apiCallMetricCollector = metricPublishers.isEmpty() ? NoOpMetricCollector.create() : MetricCollector
                .create("ApiCall");
        try {
            apiCallMetricCollector.reportMetric(CoreMetric.SERVICE_ID, "ACM");
            apiCallMetricCollector.reportMetric(CoreMetric.OPERATION_NAME, "ListCertificates");
            JsonOperationMetadata operationMetadata = JsonOperationMetadata.builder().hasStreamingSuccessResponse(false)
                    .isPayloadJson(true).build();

            HttpResponseHandler<ListCertificatesResponse> responseHandler = protocolFactory.createResponseHandler(
                    operationMetadata, ListCertificatesResponse::builder);

            HttpResponseHandler<AwsServiceException> errorResponseHandler = createErrorResponseHandler(protocolFactory,
                    operationMetadata);

            CompletableFuture<ListCertificatesResponse> executeFuture = clientHandler
                    .execute(new ClientExecutionParams<ListCertificatesRequest, ListCertificatesResponse>()
                            .withOperationName("ListCertificates")
                            .withMarshaller(new ListCertificatesRequestMarshaller(protocolFactory))
                            .withResponseHandler(responseHandler).withErrorResponseHandler(errorResponseHandler)
                            .withMetricCollector(apiCallMetricCollector).withInput(listCertificatesRequest));
            CompletableFuture<ListCertificatesResponse> whenCompleted = executeFuture.whenComplete((r, e) -> {
                metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            });
            executeFuture = CompletableFutureUtils.forwardExceptionTo(whenCompleted, executeFuture);
            return executeFuture;
        } catch (Throwable t) {
            metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    /**
     * <p>
     * Retrieves a list of certificate ARNs and domain names. You can request that only certificates that match a
     * specific status be listed. You can also filter by specific attributes of the certificate. Default filtering
     * returns only <code>RSA_2048</code> certificates. For more information, see <a>Filters</a>.
     * </p>
     * <br/>
     * <p>
     * This is a variant of {@link #listCertificates(software.amazon.awssdk.services.acm.model.ListCertificatesRequest)}
     * operation. The return type is a custom publisher that can be subscribed to request a stream of response pages.
     * SDK will internally handle making service calls for you.
     * </p>
     * <p>
     * When the operation is called, an instance of this class is returned. At this point, no service calls are made yet
     * and so there is no guarantee that the request is valid. If there are errors in your request, you will see the
     * failures only after you start streaming the data. The subscribe method should be called as a request to start
     * streaming data. For more info, see
     * {@link org.reactivestreams.Publisher#subscribe(org.reactivestreams.Subscriber)}. Each call to the subscribe
     * method will result in a new {@link org.reactivestreams.Subscription} i.e., a new contract to stream data from the
     * starting request.
     * </p>
     *
     * <p>
     * The following are few ways to use the response class:
     * </p>
     * 1) Using the subscribe helper method
     * 
     * <pre>
     * {@code
     * software.amazon.awssdk.services.acm.paginators.ListCertificatesPublisher publisher = client.listCertificatesPaginator(request);
     * CompletableFuture<Void> future = publisher.subscribe(res -> { // Do something with the response });
     * future.get();
     * }
     * </pre>
     *
     * 2) Using a custom subscriber
     * 
     * <pre>
     * {@code
     * software.amazon.awssdk.services.acm.paginators.ListCertificatesPublisher publisher = client.listCertificatesPaginator(request);
     * publisher.subscribe(new Subscriber<software.amazon.awssdk.services.acm.model.ListCertificatesResponse>() {
     * 
     * public void onSubscribe(org.reactivestreams.Subscriber subscription) { //... };
     * 
     * 
     * public void onNext(software.amazon.awssdk.services.acm.model.ListCertificatesResponse response) { //... };
     * });}
     * </pre>
     * 
     * As the response is a publisher, it can work well with third party reactive streams implementations like RxJava2.
     * <p>
     * <b>Please notice that the configuration of MaxItems won't limit the number of results you get with the paginator.
     * It only limits the number of results in each page.</b>
     * </p>
     * <p>
     * <b>Note: If you prefer to have control on service calls, use the
     * {@link #listCertificates(software.amazon.awssdk.services.acm.model.ListCertificatesRequest)} operation.</b>
     * </p>
     *
     * @param listCertificatesRequest
     * @return A custom publisher that can be subscribed to request a stream of response pages.<br/>
     *         The CompletableFuture returned by this method can be completed exceptionally with the following
     *         exceptions.
     *         <ul>
     *         <li>InvalidArgsException One or more of of request parameters specified is not valid.</li>
     *         <li>ValidationException The supplied input failed to satisfy constraints of an Amazon Web Services
     *         service.</li>
     *         <li>SdkException Base class for all exceptions that can be thrown by the SDK (both service and client).
     *         Can be used for catch all scenarios.</li>
     *         <li>SdkClientException If any client side error occurs such as an IO related failure, failure to get
     *         credentials, etc.</li>
     *         <li>AcmException Base class for all service exceptions. Unknown exceptions will be thrown as an instance
     *         of this type.</li>
     *         </ul>
     * @sample AcmAsyncClient.ListCertificates
     * @see <a href="https://docs.aws.amazon.com/goto/WebAPI/acm-2015-12-08/ListCertificates" target="_top">AWS API
     *      Documentation</a>
     */
    public ListCertificatesPublisher listCertificatesPaginator(ListCertificatesRequest listCertificatesRequest) {
        return new ListCertificatesPublisher(this, applyPaginatorUserAgent(listCertificatesRequest));
    }

    /**
     * <p>
     * Lists the tags that have been applied to the ACM certificate. Use the certificate's Amazon Resource Name (ARN) to
     * specify the certificate. To add a tag to an ACM certificate, use the <a>AddTagsToCertificate</a> action. To
     * delete a tag, use the <a>RemoveTagsFromCertificate</a> action.
     * </p>
     *
     * @param listTagsForCertificateRequest
     * @return A Java Future containing the result of the ListTagsForCertificate operation returned by the service.<br/>
     *         The CompletableFuture returned by this method can be completed exceptionally with the following
     *         exceptions.
     *         <ul>
     *         <li>ResourceNotFoundException The specified certificate cannot be found in the caller's account or the
     *         caller's account cannot be found.</li>
     *         <li>InvalidArnException The requested Amazon Resource Name (ARN) does not refer to an existing resource.</li>
     *         <li>SdkException Base class for all exceptions that can be thrown by the SDK (both service and client).
     *         Can be used for catch all scenarios.</li>
     *         <li>SdkClientException If any client side error occurs such as an IO related failure, failure to get
     *         credentials, etc.</li>
     *         <li>AcmException Base class for all service exceptions. Unknown exceptions will be thrown as an instance
     *         of this type.</li>
     *         </ul>
     * @sample AcmAsyncClient.ListTagsForCertificate
     * @see <a href="https://docs.aws.amazon.com/goto/WebAPI/acm-2015-12-08/ListTagsForCertificate" target="_top">AWS
     *      API Documentation</a>
     */
    @Override
    public CompletableFuture<ListTagsForCertificateResponse> listTagsForCertificate(
            ListTagsForCertificateRequest listTagsForCertificateRequest) {
        List<MetricPublisher> metricPublishers = resolveMetricPublishers(clientConfiguration, listTagsForCertificateRequest
                .overrideConfiguration().orElse(null));
        MetricCollector apiCallMetricCollector = metricPublishers.isEmpty() ? NoOpMetricCollector.create() : MetricCollector
                .create("ApiCall");
        try {
            apiCallMetricCollector.reportMetric(CoreMetric.SERVICE_ID, "ACM");
            apiCallMetricCollector.reportMetric(CoreMetric.OPERATION_NAME, "ListTagsForCertificate");
            JsonOperationMetadata operationMetadata = JsonOperationMetadata.builder().hasStreamingSuccessResponse(false)
                    .isPayloadJson(true).build();

            HttpResponseHandler<ListTagsForCertificateResponse> responseHandler = protocolFactory.createResponseHandler(
                    operationMetadata, ListTagsForCertificateResponse::builder);

            HttpResponseHandler<AwsServiceException> errorResponseHandler = createErrorResponseHandler(protocolFactory,
                    operationMetadata);

            CompletableFuture<ListTagsForCertificateResponse> executeFuture = clientHandler
                    .execute(new ClientExecutionParams<ListTagsForCertificateRequest, ListTagsForCertificateResponse>()
                            .withOperationName("ListTagsForCertificate")
                            .withMarshaller(new ListTagsForCertificateRequestMarshaller(protocolFactory))
                            .withResponseHandler(responseHandler).withErrorResponseHandler(errorResponseHandler)
                            .withMetricCollector(apiCallMetricCollector).withInput(listTagsForCertificateRequest));
            CompletableFuture<ListTagsForCertificateResponse> whenCompleted = executeFuture.whenComplete((r, e) -> {
                metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            });
            executeFuture = CompletableFutureUtils.forwardExceptionTo(whenCompleted, executeFuture);
            return executeFuture;
        } catch (Throwable t) {
            metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    /**
     * <p>
     * Adds or modifies account-level configurations in ACM.
     * </p>
     * <p>
     * The supported configuration option is <code>DaysBeforeExpiry</code>. This option specifies the number of days
     * prior to certificate expiration when ACM starts generating <code>EventBridge</code> events. ACM sends one event
     * per day per certificate until the certificate expires. By default, accounts receive events starting 45 days
     * before certificate expiration.
     * </p>
     *
     * @param putAccountConfigurationRequest
     * @return A Java Future containing the result of the PutAccountConfiguration operation returned by the service.<br/>
     *         The CompletableFuture returned by this method can be completed exceptionally with the following
     *         exceptions.
     *         <ul>
     *         <li>ValidationException The supplied input failed to satisfy constraints of an Amazon Web Services
     *         service.</li>
     *         <li>ThrottlingException The request was denied because it exceeded a quota.</li>
     *         <li>AccessDeniedException You do not have access required to perform this action.</li>
     *         <li>ConflictException You are trying to update a resource or configuration that is already being created
     *         or updated. Wait for the previous operation to finish and try again.</li>
     *         <li>SdkException Base class for all exceptions that can be thrown by the SDK (both service and client).
     *         Can be used for catch all scenarios.</li>
     *         <li>SdkClientException If any client side error occurs such as an IO related failure, failure to get
     *         credentials, etc.</li>
     *         <li>AcmException Base class for all service exceptions. Unknown exceptions will be thrown as an instance
     *         of this type.</li>
     *         </ul>
     * @sample AcmAsyncClient.PutAccountConfiguration
     * @see <a href="https://docs.aws.amazon.com/goto/WebAPI/acm-2015-12-08/PutAccountConfiguration" target="_top">AWS
     *      API Documentation</a>
     */
    @Override
    public CompletableFuture<PutAccountConfigurationResponse> putAccountConfiguration(
            PutAccountConfigurationRequest putAccountConfigurationRequest) {
        List<MetricPublisher> metricPublishers = resolveMetricPublishers(clientConfiguration, putAccountConfigurationRequest
                .overrideConfiguration().orElse(null));
        MetricCollector apiCallMetricCollector = metricPublishers.isEmpty() ? NoOpMetricCollector.create() : MetricCollector
                .create("ApiCall");
        try {
            apiCallMetricCollector.reportMetric(CoreMetric.SERVICE_ID, "ACM");
            apiCallMetricCollector.reportMetric(CoreMetric.OPERATION_NAME, "PutAccountConfiguration");
            JsonOperationMetadata operationMetadata = JsonOperationMetadata.builder().hasStreamingSuccessResponse(false)
                    .isPayloadJson(true).build();

            HttpResponseHandler<PutAccountConfigurationResponse> responseHandler = protocolFactory.createResponseHandler(
                    operationMetadata, PutAccountConfigurationResponse::builder);

            HttpResponseHandler<AwsServiceException> errorResponseHandler = createErrorResponseHandler(protocolFactory,
                    operationMetadata);

            CompletableFuture<PutAccountConfigurationResponse> executeFuture = clientHandler
                    .execute(new ClientExecutionParams<PutAccountConfigurationRequest, PutAccountConfigurationResponse>()
                            .withOperationName("PutAccountConfiguration")
                            .withMarshaller(new PutAccountConfigurationRequestMarshaller(protocolFactory))
                            .withResponseHandler(responseHandler).withErrorResponseHandler(errorResponseHandler)
                            .withMetricCollector(apiCallMetricCollector).withInput(putAccountConfigurationRequest));
            CompletableFuture<PutAccountConfigurationResponse> whenCompleted = executeFuture.whenComplete((r, e) -> {
                metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            });
            executeFuture = CompletableFutureUtils.forwardExceptionTo(whenCompleted, executeFuture);
            return executeFuture;
        } catch (Throwable t) {
            metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    /**
     * <p>
     * Remove one or more tags from an ACM certificate. A tag consists of a key-value pair. If you do not specify the
     * value portion of the tag when calling this function, the tag will be removed regardless of value. If you specify
     * a value, the tag is removed only if it is associated with the specified value.
     * </p>
     * <p>
     * To add tags to a certificate, use the <a>AddTagsToCertificate</a> action. To view all of the tags that have been
     * applied to a specific ACM certificate, use the <a>ListTagsForCertificate</a> action.
     * </p>
     *
     * @param removeTagsFromCertificateRequest
     * @return A Java Future containing the result of the RemoveTagsFromCertificate operation returned by the service.<br/>
     *         The CompletableFuture returned by this method can be completed exceptionally with the following
     *         exceptions.
     *         <ul>
     *         <li>ResourceNotFoundException The specified certificate cannot be found in the caller's account or the
     *         caller's account cannot be found.</li>
     *         <li>InvalidArnException The requested Amazon Resource Name (ARN) does not refer to an existing resource.</li>
     *         <li>InvalidTagException One or both of the values that make up the key-value pair is not valid. For
     *         example, you cannot specify a tag value that begins with <code>aws:</code>.</li>
     *         <li>TagPolicyException A specified tag did not comply with an existing tag policy and was rejected.</li>
     *         <li>InvalidParameterException An input parameter was invalid.</li>
     *         <li>ThrottlingException The request was denied because it exceeded a quota.</li>
     *         <li>SdkException Base class for all exceptions that can be thrown by the SDK (both service and client).
     *         Can be used for catch all scenarios.</li>
     *         <li>SdkClientException If any client side error occurs such as an IO related failure, failure to get
     *         credentials, etc.</li>
     *         <li>AcmException Base class for all service exceptions. Unknown exceptions will be thrown as an instance
     *         of this type.</li>
     *         </ul>
     * @sample AcmAsyncClient.RemoveTagsFromCertificate
     * @see <a href="https://docs.aws.amazon.com/goto/WebAPI/acm-2015-12-08/RemoveTagsFromCertificate" target="_top">AWS
     *      API Documentation</a>
     */
    @Override
    public CompletableFuture<RemoveTagsFromCertificateResponse> removeTagsFromCertificate(
            RemoveTagsFromCertificateRequest removeTagsFromCertificateRequest) {
        List<MetricPublisher> metricPublishers = resolveMetricPublishers(clientConfiguration, removeTagsFromCertificateRequest
                .overrideConfiguration().orElse(null));
        MetricCollector apiCallMetricCollector = metricPublishers.isEmpty() ? NoOpMetricCollector.create() : MetricCollector
                .create("ApiCall");
        try {
            apiCallMetricCollector.reportMetric(CoreMetric.SERVICE_ID, "ACM");
            apiCallMetricCollector.reportMetric(CoreMetric.OPERATION_NAME, "RemoveTagsFromCertificate");
            JsonOperationMetadata operationMetadata = JsonOperationMetadata.builder().hasStreamingSuccessResponse(false)
                    .isPayloadJson(true).build();

            HttpResponseHandler<RemoveTagsFromCertificateResponse> responseHandler = protocolFactory.createResponseHandler(
                    operationMetadata, RemoveTagsFromCertificateResponse::builder);

            HttpResponseHandler<AwsServiceException> errorResponseHandler = createErrorResponseHandler(protocolFactory,
                    operationMetadata);

            CompletableFuture<RemoveTagsFromCertificateResponse> executeFuture = clientHandler
                    .execute(new ClientExecutionParams<RemoveTagsFromCertificateRequest, RemoveTagsFromCertificateResponse>()
                            .withOperationName("RemoveTagsFromCertificate")
                            .withMarshaller(new RemoveTagsFromCertificateRequestMarshaller(protocolFactory))
                            .withResponseHandler(responseHandler).withErrorResponseHandler(errorResponseHandler)
                            .withMetricCollector(apiCallMetricCollector).withInput(removeTagsFromCertificateRequest));
            CompletableFuture<RemoveTagsFromCertificateResponse> whenCompleted = executeFuture.whenComplete((r, e) -> {
                metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            });
            executeFuture = CompletableFutureUtils.forwardExceptionTo(whenCompleted, executeFuture);
            return executeFuture;
        } catch (Throwable t) {
            metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    /**
     * <p>
     * Renews an eligible ACM certificate. At this time, only exported private certificates can be renewed with this
     * operation. In order to renew your Amazon Web Services Private CA certificates with ACM, you must first <a
     * href="https://docs.aws.amazon.com/privateca/latest/userguide/PcaPermissions.html">grant the ACM service principal
     * permission to do so</a>. For more information, see <a
     * href="https://docs.aws.amazon.com/acm/latest/userguide/manual-renewal.html">Testing Managed Renewal</a> in the
     * ACM User Guide.
     * </p>
     *
     * @param renewCertificateRequest
     * @return A Java Future containing the result of the RenewCertificate operation returned by the service.<br/>
     *         The CompletableFuture returned by this method can be completed exceptionally with the following
     *         exceptions.
     *         <ul>
     *         <li>ResourceNotFoundException The specified certificate cannot be found in the caller's account or the
     *         caller's account cannot be found.</li>
     *         <li>InvalidArnException The requested Amazon Resource Name (ARN) does not refer to an existing resource.</li>
     *         <li>SdkException Base class for all exceptions that can be thrown by the SDK (both service and client).
     *         Can be used for catch all scenarios.</li>
     *         <li>SdkClientException If any client side error occurs such as an IO related failure, failure to get
     *         credentials, etc.</li>
     *         <li>AcmException Base class for all service exceptions. Unknown exceptions will be thrown as an instance
     *         of this type.</li>
     *         </ul>
     * @sample AcmAsyncClient.RenewCertificate
     * @see <a href="https://docs.aws.amazon.com/goto/WebAPI/acm-2015-12-08/RenewCertificate" target="_top">AWS API
     *      Documentation</a>
     */
    @Override
    public CompletableFuture<RenewCertificateResponse> renewCertificate(RenewCertificateRequest renewCertificateRequest) {
        List<MetricPublisher> metricPublishers = resolveMetricPublishers(clientConfiguration, renewCertificateRequest
                .overrideConfiguration().orElse(null));
        MetricCollector apiCallMetricCollector = metricPublishers.isEmpty() ? NoOpMetricCollector.create() : MetricCollector
                .create("ApiCall");
        try {
            apiCallMetricCollector.reportMetric(CoreMetric.SERVICE_ID, "ACM");
            apiCallMetricCollector.reportMetric(CoreMetric.OPERATION_NAME, "RenewCertificate");
            JsonOperationMetadata operationMetadata = JsonOperationMetadata.builder().hasStreamingSuccessResponse(false)
                    .isPayloadJson(true).build();

            HttpResponseHandler<RenewCertificateResponse> responseHandler = protocolFactory.createResponseHandler(
                    operationMetadata, RenewCertificateResponse::builder);

            HttpResponseHandler<AwsServiceException> errorResponseHandler = createErrorResponseHandler(protocolFactory,
                    operationMetadata);

            CompletableFuture<RenewCertificateResponse> executeFuture = clientHandler
                    .execute(new ClientExecutionParams<RenewCertificateRequest, RenewCertificateResponse>()
                            .withOperationName("RenewCertificate")
                            .withMarshaller(new RenewCertificateRequestMarshaller(protocolFactory))
                            .withResponseHandler(responseHandler).withErrorResponseHandler(errorResponseHandler)
                            .withMetricCollector(apiCallMetricCollector).withInput(renewCertificateRequest));
            CompletableFuture<RenewCertificateResponse> whenCompleted = executeFuture.whenComplete((r, e) -> {
                metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            });
            executeFuture = CompletableFutureUtils.forwardExceptionTo(whenCompleted, executeFuture);
            return executeFuture;
        } catch (Throwable t) {
            metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    /**
     * <p>
     * Requests an ACM certificate for use with other Amazon Web Services services. To request an ACM certificate, you
     * must specify a fully qualified domain name (FQDN) in the <code>DomainName</code> parameter. You can also specify
     * additional FQDNs in the <code>SubjectAlternativeNames</code> parameter.
     * </p>
     * <p>
     * If you are requesting a private certificate, domain validation is not required. If you are requesting a public
     * certificate, each domain name that you specify must be validated to verify that you own or control the domain.
     * You can use <a href="https://docs.aws.amazon.com/acm/latest/userguide/gs-acm-validate-dns.html">DNS
     * validation</a> or <a href="https://docs.aws.amazon.com/acm/latest/userguide/gs-acm-validate-email.html">email
     * validation</a>. We recommend that you use DNS validation. ACM issues public certificates after receiving approval
     * from the domain owner.
     * </p>
     * <note>
     * <p>
     * ACM behavior differs from the <a href="https://datatracker.ietf.org/doc/html/rfc6125#appendix-B.2">RFC 6125</a>
     * specification of the certificate validation process. ACM first checks for a Subject Alternative Name, and, if it
     * finds one, ignores the common name (CN).
     * </p>
     * </note>
     * <p>
     * After successful completion of the <code>RequestCertificate</code> action, there is a delay of several seconds
     * before you can retrieve information about the new certificate.
     * </p>
     *
     * @param requestCertificateRequest
     * @return A Java Future containing the result of the RequestCertificate operation returned by the service.<br/>
     *         The CompletableFuture returned by this method can be completed exceptionally with the following
     *         exceptions.
     *         <ul>
     *         <li>LimitExceededException An ACM quota has been exceeded.</li>
     *         <li>InvalidDomainValidationOptionsException One or more values in the <a>DomainValidationOption</a>
     *         structure is incorrect.</li>
     *         <li>InvalidArnException The requested Amazon Resource Name (ARN) does not refer to an existing resource.</li>
     *         <li>InvalidTagException One or both of the values that make up the key-value pair is not valid. For
     *         example, you cannot specify a tag value that begins with <code>aws:</code>.</li>
     *         <li>TooManyTagsException The request contains too many tags. Try the request again with fewer tags.</li>
     *         <li>TagPolicyException A specified tag did not comply with an existing tag policy and was rejected.</li>
     *         <li>InvalidParameterException An input parameter was invalid.</li>
     *         <li>SdkException Base class for all exceptions that can be thrown by the SDK (both service and client).
     *         Can be used for catch all scenarios.</li>
     *         <li>SdkClientException If any client side error occurs such as an IO related failure, failure to get
     *         credentials, etc.</li>
     *         <li>AcmException Base class for all service exceptions. Unknown exceptions will be thrown as an instance
     *         of this type.</li>
     *         </ul>
     * @sample AcmAsyncClient.RequestCertificate
     * @see <a href="https://docs.aws.amazon.com/goto/WebAPI/acm-2015-12-08/RequestCertificate" target="_top">AWS API
     *      Documentation</a>
     */
    @Override
    public CompletableFuture<RequestCertificateResponse> requestCertificate(RequestCertificateRequest requestCertificateRequest) {
        List<MetricPublisher> metricPublishers = resolveMetricPublishers(clientConfiguration, requestCertificateRequest
                .overrideConfiguration().orElse(null));
        MetricCollector apiCallMetricCollector = metricPublishers.isEmpty() ? NoOpMetricCollector.create() : MetricCollector
                .create("ApiCall");
        try {
            apiCallMetricCollector.reportMetric(CoreMetric.SERVICE_ID, "ACM");
            apiCallMetricCollector.reportMetric(CoreMetric.OPERATION_NAME, "RequestCertificate");
            JsonOperationMetadata operationMetadata = JsonOperationMetadata.builder().hasStreamingSuccessResponse(false)
                    .isPayloadJson(true).build();

            HttpResponseHandler<RequestCertificateResponse> responseHandler = protocolFactory.createResponseHandler(
                    operationMetadata, RequestCertificateResponse::builder);

            HttpResponseHandler<AwsServiceException> errorResponseHandler = createErrorResponseHandler(protocolFactory,
                    operationMetadata);

            CompletableFuture<RequestCertificateResponse> executeFuture = clientHandler
                    .execute(new ClientExecutionParams<RequestCertificateRequest, RequestCertificateResponse>()
                            .withOperationName("RequestCertificate")
                            .withMarshaller(new RequestCertificateRequestMarshaller(protocolFactory))
                            .withResponseHandler(responseHandler).withErrorResponseHandler(errorResponseHandler)
                            .withMetricCollector(apiCallMetricCollector).withInput(requestCertificateRequest));
            CompletableFuture<RequestCertificateResponse> whenCompleted = executeFuture.whenComplete((r, e) -> {
                metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            });
            executeFuture = CompletableFutureUtils.forwardExceptionTo(whenCompleted, executeFuture);
            return executeFuture;
        } catch (Throwable t) {
            metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    /**
     * <p>
     * Resends the email that requests domain ownership validation. The domain owner or an authorized representative
     * must approve the ACM certificate before it can be issued. The certificate can be approved by clicking a link in
     * the mail to navigate to the Amazon certificate approval website and then clicking <b>I Approve</b>. However, the
     * validation email can be blocked by spam filters. Therefore, if you do not receive the original mail, you can
     * request that the mail be resent within 72 hours of requesting the ACM certificate. If more than 72 hours have
     * elapsed since your original request or since your last attempt to resend validation mail, you must request a new
     * certificate. For more information about setting up your contact email addresses, see <a
     * href="https://docs.aws.amazon.com/acm/latest/userguide/setup-email.html">Configure Email for your Domain</a>.
     * </p>
     *
     * @param resendValidationEmailRequest
     * @return A Java Future containing the result of the ResendValidationEmail operation returned by the service.<br/>
     *         The CompletableFuture returned by this method can be completed exceptionally with the following
     *         exceptions.
     *         <ul>
     *         <li>ResourceNotFoundException The specified certificate cannot be found in the caller's account or the
     *         caller's account cannot be found.</li>
     *         <li>InvalidStateException Processing has reached an invalid state.</li>
     *         <li>InvalidArnException The requested Amazon Resource Name (ARN) does not refer to an existing resource.</li>
     *         <li>InvalidDomainValidationOptionsException One or more values in the <a>DomainValidationOption</a>
     *         structure is incorrect.</li>
     *         <li>SdkException Base class for all exceptions that can be thrown by the SDK (both service and client).
     *         Can be used for catch all scenarios.</li>
     *         <li>SdkClientException If any client side error occurs such as an IO related failure, failure to get
     *         credentials, etc.</li>
     *         <li>AcmException Base class for all service exceptions. Unknown exceptions will be thrown as an instance
     *         of this type.</li>
     *         </ul>
     * @sample AcmAsyncClient.ResendValidationEmail
     * @see <a href="https://docs.aws.amazon.com/goto/WebAPI/acm-2015-12-08/ResendValidationEmail" target="_top">AWS API
     *      Documentation</a>
     */
    @Override
    public CompletableFuture<ResendValidationEmailResponse> resendValidationEmail(
            ResendValidationEmailRequest resendValidationEmailRequest) {
        List<MetricPublisher> metricPublishers = resolveMetricPublishers(clientConfiguration, resendValidationEmailRequest
                .overrideConfiguration().orElse(null));
        MetricCollector apiCallMetricCollector = metricPublishers.isEmpty() ? NoOpMetricCollector.create() : MetricCollector
                .create("ApiCall");
        try {
            apiCallMetricCollector.reportMetric(CoreMetric.SERVICE_ID, "ACM");
            apiCallMetricCollector.reportMetric(CoreMetric.OPERATION_NAME, "ResendValidationEmail");
            JsonOperationMetadata operationMetadata = JsonOperationMetadata.builder().hasStreamingSuccessResponse(false)
                    .isPayloadJson(true).build();

            HttpResponseHandler<ResendValidationEmailResponse> responseHandler = protocolFactory.createResponseHandler(
                    operationMetadata, ResendValidationEmailResponse::builder);

            HttpResponseHandler<AwsServiceException> errorResponseHandler = createErrorResponseHandler(protocolFactory,
                    operationMetadata);

            CompletableFuture<ResendValidationEmailResponse> executeFuture = clientHandler
                    .execute(new ClientExecutionParams<ResendValidationEmailRequest, ResendValidationEmailResponse>()
                            .withOperationName("ResendValidationEmail")
                            .withMarshaller(new ResendValidationEmailRequestMarshaller(protocolFactory))
                            .withResponseHandler(responseHandler).withErrorResponseHandler(errorResponseHandler)
                            .withMetricCollector(apiCallMetricCollector).withInput(resendValidationEmailRequest));
            CompletableFuture<ResendValidationEmailResponse> whenCompleted = executeFuture.whenComplete((r, e) -> {
                metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            });
            executeFuture = CompletableFutureUtils.forwardExceptionTo(whenCompleted, executeFuture);
            return executeFuture;
        } catch (Throwable t) {
            metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    /**
     * <p>
     * Updates a certificate. Currently, you can use this function to specify whether to opt in to or out of recording
     * your certificate in a certificate transparency log. For more information, see <a
     * href="https://docs.aws.amazon.com/acm/latest/userguide/acm-bestpractices.html#best-practices-transparency">
     * Opting Out of Certificate Transparency Logging</a>.
     * </p>
     *
     * @param updateCertificateOptionsRequest
     * @return A Java Future containing the result of the UpdateCertificateOptions operation returned by the service.<br/>
     *         The CompletableFuture returned by this method can be completed exceptionally with the following
     *         exceptions.
     *         <ul>
     *         <li>ResourceNotFoundException The specified certificate cannot be found in the caller's account or the
     *         caller's account cannot be found.</li>
     *         <li>LimitExceededException An ACM quota has been exceeded.</li>
     *         <li>InvalidStateException Processing has reached an invalid state.</li>
     *         <li>InvalidArnException The requested Amazon Resource Name (ARN) does not refer to an existing resource.</li>
     *         <li>SdkException Base class for all exceptions that can be thrown by the SDK (both service and client).
     *         Can be used for catch all scenarios.</li>
     *         <li>SdkClientException If any client side error occurs such as an IO related failure, failure to get
     *         credentials, etc.</li>
     *         <li>AcmException Base class for all service exceptions. Unknown exceptions will be thrown as an instance
     *         of this type.</li>
     *         </ul>
     * @sample AcmAsyncClient.UpdateCertificateOptions
     * @see <a href="https://docs.aws.amazon.com/goto/WebAPI/acm-2015-12-08/UpdateCertificateOptions" target="_top">AWS
     *      API Documentation</a>
     */
    @Override
    public CompletableFuture<UpdateCertificateOptionsResponse> updateCertificateOptions(
            UpdateCertificateOptionsRequest updateCertificateOptionsRequest) {
        List<MetricPublisher> metricPublishers = resolveMetricPublishers(clientConfiguration, updateCertificateOptionsRequest
                .overrideConfiguration().orElse(null));
        MetricCollector apiCallMetricCollector = metricPublishers.isEmpty() ? NoOpMetricCollector.create() : MetricCollector
                .create("ApiCall");
        try {
            apiCallMetricCollector.reportMetric(CoreMetric.SERVICE_ID, "ACM");
            apiCallMetricCollector.reportMetric(CoreMetric.OPERATION_NAME, "UpdateCertificateOptions");
            JsonOperationMetadata operationMetadata = JsonOperationMetadata.builder().hasStreamingSuccessResponse(false)
                    .isPayloadJson(true).build();

            HttpResponseHandler<UpdateCertificateOptionsResponse> responseHandler = protocolFactory.createResponseHandler(
                    operationMetadata, UpdateCertificateOptionsResponse::builder);

            HttpResponseHandler<AwsServiceException> errorResponseHandler = createErrorResponseHandler(protocolFactory,
                    operationMetadata);

            CompletableFuture<UpdateCertificateOptionsResponse> executeFuture = clientHandler
                    .execute(new ClientExecutionParams<UpdateCertificateOptionsRequest, UpdateCertificateOptionsResponse>()
                            .withOperationName("UpdateCertificateOptions")
                            .withMarshaller(new UpdateCertificateOptionsRequestMarshaller(protocolFactory))
                            .withResponseHandler(responseHandler).withErrorResponseHandler(errorResponseHandler)
                            .withMetricCollector(apiCallMetricCollector).withInput(updateCertificateOptionsRequest));
            CompletableFuture<UpdateCertificateOptionsResponse> whenCompleted = executeFuture.whenComplete((r, e) -> {
                metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            });
            executeFuture = CompletableFutureUtils.forwardExceptionTo(whenCompleted, executeFuture);
            return executeFuture;
        } catch (Throwable t) {
            metricPublishers.forEach(p -> p.publish(apiCallMetricCollector.collect()));
            return CompletableFutureUtils.failedFuture(t);
        }
    }

    @Override
    public AcmAsyncWaiter waiter() {
        return AcmAsyncWaiter.builder().client(this).scheduledExecutorService(executorService).build();
    }

    @Override
    public final AcmServiceClientConfiguration serviceClientConfiguration() {
        return this.serviceClientConfiguration;
    }

    @Override
    public final String serviceName() {
        return SERVICE_NAME;
    }

    private <T extends BaseAwsJsonProtocolFactory.Builder<T>> T init(T builder) {
        return builder
                .clientConfiguration(clientConfiguration)
                .defaultServiceExceptionSupplier(AcmException::builder)
                .protocol(AwsJsonProtocol.AWS_JSON)
                .protocolVersion("1.1")
                .registerModeledException(
                        ExceptionMetadata.builder().errorCode("InvalidTagException")
                                .exceptionBuilderSupplier(InvalidTagException::builder).httpStatusCode(400).build())
                .registerModeledException(
                        ExceptionMetadata.builder().errorCode("AccessDeniedException")
                                .exceptionBuilderSupplier(AccessDeniedException::builder).httpStatusCode(400).build())
                .registerModeledException(
                        ExceptionMetadata.builder().errorCode("TooManyTagsException")
                                .exceptionBuilderSupplier(TooManyTagsException::builder).httpStatusCode(400).build())
                .registerModeledException(
                        ExceptionMetadata.builder().errorCode("ConflictException")
                                .exceptionBuilderSupplier(ConflictException::builder).httpStatusCode(400).build())
                .registerModeledException(
                        ExceptionMetadata.builder().errorCode("InvalidParameterException")
                                .exceptionBuilderSupplier(InvalidParameterException::builder).httpStatusCode(400).build())
                .registerModeledException(
                        ExceptionMetadata.builder().errorCode("ResourceInUseException")
                                .exceptionBuilderSupplier(ResourceInUseException::builder).httpStatusCode(400).build())
                .registerModeledException(
                        ExceptionMetadata.builder().errorCode("ResourceNotFoundException")
                                .exceptionBuilderSupplier(ResourceNotFoundException::builder).httpStatusCode(400).build())
                .registerModeledException(
                        ExceptionMetadata.builder().errorCode("InvalidArgsException")
                                .exceptionBuilderSupplier(InvalidArgsException::builder).httpStatusCode(400).build())
                .registerModeledException(
                        ExceptionMetadata.builder().errorCode("InvalidArnException")
                                .exceptionBuilderSupplier(InvalidArnException::builder).httpStatusCode(400).build())
                .registerModeledException(
                        ExceptionMetadata.builder().errorCode("ThrottlingException")
                                .exceptionBuilderSupplier(ThrottlingException::builder).httpStatusCode(400).build())
                .registerModeledException(
                        ExceptionMetadata.builder().errorCode("ValidationException")
                                .exceptionBuilderSupplier(ValidationException::builder).httpStatusCode(400).build())
                .registerModeledException(
                        ExceptionMetadata.builder().errorCode("TagPolicyException")
                                .exceptionBuilderSupplier(TagPolicyException::builder).httpStatusCode(400).build())
                .registerModeledException(
                        ExceptionMetadata.builder().errorCode("RequestInProgressException")
                                .exceptionBuilderSupplier(RequestInProgressException::builder).httpStatusCode(400).build())
                .registerModeledException(
                        ExceptionMetadata.builder().errorCode("InvalidStateException")
                                .exceptionBuilderSupplier(InvalidStateException::builder).httpStatusCode(400).build())
                .registerModeledException(
                        ExceptionMetadata.builder().errorCode("InvalidDomainValidationOptionsException")
                                .exceptionBuilderSupplier(InvalidDomainValidationOptionsException::builder).httpStatusCode(400)
                                .build())
                .registerModeledException(
                        ExceptionMetadata.builder().errorCode("LimitExceededException")
                                .exceptionBuilderSupplier(LimitExceededException::builder).httpStatusCode(400).build());
    }

    private static List<MetricPublisher> resolveMetricPublishers(SdkClientConfiguration clientConfiguration,
            RequestOverrideConfiguration requestOverrideConfiguration) {
        List<MetricPublisher> publishers = null;
        if (requestOverrideConfiguration != null) {
            publishers = requestOverrideConfiguration.metricPublishers();
        }
        if (publishers == null || publishers.isEmpty()) {
            publishers = clientConfiguration.option(SdkClientOption.METRIC_PUBLISHERS);
        }
        if (publishers == null) {
            publishers = Collections.emptyList();
        }
        return publishers;
    }

    private <T extends AcmRequest> T applyPaginatorUserAgent(T request) {
        Consumer<AwsRequestOverrideConfiguration.Builder> userAgentApplier = b -> b.addApiName(ApiName.builder()
                .version(VersionInfo.SDK_VERSION).name("PAGINATED").build());
        AwsRequestOverrideConfiguration overrideConfiguration = request.overrideConfiguration()
                .map(c -> c.toBuilder().applyMutation(userAgentApplier).build())
                .orElse((AwsRequestOverrideConfiguration.builder().applyMutation(userAgentApplier).build()));
        return (T) request.toBuilder().overrideConfiguration(overrideConfiguration).build();
    }

    private HttpResponseHandler<AwsServiceException> createErrorResponseHandler(BaseAwsJsonProtocolFactory protocolFactory,
            JsonOperationMetadata operationMetadata) {
        return protocolFactory.createErrorResponseHandler(operationMetadata);
    }

    @Override
    public void close() {
        clientHandler.close();
    }
}
