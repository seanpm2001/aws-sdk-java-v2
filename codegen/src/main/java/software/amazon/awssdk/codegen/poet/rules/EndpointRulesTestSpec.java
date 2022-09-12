/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.codegen.poet.rules;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.jr.stree.JrsBoolean;
import com.fasterxml.jackson.jr.stree.JrsString;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import java.net.URI;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.lang.model.element.Modifier;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import software.amazon.awssdk.codegen.model.intermediate.IntermediateModel;
import software.amazon.awssdk.codegen.model.intermediate.MemberModel;
import software.amazon.awssdk.codegen.model.intermediate.OperationModel;
import software.amazon.awssdk.codegen.model.intermediate.ParameterHttpMapping;
import software.amazon.awssdk.codegen.model.intermediate.ShapeModel;
import software.amazon.awssdk.codegen.model.rules.endpoints.EndpointTestModel;
import software.amazon.awssdk.codegen.model.rules.endpoints.EndpointTestSuiteModel;
import software.amazon.awssdk.codegen.model.rules.endpoints.ExpectModel;
import software.amazon.awssdk.codegen.model.rules.endpoints.ParameterModel;
import software.amazon.awssdk.codegen.model.service.Location;
import software.amazon.awssdk.codegen.poet.ClassSpec;
import software.amazon.awssdk.codegen.poet.PoetExtension;
import software.amazon.awssdk.codegen.poet.PoetUtils;
import software.amazon.awssdk.core.rules.testing.AsyncTestCase;
import software.amazon.awssdk.core.rules.testing.BaseRuleSetTest;
import software.amazon.awssdk.core.rules.testing.SyncTestCase;
import software.amazon.awssdk.core.rules.testing.model.Endpoint;
import software.amazon.awssdk.core.rules.testing.model.Expect;
import software.amazon.awssdk.core.rules.testing.util.EmptyPublisher;
import software.amazon.awssdk.regions.Region;

public class EndpointRulesTestSpec implements ClassSpec {
    private final IntermediateModel model;
    private final EndpointRulesSpecUtils endpointRulesSpecUtils;
    private final PoetExtension poetExtension;

    public EndpointRulesTestSpec(IntermediateModel model) {
        this.model = model;
        this.endpointRulesSpecUtils = new EndpointRulesSpecUtils(model);
        this.poetExtension = new PoetExtension(model);
    }

    @Override
    public TypeSpec poetSpec() {
        TypeSpec.Builder b = PoetUtils.createClassBuilder(className())
                                      .addModifiers(Modifier.PUBLIC)
                                      .superclass(BaseRuleSetTest.class);

        if (hasSyncClient()) {
            b.addMethod(syncTest());
        }
        b.addMethod(asyncTest());

        if (hasSyncClient()) {
            b.addMethod(syncTestsSourceMethod());
        }
        b.addMethod(asyncTestsSourceMethod());

        return b.build();
    }

    @Override
    public ClassName className() {
        return endpointRulesSpecUtils.endpointTestsName();
    }

    private String findDefaultRequest() {
        Map<String, OperationModel> operations = this.model.getOperations();

        // Ideally look for something that we don't need to set any parameters
        // on. That means either a request with no members or one that does not
        // have any members bound to the URI path
        Optional<String> name = operations.entrySet().stream()
                                          .filter(e -> canBeEmpty(e.getValue()))
                                          .findFirst()
                                          .map(Map.Entry::getKey);

        // Failing that, just pick the first one and deal with the path
        // params...
        return name.orElseGet(() -> operations.keySet().stream().findFirst().get());
    }

    private MethodSpec syncTest() {
        AnnotationSpec methodSourceSpec = AnnotationSpec.builder(MethodSource.class)
                                                        .addMember("value", "$S", "syncTestCases")
                                                        .build();

        MethodSpec.Builder b = MethodSpec.methodBuilder("syncClient_usesCorrectEndpoint")
                                         .addModifiers(Modifier.PUBLIC)
                                         .addParameter(SyncTestCase.class, "tc")
                                         .addAnnotation(methodSourceSpec)
                                         .addAnnotation(ParameterizedTest.class)
                                         .returns(void.class);

        b.addStatement("runAndVerify(tc)");

        return b.build();

    }

    private MethodSpec asyncTest() {
        AnnotationSpec methodSourceSpec = AnnotationSpec.builder(MethodSource.class)
                                                        .addMember("value", "$S", "asyncTestCases")
                                                        .build();

        MethodSpec.Builder b = MethodSpec.methodBuilder("asyncClient_usesCorrectEndpoint")
                                         .addModifiers(Modifier.PUBLIC)
                                         .addParameter(AsyncTestCase.class, "tc")
                                         .addAnnotation(methodSourceSpec)
                                         .addAnnotation(ParameterizedTest.class)
                                         .returns(void.class);

        b.addStatement("runAndVerify(tc)");

        return b.build();

    }

    private MethodSpec syncTestsSourceMethod() {
        String opName = findDefaultRequest();
        OperationModel opModel = model.getOperation(opName);

        MethodSpec.Builder b = MethodSpec.methodBuilder("syncTestCases")
                                         .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                                         .returns(ParameterizedTypeName.get(List.class, SyncTestCase.class));

        b.addCode("return $T.asList(", Arrays.class);

        EndpointTestSuiteModel endpointTestSuiteModel = model.getEndpointTestSuiteModel();
        Iterator<EndpointTestModel> testIter = endpointTestSuiteModel.getTestCases().iterator();

        while (testIter.hasNext()) {
            EndpointTestModel test = testIter.next();

            b.addCode("new $T($S, $L, $L)",
                      SyncTestCase.class,
                      test.getDocumentation(),
                      syncOperationCallLambda(opModel, test.getParams()),
                      createExpect(test.getExpect()));

            if (testIter.hasNext()) {
                b.addCode(",");
            }
        }

        b.addStatement(")");

        return b.build();
    }

    private CodeBlock syncOperationCallLambda(OperationModel opModel, Map<String, TreeNode> params) {
        CodeBlock.Builder b = CodeBlock.builder();

        b.beginControlFlow("() -> ");
        b.addStatement("$T builder = $T.builder()", syncClientBuilder(), syncClientClass());
        b.addStatement("builder.credentialsProvider($T.CREDENTIALS_PROVIDER)", BaseRuleSetTest.class);
        b.addStatement("builder.httpClient(getSyncHttpClient())");

        b.add(setClientParams("builder", params));

        b.addStatement("$T request = $L",
                       poetExtension.getModelClass(opModel.getInputShape().getShapeName()),
                       requestCreation(opModel));

        b.addStatement("builder.build().$N(request)", opModel.getMethodName());

        b.endControlFlow();

        return b.build();
    }

    private CodeBlock asyncOperationCallLambda(OperationModel opModel, Map<String, TreeNode> params) {
        CodeBlock.Builder b = CodeBlock.builder();

        b.beginControlFlow("() -> ");
        b.addStatement("$T builder = $T.builder()", asyncClientBuilder(), asyncClientClass());
        b.addStatement("builder.credentialsProvider($T.CREDENTIALS_PROVIDER)", BaseRuleSetTest.class);
        b.addStatement("builder.httpClient(getAsyncHttpClient())");

        b.add(setClientParams("builder", params));

        b.addStatement("$T request = $L",
                       poetExtension.getModelClass(opModel.getInputShape().getShapeName()),
                       requestCreation(opModel));

        CodeBlock asyncInvoke = asyncOperationInvocation(opModel);
        b.addStatement("return builder.build().$L", asyncInvoke);

        b.endControlFlow();

        return b.build();
    }

    private CodeBlock asyncOperationInvocation(OperationModel opModel) {
        CodeBlock.Builder b = CodeBlock.builder();

        b.add("$N(", opModel.getMethodName());

        b.add("$N", "request");

        if (opModel.hasEventStreamInput()) {
            b.add(", new $T()", EmptyPublisher.class);
            b.add(", $T.mock($T.class)", Mockito.class, poetExtension.eventStreamResponseHandlerType(opModel));
        }

        b.add(")");
        return b.build();
    }

    private MethodSpec asyncTestsSourceMethod() {
        String opName = findDefaultRequest();
        OperationModel opModel = model.getOperation(opName);

        MethodSpec.Builder b = MethodSpec.methodBuilder("asyncTestCases")
                                         .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                                         .returns(ParameterizedTypeName.get(List.class, AsyncTestCase.class));

        b.addCode("return $T.asList(", Arrays.class);

        EndpointTestSuiteModel endpointTestSuiteModel = model.getEndpointTestSuiteModel();
        Iterator<EndpointTestModel> testIter = endpointTestSuiteModel.getTestCases().iterator();

        while (testIter.hasNext()) {
            EndpointTestModel test = testIter.next();

            b.addCode("new $T($S, $L, $L)",
                      AsyncTestCase.class,
                      test.getDocumentation(),
                      asyncOperationCallLambda(opModel, test.getParams()),
                      createExpect(test.getExpect()));

            if (testIter.hasNext()) {
                b.addCode(",");
            }
        }

        b.addStatement(")");

        return b.build();
    }

    private CodeBlock requestCreation(OperationModel opModel) {
        CodeBlock.Builder b = CodeBlock.builder();

        ShapeModel inputModel = opModel.getInputShape();
        b.add("$T.builder()", poetExtension.getModelClass(inputModel.getShapeName()));

        if (canBeEmpty(opModel)) {
            return b.add(".build()").build();
        }

        ShapeModel inputShape = opModel.getInputShape();
        inputShape.getMembers().forEach(m -> {
            if (!boundToPath(m)) {
                return;
            }

            b.add(".$N(", m.getFluentSetterMethodName());
            switch (m.getVariable().getSimpleType()) {
                case "Boolean":
                    b.add("true");
                    break;
                case "String":
                    b.add("$S", "aws");
                    break;
                case "Long":
                case "Integer":
                    b.add("1");
                    break;
                default:
                    throw new RuntimeException("Don't know how to set member: "
                                               + opModel.getOperationName() + "#" + m.getName()
                                               + " with type " + m.getVariable().getSimpleType());
            }
            b.add(")");
        });


        b.add(".build()");
        return b.build();
    }

    private static boolean canBeEmpty(OperationModel opModel) {
        List<MemberModel> members = opModel.getInputShape().getMembers();

        if (members == null || members.isEmpty()) {
            return true;
        }


        Optional<MemberModel> pathMemberOrStreaming = members.stream()
                                                             .filter(m -> boundToPath(m) || isStreaming(m))
                                                             .findFirst();

        return !pathMemberOrStreaming.isPresent();
    }

    private static boolean boundToPath(MemberModel member) {
        ParameterHttpMapping http = member.getHttp();

        if (http == null) {
            return false;
        }

        return http.getLocation() == Location.URI;
    }

    private static boolean isStreaming(MemberModel member) {
        ParameterHttpMapping http = member.getHttp();

        if (http == null) {
            return false;
        }

        return http.getIsStreaming();
    }

    private ClassName syncClientClass() {
        return poetExtension.getClientClass(model.getMetadata().getSyncInterface());
    }

    private ClassName syncClientBuilder() {
        return poetExtension.getClientClass(model.getMetadata().getSyncBuilderInterface());
    }

    private ClassName asyncClientClass() {
        return poetExtension.getClientClass(model.getMetadata().getAsyncInterface());
    }

    private ClassName asyncClientBuilder() {
        return poetExtension.getClientClass(model.getMetadata().getAsyncBuilderInterface());
    }

    private CodeBlock setClientParams(String builderName, Map<String, TreeNode> params) {
        CodeBlock.Builder b = CodeBlock.builder();
        params.forEach((n, v) -> {
            if (!isClientParam(n)) {
                return;
            }

            ParameterModel model = param(n);

            switch (model.getBuiltInEnum()) {
                case AWS_REGION:
                    b.addStatement("$N.region($T.of($S))", builderName, Region.class, ((JrsString) v).getValue());
                    break;
                case AWS_USE_DUAL_STACK:
                    b.addStatement("$N.dualstackEnabled($L)", builderName, ((JrsBoolean) v).booleanValue());
                    break;
                case AWS_USE_FIPS:
                    b.addStatement("$N.fipsEnabled($L)", builderName, ((JrsBoolean) v).booleanValue());
                    break;
                case SDK_ENDPOINT:
                    b.addStatement("$N.endpointOverride($T.create($S))", builderName, URI.class, ((JrsString) v).getValue());
                    break;
                default:
                    break;
            }

        });
        return b.build();
    }

    private CodeBlock createExpect(ExpectModel expect) {
        CodeBlock.Builder b = CodeBlock.builder();

        b.add("$T.builder()", Expect.class);

        if (expect.getError() != null) {
            b.add(".error($S)", expect.getError());
        } else {
            CodeBlock.Builder endpointBuilder = CodeBlock.builder();

            ExpectModel.Endpoint endpoint = expect.getEndpoint();

            endpointBuilder.add("$T.builder()", Endpoint.class);
            endpointBuilder.add(".url($T.create($S))", URI.class, endpoint.getUrl());
            endpointBuilder.add(".build()");

            b.add(".endpoint($L)", endpointBuilder.build());
        }

        b.add(".build()");

        return b.build();
    }

    private boolean isClientParam(String name) {
        ParameterModel param = param(name);

        if (param == null) {
            return false;
        }

        boolean isBuiltIn = param.getBuiltInEnum() != null;

        // TODO: handle client params
        return isBuiltIn;
    }

    private ParameterModel param(String name) {
        return model.getEndpointRuleSetModel().getParameters().get(name);
    }

    private boolean hasSyncClient() {
        return model.getOperations()
                    .values()
                    .stream()
                    .anyMatch(o -> !(o.hasEventStreamOutput() || o.hasEventStreamInput()));
    }
}
