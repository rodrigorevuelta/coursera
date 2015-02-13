package org.rodrigorr.coursera.main;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MainTest {

	static int lonelyinteger(int[] a) {
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		for (int i=0;i<a.length;i++){ 
	    	if (map.get(a[i]) == null){
	    		map.put(a[i],1);
	    	} else{
	    		Integer elements = map.get(a[i]);
	    		map.put(a[i], ++elements);
	    	}

	    }
	    for (Integer key : map.keySet()){
	    	if (map.get(key) == 1){
	    		return key;
	    	}
	    }
	    return 0;
	}
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
        int res;
        
        int _a_size = Integer.parseInt(in.nextLine());
        int[] _a = new int[_a_size];
        int _a_item;
        String next = in.nextLine();
        String[] next_split = next.split(" ");
        
        for(int _a_i = 0; _a_i < _a_size; _a_i++) {
            _a_item = Integer.parseInt(next_split[_a_i]);
            _a[_a_i] = _a_item;
        }
        
        res = lonelyinteger(_a);
        System.out.println(res);
        in.close();
        
    }

}