public class Item {
    public int value;
    public int size;
    double  density;
    public int order;
    public Item(int value, int size, int order){
        this.value = value;
        this.size = size;
        this.density = (double) value/size;
        this.order=order;
    }

    public double getDensity() {
        return density;
    }
}
