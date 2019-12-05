// Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: Apache-2.0
package software.aws.toolkits.jetbrains.services.s3.bucketEditor

import com.intellij.ui.treeStructure.treetable.TreeTableModel
import com.intellij.util.ui.ColumnInfo
import software.aws.toolkits.jetbrains.services.s3.S3VirtualObject
import software.aws.toolkits.resources.message
import javax.swing.tree.DefaultMutableTreeNode

class S3TreeTableColumn(private val columnType: ColumnType, private val valueGetter: (S3VirtualObject) -> String?) :
    ColumnInfo<Any, String>(message(columnType.message)) {
    override fun valueOf(obj: Any): String? {
        val userObject = (obj as? DefaultMutableTreeNode)?.userObject
        val file = (userObject as? S3KeyNode)?.virtualFile

        return if (file is S3VirtualObject) {
            valueGetter.invoke(file)
        } else if (columnType == ColumnType.NAME && file?.isDirectory == true) {
            file.name
        } else {
            ""
        }
    }

    override fun isCellEditable(item: Any?): Boolean = true

    override fun getColumnClass(): Class<*> = if (columnType == ColumnType.NAME) {
        TreeTableModel::class.java
    } else {
        super.getColumnClass()
    }

    enum class ColumnType(val message: String) {
        NAME("s3.name"),
        SIZE("s3.size"),
        LAST_MODIFIED("s3.last_modified")
    }
}
