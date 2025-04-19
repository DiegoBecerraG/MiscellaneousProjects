# width of screen in pixels
# 256 / 4 = 64
.eqv 	WIDTH 64
# height of screen in pixels
.eqv 	HEIGHT 64

# Set the Bitmap display to 4x4, 256x256, 0x10008000($gp) Base address

# colors
.eqv	RED 	0x00FF0000
.eqv	GREEN 	0x0000FF00
.eqv	BLUE	0x000000FF
.eqv	WHITE 	0x00FFFFFF
.eqv	YELLOW 	0x00FFFF00
.eqv	CYAN	0x0000FFFF
.eqv 	MAGENTA	0x00FF00FF

.data
colors:	.word	RED, CYAN, YELLOW, WHITE, BLUE, GREEN, MAGENTA

.text
 main:
	# draw a pixel in the center of the screen
	addi 	$a0, $0, WIDTH    # a0 = X = WIDTH/2
	sra 	$a0, $a0, 1
	addi 	$a1, $0, HEIGHT   # a1 = Y = HEIGHT/2
	sra 	$a1, $a1, 1
	
mloop:	
	# Main loop for creating the square
one:	bge	$t0, 7, o1
	jal	color_change
	la 	$a2, ($s3)
	jal	draw_pixel
	addi	$t0, $t0, 1
	addi	$a0, $a0, 1
	jal	pause
	j	one
	
o1:	addi	$t0, $0, 0
	jal	reset
	
two:	bge	$t1, 7, o2
	jal	color_change
	la 	$a2, ($s3)
	jal	draw_pixel
	addi	$t1, $t1, 1
	addi	$a1, $a1, 1
	jal	pause
	j	two
	
o2:	addi	$t1, $0, 0
	jal	reset
	
three:	bge	$t2, 7, o3
	jal	color_change
	la 	$a2, ($s3)
	jal	draw_pixel
	addi	$t2, $t2, 1
	addi	$a0, $a0, -1
	jal	pause
	j	three

o3:	addi	$t2, $0, 0
	jal 	reset

four:	bge	$t3, 7, o4
	jal	color_change
	la 	$a2, ($s3)
	jal	draw_pixel
	addi	$t3, $t3, 1
	addi	$a1, $a1, -1
	jal	pause
	j	four
	
o4:	addi	$t3, $0, 0
	jal	reset
	
keyLoop:
	# check for input
	lw 	$t0, 0xffff0000  #t1 holds if input available
    	beq 	$t0, 0, keyLoop   #If no input, keep displaying
	
	# process input
	lw 	$s1, 0xffff0004
	beq	$s1, 32, exit	# input space
	beq	$s1, 119, up 	# input w
	beq	$s1, 115, down 	# input s
	beq	$s1, 97, left  	# input a
	beq	$s1, 100, right	# input d
	# invalid input, ignore
	j	keyLoop
	
	# process valid input
up:	li	$a2, 0		# black out the pixel
	jal	redraw
	addi	$a1, $a1, -1
	beq	$a1, $a1, mloop

down:	li	$a2, 0		# black out the pixel
	jal	draw_pixel
	addi	$a1, $a1, 1
	beq	$a1, $a1, mloop
	
left:	li	$a2, 0		# black out the pixel
	jal	draw_pixel
	addi	$a0, $a0, -1
	beq	$a0, $a0, mloop
	
right:	li	$a2, 0		# black out the pixel
	jal	draw_pixel
	addi	$a0, $a0, 1
	beq	$a0, $a0, mloop
	
exit:	li	$v0, 10
	syscall

#################################################
# Changes the color array to the next avaliable one
color_change:
	blt	$a3, 28, r
	addi	$a3, $0, 0
r:	lw	$s3, colors($a3)
	addi	$a3, $a3, 4
	jr 	$ra

#################################################
# subroutine to wait a little time
# $a3 = amount to pause
pause: 
	# save $ra
	addi	$sp, $sp, -8
	sw	$ra, ($sp)
	sw	$a3, 4($sp)
	
	move	$t8, $a0
	li	$v0, 32
	li	$a0, 5
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
	mul	$t9, $a1, WIDTH   # y * WIDTH
	add	$t9, $t9, $a0	  # add X
	mul	$t9, $t9, 4	  # multiply by 4 to get word offset
	add	$t9, $t9, $gp	  # add to base address
	sw	$a2, ($t9)	  # store color at memory location
	jr 	$ra

############################################
# Resets the color array back to the beggining
reset:	blt	$a3, 28, e
	addi	$a3, $0, 0
e:	jr	$ra
	
##############################################
# Blacks out the current square on display
redraw:
o:	bge	$t4, 7, tw
	jal	draw_pixel
	addi	$t4, $t4, 1
	addi	$a0, $a0, 1
	j	o
	
tw:	bge	$t5, 7, t
	jal	draw_pixel
	addi	$t5, $t5, 1
	addi	$a1, $a1, 1
	j	tw
	
t:	bge	$t6, 7, f
	jal	draw_pixel
	addi	$t6, $t6, 1
	addi	$a0, $a0, -1
	j	t

f:	bge	$t7, 7, l
	jal	draw_pixel
	addi	$t7, $t7, 1
	addi	$a1, $a1, -1
	j	f

l: 	jr	$ra
