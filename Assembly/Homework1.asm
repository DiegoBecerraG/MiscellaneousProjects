# Variables

.data
a: 	.word	1
b: 	.word	1
c: 	.word	1

ans1: 	.word	1
ans2: 	.word	1
ans3: 	.word	1

uName:	.space	20
name:	.asciiz "What is your name? " 
int:	.asciiz "Please enter a number between 1-100: "
result:	.asciiz "your answers are: "

.text
main:
	# prompts user for their name
	li $v0, 4
	la $a0, name
	syscall
	
	# gets name from user
	li $v0, 8
	la $a0, uName
	li $a1, 20
	syscall 
	
	# prompts user for a number
	li $v0, 4
	la $a0, int
	syscall
	
	# gats number from user
	li $v0, 5
	syscall
	sw $v0, a
	
	# repeats previous two steps twice
	li $v0, 4
	la $a0, int
	syscall
	
	li $v0, 5
	syscall
	sw $v0, b
	
	li $v0, 4
	la $a0, int
	syscall
	
	li $v0, 5
	syscall
	sw $v0, c
	
	# ans1 = 2a - c + 4
	lw $t0, a
	lw $t2, c
	add 	$t3, $t0, $t0
	sub 	$t4, $t3, $t2
	addi	$t5, $t4, 4
	sw $t5, ans1
	
	# ans2 = b - c + (a - 2)
	lw $t0, a
	lw $t1, b
	lw $t2, c
	subi	$t3, $t0, 2
	add	$t4, $t1, $t3
	sub	$t5, $t4, $t2
	sw $t5, ans2
	
	# ans3 = (a + 3) - (b - 1) + (c + 3)
	lw $t0, a
	lw $t1, b
	lw $t2, c
	addi	$t3, $t0, 3
	subi	$t4, $t1, 1
	addi	$t5, $t2, 3
	sub	$t6, $t3, $t4
	add	$t7, $t5, $t6
	sw $t7, ans3
	
	# Prints results
	li $v0, 4
	la $a0, uName
	syscall
	
	li $v0, 4
	la $a0, result
	syscall
	
	li $v0, 1
	lw $a0, ans1
	syscall
	
	li $a0, 32
	li $v0, 11
	syscall
	
	li $v0, 1
	lw $a0, ans2
	syscall
	
	li $a0, 32
	li $v0, 11
	syscall
	
	li $v0, 1
	lw $a0, ans3
	syscall

exit: 	li $v0, 10
	syscall
