package com.studhub.app.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Report
import com.studhub.app.domain.repository.ReportRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.random.Random

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ReportRepositoryImplTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var reportRepo: ReportRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun setAndGetSameReport() {
        lateinit var report: Report

        runBlocking {

            val reportProduct = Report(
                id = Random.nextLong().toString(),
                reportedItemId = "Product ${Random.nextLong()}",
                description = "First Report Test"
            )

            reportRepo.createReport(reportProduct).collect {
                when (it) {
                    is ApiResponse.Success -> report = it.data
                    is ApiResponse.Failure -> Assert.fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }

        runBlocking {
            reportRepo.getReportsForItem(report.reportedItemId).collect {
                when (it) {
                    is ApiResponse.Success -> assert(it.data == listOf(report))
                    is ApiResponse.Failure -> Assert.fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }
    }

    @Test
    fun setAndGetReportsForSameItem() {
        lateinit var report1: Report
        lateinit var report2: Report

        runBlocking {

            val reportProduct1 = Report(
                id = Random.nextLong().toString(),
                reportedItemId = "Product with many reports",
                description = "First Report Test"
            )

            val reportProduct2 = Report(
                id = Random.nextLong().toString(),
                reportedItemId = "Product with many reports",
                description = "First Report Test"
            )

            reportRepo.createReport(reportProduct1).collect {
                when (it) {
                    is ApiResponse.Success -> report1 = it.data
                    is ApiResponse.Failure -> Assert.fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }

            reportRepo.createReport(reportProduct2).collect {
                when (it) {
                    is ApiResponse.Success -> report2 = it.data
                    is ApiResponse.Failure -> Assert.fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }

        runBlocking {
            reportRepo.getReportsForItem(report1.reportedItemId).collect {
                when (it) {
                    is ApiResponse.Success -> assert(it.data == listOf(report1, report2))
                    is ApiResponse.Failure -> Assert.fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }
    }

    @Test
    fun addAndRemoveReport() {
        val reportProduct = Report(
            id = Random.nextLong().toString(),
            reportedItemId = "Product ${Random.nextLong()}",
            description = "First Report Test"
        )

        runBlocking {
            reportRepo.createReport(reportProduct).collect() {
                when (it) {
                    is ApiResponse.Failure -> Assert.fail(it.message)
                    ApiResponse.Loading -> {}
                    is ApiResponse.Success -> {}
                }
            }
        }

        runBlocking {
            reportRepo.deleteReportsForItem(reportProduct.reportedItemId).collect {
                when (it) {
                    is ApiResponse.Failure -> Assert.fail(it.message)
                    is ApiResponse.Loading -> {}
                    is ApiResponse.Success -> {}
                }
            }
        }
    }
}
