import java.util.ArrayList;
public class Position
{
	String word = "";
	LinkedList<ArrayList> letters;
	public int size;

	public Position() 
	{
		letters = new LinkedList<>();
	}

	public void add(char letter, int line, int column)
	{
		ArrayList<Integer> positions = format(letter,line,column);
		
		letters.add(positions);		
		word += letter; 
		size++;
	}
	
	public void remove() //removes last
	{
		word = word.substring(0,letters.size()-1);
		letters.remove(getLast()); //alterar para o remove normal
		size--;
	}
	
	public String toString()
	{
		String str = "";
		
		for(int i = 0, num = letters.size(); i<num ; i++)
		{
			ArrayList<Integer> letter = letters.get(i);
			char ch = toChar(letter.get(0));
			if (i>0)
			{
				str+= "->";
			}
			str+= "(" + ch + ":(" + letter.get(1) + "," + letter.get(2) + "))";	
		}
		return str;
	}
	
	public char toChar(int l) //passa para char
	{
		return (char)l;
	}
	
	public char toMin(char letter)
	{
		int ascii = (int) letter + 32;
		return toChar(ascii);
	}
	
	public String getWord() //palavra devolvida como minuscula para comparar dentro da hash
	{
		return word;
	}
	
	public ArrayList format(char letter, int line, int column)
	{
		ArrayList<Integer> ret = new ArrayList<>();
		
		int ascii = (int) letter;
		ret.add(ascii);
		ret.add(line);
		ret.add(column);
		
		return ret;
	}
	
	public boolean contains(char letter, int line, int column) // ["A",coluna,linha]
	{
		ArrayList<Integer> possible1 =  format(letter,line,column);
		
		for(int i = 0, num = letters.size(); i<num ; i++)
		{
			ArrayList<Integer> option = letters.get(i);
			if(option.equals(possible1))
			{
				return true;
			}
		}
		
		return false;
	}

	public ArrayList getLast()
	{
		return letters.get(letters.size()-1);
	}
	
}
