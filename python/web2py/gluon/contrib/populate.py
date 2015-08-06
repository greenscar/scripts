import re, cPickle, random, base64, datetime



class Learner:
    def __init__(self):
        self.db={}
    def learn(self,text):
        replacements1={'[^a-zA-Z0-9\.;:\-]':' ',
                       '\s+':' ',', ':' , ', '\. ':' . ',
                       ': ':' : ','; ':' ; '}
        for key,value in replacements1.items(): text=re.sub(key,value,text)
        items=[item.lower() for item in text.split(' ')]
        for i in range(len(items)-1):
            item=items[i]
            nextitem=items[i+1]
            if not self.db.has_key(item): self.db[item]={}
            if not self.db[item].has_key(nextitem): self.db[item][nextitem]=1
            else: self.db[item][nextitem]+=1
    def save(self,filename):
        cPickle.dump(self.db,open(filename,'wb'))
    def load(self,filename):
        self.loadd(cPickle.load(open(filename,'rb')))
    def loadd(self,db):
        self.db=db
    def generate(self,length=10000,prefix='http://127.0.0.1:8000/'):
        replacements2={' ,':',', ' \.':'.\n', ' :':':', ' ;':';', '\n\s+':'\n' }
        keys=self.db.keys()
        key=keys[random.randint(0,len(keys)-1)]
        words=key
        words=words.capitalize()
        regex=re.compile('[a-z]+')
        for i in range(length):
            okey=key
            if not key in self.db: break # should not happen
            db=self.db[key]
            s=sum(db.values())
            i=random.randint(0,s-1)
            for key,value in db.items():
                if i<value: break
                else: i-=value
            if okey=='.': key1=key.capitalize()
            else: key1=key
            if prefix and regex.findall(key1) and \
                    random.random()<0.01:
                key1='<a href="%s%s">%s</a>' % (prefix,key1,key1)
            words+=' '+key1
        text=words
        for key,value in replacements2.items():
            text=re.sub(key,value,text)
        return text+'.\n'

def da_du_ma(n=4):
    return ''.join([['da','du','ma','mo','ce','co',
                     'pa','po','sa','so','ta','to']\
                        [random.randint(0,11)] for i in range(n)])

def populate(table, n, default=True):
    ell=Learner()
    #ell.learn(open('20417.txt','r').read())
    #ell.save('frequencies.pickle')
    #ell.load('frequencies.pickle')
    ell.loadd(IUP)
    for i in range(n):
        record={}
        for fieldname in table.fields:
            field = table[fieldname]
            if field.type == 'id':
                continue
            elif default and field.default:
                record[fieldname]=field.default
            elif field.type == 'text':
                record[fieldname]=ell.generate(random.randint(10,100),prefix=None)
            elif field.type == 'boolean':
                record[fieldname]=random.random()>0.5
            elif field.type in ['datetime', 'date']:
                record[fieldname] = \
                    datetime.datetime(2009,1,1) - \
                    datetime.timedelta(days=random.randint(0,10000))
            elif field.type == 'time':
                h = random.randint(0,23)
                m = 15*random.randint(0,3)
                record[fieldname] = datetime.time(h,m,0)
            elif field.type == 'password':
                record[fieldname] = ''
            elif field.type == 'upload':
                record[fieldname] = None
            elif field.type in ['integer','double']:
                try:
                    record[fieldname] = random.randint(field.requires.minimum,field.requires.maximum-1)
                except:
                    record[fieldname] = random.randint(0,1000)
            elif field.type[:10] == 'reference ':
                if table._db._dbname=='gql':
                    record[fieldname] = table._db(table._db[field.type[10:]].id>0)\
                        .select().first()
                else:
                    record[fieldname] = table._db(table._db[field.type[10:]].id>0)\
                        .select(orderby='<random>').first()
            elif field.type=='string' and hasattr(field.requires,'options'):
                options=field.requires.options()
                record[fieldname] = options[random.randint(0,len(options)-1)][0]
            elif field.type=='string' and fieldname.find('url')>=0:
                record[fieldname] = 'http://%s.example.com' % da_du_ma(4)
            elif field.type=='string' and fieldname.find('email')>=0:
                record[fieldname] = '%s@example.com' % da_du_ma(4)
            elif field.type=='string' and fieldname.find('name')>=0:
                record[fieldname] = da_du_ma(4).capitalize()
            elif field.type=='string':
                record[fieldname] = ell.generate(10, prefix=False)[:field.length]
        table.insert(**record)

if __name__ == '__main__':
    ell=Learner()
    ell.loadd(eval(base64.b64decode(IUP)))
    print ell.generate(200,prefix=None)