package com.datdang.snakeblock.domain.model

data class GameState(
    val board: Array<Array<CellType>>,
    val snakePath: List<Coordinate>,
    val totalRedBlocks: Int,
    val redBlocksFilled: Int,
    val isGameWon: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameState

        if (!board.contentDeepEquals(other.board)) return false
        if (snakePath != other.snakePath) return false
        if (totalRedBlocks != other.totalRedBlocks) return false
        if (redBlocksFilled != other.redBlocksFilled) return false
        if (isGameWon != other.isGameWon) return false

        return true
    }

    override fun hashCode(): Int {
        var result = board.contentDeepHashCode()
        result = 31 * result + snakePath.hashCode()
        result = 31 * result + totalRedBlocks
        result = 31 * result + redBlocksFilled
        result = 31 * result + isGameWon.hashCode()
        return result
    }
}