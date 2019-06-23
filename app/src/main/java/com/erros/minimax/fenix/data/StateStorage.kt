package com.erros.minimax.fenix.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import com.erros.minimax.fenix.view.base.State
import com.erros.minimax.fenix.view.utils.toByteArray
import com.erros.minimax.fenix.view.utils.toParcelable
import java.util.concurrent.Executors

interface StateStorage {
    fun <S> setStateForId(id: Int, state: S) where S : State
    fun <S, C> getStateForId(id: Int, parcelableCreator: C, callback: (S?) -> Unit) where S : State, C : Parcelable.Creator<S>
}

class SQLiteStateStorage(
        context: Context
) : StateStorage {

    private companion object {
        const val TABLE_STATES = "states"
        const val FIELD_ID = "id"
        const val FIELD_STATE_ID = "stateId"
        const val FIELD_STATE_VALUE = "stateValue"
    }

    private val sqLiteOpenHelper = StatesSQLiteOpenHelper(context)
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    override fun <S : State> setStateForId(id: Int, state: S) {
        executor.execute {
            val byteArray = state.toByteArray()
            val byteArraySize = byteArray.size
            if (byteArraySize != 0) {
                val db = sqLiteOpenHelper.writableDatabase
                val byteArrayMaxSize = 512 * 1024
                val byteListsToSave = Array(byteArraySize / byteArrayMaxSize + 1) { byteArrayOf() }
                for (index in 0 until byteListsToSave.size) {
                    val startIndex = index * byteArrayMaxSize
                    val endIndex = startIndex + byteArrayMaxSize
                    if (endIndex > byteArraySize) {
                        byteListsToSave[index] = byteArray.sliceArray(startIndex until byteArraySize)
                    } else {
                        byteListsToSave[index] = byteArray.sliceArray(startIndex until endIndex)
                    }
                }
                byteListsToSave.forEach {
                    val row = ContentValues()
                    row.put(FIELD_STATE_ID, id)
                    row.put(FIELD_STATE_VALUE, it)
                    db.insert(TABLE_STATES, null, row)
                }
            }
        }
    }

    override fun <S : State, C : Parcelable.Creator<S>> getStateForId(id: Int, parcelableCreator: C, callback: (S?) -> Unit) {
        executor.execute {
            val db = sqLiteOpenHelper.readableDatabase
            val cursor =
                    db.rawQuery("select $FIELD_STATE_VALUE from $TABLE_STATES where $FIELD_STATE_ID = $id order by $FIELD_ID", null)
            if (cursor.moveToFirst()
                    && !cursor.isNull(0)) {

                val byteLists = mutableListOf<Byte>()
                do {
                    val byteArray = cursor.getBlob(0)
                    byteLists.addAll(byteArray.toTypedArray())
                } while (cursor.moveToNext())
                cursor.close()
                val state = byteLists.toByteArray().toParcelable(parcelableCreator)
                handler.post {
                    callback(state)
                }
            } else {
                cursor.close()
                handler.post {
                    callback(null)
                }
            }
        }

    }

    private class StatesSQLiteOpenHelper(
            context: Context
    ) : SQLiteOpenHelper(context, "savedStates.db", null, 1) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                    StringBuilder().append("create table ")
                            .append(TABLE_STATES)
                            .append(" (").append(FIELD_ID).append(" integer primary key autoincrement, ")
                            .append(FIELD_STATE_ID).append(" integer, ")
                            .append(FIELD_STATE_VALUE).append(" blob)")
                            .toString()
            )
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        }
    }

}