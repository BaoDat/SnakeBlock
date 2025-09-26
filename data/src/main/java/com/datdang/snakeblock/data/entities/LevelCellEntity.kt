package com.datdang.snakeblock.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "level_cells",
    primaryKeys = ["levelNumber", "row", "col"],
    foreignKeys = [
        ForeignKey(
            entity = LevelEntity::class,
            parentColumns = ["levelNumber"],
            childColumns = ["levelNumber"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["levelNumber"])]
)
data class LevelCellEntity(
    val levelNumber: Int,
    val row: Int,
    val col: Int,
    val cellType: String // "RED_BLOCK", "SNAKE_HEAD"
)