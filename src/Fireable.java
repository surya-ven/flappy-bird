import bagel.Input;

public interface Fireable {
    boolean triggered(Input input);
    void shoot();
}
