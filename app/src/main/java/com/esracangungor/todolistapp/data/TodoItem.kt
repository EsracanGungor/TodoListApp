package com.esracangungor.todolistapp.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todoItems")
data class TodoItem(
    @ColumnInfo(name = "title") val todoTitle: String?,
    @ColumnInfo(name = "description") val todoDescription: String?,
    @ColumnInfo(name = "timestamp") val timeStamp: String?,
    @ColumnInfo(name = "category") val todoCategory: String?
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
        id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(todoTitle)
        parcel.writeString(todoDescription)
        parcel.writeString(timeStamp)
        parcel.writeString(todoCategory)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TodoItem> {
        override fun createFromParcel(parcel: Parcel): TodoItem {
            return TodoItem(parcel)
        }

        override fun newArray(size: Int): Array<TodoItem?> {
            return arrayOfNulls(size)
        }
    }

}