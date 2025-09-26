#!/usr/bin/env python3
import sqlite3
import os
import random

def create_database():
    # Tạo thư mục assets nếu chưa có
    assets_dir = "app/src/main/assets/database"
    os.makedirs(assets_dir, exist_ok=True)
    
    # Xóa database cũ nếu có
    db_path = os.path.join(assets_dir, "snake_block_levels.db")
    if os.path.exists(db_path):
        os.remove(db_path)
        print(f"Removed old database: {db_path}")
    
    # Tạo database mới
    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()
    
    # Tạo bảng levels
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS levels (
            levelNumber INTEGER PRIMARY KEY,
            name TEXT NOT NULL,
            difficulty TEXT NOT NULL DEFAULT 'EASY',
            isUnlocked INTEGER NOT NULL DEFAULT 0,
            bestMoves INTEGER,
            isCompleted INTEGER NOT NULL DEFAULT 0
        )
    ''')
    
    # Tạo bảng level_cells
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS level_cells (
            levelNumber INTEGER,
            row INTEGER,
            col INTEGER,
            cellType TEXT NOT NULL,
            PRIMARY KEY (levelNumber, row, col),
            FOREIGN KEY (levelNumber) REFERENCES levels(levelNumber) ON DELETE CASCADE
        )
    ''')
    
    # Generate 100 levels
    for level_num in range(1, 101):
        difficulty = "EASY" if level_num <= 30 else "MEDIUM" if level_num <= 70 else "HARD"
        is_unlocked = 1 if level_num == 1 else 0
        
        # Insert level
        cursor.execute('''
            INSERT INTO levels (levelNumber, name, difficulty, isUnlocked, isCompleted)
            VALUES (?, ?, ?, ?, 0)
        ''', (level_num, f"Level {level_num}", difficulty, is_unlocked))
        
        # Generate level data
        level_data = generate_level(level_num)
        
        # Process level cells to avoid duplicates
        cell_positions = {}
        for row, col, cell_type in level_data:
            position_key = (row, col)
            if position_key not in cell_positions:
                cell_positions[position_key] = cell_type
            elif cell_type == 'SNAKE_HEAD':
                # SNAKE_HEAD takes priority over RED_BLOCK at same position
                cell_positions[position_key] = cell_type
        
        # Insert level cells
        for (row, col), cell_type in cell_positions.items():
            cursor.execute('''
                INSERT INTO level_cells (levelNumber, row, col, cellType)
                VALUES (?, ?, ?, ?)
            ''', (level_num, row, col, cell_type))
    
    conn.commit()
    conn.close()
    print(f"Database created successfully at: {db_path}")

def generate_level(level_num):
    """Generate level data based on level number"""
    random.seed(level_num)  # Consistent generation
    cells = []
    
    if level_num == 1:
        # Simple 2x2 square - snake head will be at same position as one red block
        cells = [(2, 2, 'RED_BLOCK'), (2, 3, 'RED_BLOCK'), 
                (3, 2, 'RED_BLOCK'), (3, 3, 'RED_BLOCK'),
                (2, 2, 'SNAKE_HEAD')]  # This will override the RED_BLOCK at (2,2)
    elif level_num == 2:
        # L-shape - snake head will be at same position as one red block
        cells = [(1, 1, 'RED_BLOCK'), (1, 2, 'RED_BLOCK'), (1, 3, 'RED_BLOCK'),
                (2, 1, 'RED_BLOCK'), (3, 1, 'RED_BLOCK'),
                (3, 1, 'SNAKE_HEAD')]  # This will override the RED_BLOCK at (3,1)
    elif level_num == 3:
        # Straight line - snake head will be at same position as one red block
        cells = [(4, 2, 'RED_BLOCK'), (4, 3, 'RED_BLOCK'), (4, 4, 'RED_BLOCK'),
                (4, 5, 'RED_BLOCK'), (4, 6, 'RED_BLOCK'),
                (4, 2, 'SNAKE_HEAD')]  # This will override the RED_BLOCK at (4,2)
    elif level_num <= 10:
        # Simple patterns
        cells = generate_simple_pattern(level_num)
    elif level_num <= 30:
        # Easy patterns
        cells = generate_easy_pattern(level_num)
    elif level_num <= 70:
        # Medium patterns
        cells = generate_medium_pattern(level_num)
    else:
        # Hard patterns
        cells = generate_hard_pattern(level_num)
    
    return cells

def generate_simple_pattern(level_num):
    """Generate simple patterns for levels 4-10"""
    patterns = {
        4: [(2, 2, 'RED_BLOCK'), (2, 3, 'RED_BLOCK'), (2, 4, 'RED_BLOCK'),
            (3, 3, 'RED_BLOCK'), (4, 3, 'RED_BLOCK'), (2, 3, 'SNAKE_HEAD')],  # T-shape
        5: [(3, 4, 'RED_BLOCK'), (4, 3, 'RED_BLOCK'), (4, 4, 'RED_BLOCK'),
            (4, 5, 'RED_BLOCK'), (5, 4, 'RED_BLOCK'), (4, 4, 'SNAKE_HEAD')],  # Plus
        6: [(2, 2, 'RED_BLOCK'), (2, 3, 'RED_BLOCK'), (2, 4, 'RED_BLOCK'),
            (3, 2, 'RED_BLOCK'), (3, 3, 'RED_BLOCK'), (3, 4, 'RED_BLOCK'),
            (2, 2, 'SNAKE_HEAD')],  # Rectangle
        7: [(1, 1, 'RED_BLOCK'), (2, 2, 'RED_BLOCK'), (3, 3, 'RED_BLOCK'),
            (4, 4, 'RED_BLOCK'), (5, 5, 'RED_BLOCK'), (1, 1, 'SNAKE_HEAD')],  # Diagonal
        8: [(2, 2, 'RED_BLOCK'), (3, 2, 'RED_BLOCK'), (4, 2, 'RED_BLOCK'),
            (4, 3, 'RED_BLOCK'), (4, 4, 'RED_BLOCK'), (3, 4, 'RED_BLOCK'),
            (2, 4, 'RED_BLOCK'), (2, 2, 'SNAKE_HEAD')],  # U-shape
        9: [(2, 2, 'RED_BLOCK'), (2, 3, 'RED_BLOCK'), (3, 3, 'RED_BLOCK'),
            (3, 4, 'RED_BLOCK'), (4, 4, 'RED_BLOCK'), (4, 5, 'RED_BLOCK'),
            (2, 2, 'SNAKE_HEAD')],  # Zigzag
        10: [(2, 4, 'RED_BLOCK'), (3, 4, 'RED_BLOCK'), (4, 2, 'RED_BLOCK'),
             (4, 3, 'RED_BLOCK'), (4, 4, 'RED_BLOCK'), (4, 5, 'RED_BLOCK'),
             (4, 6, 'RED_BLOCK'), (5, 4, 'RED_BLOCK'), (6, 4, 'RED_BLOCK'),
             (4, 4, 'SNAKE_HEAD')]  # Cross
    }
    return patterns.get(level_num, patterns[4])

def generate_easy_pattern(level_num):
    """Generate easy patterns for levels 11-30"""
    cells = []
    pattern_type = level_num % 4
    
    if pattern_type == 0:  # Rectangle
        width = 3 + (level_num % 3)
        height = 2 + (level_num % 2)
        start_row = 2
        start_col = 2
        
        for r in range(start_row, start_row + height):
            for c in range(start_col, start_col + width):
                cells.append((r, c, 'RED_BLOCK'))
        cells.append((start_row, start_col, 'SNAKE_HEAD'))
        
    elif pattern_type == 1:  # L-shape variations
        size = 3 + (level_num % 2)
        start_row = 2
        start_col = 2
        
        # Horizontal part
        for c in range(start_col, start_col + size):
            cells.append((start_row, c, 'RED_BLOCK'))
        # Vertical part
        for r in range(start_row + 1, start_row + size):
            cells.append((r, start_col, 'RED_BLOCK'))
        cells.append((start_row + size - 1, start_col, 'SNAKE_HEAD'))
        
    elif pattern_type == 2:  # Cross pattern
        center_row, center_col = 4, 4
        size = 1 + (level_num % 2)
        
        # Horizontal line
        for c in range(center_col - size, center_col + size + 1):
            cells.append((center_row, c, 'RED_BLOCK'))
        # Vertical line
        for r in range(center_row - size, center_row + size + 1):
            cells.append((r, center_col, 'RED_BLOCK'))
        cells.append((center_row, center_col, 'SNAKE_HEAD'))
        
    else:  # Spiral pattern
        cells = [(3, 3, 'RED_BLOCK'), (3, 4, 'RED_BLOCK'), (3, 5, 'RED_BLOCK'),
                (4, 5, 'RED_BLOCK'), (5, 5, 'RED_BLOCK'), (5, 4, 'RED_BLOCK'),
                (5, 3, 'RED_BLOCK'), (4, 3, 'RED_BLOCK'), (3, 3, 'SNAKE_HEAD')]
    
    return cells

def generate_medium_pattern(level_num):
    """Generate medium patterns for levels 31-70"""
    cells = []
    block_count = 8 + (level_num % 6)
    used_positions = set()
    
    # Create random blocks
    for _ in range(block_count):
        while True:
            row = random.randint(1, 7)
            col = random.randint(1, 7)
            if (row, col) not in used_positions:
                used_positions.add((row, col))
                cells.append((row, col, 'RED_BLOCK'))
                break
    
    # Add snake head at random position
    if cells:
        start_block = random.choice(cells)
        cells.append((start_block[0], start_block[1], 'SNAKE_HEAD'))
    
    return cells

def generate_hard_pattern(level_num):
    """Generate hard patterns for levels 71-100"""
    cells = []
    
    # Create multiple clusters
    cluster_count = 2 + (level_num % 3)
    used_positions = set()
    
    for cluster in range(cluster_count):
        center_row = random.randint(2, 6)
        center_col = random.randint(2, 6)
        cluster_size = 4 + (level_num % 4)
        
        for _ in range(cluster_size):
            row = center_row + random.randint(-2, 2)
            col = center_col + random.randint(-2, 2)
            
            if 0 <= row <= 8 and 0 <= col <= 8 and (row, col) not in used_positions:
                used_positions.add((row, col))
                cells.append((row, col, 'RED_BLOCK'))
    
    # Add snake head
    if cells:
        start_block = random.choice(cells)
        cells.append((start_block[0], start_block[1], 'SNAKE_HEAD'))
    
    return cells

if __name__ == "__main__":
    create_database()