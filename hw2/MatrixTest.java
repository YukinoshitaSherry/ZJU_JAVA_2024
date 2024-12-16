package hw2;

import java.util.Scanner;

public class MatrixTest {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n矩阵运算测试系统");
            System.out.println("1. 测试不同维度矩阵运算");
            System.out.println("2. 测试基本运算(加减乘、标量乘法)");
            System.out.println("3. 测试链式运算");
            System.out.println("4. 测试可变矩阵与不可变矩阵转换");
            System.out.println("5. 性能测试");
            System.out.println("0. 退出");
            
            int choice = scanner.nextInt();
            if (choice == 0) break;
            
            switch (choice) {
                case 1: testDifferentDimensions(); break;
                case 2: testBasicOperations(); break;
                case 3: testChainOperations(); break;
                case 4: testMatrixConversion(); break;
                case 5: performanceTest(); break;
            }
        }
        scanner.close();
    }
    
    private static void testDifferentDimensions() {
        System.out.println("输入第一个矩阵的维度 (行 列):");
        int rows1 = scanner.nextInt();
        int cols1 = scanner.nextInt();
        
        System.out.println("输入第二个矩阵的维度 (行 列):");
        int rows2 = scanner.nextInt();
        int cols2 = scanner.nextInt();
        
        MutableMatrix m1 = inputMatrix("第一个矩阵", rows1, cols1);
        MutableMatrix m2 = inputMatrix("第二个矩阵", rows2, cols2);
        
        try {
            System.out.println("矩阵乘法结果:");
            MutableMatrix result = m1.multiply(m2);
            printMatrix(result);
        } catch (IllegalArgumentException e) {
            System.out.println("错误: " + e.getMessage());
        }
    }
    
    private static void testBasicOperations() {
        System.out.println("输入测试矩阵维度 (行 列):");
        int rows = scanner.nextInt();
        int cols = scanner.nextInt();
        
        MutableMatrix m1 = inputMatrix("第一个矩阵", rows, cols);
        MutableMatrix m2 = inputMatrix("第二个矩阵", rows, cols);
        
        System.out.println("加法结果:");
        printMatrix(m1.add(m2));
        
        System.out.println("减法结果:");
        printMatrix(m1.subtract(m2));
        
        System.out.println("输入标量乘数:");
        double scalar = scanner.nextDouble();
        System.out.println("标量乘法结果:");
        printMatrix(m1.scalarMultiply(scalar));
    }
    
    private static void testChainOperations() {
        System.out.println("输入矩阵维度 (行 列):");
        int rows = scanner.nextInt();
        int cols = scanner.nextInt();
        
        MutableMatrix m1 = inputMatrix("第一个矩阵", rows, cols);
        MutableMatrix m2 = inputMatrix("第二个矩阵", rows, cols);
        MutableMatrix m3 = inputMatrix("第三个矩阵", rows, cols);
        
        System.out.println("链式运算 (m1.add(m2).add(m3)) 结果:");
        printMatrix(m1.add(m2).add(m3));
    }
    
    private static void testMatrixConversion() {
        System.out.println("输入矩阵维度 (行 列):");
        int rows = scanner.nextInt();
        int cols = scanner.nextInt();
        
        MutableMatrix mutable = inputMatrix("可变矩阵", rows, cols);
        System.out.println("原始可变矩阵:");
        printMatrix(mutable);
        
        ImmutableMatrix immutable = mutable.toImmutable();
        System.out.println("转换为不可变矩阵:");
        printMatrix(immutable);
        
        MutableMatrix converted = immutable.toMutable();
        System.out.println("再次转换为可变矩阵:");
        printMatrix(converted);
    }
    
    private static void performanceTest() {
        System.out.println("\n=== 性能测试开始 ===");
        
        // 测试不同维度
        int[] dimensions = {10, 50, 100, 200, 500};
        for (int dim : dimensions) {
            System.out.println("\n测试 " + dim + "x" + dim + " 维度矩阵:");
            testPerformanceForDimension(dim);
        }
    }
    
    private static void testPerformanceForDimension(int dim) {
        // 准备测试数据
        MutableMatrix mm1 = generateRandomMatrix(dim, dim);
        MutableMatrix mm2 = generateRandomMatrix(dim, dim);
        ImmutableMatrix im1 = mm1.toImmutable();
        ImmutableMatrix im2 = mm2.toImmutable();
        
        // 测试加法性能
        System.out.println("\n加法操作:");
        long startTime = System.nanoTime();
        mm1.add(mm2);
        long mutableAddTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        im1.add(im2);
        long immutableAddTime = System.nanoTime() - startTime;
        
        System.out.printf("可变矩阵: %.3f ms\n", mutableAddTime / 1_000_000.0);
        System.out.printf("不可变矩阵: %.3f ms\n", immutableAddTime / 1_000_000.0);
        
        // 测试乘法性能
        System.out.println("\n乘法操作:");
        startTime = System.nanoTime();
        mm1.multiply(mm2);
        long mutableMultTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        im1.multiply(im2);
        long immutableMultTime = System.nanoTime() - startTime;
        
        System.out.printf("可变矩阵: %.3f ms\n", mutableMultTime / 1_000_000.0);
        System.out.printf("不可变矩阵: %.3f ms\n", immutableMultTime / 1_000_000.0);
        
        // 测试链式操作性能
        System.out.println("\n链式操作 (add->multiply->scalarMultiply):");
        startTime = System.nanoTime();
        mm1.add(mm2).multiply(mm2).scalarMultiply(2.0);
        long mutableChainTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        im1.add(im2).multiply(im2).scalarMultiply(2.0);
        long immutableChainTime = System.nanoTime() - startTime;
        
        System.out.printf("可变矩阵: %.3f ms\n", mutableChainTime / 1_000_000.0);
        System.out.printf("不可变矩阵: %.3f ms\n", immutableChainTime / 1_000_000.0);
    }
    
    private static MutableMatrix inputMatrix(String name, int rows, int cols) {
        System.out.println("输入" + name + "的元素 (" + rows + "x" + cols + "):");
        MutableMatrix matrix = new MutableMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix.set(i, j, scanner.nextDouble());
            }
        }
        return matrix;
    }
    
    private static void printMatrix(MutableMatrix m) {
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getCols(); j++) {
                System.out.printf("%.2f ", m.get(i, j));
            }
            System.out.println();
        }
    }
    
    private static void printMatrix(ImmutableMatrix m) {
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getCols(); j++) {
                System.out.printf("%.2f ", m.get(i, j));
            }
            System.out.println();
        }
    }
    
    private static MutableMatrix generateRandomMatrix(int rows, int cols) {
        MutableMatrix matrix = new MutableMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix.set(i, j, Math.random() * 10);
            }
        }
        return matrix;
    }
}