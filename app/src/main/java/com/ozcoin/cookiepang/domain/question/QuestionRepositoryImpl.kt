package com.ozcoin.cookiepang.domain.question

import com.ozcoin.cookiepang.data.ask.AskRemoteDataSource
import com.ozcoin.cookiepang.data.ask.toDomain
import com.ozcoin.cookiepang.data.request.NetworkResult
import com.ozcoin.cookiepang.domain.ask.AskStatusType
import com.ozcoin.cookiepang.domain.ask.toQuestion
import com.ozcoin.cookiepang.utils.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.streams.toList

class QuestionRepositoryImpl @Inject constructor(
    private val askRemoteDataSource: AskRemoteDataSource
) : QuestionRepository {

    override suspend fun getQuestionList(userId: String): DataResult<List<Question>> =
        withContext(Dispatchers.IO) {
            val response = askRemoteDataSource.getAskList(userId)
            if (response is NetworkResult.Success) {
                val list = response.response.stream()
                    .map { it.toDomain() }
                    .filter { it.status != AskStatusType.DELETED }
                    .map { it.toQuestion() }
                    .toList()
                DataResult.OnSuccess(list)
            } else {
                DataResult.OnFail
            }
        }

    override suspend fun ignoreQuestion(question: Question): Boolean = withContext(Dispatchers.IO) {
        delay(500L)
        true
    }
}
