package com.adag.entropi;

public class Cypher {

    int cypher_key;

    public Cypher(int key){
        this.cypher_key = key;
    }

    public String enc(String input){
        char[] secret = input.toCharArray();
        for(int i = 0; i < secret.length;i++){
            secret[i] = (char)((int)secret[i] ^ this.cypher_key);
        }
        return secret.toString();
    }
}
