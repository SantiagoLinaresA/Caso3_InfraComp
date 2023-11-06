import string
len = 4
dist = 27//4
letters = list(string.ascii_lowercase)

rta = ['x']*len
for i in range(len):
    rta[i] = letters[i*dist]
    
    
while True:
    print(''.join(rta))
    for i in range(len-1, -1, -1):
        if rta[i] != letters[-1]:
            rta[i] = letters[letters.index(rta[i])+1]
            break
        else:
            rta[i] = letters[0]
    if rta[0] == letters[-1]:
        break
    
print(''.join(rta))
