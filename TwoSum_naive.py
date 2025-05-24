arr = [0, 1, 2, 3, 4, 5]
target = 7

def two_sum(arr, target):
    index_map = {}
    for i, num in enumerate(arr):
        complement = target - num
        if complement in index_map:
            return [index_map[complement], i]
        index_map[num] = i
    return []  # Correctly placed outside the loop

result = two_sum(arr, target)
print("Indices:", result)