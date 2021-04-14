public class Element<T> {
    protected T value;
    private boolean active;
    public boolean flag;

    public Element(T value)
    {
        this.value = value;
        this.active = true;
        flag = false;
    }

    public void setElement(T value)
    {
        this.value = value;
    }

    public T getElement()
    {
        return value;
    }
    
    public boolean getFlag()
    {
    	return flag;
    }

    public boolean isActive()
    {
        return active;
    }

    public void remove()
    {
        this.active = false;
    }
    
    public void setFlag() //se for uma palavra completa
    {
    	flag = true;
    }
}