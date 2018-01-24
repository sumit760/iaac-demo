package com.comcast.test.citf.common.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Utility class for Dictionary.
 * 
 * @author spal004c
 *
 */
public class Dictionary {
	
	private List<String> nouns = new ArrayList<String>();
    private List<String> adjectives = new ArrayList<String>();

    private final int prime;
    
    /**
     * Constructor that initializes lists from text files.
     */
    public Dictionary() {
        try {
            load("adjectives.txt", adjectives);
            load("nouns.txt", nouns);
        } catch (IOException e) {
            throw new Error(e);
        }

        int combo = size();

        int primeCombo = 2;
        while (primeCombo<=combo) {
            int nextPrime = primeCombo+1;
            primeCombo *= nextPrime;
        }
        prime = primeCombo+1;
    }

    /**
     * Generates the total size of the initialized lists.
     * 
     * @return Size of the list.
     */
    public int size() {
        return nouns.size()*adjectives.size();
    }
    
    /**
     * Sufficiently big prime that's bigger than {@link #size()}
     * 
     * @return the generated prime.
     */
    public int getPrime() {
        return prime;
    }
    
    /**
     * Generates a word.
     * 
     * @param i 
     * 			 Random integer the maximum size of which is equal to the size
     * 			 of the list.
     * 
     * @return the generated word.
     */
    public String word(int i) {
        int a = i%adjectives.size();
        int n = i/adjectives.size();

        return adjectives.get(a)+"_"+nouns.get(n);
    }
    
    
   	
    /**
     * Reads a file and puts all the lines of the file in a List.
     * 
     * @param name File name 
     * 							The file name.
     * @param col 
     * 							The list name.
     * @throws IOException
     */
    private void load(String name, List<String> col) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(name)));
        try {
            String line;
            while ((line=r.readLine())!=null)
                col.add(line);
        } finally {
            r.close();
        }
    }

    static final Dictionary INSTANCE = new Dictionary();
}
