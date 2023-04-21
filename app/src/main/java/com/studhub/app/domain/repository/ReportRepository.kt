package com.studhub.app.domain.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Report
import kotlinx.coroutines.flow.Flow

interface ReportRepository {

    /**
     * create a [report] on the database of Firebase
     * @param [report] the report we want to create
     * @return A [Flow] of [ApiResponse] with the last one containing the [report] pushed to the database on success
     */
    suspend fun createReport(report: Report): Flow<ApiResponse<Report>>

    /**
     * get a list of [reports] with all the reports on the database of Firebase filtered by [itemId]
     * @param [itemId] the item that lets us filter the reports
     * @return A [Flow] of [ApiResponse] with the last one containing the list of [report] pushed to the database on success
     */
    suspend fun getReportsForItem(itemId: String): Flow<ApiResponse<List<Report>>>

    /**
     * remove a report with the given [reportId]
     * @param [reportId] the reportId we want to match
     * @return A [Flow] of [ApiResponse] with the last one containing the [Boolean] value of the resulting operation
     */
    suspend fun deleteReport(reportId: String): Flow<ApiResponse<Boolean>>
}
