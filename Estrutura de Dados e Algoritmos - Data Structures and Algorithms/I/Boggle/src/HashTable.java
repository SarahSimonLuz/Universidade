public abstract class HashTable<T> 
{

    public Element<T>[] hashTable;
    protected int size;
    private int used;
  

    //construtor vazio
    public HashTable()
    {
        this.hashTable = new Element[7];
        this.size = 7;
        this.used = 0;
    }

    //construtor para uma tabela de dimensao n
    public HashTable(int size)
    {
        this.hashTable= new Element[size];
        this.size = size;
        this.used = 0;
    }

    //retorna o nº de elementos da tabela
    public int ocupados()
    {
        return this.used;
    }
    
    //retorna o factor de carga
    public float factorCarga()
    {
        return used/size;
    }

    // retornará a posição em que s será inserido ou se s existe
    // a sua localização na tabela
    protected abstract int procPos(T x);

    //uma nova tabela de dimensão especificada
    public void alocarTabela(int dim)
    {
        this.hashTable = new Element[dim];
        this.size = dim;
        this.used = 0;

    }
    //esvaziar a tabela em uso
    public void tornarvazia()
    {
        this.hashTable = new Element[size];
        this.used = 0;
    }
    
    //retornar o elemento que esta na tabela, se x nao esta la retorna null
    public T procurar(T x)
    {
    	int pos = procPos(x);
        if(hashTable[pos] == null)
            return null;
        else if (hashTable[pos].getElement().equals(x) && hashTable[pos].isActive())
             return x;
        else
             return null;
        
    }

    //remove o elemento da tabela
    public void remove(T x)
    {
        int pos = procPos(x);
        hashTable[pos].remove();
        used--;
    }
    
    public void setTrue(T x)
    {
        int pos = procPos(x);
        hashTable[pos].setFlag();
    }
    
    public boolean getFlag(T x)
    {
        int pos = procPos(x);
        return hashTable[pos].getFlag();
    }

    //insere x na tabela
    public void insere(T x)
    {
    	if(procurar(x) != null)
    	{
    		return;
    	}
        float carga = factorCarga();
        int pos = procPos(x);

        if (carga > (size / 2))
            rehash();
        /*
        //se já existe na tabela
        if (hashTable[pos].isActive() && hashTable[pos]!=null)
            return;
        */
        hashTable[pos] = new Element<>(x);

        used++;
    }

    //faz "rehashing"
    public void rehash()
    {
        int newSize = resize(size*2 + 1);
        Element<T> [] temp = hashTable;
        hashTable = new Element[newSize];

        for(Element<T> i : temp){
            if(i != null)
                insere(i.value);
        }
    }

    //lista os elementos da tabela
    public void print() 
    {
        for (int i = 0; i < size; i++) {
            if (hashTable[i] != null) {
                if (hashTable[i].isActive()) {
                    System.out.println("" + i + " : " + hashTable[i].value);
                } else {
                    System.out.println("" + i + " : " + "removed");
                }
            } else {
                System.out.println("" + i + " : " );
            }

        }
    }
    //tamanho primo
    public int resize(int size){
        for(int i = 2; i < size; i++){
            if (size % i == 0){
                size++;
                i = 2;
            }
        }
        return size;
    }

}