package com.thoriq.flog.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp

@Entity(tableName = "fishes")
@Parcelize
data class Fish(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "nama")
    var nama: String?,

    @ColumnInfo(name = "berat")
    var berat: Double?,

    @ColumnInfo(name = "harga")
    var harga: Double?,

    @ColumnInfo(name = "latitude")
    var latitude: Double?,

    @ColumnInfo(name = "longitude")
    var longitude: Double?,

    @ColumnInfo(name = "created_at")
    var createdAt: String
) : Parcelable
