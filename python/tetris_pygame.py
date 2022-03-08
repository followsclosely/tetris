import pygame

from tetris import Grid, Piece, PieceFactory, Block
"""All GUI related code (pygame) is in this module"""

grid = Grid(10, 20)
piece_factory = PieceFactory(10)

# GLOBALS VARS
window_width = 600
window_height = 700

#Each block is 30x30 in size
block_size = 30

# play area is 10 blocks wide
play_width = grid.width * 30  

# play area is 20 blocks high
play_height = grid.height * 30 

top_left_x = (window_width - play_width) // 2
top_left_y = window_height - play_height

pygame.font.init()

def draw_text_centered(surface, lines, size : int, color):
    font = pygame.font.SysFont("comicsans", size, bold=True)

    center_height =  window_height/2 - (size * len(lines))
    for text in lines:
        label = font.render(text, 1, color)
        surface.blit(label, (window_width / 2 - (label.get_width()/2), center_height))
        center_height += label.get_height() + 5


def draw_hold(surface, hold : Piece):
    """Draw all the piece in the queue"""
    font = pygame.font.SysFont('comicsans', 26)
    label = font.render('Hold', 1, (255, 255, 255))
    surface.blit(label, (top_left_x + 15 + (0.5+grid.width)*block_size, top_left_y - 20 ))

    if piece_factory.hold:
        draw_piece(surface, hold, top_left_x + 10 + (grid.width)*block_size, top_left_y + 30, block_size)

def draw_stats(surface, score, level, lines, total_lines):
    font = pygame.font.SysFont('comicsans', 20)
    label = font.render('Score', 1, (255, 255, 255))
    surface.blit(label, (top_left_x + 15 + (0.5+grid.width)*block_size, top_left_y + 100 ))

    font = pygame.font.SysFont('comicsans', 14)
    label = font.render("{:,}".format(score), 1, (255, 255, 255))
    surface.blit(label, (top_left_x + 15 + (0.5+grid.width)*block_size, top_left_y + 130 ))

    font = pygame.font.SysFont('comicsans', 20)
    label = font.render('Level', 1, (255, 255, 255))
    surface.blit(label, (top_left_x + 15 + (0.5+grid.width)*block_size, top_left_y + 170 ))

    font = pygame.font.SysFont('comicsans', 14)
    label = font.render("{:,}".format(level), 1, (255, 255, 255))
    surface.blit(label, (top_left_x + 15 + (0.5+grid.width)*block_size, top_left_y + 200 ))

    font = pygame.font.SysFont('comicsans', 20)
    label = font.render('Lines', 1, (255, 255, 255))
    surface.blit(label, (top_left_x + 15 + (0.5+grid.width)*block_size, top_left_y + 240 ))

    font = pygame.font.SysFont('comicsans', 14)
    label = font.render("{:,}".format(total_lines), 1, (255, 255, 255))
    surface.blit(label, (top_left_x + 15 + (0.5+grid.width)*block_size, top_left_y + 270 ))

    for i in range(len(lines)):
        label = font.render("{}x {:,}".format(i+1, lines[i]), 1, (255, 255, 255))
        surface.blit(label, (top_left_x + 25 + (0.5+grid.width)*block_size, top_left_y + 270 + (label.get_height()*(i+1)) ))


def draw_next_pieces(surface, queue):
    """Draw all the piece in the queue"""

    draw_piece(surface, queue[0], top_left_x - block_size*4.5, top_left_y + 30, block_size)

    next_block_size = 15
    font = pygame.font.SysFont('comicsans', 26)
    label = font.render('Next', 1, (255, 255, 255))
    surface.blit(label, (top_left_x - next_block_size*6.5, top_left_y - 20 ))

    for i in range(1, len(queue)):
        next = queue[i]
        draw_piece(surface, next, top_left_x - next_block_size*5 , top_left_y + block_size + (next_block_size*2*i) + (i*10) + 30, next_block_size)


def draw_grid(surface, grid : Grid):
    sx = top_left_x
    sy = top_left_y

    pygame.draw.line(surface, (128, 128, 128), (top_left_x + 0*block_size, top_left_y),(top_left_x + 0*block_size, top_left_y + play_height))
    pygame.draw.line(surface, (128, 128, 128), (top_left_x + 10*block_size, top_left_y),(top_left_x + 10*block_size, top_left_y + play_height))


def draw_window(surface, grid : Grid, score, level, lines, total_lines):
    surface.fill((0, 0, 0))

    font = pygame.font.SysFont('comicsans', 60)
    label = font.render('Tetris', 1, (255, 255, 255))
    surface.blit(label, (window_width / 2 - (label.get_width() / 2), 10))

    draw_hold(surface, piece_factory.hold)
    draw_next_pieces(surface, piece_factory.queue)
    draw_grid(surface, grid)
    draw_stats(surface, score, level, lines, total_lines)

    for y in range(len(grid.blocks)):
        for x in range(len(grid.blocks[y])):
            b = grid.blocks[y][x]
            if( b != None ):
                draw_block(surface, x, y, top_left_x, top_left_y, block_size, b.color, b.shadow)

    draw_piece(surface, current, top_left_x, top_left_y, block_size)
        

def draw_piece(surface, piece : Piece, deltax, deltay, block_size):
    for b in piece.coordinates:
        draw_block(surface, b.x, b.y, deltax, deltay, block_size, piece.block.color, piece.block.shadow)

def draw_block(surface, x, y, deltax, deltay, block_size, color, shadow):
    border = round(block_size/11)
    pygame.draw.rect(surface, color, (1 + deltax + x*block_size, 1+ deltay + y*block_size, block_size, block_size), 0)
    pygame.draw.rect(surface, shadow, (border + deltax + x*block_size, border + deltay + y*block_size, block_size-border-2, block_size-border-2), 0)

def main_menu(win):
    run = True
    while run:
        win.fill((0,0,0))
        draw_text_centered(win, ['Press', 'Any Key', 'To Play'], 50, (255,255,255))
        pygame.display.update()
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                run = False
            if event.type == pygame.KEYDOWN:
                main(win)

    pygame.display.quit()

score = 0
level = 0
last_level_up = 0
lines = [0,0,0,0]
total_lines = 0

def main(win):
    global current, total_lines, score, level, last_level_up

    current = piece_factory.get()
    current.translate(3,3, grid)

    game_run = True
    
    clock = pygame.time.Clock()

    draw_window(win, grid, score, level, lines, total_lines)
    pygame.display.update()

    clock = pygame.time.Clock()
    drop_time = 0
    drop_time_threshhold = 800

    while game_run:
        repaint = False

        for event in pygame.event.get():
            repaint = True
            if event.type == pygame.QUIT:
                game_run = False
            if event.type == pygame.KEYDOWN:
                print(event)
                if event.key == pygame.K_LEFT:
                    current.translate(-1,0, grid)
                if event.key == pygame.K_RIGHT:
                    current.translate(1,0, grid)
                if event.key == pygame.K_w:
                    current.translate(0,-1, grid)
                if event.key == pygame.K_s:
                    current.translate(0,1, grid)
                if event.key == pygame.K_h:
                    current = piece_factory.swap_hold(current)
                    current.translate(3,3, grid)
                if event.key == pygame.K_UP:
                    current.rotate(1, grid)
                if event.key == pygame.K_DOWN:
                    current.rotate(-1, grid)
                if event.key == pygame.K_SPACE:
                    while current.translate(0, 1, grid):
                        pass
                    drop_time = drop_time_threshhold - 250

        drop_time += clock.get_rawtime()
        clock.tick()

        if drop_time > drop_time_threshhold:
            repaint = True
            drop_time = 0
            if (current.translate(0,1, grid)):
                pass
            else:
                grid.add_piece(current)
                rows = grid.clear_rows()
                if( rows > 0):
                    lines[rows-1] += 1
                    total_lines += rows

                    #https://tetris.fandom.com/wiki/Scoring
                    line_bonus = [40, 100, 300, 1200]
                    score += (line_bonus[rows-1] * (level+1))

                    if total_lines >= last_level_up + ((level+1) * 10):
                        level += 1
                        last_level_up = total_lines%10 * 10
                        if drop_time_threshhold >= 200:
                            drop_time_threshhold -= 60


                current = piece_factory.get()
                current.translate(3,3, grid)

        if repaint:
            draw_window(win, grid, score, level, lines, total_lines)
            pygame.display.update()


win = pygame.display.set_mode((window_width, window_height))
pygame.display.set_caption('Tetris')
main_menu(win)