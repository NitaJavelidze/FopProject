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


5. check if a number is prime

   var N = 13

var isPrime = true

if N <= 1 {
  
isPrime = false

} else {
 
var i = 2

 while i < N {

  if N % i == 0 {
        
isPrime = false
           
i = N 
      
}
       
i = i+1
    
}

}

print(isPrime)



6. check if a number is a palindrome

let num = 12345

let reversed = 0

while num != 0 {
    
let digit = num % 10

 reversed = reversed * 10

reversed = reversed + digit
   
num = num / 10

}

if num == reversed {

print("true")

} else {
   
print("false")

}




7. find largest digit in a number

var num = 3947

var largestDigit = 0

while num != 0 {

 let digit = num % 10

 if digit > largestDigit {
       
largestDigit = digit
    
}
   
num = num / 10

}

print(largestDigit)



8.sum of digits

  var num = 1234

 var sumOfDigits = 0

while num != 0 {

 sumOfDigits = sumOfDigits+ num % 10
   
num = num/10

}

print(sumOfDigits)



9. multiplication table

var N = 5

for i in 1...10 {
  
print(N * i)

}



10.n-th fibonacci number

var N = 10

var a = 0

var b = 1

for _ in 2...N {
    
let next = a + b

 a = b

 b = next

}

print(b)










In this project we used these subsets of Swift:  let, var, while, if, for, print and basic arithmetics

Unfortunately, we were not able to build an interpreter that would read and execute all of the algorithms but we tried our best and we hope that you will appreciate all of our work and effort!:)

Happy New Year!!!
