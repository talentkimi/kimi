package guava;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;


import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public class MutliMapTest {
	public static void main(String... args) {
		List<String> list = Lists.newArrayList();
		List<Integer> list2 = Lists.newArrayList(1,2,3);
		ImmutableList<Integer> list3 = ImmutableList.of(1, 2, 3); // static final
		
		
		Map<String, Integer> map = Maps.newHashMap();  
		ImmutableMap<String, Integer> map2 = ImmutableMap.of(
			    "1", 1,
			    "2", 2,
			    "3", 3
			);
		System.err.println(map2.get("1"));
		
		ImmutableMap.Builder<String, Integer> builder = ImmutableMap.builder();
//		for(int i=0;i<10;i++) {
//		  builder.put(name, age);
//		}
		ImmutableMap<String, Integer> map3 = builder.build();
		
		//java.util.concurrent.ConcurrentMap   它适合多线程操作, 高并发, 无阻塞, 无死锁, 无冲突, 无测漏
//		ConcurrentMap<String, String> cache = new MapMaker()
//	    // 如果一个计算一年有效 (嗯, 明年奖金减半!), 那么设一个过期时间
//	    .expiration(365, TimeUnit.DAYS)
//	    .makeComputingMap(new Function<String, String>() {
//	      @Override public String apply(String name) {
//	        return null;
//	      }
//	    });
		
		
		
  Multimap<String, String> myMultimap = ArrayListMultimap.create();

  // Adding some key/value
  myMultimap.put("Fruits", "Bannana");
  myMultimap.put("Fruits", "Apple");
  myMultimap.put("Fruits", "Pear");
  myMultimap.put("Vegetables", "Carrot");

  // Getting the size
  int size = myMultimap.size();
  System.out.println(size);  // 4

 
  Collection<String> fruits = myMultimap.get("Fruits");
  System.out.println(fruits); // [Bannana, Apple, Pear]

  Collection<String> vegetables = myMultimap.get("Vegetables");
  System.out.println(vegetables); // [Carrot]

  // 循环输出
  for(String value : myMultimap.values()) {
   System.out.println(value);
  }

  // 移走某个值
  myMultimap.remove("Fruits","Pear");
  System.out.println(myMultimap.get("Fruits")); // [Bannana, Pear]

  //移走某个KEY的所有对应value
  myMultimap.removeAll("Fruits");
  System.out.println(myMultimap.get("Fruits")); // [] (Empty Collection!)
 }
}
