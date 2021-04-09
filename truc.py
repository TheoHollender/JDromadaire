truc = lambda n: print("\n".join("".join(str(j) for j in range(1, i+1)) for i in (*range(0, n), *range(n, 0, -1))))
