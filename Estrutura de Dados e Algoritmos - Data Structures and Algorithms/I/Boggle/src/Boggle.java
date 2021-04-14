import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Boggle
{
	static int[][] adjacentes = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}}; //nw,n,ne,w,e,sw,s,se
	
    public static void main(String args[]) throws IOException
    {
        HashTable<String> dicionario = new LinearHashTable<String>();
        dicionario.alocarTabela(1000000);
        inserirDic(dicionario);
        char[][] boggle = readBoggle("input.txt");
      
        for(int i = 0; i<4;i++) //i line
        {
    		LinkedList<String> words = new LinkedList<String>();
            for(int j = 0; j<4 ; j++) //j column
            {
                Position word = new Position();
                word.add(boggle[i][j],i,j);
                searchWord(word,boggle,dicionario,words);
            }
            
            for(int j=0;j<words.size();j++)
            {
            	System.out.println(words.get(j));
            }
        }
    }
    
    public static void inserirDic(HashTable<String> dicionario) throws IOException
    {
        File ficheiro = new File("allWords.txt");
        BufferedReader buffRead = new BufferedReader(new FileReader(ficheiro) );
        String linha ="";

        while(true)
        {
            if ((linha = buffRead.readLine()) != null) 
            {
            	for(int i = 1; i<linha.length(); i++)
            	{
            		dicionario.insere(linha.substring(0,i));
            	}
                dicionario.insere(linha);
                dicionario.setTrue(linha);
                
            } else
                break;
        }
    }
    
    public static char[][] readBoggle(String input)  throws IOException
    {
    	File ficheiro = new File(input);
        BufferedReader buffRead = new BufferedReader(new FileReader(ficheiro));
        char[][] ret = new char [4][4];
        String linha ="";
        int count = 0;
        
        while(true)
        {
            if ((linha = buffRead.readLine()) != null) 
            {
                for(int i = 0; i<4; i++)
                {
                	for(int j = 0; j<4; j++)
                	{
                		ret[i][j] = linha.charAt(count);
                		count++;
                	}
                }
            } 
            else
                break;
        }
    	return ret;
    }
    
    public static boolean possible(char[][] boggle,int line, int column, Position word)
    {
    		return column >= 0 && column <= 3 && line >= 0 && line <= 3 && word.contains(boggle[line][column], line, column)==false ;
    }
  
	public static LinkedList<String> searchWord(Position word, char[][] boggle, HashTable<String> dicionario, LinkedList<String> words)
    {
    	if(dicionario.procurar(word.getWord()) == null)
    	{
    		return words;
    	}
    	else
    	{
    		if(dicionario.getFlag(word.getWord()) == true) //é uma palavra ou prefixo
    		{
    			words.add(word.toString());
    		}
            for(int i = 0; i<adjacentes.length;i++) 
            {
                ArrayList<Integer> letters = word.getLast();
                int line = letters.get(1)+adjacentes[i][0];
                int column = letters.get(2)+adjacentes[i][1];
                
                if(possible(boggle,line,column,word))
                {
                	word.add(boggle[line][column],line,column);
                	searchWord(word,boggle,dicionario,words);
                	word.remove();
                }
            }   
    	}
    	return words;
    }

}
