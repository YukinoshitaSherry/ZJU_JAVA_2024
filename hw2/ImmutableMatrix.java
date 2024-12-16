package hw2;

public final class ImmutableMatrix {
    private final double[][] data;
    
    public ImmutableMatrix(double[][] data) {
        this.data = deepCopy(data);
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
        double[][] result = deepCopy(data);
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[0].length; j++) {
                result[i][j] += other.data[i][j];
            }
        }
        return new ImmutableMatrix(result);
    }
    
    public MutableMatrix toMutable() {
        return new MutableMatrix(deepCopy(this.data));
    }
}