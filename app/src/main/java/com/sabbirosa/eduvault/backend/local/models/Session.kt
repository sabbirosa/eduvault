package com.sabbirosa.eduvault.backend.local.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId


class Session : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var userId: String = ""
    var fullName: String = ""
    var email: String = ""
    var studentId: String = ""
    var department: String = ""
}