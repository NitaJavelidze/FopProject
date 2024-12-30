For this group project we had to build an interpreter for following Swift algorithms:


1. sum of first n numbers

var N = 10

var sum = 0

for i in 1...N {
 
sum = sum + i

}

print(sum)


2. factorial of n


   var N = 5
 
 var factorial = 1


for i in 1...N {
    
factorial =factorial* i

}

print(factorial)
	
    	


3.gcd of two numbers


var a = 48

var b = 18

var x = a

var y = b

while y != 0 {
   
let remainder = x % y
   
x = y
   
 y = remainder

}

print(x)

    		
    		
4. reverse a number

let num = 12345

let reversed = 0

while num != 0 {

  let digit = num % 10
  
  reversed = reversed * 10
 
  reversed = reversed + digit
   

num = num / 10

}

print(reversed)













In this project we used these subsets of swift:  let, var, while, if, for, print and basic arithmetics
