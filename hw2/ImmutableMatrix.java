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
    
    public int getRows() {
        return data.length;
    }
    
    public int getCols() {
        return data[0].length;
    }
    
    public MutableMatrix toMutable() {
        return new MutableMatrix(deepCopy(this.data));
    }
}