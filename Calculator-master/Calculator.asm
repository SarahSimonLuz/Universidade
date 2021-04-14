.data 

userInput: .space 20
pilha: .space 32 
erroPush: .asciiz "ERRO: A pilha esta cheia."
erroOperandos: .asciiz "ERRO: Numero de operandos insuficiente."
erroOperacao: .asciiz "ERRO: Operacao nao existente."
erroAritmetico: .asciiz "ERRO: Operacao impossivel."
prompt: .asciiz "Pilha: \n"
pilhaVazia: .asciiz "(vazia)\n"
paragrafo: .asciiz "\n"
seta: .asciiz "-> "
nega: .asciiz "nega\n"
swap: .asciiz "swap\n"
del: .asciiz "del\n"
clear: .asciiz "clear\n"
dup: .asciiz "dup\n"
off: .asciiz "off\n"
.text

main: 
la $s0,pilha #endereco do inicio da pilha
li $s1,0 #index 
	
mainLoop:
#obter o input do user como text
li $v0,4  #4 é o valor do syscall para imprimir strings
la $a0,prompt #vai buscar o endereco da mensagem do output
syscall

bne $s1,$zero,notVazio 
nop

Vazio:
li $v0,4
la $a0,pilhaVazia
syscall

notVazio:
li $v0,4
la $a0,seta
syscall

li $v0,8 #8 é o valor do syscall para ler string (input)
la $a0, userInput #vai guardar o input no a0
li $a1,20 #espaço disponivel que o input pode ocupar
syscall


jal decode # vai saltar para o decode com os valores de a0 e a1
nop
######### MOSTRAR ARRAY
move $s1,$v0
beq $v0,$zero,PromptArray
nop
move $s1,$v0

PromptArray:
li $t0,0
move $t1,$s0
loopPrompt:
beq $t0,$s1,mainLoop
nop
lw $a0,0($t1)
add $t1,$t1,4
addi $t0,$t0,4
li $v0,1
syscall
li $v0,4
la $a0,paragrafo
syscall

j loopPrompt
nop

decode:
li $v0,0 #inicializa as variaveis todas a zero
li $t0,0
li $t1,0
li $t2,0
li $t3,10 #menos esta
addi $sp,$sp,-16  #decrementar espaco na pilha
sw $s0,0($sp)  #guardar a word de s0 em sp
sw $s1,4($sp)  #guarda a word de s1 em sp 
sw $ra,8($sp) #guarda o ra em sp porque vai ser chamada uma funçao dentro desta

loop:
lb $t0,0($a0)  
lb $t1,1($a0)
blt $t0,0x30,CHAR #os numeros estão entre 30 e 39, se nao tiver salta para CHAR
bgt $t0,0x39,CHAR  # '' 
beq $t0,$zero,FIMloop #ver se acabou o input(caracter nulo)
nop
sub $t0,$t0,0x30 #temos de subtrair o 30 para descodificar o numero ascii
beq $t1,0xa,FIMloop #vai ver se o codigo ascii já terminou
nop
mult $t0,$t3 #vai ser multiplicado por 10 porque existem outros numeros a seguir
mflo $t0     #vai haver bug com numeros acima de dois algarismos
add $t2,$t2,$t0 #soma  
j loop 
add $a0,$a0,1 #salta para o proximo elemento do array

FIMloop:
add $t2,$t2,$t0 
move $a0,$t2 #t2 guarda o input descodificado e passa para o a0 
jal push 
nop
j fimDecode
nop

CHAR:

sw $ra,12($sp)	
jal VERIFICAR
nop

fimDecode:
lw $s0,0($sp)
lw $s1,4($sp)
lw $ra,8($sp)
addi $sp,$sp,16
	 
jr $ra
nop
##############################
VERIFICAR:
addi $sp,$sp,-16
sw $s0,0($sp)
sw $s1,4($sp)
sw $ra,8($sp)
sw $a0,12($sp)


lb $t0,0($a0)

beq $t0 ,0x2b, soma
beq $t0, 0x2d, subtracao
beq $t0, 0x2a, multiplicacao
beq $t0, 0x2f, divisao
nop

lw $a0,12($sp)
la $a1,dup
jal comparar
nop
beq $v0,1,dupOP
nop

lw $a0,12($sp)
la $a1,swap
jal comparar
nop
beq $v0,1,swapOP
nop

lw $a0,12($sp)
la $a1,nega
jal comparar
nop
beq $v0,1,negaOP
nop

lw $a0,12($sp)
la $a1,clear
jal comparar
nop
beq $v0,1,clearOP
nop

lw $a0,12($sp)
la $a1,del
jal comparar
nop
beq $v0,1,delOP
nop

lw $a0,12($sp)
la $a1,off
jal comparar
nop
beq $v0,1,offOP
nop

###########FIM DE TODAS AS VERIFICAÇOES
j ERROoperacao
nop
######################
dupOP:
move $t0,$s0
add $t0,$s0,$s1
lw $a0,-4($t0)
li $a1,0
jal pushEpop
nop
j fimVerificar
nop

swapOP:
add $s0,$s0,$s1
addi $s0,$s0,-4

lw $t0,0($s0)
lw $t1,-4($s0)

sw $t1,0($s0)
sw $t0,-4($s0)

j fimVerificar
move $v0,$s1

delOP:
move $v0,$s1
j fimVerificar
addi $v0,$v0,-4

clearOP:
j fimVerificar
li $v0,0

negaOP:
move $t0,$s0
add $t0,$s0,$s1
lw $a0,-4($t0)
sub $a0,$zero,$a0
addi $a1,$zero,1
jal pushEpop
nop
j fimVerificar
nop

soma:
move $t0,$s0
add $t0,$s0,$s1
lw $a0,-4($t0)
lw $t1,-8($t0)

add $a0,$a0,$t1
li $a1,2
jal pushEpop
nop

j fimVerificar
nop

subtracao:
move $t0,$s0
add $t0,$s0,$s1
lw $a0,-4($t0)
lw $t1,-8($t0)

sub $a0,$t1,$a0
li $a1,2
jal pushEpop
nop

j fimVerificar
nop

multiplicacao:
move $t0,$s0
add $t0,$s0,$s1
lw $a0,-4($t0)
lw $t1,-8($t0)

mult $a0,$t1
mflo $a0
li $a1,2
jal pushEpop
nop

j fimVerificar
nop


divisao:
move $t0,$s0
add $t0,$s0,$s1
lw $a0,-4($t0)
lw $t1,-8($t0)

beq $a0,$zero,ERROdiv
nop

div $t1,$a0
mflo $a0
li $a1,2

jal pushEpop
nop

j fimVerificar
nop


offOP:
j FIM
nop

fimVerificar:
lw $s0,0($sp)
lw $s1,4($sp)
lw $ra,8($sp)
addi $sp,$sp,16
	 
jr $ra
nop
##########################################################
push:

beq $s1,32,ERROpush
nop
move $v0,$s1

sw $a0,pilha($s1)
addi $v0,$v0,4
jr $ra
nop
##########################################################
pushEpop:
#a0, 0 int push e $a1, numero de pops
addi $sp,$sp,-4
sw $s1,0($sp)

sll $a1,$a1,2

beq $s1,$zero,ERROoperandos
nop 
beq $s1,32,ERROpush
nop
blt $s1,$a1,ERROoperandos
nop

sub $s1,$s1,$a1
sw $a0,pilha($s1)
move $v0,$s1
addi $v0,$v0,4


FIMpushePop:
lw $s1,0($sp)
addi $sp,$sp,4
jr $ra
nop
##########################################

comparar:
lb $t0,0($a0)
lb $t1,0($a1)
addi $a0,$a0,1
addi $a1,$a1,1
bne $t0,$t1,diferentes
nop
add $t1,$t1,$t0
beq $t1,$zero,iguais
nop
j comparar
nop

diferentes:
j FIMcomparar
li $v0,-1

iguais:
li $v0,1

FIMcomparar:
jr $ra
nop	
#############################################
ERROdiv:
li $v0,4
la $a0,erroAritmetico	
syscall
j FIM
nop

ERROoperacao:
li $v0,4
la $a0,erroOperacao
syscall
j FIM
nop

ERROpush:
li $v0,4
la $a0,erroPush
syscall
j FIM
nop

ERROoperandos:
li $v0,4
la $a0,erroOperandos	
syscall
j FIM
nop



FIM:

