package hw2;

public class MatrixTest {
    public static void main(String[] args) {
        testMutableMatrix();
        testImmutableMatrix();
        testConversion();
    }
    
    private static void testMutableMatrix() {
        System.out.println("测试可变矩阵链式操作:");
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
        
        m1.add(m2).add(m3);
        printMatrix(m1);
    }
    
    private static void testImmutableMatrix() {
        System.out.println("\n测试不可变矩阵:");
        ImmutableMatrix im1 = new ImmutableMatrix(new double[][]{{1,2},{3,4}});
        ImmutableMatrix im2 = new ImmutableMatrix(new double[][]{{5,6},{7,8}});
        ImmutableMatrix result = im1.add(im2);
        printMatrix(result);
    }
    
    private static void testConversion() {
        System.out.println("\n测试矩阵转换:");
        MutableMatrix m = new MutableMatrix(new double[][]{{1,2},{3,4}});
        ImmutableMatrix im = m.toImmutable();
        MutableMatrix m2 = im.toMutable();
        printMatrix(m2);
    }
    
    private static void printMatrix(MutableMatrix m) {
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 2; j++) {
                System.out.print(m.get(i, j) + " ");
            }
            System.out.println();
        }
    }
    
    private static void printMatrix(ImmutableMatrix m) {
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 2; j++) {
                System.out.print(m.get(i, j) + " ");
            }
            System.out.println();
        }
    }
}