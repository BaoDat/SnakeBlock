package com.datdang.snakeblock.domain.model

enum class CellType {
    EMPTY,
    RED_BLOCK, // Mục tiêu
    SNAKE_HEAD,
    SNAKE_BODY // Các ô đã đi qua
    // OBSTACLE, // Có thể thêm sau này
}