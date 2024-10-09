package one.empty3.libs;

public class Colors {
    private int color;

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public Colors(int color) {
        this.color = color;
    }


    @Override
    public String toString() {
        return "Colors{" +
                "color=" + color +
                '}';
    }
}