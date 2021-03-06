// Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: Apache-2.0

package software.aws.toolkits.jetbrains.services.clouddebug.execution.steps

import software.amazon.awssdk.services.toolkittelemetry.model.Unit
import software.aws.toolkits.jetbrains.services.clouddebug.CloudDebugConstants.INSTRUMENTED_STATUS
import software.aws.toolkits.jetbrains.services.clouddebug.CloudDebugConstants.INSTRUMENT_IAM_ROLE_KEY
import software.aws.toolkits.jetbrains.services.clouddebug.execution.Context
import software.aws.toolkits.jetbrains.services.clouddebug.execution.MessageEmitter
import software.aws.toolkits.jetbrains.services.clouddebug.execution.Step
import software.aws.toolkits.jetbrains.services.clouddebug.resources.CloudDebuggingResources
import software.aws.toolkits.jetbrains.services.ecs.execution.EcsServiceCloudDebuggingRunSettings
import software.aws.toolkits.jetbrains.services.telemetry.TelemetryConstants
import software.aws.toolkits.jetbrains.services.telemetry.TelemetryConstants.TelemetryResult
import software.aws.toolkits.jetbrains.services.telemetry.TelemetryService
import software.aws.toolkits.resources.message
import java.time.Duration
import java.time.Instant

// TODO when doing the correct service call remove project since we don't need it anymore
class RetrieveRole(private val settings: EcsServiceCloudDebuggingRunSettings) : Step() {
    override val stepName = message("cloud_debug.step.retrieve_execution_role")

    override fun execute(
        context: Context,
        messageEmitter: MessageEmitter,
        ignoreCancellation: Boolean
    ) {
        val startTime = Instant.now()
        var result = TelemetryResult.Succeeded
        try {
            val description = CloudDebuggingResources.describeInstrumentedResource(context.project, settings.clusterArn, settings.serviceArn)
            if (description == null || description.status != INSTRUMENTED_STATUS || description.taskRole.isEmpty()) {
                throw RuntimeException("Resource somehow became de-instrumented?")
            }

            context.putAttribute(INSTRUMENT_IAM_ROLE_KEY, description.taskRole)
        } catch (e: Exception) {
            result = TelemetryResult.Failed
            throw RuntimeException(message("cloud_debug.step.retrieve_execution_role.failed"), e)
        } finally {
            TelemetryService.getInstance().record(context.project) {
                datum("${TelemetryConstants.CLOUDDEBUG_TELEMETRY_PREFIX}_retrieverole") {
                    createTime(startTime)
                    metadata(TelemetryConstants.CLOUDDEBUG_WORKFLOWTOKEN, context.workflowToken)
                    metadata(TelemetryConstants.RESULT, result.name)
                    unit(Unit.MILLISECONDS)
                    value(Duration.between(startTime, Instant.now()).toMillis().toDouble())
                }
            }
        }
    }
}
