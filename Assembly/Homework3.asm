.data

height:	.word	0
weight:	.word	0
bmi:	.double	01.00000
name:	.word	0

fu:	.float	18.5
fn:	.float	25.0
fo:	.float	30.0

wN:	.asciiz "What is your name? "
wH:	.asciiz "What is your height in inches: "
wW:	.asciiz "Now enter your weight in pounds (round to a whole number): "

bmiR: 	.asciiz "your BMI is: "
null:	.asciiz "\n"

uW:	.asciiz "This is considered underweight."
nW:	.asciiz "This is a normal weight."
oW:	.asciiz "This is considered overweight."
ob:	.asciiz "This is considered obese."

.text
	# prompts user for their name
	li 	$v0, 4
	la 	$a0, wN
	syscall
	
	# gets name from user
	li 	$v0, 8
	la 	$a0, name
	li 	$a1, 20
	syscall
	
	# prompts user for their height
	li 	$v0, 4
	la 	$a0, wH
	syscall
	
	# gets height from user
	li 	$v0, 5
	syscall
	sw	$v0, height
	
	# prompts user for their weight
	li 	$v0, 4
	la 	$a0, wW
	syscall
	
	# gets weight from user
	li 	$v0, 5
	syscall
	sw	$v0, weight
	
	# Stores values
	lw	$t0, height
	lw	$t1, weight
	ldc1	$f4, bmi
	li	$t3, 703
	
	# Calculates BMI
	mul	$t1, $t1, $t3	# weight *= 703
	mul	$t0, $t0, $t0	# height *= height
	
	sw	$t0, height
	sw	$t1, weight
	
	mtc1	$t0, $f2	# height
	mtc1	$t1, $f6	# weight
	
	cvt.s.w $f6, $f6	# weight
	cvt.s.w $f2, $f2	# height
	
	div.s	$f4, $f6, $f2	# weight / height
	
	# Stores calculated values
	swc1	$f4, bmi
	
	# Prints results
	li	$v0, 4
	la	$a0, name
	syscall
	
	li	$v0, 4
	la	$a0, bmiR
	syscall
	
	li	$v0, 2
	lwc1	$f12, bmi
	syscall
	
	li	$v0, 4
	la	$a0, null
	syscall
	
	# Tells you information on your weight health
	lwc1	$f4, bmi
	
	l.s	$f1, fu
	c.lt.s 	$f4, $f1
	bc1t	under
	
	l.s	$f1, fn
	c.lt.s	$f4, $f1
	bc1t	norm
	
	l.s	$f1, fo
	c.lt.s	$f4, $f1
	bc1t	over
	
	li	$v0, 4
	la	$a0, ob
	syscall
	j	exit
	
under:	li	$v0, 4
	la	$a0, uW
	syscall
	j	exit
	
norm:	li	$v0, 4
	la	$a0, nW
	syscall
	j	exit
	
over:	li	$v0, 4
	la	$a0, oW
	syscall
	j	exit
	
exit: 	li 	$v0, 10
	syscall
