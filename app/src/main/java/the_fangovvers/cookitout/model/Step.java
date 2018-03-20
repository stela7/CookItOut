package the_fangovvers.cookitout.model;

public class Step {

    private String step;

    public Step(String step) {
        this.step = step;
    }

    public String getStep() {
        return step;
    }

    @Override
    public String toString() {
        return "Step{" +
                "step='" + step + '\'' +
                '}';
    }
}
