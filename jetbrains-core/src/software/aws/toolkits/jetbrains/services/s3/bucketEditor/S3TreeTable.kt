// Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: Apache-2.0
package software.aws.toolkits.jetbrains.services.s3.bucketEditor

import com.intellij.ide.dnd.DnDAware
import com.intellij.openapi.application.runInEdt
import com.intellij.ui.treeStructure.treetable.TreeTable
import java.awt.Point
import java.awt.event.MouseEvent
import javax.swing.JComponent

class S3TreeTable(private val treeTableModel: S3TreeTableModel) : TreeTable(treeTableModel), DnDAware {
    fun refresh() {
        runInEdt {
            clearSelection()
            val structureTreeModel = treeTableModel.structureTreeModel
            structureTreeModel.invalidate()
        }
    }

    override fun isOverSelection(point: Point?): Boolean = true

    override fun dropSelectionButUnderPoint(point: Point?) {
        dropSelectionButUnderPoint(point)
    }

    override fun getComponent(): JComponent = this

    override fun processMouseEvent(e: MouseEvent?) {
        super.processMouseEvent(e)
    }
}
