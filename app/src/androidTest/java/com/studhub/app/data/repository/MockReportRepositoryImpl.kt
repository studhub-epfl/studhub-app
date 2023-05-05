package com.studhub.app.data.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Report
import com.studhub.app.domain.repository.ReportRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockReportRepositoryImpl : ReportRepository {
    private val reportDB = HashMap<String, Report>()

    override suspend fun createReport(report: Report): Flow<ApiResponse<Report>> {
        return flow {
            emit(ApiResponse.Loading)
            reportDB[report.id] = report
            emit(ApiResponse.Success(report))
        }
    }

    override suspend fun getReportsForItem(itemId: String): Flow<ApiResponse<List<Report>>> {
        return flow {
            emit(ApiResponse.Loading)
            emit(ApiResponse.Success(reportDB.values.filter { k -> k.reportedItemId == itemId }))
        }
    }

    override suspend fun deleteReport(reportId: String): Flow<ApiResponse<Boolean>> {
        return flow {
            emit(ApiResponse.Loading)
            reportDB.remove(reportId)
            emit(ApiResponse.Success(true))
        }
    }
}
