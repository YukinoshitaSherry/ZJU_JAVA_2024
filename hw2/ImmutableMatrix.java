package hw2;

public final class ImmutableMatrix {
    private final double[][] data;
    
    public ImmutableMatrix(double[][] data) {
        this.data = deepCopy(data);
    }
    
    public ImmutableMatrix(MutableMatrix matrix) {
        this.data = deepCopy(matrix.getData());
    }
    
    private double[][] deepCopy(double[][] src) {
        double[][] dest = new double[src.length][src[0].length];
        for(int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, dest[i], 0, src[i].length);
        }
        return dest;
    }
    
    public double get(int i, int j) {
        return data[i][j];
    }
    
    public ImmutableMatrix add(ImmutableMatrix other) {
        if(data.length != other.data.length || data[0].length != other.data[0].length) {
            throw new IllegalArgumentException("矩阵维度不匹配");
        }
        
        double[][] result = new double[data.length][data[0].length];
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[0].length; j++) {
                result[i][j] = data[i][j] + other.data[i][j];
            }
        }
        return new ImmutableMatrix(result);
    }
    
    public ImmutableMatrix subtract(ImmutableMatrix other) {
        if(data.length != other.data.length || data[0].length != other.data[0].length) {
            throw new IllegalArgumentException("矩阵维度不匹配");
        }
        
        double[][] result = new double[data.length][data[0].length];
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[0].length; j++) {
                result[i][j] = data[i][j] - other.data[i][j];
            }
        }
        return new ImmutableMatrix(result);
    }
    
    public ImmutableMatrix multiply(ImmutableMatrix other) {
        if(data[0].length != other.data.length) {
            throw new IllegalArgumentException("矩阵维度不匹配，无法相乘");
        }
        
        double[][] result = new double[data.length][other.data[0].length];
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < other.data[0].length; j++) {
                for(int k = 0; k < data[0].length; k++) {
                    result[i][j] += data[i][k] * other.data[k][j];
                }
            }
        }
        return new ImmutableMatrix(result);
    }
    
    public ImmutableMatrix scalarMultiply(double scalar) {
        double[][] result = new double[data.length][data[0].length];
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[0].length; j++) {
                result[i][j] = data[i][j] * scalar;
            }
        }
        return new ImmutableMatrix(result);
    }
    
    public ImmutableMatrix transpose() {
        double[][] result = new double[data[0].length][data.length];
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[0].length; j++) {
                result[j][i] = data[i][j];
            }
        }
        return new ImmutableMatrix(result);
    }
    
    public int getRows() {
        return data.length;
    }
    
    public int getCols() {
        return data[0].length;
    }
    
    public MutableMatrix toMutable() {
        return new MutableMatrix(deepCopy(this.data));
    }
    
    public double[][] getData() {
        return deepCopy(data);
    }
}