# Bitmap Homework 
# sample solution
# 
# Instructions: 
#   Set up display:
#         set pixel dim to 4x4
#         set display dim to 256x256
#	  base address at $gp
# 	Connect to MIPS and run
#    Connect keyboard simulator using default settings
#	type in bottom pane

# set up some constants
# width of screen in pixels
# 256 / 4 = 64
.eqv WIDTH 64
# height of screen in pixels
.eqv HEIGHT 64
# memory address of pixel (0, 0)
#.eqv MEM 0x10010000 
# colors
.eqv	RED 	0x00FF0000
.eqv	GREEN 	0x0000FF00
.eqv	BLUE	0x000000FF
.eqv	WHITE	0x00FFFFFF
.eqv	YELLOW	0x00FFFF00
.eqv	CYAN	0x0000FFFF
.eqv	MAGENTA 0x00FF00FF

.data
colors:	.word	MAGENTA, CYAN, YELLOW, BLUE, GREEN, RED

.text
main:
	# set up the initial box 
	addi 	$a0, $0, WIDTH    # a0 = X = WIDTH/2   a0 = a0 + width
	sra 	$a0, $a0, 1       # width/2
	addi 	$a1, $0, HEIGHT   # a1 = Y = HEIGHT/2
	sra 	$a1, $a1, 1       # a1/2
	la 	$a2, colors       # a2 = red (ox00RRGGBB)
	
loop:	jal 	draw_box   
        # call function

	# check for input
	lw $t0, 0xffff0000  #t1 holds if input available   Loads words
    	beq $t0, 0, loop    #If no input(0), keep displaying(loop)
	
	# process input
	lw 	$s1, 0xffff0004
	beq	$s1, 32, exit	# input space (If 32, exit the code)
	beq	$s1, 119, up 	# input w
	beq	$s1, 115, down 	# input s
	beq	$s1, 97, left  	# input a
	beq	$s1, 100, right	# input d
	# invalid input, ignore
	b	loop
	
		# process valid input
	
up:	li	$a3, 9		# black out the box  Load Immediate   ##########
	jal	draw_box
	li	$a3, 0
	addi	$a1, $a1, -1   # a1
	jal	draw_box
	j	loop           # Loads Imediately

down:	li	$a3, 9		# black out the box
	jal	draw_box
	li	$a3, 0
	addi	$a1, $a1, 1 
	jal	draw_box
	j	loop
	
left:	li	$a3, 9		# black out the box
	jal	draw_box
	li	$a3, 0
	addi	$a0, $a0, -1   # a0 
	jal	draw_box
	j	loop
	
right:	li	$a3, 9		# black out the box
	jal	draw_box
	li	$a3, 0
	addi	$a0, $a0, 1     
	jal	draw_box
	j	loop
		
exit:	li	$v0, 10       # v0
	syscall
	

#################################################
# subroutine to draw a BOX
# $a0 = X  (x, y) is upper left corner of 7x7 box
# $a1 = Y
# $a2 = address of color
# $a3 = current color count; if ==9 then color is black
draw_box:
	# save $ra
	addi	$sp, $sp, -4
	sw	$ra, ($sp)
	
	# s1 = address = $gp + 4*(x + y*width)
	mul	$s1, $a1, WIDTH   # y * WIDTH
	add	$s1, $s1, $a0	  # add X
	mul	$s1, $s1, 4	  # multiply by 4 to get word offset
	add	$s1, $s1, $gp	  # add to base address
	lw	$v0, ($a2)	  # get initial color
	
	# draw top line
	li	$t0, 0
top:	addi	$s1, $s1, 4	  # move to next word (right)
	jal	update_color	  # color returned in $V0
	sw	$v0, ($s1)	  # store color at memory location
	addi	$t0, $t0, 1	  # counter ++
	jal	pause
	blt	$t0, 8, top
	
	li	$t0, 0
rght:	addi	$s1, $s1, 256
	jal	update_color
	sw	$v0, ($s1)
	addi	$t0, $t0, 1
	jal	pause
	blt	$t0, 8, rght
	
	li	$t0, 0
bottom: addi	$s1, $s1, -4
	jal	update_color
	sw	$v0, ($s1)
	addi	$t0, $t0, 1
	jal	pause
	blt	$t0, 8, bottom

	li	$t0, 0
lft:	addi	$s1, $s1, -256
	jal	update_color
	sw	$v0, ($s1)
	addi	$t0, $t0, 1
	jal	pause
	blt	$t0, 8, lft	
	
	# restore $ra
	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr 	$ra
	
#################################################
# subroutine to get next color and place in $a3
# $a2 = address of current color 
# $a3 is the current color offset
# $v0 color code
update_color:
	# save $ra
	addi	$sp, $sp, -4
	sw	$ra, ($sp)
	
	bne	$a3, 9, update
	li	$v0, 0		# color is black
	j	exit_color
	
update:	add	$t7, $a2, $a3
	lw	$v0, ($t7)
	addi	$a3, $a3, 4   # point to next color
	blt	$a3, 20, exit_color
	li	$a3, 0
	
exit_color:
	# restore $ra
	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr	$ra
	
#################################################
# subroutine to wate a little time
# $a0 - amount to pause
pause:
	# save $ra
	addi	$sp, $sp, -8
	sw	$ra, ($sp)
	sw	$a0, 4($sp)
	
	li	$v0, 32 #syscall value for sleep
	li	$a0, 5	  # sleep 5 ms
	syscall
	
	# restore $ra
	lw	$ra, ($sp)
	lw	$a0, 4($sp)
	addi	$sp, $sp, 8
	jr	$ra

	
