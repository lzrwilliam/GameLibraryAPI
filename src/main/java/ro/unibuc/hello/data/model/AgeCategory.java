
package ro.unibuc.hello.data.model;


public enum AgeCategory {
    EVERYONE("Everyone"), 
    TEENS("Teens (13+)"), 
    MATURE("Mature (18+)");

    private final String description;

    AgeCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
