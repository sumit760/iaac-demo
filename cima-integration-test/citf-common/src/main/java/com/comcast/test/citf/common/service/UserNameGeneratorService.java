package com.comcast.test.citf.common.service;

import org.springframework.stereotype.Service;

/**
 * Utility class for generating random user names.
 * 
 * @author spal004c
 *
 */


@Service("usernameGenerator")
public class UserNameGeneratorService {
	
	private int pos;
	
	/**
	 * Constructor to initialize UserNameGeneratorService.
	 * 
	 * @param seed An initial seed.
	 */
    public UserNameGeneratorService(int seed) {
        this.pos = seed;
    }
    
    /**
     * Overloaded constructor specifying current system time in millisecond
     */
    public UserNameGeneratorService() {
        this((int) System.currentTimeMillis());
    } 
    
    
    /**
     * Returns the next random word from dictionary.
     * 
     * @return The next random word.
     */
    public synchronized String next() {
        Dictionary d = Dictionary.INSTANCE;
        pos = Math.abs(pos+d.getPrime()) % d.size();
        return d.word(pos);
    }

}
