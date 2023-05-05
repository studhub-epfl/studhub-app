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
            reportRepo.getReportsForItem(report.id).collect {
                when (it) {
                    is ApiResponse.Success -> assert(it.data == listOf(report))
                    is ApiResponse.Failure -> Assert.fail(it.message)
                    is ApiResponse.Loading -> {}
                }
            }
        }
    }
}
