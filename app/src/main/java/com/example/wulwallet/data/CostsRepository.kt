package com.example.wulwallet.data


import com.example.wulwallet.data.local.Idea
import com.example.wulwallet.data.local.IdeaDao
import kotlinx.coroutines.flow.Flow


class IdeaRepository(private val ideaDao: IdeaDao) {

    val allIdeas: Flow<List<Idea>> = ideaDao.getAllIdeas()

    suspend fun insertIdea(idea: Idea) {
        ideaDao.insertIdea(idea)
    }

    suspend fun updateIdea(idea: Idea) {
        ideaDao.updateIdea(idea)
    }

    suspend fun deleteIdea(idea: Idea) {
        ideaDao.deleteIdea(idea)
    }

    suspend fun getAllIdeasByUser(userId: Int): List<Idea> {
        return ideaDao.getIdeasByUser(userId)
    }
}