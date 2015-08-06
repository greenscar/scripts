import string
def test(x):
   punc = [string.punctuation]
   return " ".join([str(x) for x in string.punctuation])
   
   
if __name__ == "__main__":
   print test("joe")

