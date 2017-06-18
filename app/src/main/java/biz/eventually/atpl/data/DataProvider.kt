package biz.eventually.atpl.data

import biz.eventually.atpl.data.model.Source
import biz.eventually.atpl.data.model.Subject
import biz.eventually.atpl.data.model.Topic
import biz.eventually.atpl.data.network.Question
import biz.eventually.atpl.data.service.SourceService
import io.reactivex.Observable

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by thibault on 21/03/17.
 */
@Singleton
class DataProvider @Inject constructor(private val sourceService: SourceService) {

    val token = "b5055e03-5ea6-445d-b3ae-dda8ef1e1b24"

    fun dataGetSources() : Observable<List<Source>?>? {
        return sourceService.loadSources().map { api -> toAppSources(api.data) }
    }

    fun dataGetSubjects(sourceId: Int) : Observable<List<Subject>?>? {
        return sourceService.loadSubjects(sourceId, token).map { api -> toAppSubjects(api.data) }
    }

    fun dataGetTopicQuestions(topicId: Int) : Observable<Topic>? {
        return sourceService.loadQuestions(topicId, token).map { response ->
            response.data?.let(::toAppTopic)
        }
    }

    fun updateFollow(questionId: Int, good: Boolean) : Observable<Question>? {
        val isGood = if (good) 1 else 0
        return sourceService.updateFollow(questionId, isGood , token).map { response ->
            response.data?.let(::toAppQuestion)
        }
    }

    fun updateFocus(questionId: Int, care: Boolean) : Observable<Int>? {
        val doCare = if (care) 1 else 0
        return sourceService.updateFocus(questionId, doCare , token).map { response ->
            response.data?.let({ it.focus?.let { if (it) 1 else 0 } }) ?: -1
        }
    }
}
