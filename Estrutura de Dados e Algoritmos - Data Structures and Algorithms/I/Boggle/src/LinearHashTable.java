public class LinearHashTable<T> extends HashTable<T> 
{

    @Override
    protected int procPos(T s) 
    {
        int pos = s.hashCode() % size;

        if (pos < 0) {
            pos *= -1;
        }

        while (true) 
        {
            if (hashTable[pos] == null)
            {
                return pos;
            } else if (hashTable[pos].value.equals(s) && hashTable[pos].isActive()) 
            {
                return pos;
            }
            if (pos + 1 == size) 
            {
                pos = 0;
            } else {
                pos++;
            }
        }
    }
}