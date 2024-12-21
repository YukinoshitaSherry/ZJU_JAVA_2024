任务1: JDK库中的不变类分析
以下是两个典型的不变类示例及分析:
String类
String类是不可变类，其值在创建后不能被修改。这是因为String类的所有字段都是final的，并且类本身是final的，这意味着它不能被继承。String类提供了很多方法，但是这些方法都不会改变原有字符串的内容，而是返回一个新的字符串对象。
例子：
```java
String str = "Hello";
str = str + " World"; // 创建新对象
```
源码分析：
```java
public final class String {
    private final char[] value; // 内部数组是final的
    private final int hash;     // hash值也是final的
    
    // 构造方法创建新对象
    public String(char[] value) {
        this.value = Arrays.copyOf(value, value.length);
    }
    
    // 所有修改操作都会返回新对象
    public String concat(String str) {
        return new String(this.value + str.value);
    }
}
```

Integer类
Integer类是不可变类，其值在创建后不能被修改。因为它的构造方法会创建一个包含给定整数值的新Integer对象，这个对象的状态是固定的。Integer类提供了很多方法来操作整数，但这些方法都不会改变Integer对象的状态，而是返回新的Integer对象或者原始值。
例子：
```java
Integer num = 10;
num = num + 1; // 创建新对象
```

源码分析：
```java
public final class Integer {
    private final int value; // 内部值是final的
    
    // 操作都返回新对象
    public Integer(int value) {
        this.value = value;
    }
}
```

共性：
final字段：不变类的字段通常是final的，这意味着它们在初始化后不能被重新赋值。
没有可变方法：不变类不提供修改其状态的方法。任何看似修改状态的方法实际上都会返回一个新的对象。
线程安全：由于状态不可变，不变类的对象是线程安全的，不需要额外的同步。
安全性：不变对象可以自由地在多个线程或多个客户端之间共享，而不担心数据一致性问题。
这些特性使得不变类在多线程环境中非常有用，因为它们减少了同步的需要，并且可以安全地共享。在设计自己的类时，如果不需要修改对象的状态，考虑使其不变可以提高代码的安全性和简洁性。


