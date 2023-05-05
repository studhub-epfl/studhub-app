package com.studhub.app.data.repository

import com.google.android.gms.measurement.sdk.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
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
        val reportToPush = report.copy()
        return flow {
            emit(ApiResponse.Loading)
            val query = db.child(report.reportedItemId).child(report.id).setValue(reportToPush)
            query.await()
            if (query.isSuccessful) {
                reportToPush.id = query.key.orEmpty()
                emit(ApiResponse.Success(reportToPush))
            } else {
                val errorMessage = query.exception?.message.orEmpty()
                emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
            }
        }
    }

    override suspend fun getReportsForItem(itemId: String): Flow<ApiResponse<List<Report>>> = flow {
        emit(ApiResponse.Loading)
        val query = db.child(itemId).get()

        query.await()

        if (query.isSuccessful) {
            val reports = mutableListOf<Report>()

            for (reportsSnapshot in query.result.children) {
                val retrievedReport: Report? = reportsSnapshot.getValue(Report::class.java)
                if (retrievedReport != null) {
                    reports.add(retrievedReport)
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

        // remove the old value on the database
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
