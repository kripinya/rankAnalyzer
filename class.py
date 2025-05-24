class student:
    def __init__(self, name, age):
        self.name = name
        self.age = age
    def introduce(self):
        return f"my name is {self.name}, and i am {self.age} years old."
s1 = student("ananya", 20)
print(s1.introduce())