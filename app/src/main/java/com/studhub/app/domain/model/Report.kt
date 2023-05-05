package com.studhub.app.domain.model

import java.util.*

data class Report(
    var id: String = "",
    val reporterUserId: String = "",
    val reportedItemId: String = "",
    val reportType: ReportType = ReportType.CONTENT,
    val description: String = "",
    val createdAt: Date = Date(),
    val status: ReportStatus = ReportStatus.PENDING
)

enum class ReportType {
    CONTENT, // For listing purposes
    BEHAVIOR // Fo messaging purposes
}

enum class ReportStatus {
    PENDING,
    IN_PROGRESS,
    RESOLVED
}
