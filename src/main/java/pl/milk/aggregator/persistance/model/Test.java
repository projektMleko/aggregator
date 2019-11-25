package pl.milk.aggregator.persistance.model;

import javax.persistence.*;

@Entity
public class Test {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "test_Sequence")
    @SequenceGenerator(name = "test_Sequence", sequenceName = "TEST_SEQ")
    private Long id;

    @Column(name = "STR_VALUE")
    private String stringValue;

    @Column(name = "NUM_VALUE")
    private int numberValue;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_container_id", nullable = false, foreignKey = @ForeignKey(name = "FK_TEST_CONTAINER"))
    private TestContainer testContainer;

    public Test() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public int getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(int numberValue) {
        this.numberValue = numberValue;
    }

    public TestContainer getTestContainer() {
        return testContainer;
    }

    public void setTestContainer(TestContainer testContainer) {
        this.testContainer = testContainer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
