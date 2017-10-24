package com.list_sample.todosample.model

import io.realm.RealmObject

/**
 * Created by monkey on 2017/10/24.
 */
open class TodoModel(
        open var todo: String = ""
): RealmObject() {}