import random
"""The core tetris classes are in this module"""

class Grid:
    """"A Grid is a two dimentional array of blocks (ints for now)."""
    def __init__(self, width, height):
        self.width = width
        self.height = height
        self.blocks = [[None for _ in range(width)] for _ in range(height)]
    
    def add_piece(self, piece):
        #Populate the grid with blocks from this piece
        for c in piece.coordinates:
            self.blocks[c.y][c.x] = piece.index

    def is_row_completed(self, y):
        return not(None in self.blocks[y])

    def clear_rows(self):
        row_count = 0
        row_counts = [0 for col in range(self.height)]

        for y in reversed(range(self.height)):
            row_counts[y] = row_count
            if not(None in self.blocks[y]):
                row_count += 1

        for y in reversed(range(self.height)):
            if row_counts[y] > 0:
                #print('[Row({y})={count}]'.format(y=y, count=row_counts[y]))
                for x in range(self.width):
                    self.blocks[y+row_counts[y]][x] = self.blocks[y][x]
                        
        if row_counts[0] > 0:
            for x in range(self.width):
                self.blocks[y][x] = None

        return row_count

class Coordinate:
    """This class holds an x,y coordinate. This is added for only read-ability as I don't like the tuple accessor"""
    def __init__(self, x, y):
        self.x = x
        self.y = y
    def __str__(self):
        return '[{c.x},{c.y}]'.format(c=self)

class Piece:
    """A Piece is a combination of blocks that can be transformed and rotated """
    def __init__(self, index, coordinates, rotation):
        self.index = index
        self.coordinates = coordinates
        self.new_coordinates = [Coordinate(0,0) for i in range(len(coordinates))] 
        self.rotation = rotation
        
    def rotate(self, direction, grid):
        """Performs a simple rotation on the Piece. If the value in the grid is not None then it will be checked for room to rotate the piece."""
        if self.rotation == -1:
            return

        centroid = self.coordinates[0]
        for i in range(1, len(self.coordinates)):
            self.new_coordinates[i].x = self.coordinates[i].y + (centroid.x - centroid.y)
            self.new_coordinates[i].y = centroid.x + (centroid.y - self.coordinates[i].x)

        return self.__verify_and_commit(grid, True)

    def translate(self, dx, dy, grid, validate = True):
        """Performs a simple translation on the Piece. If the grid is not None then it will be checked for room to move the piece."""

        for i in range(len(self.coordinates)):
            self.new_coordinates[i].x = self.coordinates[i].x + dx
            self.new_coordinates[i].y = self.coordinates[i].y + dy

        return self.__verify_and_commit(grid, validate)
    
    def __verify_and_commit(self, grid, validate):
        """Verify that the translated block is still within the bounds and all the blocks in the grid are empty (None)"""

        if validate:
            for i in range(len(self.coordinates)):
                if( self.new_coordinates[i].y >= 0 and 
                    ( not(0 <= self.new_coordinates[i].x < grid.width) 
                        or not(0 <= self.new_coordinates[i].y < grid.height)
                        or not(grid.blocks[self.new_coordinates[i].y][self.new_coordinates[i].x] == None)
                    )):
                    return False

        """Commit this move by moving the values from new_coordinates to the coordinates"""
        for i in range(len(self.coordinates)):
            self.coordinates[i].x = self.new_coordinates[i].x
            self.coordinates[i].y = self.new_coordinates[i].y
        return True

class PieceFactory:
    """This class generates pieces and places them in the queue for consumption."""
    
    def __init__(self, queue_size):
        self.hold : Piece = None
        self.queue = []
        for i in range(queue_size):
            self.queue.append(self.__random_piece())

    def get(self):
        p = self.queue.pop(0)
        self.queue.append(self.__random_piece())
        return p
    
    def swap_hold(self, piece):
        if(self.hold):
            current = self.hold
            self.hold = self.__piece(piece.index)
            return current
        else :
            self.hold = self.__piece(piece.index)
            return self.get()

    def __random_piece(self):
        return self.__piece(random.randint(1, 7))

    def __piece(self, i : int):
        if i == 1:
            # ☒☒☒☐
            # ☐☒☐☐
            return Piece(i, [Coordinate(1,0), Coordinate(0,0), Coordinate(2,0), Coordinate(1,1)], 0)
        if i == 2:
            # ☐☒☒☐
            # ☐☒☒☐
            return Piece(i, [Coordinate(1,0), Coordinate(1,1), Coordinate(2,0), Coordinate(2,1)], -1)
        if i == 3:
            # ☒☐☐☐
            # ☒☒☒☐
            return Piece(i, [Coordinate(1,1), Coordinate(0,1), Coordinate(2,1), Coordinate(0,0)], 0)
        if i == 4:
            # ☐☐☐☒
            # ☐☒☒☒
            return Piece(i, [Coordinate(2,1), Coordinate(1,1), Coordinate(3,1), Coordinate(3,0)], 0)
        if i == 5:
            # ☒☒☐☐
            # ☐☒☒☐
            return Piece(i, [Coordinate(1,0), Coordinate(0,0),Coordinate(1,1),Coordinate(2,1)], 0)
        if i == 6:
            # ☐☐☒☒
            # ☐☒☒☐
            return Piece(i, [Coordinate(2,0), Coordinate(3,0), Coordinate(1,1), Coordinate(2,1)], 0)
        else:
            # ☒☒☒☒
            # ☐☐☐☐
            return Piece(i, [Coordinate(1,0), Coordinate(0,0), Coordinate(2,0), Coordinate(3,0)], 0)