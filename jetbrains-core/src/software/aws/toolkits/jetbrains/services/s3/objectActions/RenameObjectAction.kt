// Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: Apache-2.0
package software.aws.toolkits.jetbrains.services.s3.objectActions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.vfs.VirtualFile
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CopyObjectRequest
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.aws.toolkits.jetbrains.components.telemetry.ActionButtonWrapper
import software.aws.toolkits.jetbrains.services.s3.S3VirtualBucket
import software.aws.toolkits.jetbrains.services.s3.S3VirtualDirectory
import software.aws.toolkits.resources.message
import javax.swing.JTable

class RenameObjectAction(private var table: JTable, val bucket: S3VirtualBucket) :
    ActionButtonWrapper(message("s3.rename.object.action"), null, null) {

    @Suppress("unused")
    override fun doActionPerformed(e: AnActionEvent) {
        val project = e.getRequiredData(LangDataKeys.PROJECT)
        val client: S3Client = bucket.client
        val row = table.selectedRow
        // val path = treeTable.getPathForRow(treeTable.convertRowIndexToModel(row))
        val file = "fix/me"
        //val node = (path.lastPathComponent as DefaultMutableTreeNode).userObject as S3KeyNode
        //val file = node.virtualFile
/*
        val response = Messages.showInputDialog(project,
            message("s3.rename.object.title", file.name),
            message("s3.rename.object.action"),
            null,
            file.name,
            object : InputValidator {
                override fun checkInput(inputString: String?): Boolean = true

                override fun canClose(inputString: String?): Boolean = checkInput(inputString)
            }
        )
        if (response != null) {
            ApplicationManager.getApplication().executeOnPooledThread {
                try {
                    renameObjectAction(response, file, client)
                    treeTable.refresh()
                } catch (e: Exception) {
                    e.notifyError(message("s3.rename.object.failed"))
                }
            }
        }*/
    }

    override fun isEnabled(): Boolean = !(table.rowCount == 0 || (table.selectedRow < 0) ||
        (table.getValueAt(table.selectedRow, 1) == "") || (table.selectedRows.size > 1))

    fun renameObjectAction(response: String, file: VirtualFile, client: S3Client) {
        val bucketName = bucket.getVirtualBucketName()
        val copySource: String
        val copyDestination: String
        if (file.parent is S3VirtualDirectory) {
            copySource = "${file.parent.name}/${file.name}"
            copyDestination = "${file.parent.name}/$response"
        } else {
            copySource = file.name
            copyDestination = response
        }
        val copyObjectRequest: CopyObjectRequest =
            when (file.name.contains("/")) {
                true -> CopyObjectRequest.builder()
                    .copySource("$bucketName/$copySource")
                    .bucket(bucketName)
                    .key(copyDestination)
                    .build()

                false -> CopyObjectRequest.builder()
                    .copySource("$bucketName/$copySource")
                    .bucket(bucketName)
                    .key(copyDestination)
                    .build()
            }
        client.copyObject(copyObjectRequest)

        val deleteObjectRequest = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(copySource)
            .build()
        client.deleteObject(deleteObjectRequest)
    }
}
