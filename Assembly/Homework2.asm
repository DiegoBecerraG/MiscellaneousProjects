# Name: Diego Becerra-Grimaldo
# Assignment: Homework 2
# Date: 20 February 2022


.data
input:	.space	20
text: 	.asciiz "Enter some text: "
bye:	.asciiz "Goodbye!"
term:	.asciiz	""
nl:	.asciiz "\n"

countS: .word	0
countC:	.word	0

chars:	.asciiz " characters "
words:	.asciiz " words "

.text
main:
	# prompts user for text
	li	$v0, 54
	la	$a0, text
	la	$a1, input
	li	$a2, 20	
	syscall
	
	beq	$a1, -2, exit
	beq	$a1, -3, exit
	
	# prints text inputed
	li 	$v0, 4
	la	$a0, input
	syscall
	
	jal	count
	
	li	$v0, 4
	la	$a0, term
	syscall
	
	# pops number of spaces/words - 1
	lw	$t0, ($sp)	
	addi	$sp, $sp, 4
	addi	$t0, $t0, 1
	sw	$t0, countS
	
	# pops number of characters
	lw	$t1, ($sp)
	addi	$sp, $sp, 4
	subi	$t1, $t1, 1
	sw	$t1, countC
	
	# prints results
	li 	$v0, 1
	lw 	$a0, countS
	syscall
	
	li 	$v0, 4
	la 	$a0, words
	syscall
	
	li 	$v0, 1
	lw 	$a0, countC
	syscall
	
	li 	$v0, 4
	la 	$a0, chars
	syscall
	
	li	$v0, 4
	la	$a0, nl
	syscall
	
	j 	main
gb:	 # goodbye text
	li	$v0, 59
	la	$a0, bye
	la	$a1, term
	syscall
	
exit: 	li	$v0, 10
	syscall
	
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #  
count:	# counts number of words and characters
	li	$t0, 0		# i
	la	$t1, input
	li	$t2, ' '
	lb	$t4, term
	
	li	$s0, 0
	li	$s1, 0
	
	lb	$t3, 0($t1)
while:	beq 	$t4, $t3, end
 	addi	$t1, $t1, 1
 	lb 	$t3, 0($t1)	# "points" to the character in the sentence
 	
white: 	bne	$t3, $t2, else
	addi	$s1, $s1, 1	# whitespace count++
	j	while
	
else:	beq	$t3, $t2, while
	addi	$s0, $s0, 1	# counts characters
	j 	while
 		
end:	# stores number of characters into stack
 	addi	$sp, $sp, -4
 	sw	$s0, ($sp)
 	
 	# stores number of spaces/words - 1 into stack
 	addi	$sp, $sp, -4
 	sw	$s1, ($sp)
 	jr	$ra