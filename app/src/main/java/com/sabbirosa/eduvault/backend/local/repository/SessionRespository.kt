package com.sabbirosa.eduvault.backend.local.repository

import com.sabbirosa.eduvault.backend.local.models.Session
import com.sabbirosa.eduvault.backend.models.UserData
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.delete
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val realm: Realm
) {
    suspend fun createSession(data: UserData) {
        realm.write {
            val sessionData = Session().apply {
                userId = data.userId
                fullName = data.fullName
                email = data.email
                studentId = data.studentId
                department = data.department
            }
            copyToRealm(sessionData, updatePolicy = UpdatePolicy.ALL)
        }
    }

    suspend fun deleteSession(id: ObjectId) {
        realm.write {
            val sessionObject = query<Session>("_id == $0", id).first().find()
            if (sessionObject != null) {
                delete(sessionObject)
            }
        }
    }

    suspend fun deleteAllSession() {
        realm.write {
            // Query to find all Session objects
            val sessionObjects = query<Session>().find()

            // Check if the result is not empty and then delete all the sessions
            if (sessionObjects.isNotEmpty()) {
                delete(sessionObjects)
            }
        }
    }

    fun getAllSession(): Flow<List<Session>> {
        return realm.query<Session>().asFlow().map { results ->
            results.list.toList()
        }
    }
}