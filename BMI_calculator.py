weight = float(input("enter your weight: \n"))
height = float(input("enter your height: \n"))
bmi = weight/(height*height)
print("your BMI is ", bmi)
if bmi < 18.5 :
    print("underweight")
elif bmi < 25:
    print("normal")
elif bmi < 30:
    print("Overweight")
else:
    print("band kar de khaana")
