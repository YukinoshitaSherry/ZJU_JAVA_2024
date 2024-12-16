package hw2;

public class MutableMatrix {
    private final double[][] data;
    
    public MutableMatrix(int rows, int cols) {
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
        if(data.length != other.data.length || data[0].length != other.data[0].length) {
            throw new IllegalArgumentException("矩阵维度不匹配");
        }
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[0].length; j++) {
                data[i][j] += other.data[i][j];
            }
        }
        return this;
    }
    
    public ImmutableMatrix toImmutable() {
        return new ImmutableMatrix(this.data);
    }
}

