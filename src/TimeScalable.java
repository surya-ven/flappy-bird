import bagel.Input;

public interface TimeScalable {
    double TIMESCALE_MIN = 1;
    double TIMESCALE_MAX = 5;
    double MULTIPLIER = 1.5;
    void timeScale(Input input);
}
