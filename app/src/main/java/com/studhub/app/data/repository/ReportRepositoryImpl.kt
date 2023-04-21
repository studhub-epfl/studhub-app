package com.studhub.app.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Report
import com.studhub.app.domain.repository.ReportRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Singleton
class ReportRepositoryImpl : ReportRepository {
    private val db = Firebase.database.getReference("reports")

    override suspend fun createReport(report: Report): Flow<ApiResponse<Report>> {
        val reportId: String = db.push().key.orEmpty()
        val reportToPush: Report = report.copy(id = reportId)

        return flow {
            emit(ApiResponse.Loading)

            val query = db.child(reportId).setValue(reportToPush)

            query.await()

            if (query.isSuccessful) {
                emit(ApiResponse.Success(reportToPush))
            } else {
                val errorMessage = query.exception?.message.orEmpty()
                emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
            }
        }
    }

    override suspend fun getReportsForItem(itemId: String): Flow<ApiResponse<List<Report>>> = flow {
        emit(ApiResponse.Loading)
        val query = db.orderByChild("reportedItemId").equalTo(itemId).get()

        query.await()

        if (query.isSuccessful) {
            val reports = mutableListOf<Report>()

            query.result.children.forEach { snapshot ->
                val report = snapshot.getValue(Report::class.java)
                if (report != null) {
                    reports.add(report)
                }
            }

            emit(ApiResponse.Success(reports))
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
        }
    }

    override suspend fun deleteReport(reportId: String): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)

        val query = db.child(reportId).removeValue()

        query.await()

        if (query.isSuccessful) {
            emit(ApiResponse.Success(true))
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
        }
    }

}
