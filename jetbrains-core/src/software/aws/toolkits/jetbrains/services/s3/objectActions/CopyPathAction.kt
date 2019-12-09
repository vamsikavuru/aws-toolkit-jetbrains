// Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: Apache-2.0

package software.aws.toolkits.jetbrains.services.s3.objectActions

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ide.CopyPasteManager
import software.aws.toolkits.jetbrains.components.telemetry.ActionButtonWrapper
import software.aws.toolkits.jetbrains.services.s3.S3VirtualBucket
import software.aws.toolkits.resources.message
import java.awt.datatransfer.StringSelection
import javax.swing.JTable

class CopyPathAction(
    private var treeTable: JTable,
    val bucket: S3VirtualBucket
) : ActionButtonWrapper(message("s3.copy.path"), null, AllIcons.Actions.Copy) {
    override fun isEnabled(): Boolean = treeTable.selectedRows.size == 1

    override fun doActionPerformed(e: AnActionEvent) {/*
        treeTable.getSelectedAsVirtualFiles().firstOrNull()?.let {
            CopyPasteManager.getInstance().setContents(StringSelection(it.path))*/
        }
    }
