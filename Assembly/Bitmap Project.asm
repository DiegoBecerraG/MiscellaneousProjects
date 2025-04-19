# width of screen in pixels
# 256 / 16 = 16
.eqv 	WIDTH 16
# height of screen in pixels
# 512 / 16 = 32
.eqv 	HEIGHT 32

# Tetris board is 10x20

# Each "block" will be 16x16 pixels

# Colors; $s2 - Holds color value
.eqv	YELLOW 	0x00FFFF00
.eqv	PURPLE	0x00800080
.eqv	CYAN	0x0000FFFF
.eqv	WHITE	0x00FFFFFF
.eqv	BLACK	0X00000000
.eqv	BLUE	0X000000FF
.eqv	GREEN	0X0000FF00

.text	
main:	li	$s0, 6	# Starting Y Value for blocks
	li	$s1, 0	# Left Offset
	li	$s2, 0	# Right Offset
	li	$s5, 0	# Down offset
# Border
	# Sets up starting location and border color
	addi	$a2, $0, WHITE
	addi	$a0, $0, 2
	addi	$a1, $0, 5
	
topB:	beq	$t0, 12, topBE	# Top Border
	jal	draw_pixel
	addi	$a0, $a0, 1
	addi	$t0, $t0, 1
	j	topB
	
topBE: 	addi	$t0, $0, 0	# Resets loop counter
	addi	$a0, $a0, -1	# sets x-value to correct place

rightB:	beq	$t0, 21, rightBE # Right Border
	addi	$a1, $a1, 1
	jal	draw_pixel
	addi	$t0, $t0, 1
	j	rightB
	
rightBE:
	addi	$t0, $0, 0
	
botB:	beq	$t0, 11, botBE	# Bottom Border
	addi	$a0, $a0, -1
	jal	draw_pixel
	addi	$t0, $t0, 1
	j	botB
	
botBE:	addi	$t0, $0, 0
	
leftB:	beq	$t0, 20, leftBE	# Left Border
	addi	$a1, $a1, -1
	jal	draw_pixel
	addi	$t0, $t0, 1
	j	leftB
	
leftBE:	addi	$t0, $0, 0

gameLoop: 
	# Chooses what block to spawn
	li 	$v0, 42
	li 	$a1, 5
    	syscall
    	
    	add	$s4, $0, $a0	# $s4 - Stores what block is being used in this loop
    	
    	# Starting dimmensions
    	addi	$a0, $0, WIDTH
	sra	$a0, $a0, 1
	add	$a1, $0, $s0
	
    	beq	$s4, 0, jtSquare
	beq	$s4, 1, jtLine
	beq	$s4, 2, jtT
	beq	$s4, 3, jtL
	beq	$s4, 4, jtS
	
jtSquare:	# Jump to Square
	jal	draw_square
	j	input
	
jtSquareBL:	# Jump to square black left
	blt	$s1, 3, input
	addi	$a0, $a0, -1
	addi	$s1, $a0, 0
	jal	bSquare
	j	left2
	
jtSquareBR:	# Jump to square black right
	addi	$s2, $a0, 1
	bge	$s2, 13, input
	addi	$a0, $a0, -1
	jal	bSquare
	j	right2
	
jtSquareBD:	# Jump to square black down
	addi	$s5, $a1, 2
	addi	$a0, $a0, -1
	jal	bSquare
	j	down2
	
jtLine:	# Jump to line
	jal	draw_line
	j	input
	
jtLineBL:
	ble	$s1, 3, input
	addi	$a0, $a0, -1
	addi	$s1, $a0, 0
	jal	bLine
	j	left2
	
jtLineBR:
	addi	$s2, $a0, 1
	bge	$s2, 12, input
	addi	$a0, $a0, -1
	jal	bLine
	j	right2
	
jtLineBD:
	addi	$s5, $a1, 1
	addi	$a0, $a0, -1
	jal	bLine
	j	down2

jtT:	# Jump to T
	jal	draw_t
	j	input
	
jtTBL:
	blt	$s1, 4, input
	addi	$a0, $a0, -1
	addi	$s1, $a0, -1
	jal	bT
	j	left2
	
jtTBR:
	addi	$s2, $a0, 1
	bge	$s2, 13, input
	addi	$a0, $a0, -1
	jal	bT
	j	right2
	
jtTBD:
	addi	$s5, $a1, 2
	add	$s2, $0, WIDTH
	addi	$a0, $a0, -1
	jal	bT
	j	down2
	
jtL: 
	jal	draw_l
	j	input
	
jtLL:
	addi	$s1, $a0, 0
	blt	$s1, 5, input
	addi	$a0, $a0, -1
	jal	bL
	j	left2
	
jtLR:
	addi	$s2, $a0, 1
	bge	$s2, 12, input
	addi	$a0, $a0, -1
	jal	bL
	j	right2
	
jtLD: 
	addi	$s5, $a1, 2
	addi	$a0, $a0, -1
	jal	bL
	j	down2
	
jtS:
	jal	draw_s
	j	input
	
jtSL:
	addi	$s1, $a0, 1
	blt	$s1, 5, input
	jal	bS
	j	left2
	
jtSR:
	addi	$s2, $a0, 1
	bge	$s2, 11, input
	jal	bS
	j	right2
	
jtSD: 
	addi	$s5, $a1, 2
	jal	bS
	j	down2
	
	# check for input
input:	lw 	$t0, 0xffff0000  # t1 holds if input available   Loads words
    	beq 	$t0, 0, input     # If no input(0), keep displaying(loop)
    	
	
	# $s3- process input
	lw 	$s3, 0xffff0004
	beq	$s3, 0, gravity
	beq	$s3, 32, exit	# input space (If 32, exit the code)
	beq	$s3, 115, down 	# input s
	beq	$s3, 97, left  	# input a
	beq	$s3, 100, right	# input d
	# invalid input, ignore
	b	input
	
gravity:
	jal	pause
	j	down
	
left:	# Blacks out current shape
	beq	$s4, 0, jtSquareBL
	beq	$s4, 1, jtLineBL
	beq	$s4, 2, jtTBL
	beq	$s4, 3, jtLL
	beq	$s4, 4, jtSL
	
left2:	# Prints new shape one pixel to the left
	beq	$s4, 0, squareLeft
	beq	$s4, 1, lineLeft
	beq	$s4, 2, tLeft
	beq	$s4, 3, lLeft
	beq	$s4, 4, sLeft
	j	input
	
right:	# Blacks out current shape
	beq	$s4, 0, jtSquareBR
	beq	$s4, 1, jtLineBR
	beq	$s4, 2, jtTBR
	beq	$s4, 3, jtLR
	beq	$s4, 4, jtSR

right2:	# Prints new shape one pixel to the right
	beq	$s4, 0, squareRight
	beq	$s4, 1, lineRight
	beq	$s4, 2, tRight
	beq	$s4, 3, lRight
	beq	$s4, 4, sRight
	j	input
	
down:	# Blacks out current shape
	beq	$s4, 0, jtSquareBD
	beq	$s4, 1, jtLineBD
	beq	$s4, 2, jtTBD
	beq	$s4, 3, jtLD
	beq	$s4, 4, jtSD
	
down2:	# Prints new shape one pixel to the down
	beq	$s4, 0, squareDown
	beq	$s4, 1, lineDown
	beq	$s4, 2, tDown
	beq	$s4, 3, lDown
	beq	$s4, 4, sDown
	
squareLeft:	# Moves square left
	addi	$a0, $a0, -2
	subi	$s1, $a0, 1
	jal	draw_square
	j	input
	
squareRight:	# Moves square right
	addi	$s2, $a0, 1
	jal	draw_square
	j	input
	
squareDown:	# Moves square down
	addi	$a0, $a0, -1
	addi	$a1, $a1, 1
	jal	draw_square
	bge	$s5, 25, gameLoop
	j	input
	
lineLeft:
	addi	$a0, $a0, -2
	subi	$s1, $a0, 1
	jal	draw_line
	j	input
lineRight:
	addi	$s2, $a0, 1
	jal	draw_line
	j	input
lineDown:
	addi	$a0, $a0, -1
	addi	$a1, $a1, 1
	jal	draw_line
	bge	$s5, 25, gameLoop
	j	input

tLeft:
	addi	$a0, $a0, -2
	jal	draw_t
	subi	$s1, $a0, 2
	j	input
	
tRight:
	jal	draw_t
	addi	$s2, $a0, 2
	j	input
	
tDown:
	addi	$a0, $a0, -1
	addi	$a1, $a1, 1
	jal	draw_t
	bge	$s5, 25, gameLoop
	j	input
	
lLeft:
	addi	$a0, $a0, -2
	jal	draw_l
	subi	$s1, $a0, 1
	j	input
	
lRight:
	jal	draw_l
	j	input
	
lDown: 
	addi	$a1, $a1, 1
	addi	$a0, $a0, -1
	jal	draw_l
	bge	$s5, 25, gameLoop
	j	input
	
sLeft:
	addi	$a0, $a0, -1
	jal	draw_s
	subi	$s1, $a0, 1
	j	input

sRight:
	addi	$a0, $a0, 1
	jal	draw_s
	j	input

sDown:
	addi	$a1, $a1, 1
	jal	draw_s
	bge	$s5, 25, gameLoop
	j	input

exit:	li	$v0, 10
	syscall

#################################################
draw_square:	# Draw Square piece
	addi	$sp, $sp, -4
	sw	$ra, ($sp)
	
	addi	$t0, $0, 0
	subi	$s1, $a0, 1
	addi	$a2, $0, YELLOW
tsql:	beq	$t0, 2, tsqle	# Top Square loop
	jal	draw_pixel
	addi	$a0, $a0, 1
	addi	$t0, $t0, 1
	j	tsql
tsqle:	addi	$t0, $0, 0
	addi	$a0, $a0, -2
	addi	$a1, $a1, 1
bsql:	beq	$t0, 2, bsqle	# Bottom Square loop
	jal	draw_pixel
	addi	$a0, $a0, 1
	addi	$t0, $t0, 1
	j	bsql
bsqle:	addi	$t0, $0, 0
	
	addi	$a0, $a0, -1
	addi	$a1, $a1, -1
	
	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr	$ra

##############################################
draw_line:	# Draw line piece
	addi	$sp, $sp, -4
	sw	$ra, ($sp)

	addi	$t0, $0, 0
	addi	$a2, $0, CYAN
	subi	$s1, $a0, 1
	ble	$s1, 2, input
	addi	$a0, $a0, -1
lLoop:	beq	$t0, 4, lLoopE	# Line loop
	jal	draw_pixel
	addi	$a0, $a0, 1
	addi	$t0, $t0, 1
	j	lLoop
lLoopE:	addi	$t0, $0, 0
	addi	$a0, $a0, -2

	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr	$ra

################################################
draw_t:	# Draw T piece
	addi	$sp, $sp, -4
	sw	$ra, ($sp)
	
	addi	$a2, $0, PURPLE
	subi	$s1, $a0, 1
	jal	draw_pixel
	addi	$a0, $a0, -1
	addi	$a1, $a1, 1
	addi	$t0, $0, 0
btl:	beq	$t0, 3, btle	# Bottom T loop
	jal	draw_pixel
	addi	$a0, $a0, 1
	addi	$t0, $t0, 1
	j	btl
btle:	addi	$t0, $0, 0
	addi	$a0, $a0, -1
	addi	$a1, $a1, -1

	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr	$ra
	
#############################################
draw_l:	# Draw L piece
	addi	$sp, $sp, -4
	sw	$ra, ($sp)
	
	addi	$a2, $0, BLUE
	jal	draw_pixel
	addi	$a1, $a1, 1
	addi	$t0, $0, 0
bll:	beq	$t0, 3, blle	# L loop
	jal	draw_pixel
	addi	$a0, $a0, 1
	addi	$t0, $t0, 1
	j	bll
blle:	addi	$t0, $0, 0
	addi	$a0, $a0, -2
	addi	$a1, $a1, -1

	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr	$ra
	
################################################
draw_s:
	addi	$sp, $sp, -4
	sw	$ra, ($sp)
	
	addi	$a2, $0, GREEN
	addi	$t0, $0, 0
tsl:	beq	$t0, 2, tsle
	jal	draw_pixel
	addi	$a0, $a0, 1
	addi	$t0, $t0, 1
	j	tsl
tsle:	addi	$a1, $a1, 1
	addi	$a0, $a0, -1
	addi	$t0, $0, 0
bsl:	beq	$t0, 2, bsle
	jal	draw_pixel
	addi	$a0, $a0, 1
	addi	$t0, $t0, 1
	j	bsl
bsle:	addi	$t0, $0, 0
	addi	$a0, $a0, -3
	addi	$a1, $a1, -1
	
	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr	$ra
	
################################################
bSquare:	# Blacks out square
	addi	$sp, $sp, -4
	sw	$ra, ($sp)
	
	addi	$t0, $0, 0
	addi	$a2, $0, BLACK
tsqlb:	beq	$t0, 2, tsqleb	# Top Square loop
	jal	draw_pixel
	addi	$a0, $a0, 1
	addi	$t0, $t0, 1
	j	tsqlb
tsqleb:	addi	$t0, $0, 0
	addi	$a0, $a0, -2
	addi	$a1, $a1, 1
bsqlb:	beq	$t0, 2, bsqleb	# Bottom Square loop
	jal	draw_pixel
	addi	$a0, $a0, 1
	addi	$t0, $t0, 1
	j	bsqlb
bsqleb:	addi	$t0, $0, 0
	
	addi	$a0, $a0, -1
	addi	$a1, $a1, -1
	
	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr	$ra

#################################################
bLine:	# Blacks out line
	addi	$sp, $sp, -4
	sw	$ra, ($sp)

	addi	$t0, $0, 0
	addi	$a2, $0, BLACK
	ble	$s1, 2, input
	addi	$a0, $a0, -1
lLoopB:	beq	$t0, 4, lLoopEB	# Line loop
	jal	draw_pixel
	addi	$a0, $a0, 1
	addi	$t0, $t0, 1
	j	lLoopB
lLoopEB:	
	addi	$t0, $0, 0
	addi	$a0, $a0, -2

	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr	$ra

#################################################
bT:	# Blacks out T
	addi	$sp, $sp, -4
	sw	$ra, ($sp)
	
	addi	$a2, $0, BLACK
	jal	draw_pixel
	addi	$a0, $a0, -1
	addi	$a1, $a1, 1
	addi	$t0, $0, 0
btlB:	beq	$t0, 3, btleB	# Bottom T loop
	jal	draw_pixel
	addi	$a0, $a0, 1
	addi	$t0, $t0, 1
	j	btlB
btleB:	addi	$t0, $0, 0
	addi	$a0, $a0, -1
	addi	$a1, $a1, -1

	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr	$ra
	
#################################################
bL:	# Draw L piece
	addi	$sp, $sp, -4
	sw	$ra, ($sp)
	
	addi	$a2, $0, BLACK
	jal	draw_pixel
	addi	$a1, $a1, 1
	addi	$t0, $0, 0
bllb:	beq	$t0, 3, blleb	# L loop
	jal	draw_pixel
	addi	$a0, $a0, 1
	addi	$t0, $t0, 1
	j	bllb
blleb:	addi	$t0, $0, 0
	addi	$a0, $a0, -2
	addi	$a1, $a1, -1

	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr	$ra
	
################################################
bS:
	addi	$sp, $sp, -4
	sw	$ra, ($sp)
	
	addi	$a2, $0, BLACK
	addi	$t0, $0, 0
tslb:	beq	$t0, 2, tsleb
	jal	draw_pixel
	addi	$a0, $a0, 1
	addi	$t0, $t0, 1
	j	tslb
tsleb:	addi	$a1, $a1, 1
	addi	$a0, $a0, -1
	addi	$t0, $0, 0
bslb:	beq	$t0, 2, bsleb
	jal	draw_pixel
	addi	$a0, $a0, 1
	addi	$t0, $t0, 1
	j	bslb
bsleb:	addi	$t0, $0, 0
	addi	$a0, $a0, -3
	addi	$a1, $a1, -1
	
	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr	$ra
	
#################################################
pause: 
	# save $ra
	addi	$sp, $sp, -8
	sw	$ra, ($sp)
	sw	$a3, 4($sp)
	
	move	$t8, $a0
	li	$v0, 32
	li	$a0, 100
	syscall
	move	$a0, $t8
	
	# restore $ra
	lw	$ra, ($sp)
	lw	$a3, 4($sp)
	addi	$sp, $sp, 8
	jr	$ra

#################################################
# subroutine to draw a pixel
# $a0 = X
# $a1 = Y
# $a2 = color
draw_pixel:
	# s1 = address = $gp + 4*(x + y*width)
	addi	$sp, $sp, -4
	sw	$ra, ($sp)
	
	mul	$t9, $a1, WIDTH   # y * WIDTH
	add	$t9, $t9, $a0	  # add X
	mul	$t9, $t9, 4	  # multiply by 4 to get word offset
	add	$t9, $t9, $gp	  # add to base address
	sw	$a2, ($t9)	  # store color at memory location
	
	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr 	$ra
