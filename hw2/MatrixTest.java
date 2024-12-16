package hw2;

public class MatrixTest {
    public static void main(String[] args) {
        // 测试不同维度的矩阵
        testDifferentDimensions();
        // 测试各种运算
        testOperations();
        // 测试链式操作
        testChainOperations();
    }
    
    private static void testDifferentDimensions() {
        System.out.println("测试不同维度矩阵:");
        MutableMatrix m1 = new MutableMatrix(3, 4);
        MutableMatrix m2 = new MutableMatrix(4, 2);
        
        // 初始化矩阵
        for(int i = 0; i < m1.getRows(); i++) {
            for(int j = 0; j < m1.getCols(); j++) {
                m1.set(i, j, i + j);
            }
        }
        
        for(int i = 0; i < m2.getRows(); i++) {
            for(int j = 0; j < m2.getCols(); j++) {
                m2.set(i, j, i * j);
            }
        }
        
        System.out.println("矩阵乘法:");
        MutableMatrix result = m1.multiply(m2);
        printMatrix(result);
    }
    
    private static void testOperations() {
        System.out.println("\n测试各种运算:");
        MutableMatrix m = new MutableMatrix(2, 3);
        for(int i = 0; i < m.getRows(); i++) {
            for(int j = 0; j < m.getCols(); j++) {
                m.set(i, j, i + j);
            }
        }
        System.out.println("原始矩阵:");
        printMatrix(m);
        
        System.out.println("标量乘法 (2.0):");
        m.scalarMultiply(2.0);
        printMatrix(m);
    }
    
    private static void testChainOperations() {
        System.out.println("\n测试链式运算:");
        MutableMatrix m1 = new MutableMatrix(2, 2);
        MutableMatrix m2 = new MutableMatrix(2, 2);
        MutableMatrix m3 = new MutableMatrix(2, 2);
        
        // 初始化矩阵
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 2; j++) {
                m1.set(i, j, 1.0);
                m2.set(i, j, 2.0);
                m3.set(i, j, 3.0);
            }
        }
        
        System.out.println("链式运算结果:");
        m1.add(m2).multiply(m3).scalarMultiply(2.0);
        printMatrix(m1);
    }
    
    private static void printMatrix(MutableMatrix m) {
        for(int i = 0; i < m.getRows(); i++) {
            for(int j = 0; j < m.getCols(); j++) {
                System.out.print(m.get(i, j) + " ");
            }
            System.out.println();
        }
    }
    
    private static void printMatrix(ImmutableMatrix m) {
        for(int i = 0; i < m.getRows(); i++) {
            for(int j = 0; j < m.getCols(); j++) {
                System.out.print(m.get(i, j) + " ");
            }
            System.out.println();
        }
    }
}