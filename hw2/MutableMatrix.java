package hw2;

public class MutableMatrix {
    private final double[][] data;
    
    public MutableMatrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("矩阵维度必须为正数");
        }
        data = new double[rows][cols];
    }
    
    public MutableMatrix(double[][] data) {
        this.data = new double[data.length][data[0].length];
        for(int i = 0; i < data.length; i++) {
            System.arraycopy(data[i], 0, this.data[i], 0, data[i].length);
        }
    }
    
    public void set(int i, int j, double value) {
        data[i][j] = value;
    }
    
    public double get(int i, int j) {
        return data[i][j];
    }
    
    public double[][] getData() {
        return data;
    }
    
    public MutableMatrix add(MutableMatrix other) {
        checkDimensions(other);
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[0].length; j++) {
                data[i][j] += other.data[i][j];
            }
        }
        return this;
    }
    
    public MutableMatrix subtract(MutableMatrix other) {
        checkDimensions(other);
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[0].length; j++) {
                data[i][j] -= other.data[i][j];
            }
        }
        return this;
    }
    
    public MutableMatrix multiply(MutableMatrix other) {
        if (data[0].length != other.data.length) {
            throw new IllegalArgumentException("矩阵维度不匹配,无法相乘");
        }
        
        double[][] result = new double[data.length][other.data[0].length];
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < other.data[0].length; j++) {
                for(int k = 0; k < data[0].length; k++) {
                    result[i][j] += data[i][k] * other.data[k][j];
                }
            }
        }
        
        // 不直接赋值data，而是复制结果到现有数组
        for(int i = 0; i < result.length; i++) {
            System.arraycopy(result[i], 0, data[i], 0, result[i].length);
        }
        return this;
    }
    
    public MutableMatrix scalarMultiply(double scalar) {
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[0].length; j++) {
                data[i][j] *= scalar;
            }
        }
        return this;
    }
    
    private void checkDimensions(MutableMatrix other) {
        if(data.length != other.data.length || data[0].length != other.data[0].length) {
            throw new IllegalArgumentException("矩阵维度不匹配");
        }
    }
    
    public int getRows() {
        return data.length;
    }
    
    public int getCols() {
        return data[0].length;
    }
    
    public ImmutableMatrix toImmutable() {
        return new ImmutableMatrix(this.data);
    }
}

