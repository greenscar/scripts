def buildConnectionString(params):
   """Build a connection string from a dictionary of paramaters.
   
   Returns string."""
   return ";".join(["%s=%s" % (k, v) for k, v in params.items()])
   
if __name__ == "__main__":
   myParams = {"server":"servername", \
               "database":"dbname", \
               "uid":"userid", \
               "pwd":"password" \
               }
   print buildConnectionString(myParams)
