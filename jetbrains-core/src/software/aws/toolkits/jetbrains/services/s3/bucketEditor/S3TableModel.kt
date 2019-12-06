// Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: Apache-2.0

package software.aws.toolkits.jetbrains.services.s3.bucketEditor

import software.aws.toolkits.resources.message
import javax.swing.table.DefaultTableModel

class S3TableModel() : DefaultTableModel(S3ColumnType.values().map { it.message }.toTypedArray(), 1) {
    override fun isCellEditable(row: Int, column: Int) = false
}

enum class S3ColumnType(val message: String) {
    NAME(message("s3.name")),
    SIZE(message("s3.size")),
    LAST_MODIFIED(message("s3.last_modified"))
}

